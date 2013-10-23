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

package com.lyncode.xoai.dataprovider.transform;

import com.lyncode.xoai.dataprovider.data.MetadataTransformer;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Transformers;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Transformers.Transformer;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class TransformManager {
    // private static Logger log = LogManager.getLogger(TransformManager.class);
    private static TransformerFactory tFactory = TransformerFactory.newInstance();
    private Map<String, MetadataTransformer> _contexts;

    public TransformManager(ResourceResolver resolver, Transformers config)
            throws ConfigurationException {
        _contexts = new HashMap<String, MetadataTransformer>();
        if (config != null) {
            for (Transformer t : config.getTransformer()) {
                try {
                    _contexts.put(t.getId(),
                            new MetadataTransformer(new XSLTransformer(
                                    tFactory.newTransformer(new StreamSource(resolver.getResource(t.getXSLT())))
                            ))
                    );
                } catch (TransformerConfigurationException e) {
                    throw new ConfigurationException(e.getMessage(), e);
                } catch (IOException e) {
                    throw new ConfigurationException(e.getMessage(), e);
                }
            }
        }
    }

    public boolean transformerExists(String id) {
        return this._contexts.containsKey(id);
    }

    public MetadataTransformer getTransformer(String id) {
        return _contexts.get(id);
    }

}
