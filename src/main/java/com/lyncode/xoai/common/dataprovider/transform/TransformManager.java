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

package com.lyncode.xoai.common.transform;

import java.util.HashMap;
import java.util.Map;

import com.lyncode.xoai.common.exceptions.ConfigurationException;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Transformers;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Transformers.Transformer;


/**
 * @author DSpace @ Lyncode
 * @version 1.0.1
 */
public class TransformManager {
    //private static Logger log = LogManager.getLogger(TransformManager.class);
    private Map<String, AbstractTransformer> _contexts;


    public TransformManager (Transformers config) throws ConfigurationException {
        _contexts = new HashMap<String, AbstractTransformer>();
        try {
            for (Transformer t : config.getTransformer()) {
                Class<?> c = Class.forName(t.getClazz());
                Object obj = c.newInstance();
                if (obj instanceof AbstractTransformer) {
                    ((AbstractTransformer) obj).load(t.getParameter());
                    _contexts.put(t.getId(), (AbstractTransformer) obj);
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }

    public boolean transformerExists (String id) {
        return this._contexts.containsKey(id);
    }

    public AbstractTransformer getTransformer (String id) {
        return _contexts.get(id);
    }

}
