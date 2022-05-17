package io.gdcc.xoai.tests.util;

import io.gdcc.xoai.xml.EchoElement;
import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 4)
public class EchoElementBenchmark {
    
    @Test
    @EnabledIfSystemProperty(named = "benchmark", matches = "true")
    void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(EchoElementBenchmark.class.getSimpleName())
            .build();
    
        new Runner(opt).run();
    }
    
    public static final Path sourceFile = Path.of("src", "test", "resources", "ddi-codebook-2.5-example.xml");
    public static Path largeXmlFile;
    
    @State(Scope.Benchmark)
    public static class EchoBenchmarkState {
        public InputStream fileStream;
        public File outputFile;
        public FileOutputStream outputStream;
        public XmlWriter xmlWriter;
        
        @Setup(Level.Invocation)
        public void setUp() throws IOException, XMLStreamException {
            fileStream = Channels.newInputStream(FileChannel.open(largeXmlFile, StandardOpenOption.READ));
            outputFile = File.createTempFile("writedata-", ".xml");
            outputStream = new FileOutputStream(outputFile);
            xmlWriter = new XmlWriter(outputStream);
        }
        
        @TearDown(Level.Invocation)
        public void tearDown() throws IOException {
            outputFile.delete();
        }
    
        @Setup(Level.Trial)
        public static void setUpXML() {
            largeXmlFile = generateLargeXmlBlob(1024 * 1024 * 50, sourceFile);
        }
    
        @TearDown(Level.Trial)
        public static void tearDownXML() throws IOException {
            Files.delete(largeXmlFile);
        }
    }
    
    @Benchmark
    public void writeWithStream(EchoBenchmarkState state) throws IOException {
        // Copied from http://github.com/IQSS/dataverse/blob/e8435ac1fe73cda2b0e1e50c398370b8aa5eb94a/src/main/java/edu/harvard/iq/dataverse/harvest/server/xoai/Xrecord.java#L138-L146
        
        int bufsize;
        byte[] buffer = new byte[4 * 8192];
    
        while ((bufsize = state.fileStream.read(buffer)) != -1) {
            state.outputStream.write(buffer, 0, bufsize);
            state.outputStream.flush();
        }
        state.outputStream.close();
    }
    
    @Benchmark
    public void writeWithEchoElement(EchoBenchmarkState state) throws XmlWriteException, XMLStreamException, IOException {
        state.xmlWriter.write(new EchoElement(state.fileStream));
        state.xmlWriter.close();
        state.outputStream.close();
    }
    
    
    private static Path generateLargeXmlBlob(long targetSizeInBytes, Path source) {
        try {
            // read example data
            String exampleXml = Files.readString(source, StandardCharsets.UTF_8);
            long exampleSize = exampleXml.length();
    
            // create temporary file
            Path tempFile = Files.createTempFile("echoelement-", ".xml");
    
            // calculate number of repeats (integer division means we don't need to fiddle with rounding)
            long repeats = targetSizeInBytes / exampleSize;
    
            // fill in data until reaching sizeInBytes
            ByteBuffer buffer = ByteBuffer.wrap(exampleXml.getBytes(StandardCharsets.UTF_8));
            try (
                FileChannel channel = FileChannel.open(tempFile, StandardOpenOption.WRITE)
            ) {
                channel.write(ByteBuffer.wrap("<collection>".getBytes(StandardCharsets.UTF_8)));
                
                for (int i = 0; i < repeats; i++) {
                    channel.write(buffer);
                    buffer.rewind();
                }
                
                channel.write(ByteBuffer.wrap("</collection>".getBytes(StandardCharsets.UTF_8)));
            }
    
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
