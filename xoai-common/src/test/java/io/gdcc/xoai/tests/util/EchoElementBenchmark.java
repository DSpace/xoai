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
    
    /**
     * Store the state of the running benchmark run
     */
    @State(Scope.Benchmark)
    public static class EchoBenchmarkState {
        public InputStream fileStream;
        public File outputFile;
        public FileOutputStream outputStream;
        public XmlWriter xmlWriter;
    
        /**
         * On every invocation of a @Benchmark method below, reopen the InputStream (doesn't count agains ops)
         * and create a temporary file to write to.
         */
        @Setup(Level.Invocation)
        public void setUp() throws IOException, XMLStreamException {
            fileStream = Channels.newInputStream(FileChannel.open(largeXmlFile, StandardOpenOption.READ));
            outputFile = File.createTempFile("writedata-", ".xml");
            outputStream = new FileOutputStream(outputFile);
            xmlWriter = new XmlWriter(outputStream);
        }
    
        /**
         * After every method invocation, remove the temporary file to keep things nice and clean.
         */
        @TearDown(Level.Invocation)
        public void tearDown() throws IOException {
            outputFile.delete();
        }
    
        /**
         * Once for every benchmark instantiation / fork, create the large XML blob with 50 MiB
         */
        @Setup(Level.Trial)
        public static void setUpXML() {
            largeXmlFile = generateLargeXmlBlob(1024 * 1024 * 50, sourceFile);
        }
    
        /**
         * Remove the large blob afterwards to keep things tidy.
         */
        @TearDown(Level.Trial)
        public static void tearDownXML() throws IOException {
            Files.delete(largeXmlFile);
        }
    }
    
    /**
     * Copy the large XML blob with low level stream handling, just like it is done in Dataverse XOAI implementation
     * right now.
     *
     * This benchmark is IO bound only, as almost no CPU overhead is involved here. It should be MUCH faster
     * than the other test. (~40x on a NVMe disk) It uses a very limited amount of memory, as the buffer is rather small.
     */
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
    
    /**
     * Copy the large XML blob, but use the EchoElement, which parses the XML and checks the namespaces etc
     * before writing the result.
     *
     * This is CPU bound due to the analysis. (~1ops/sec = 50MiB/s on an i7 laptop) It uses much more memory, as
     * the StAX API uses much more and larger buffers internally. (But it's not copying the input stream to memory before)
     */
    @Benchmark
    public void writeWithEchoElement(EchoBenchmarkState state) throws XmlWriteException, XMLStreamException, IOException {
        state.xmlWriter.write(new EchoElement(state.fileStream));
        state.xmlWriter.close();
        state.outputStream.close();
    }
    
    
    /**
     * Generate a larger XML blob by replicating an existing source XML file until it has the desired size.
     * Will wrap the source XML inside of <collection></collection> as child elements.
     *
     * @param targetSizeInBytes How large should the blob grow?
     * @param source Where to find the origin XML data
     * @return A handle to the temporary file created with the large blob or null if sth. goes wrong
     */
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
