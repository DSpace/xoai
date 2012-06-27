/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.common.dataprovider.configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.lyncode.xoai.common.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.common.dataprovider.xml.xoaiconfig.Configuration;

/**
 * @author DSpace @ Lyncode
 * @version 2.0.0
 */
public class ConfigurationManager {
    public static Configuration readConfiguration (String filename) throws ConfigurationException {
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class.getPackage().getName());
            Unmarshaller marshaller = context.createUnmarshaller();
            FileInputStream reader = new FileInputStream(filename);
            Object obj = marshaller.unmarshal(reader);
            reader.close();
            if (obj instanceof Configuration) {
                return (Configuration) obj;
            } else throw new ConfigurationException("Invalid configuration bundle");
        } catch (IOException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }

    public static void writeConfiguration (Configuration config, String filename) throws ConfigurationException {
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class.getPackage().getName());
            Marshaller marshaller = context.createMarshaller();
            FileOutputStream writer = new FileOutputStream(filename);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, writer);
            writer.close();
        } catch (IOException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }
}
