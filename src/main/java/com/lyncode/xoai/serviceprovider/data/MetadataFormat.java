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
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */

package com.lyncode.xoai.serviceprovider.data;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class MetadataFormat
{
    private String metadataPrefix;

    private String schema;

    private String metadataNamespace;

    public String getMetadataPrefix()
    {
        return metadataPrefix;
    }

    public void setMetadataPrefix(String metadataPrefix)
    {
        this.metadataPrefix = metadataPrefix;
    }

    public String getSchema()
    {
        return schema;
    }

    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    public String getMetadataNamespace()
    {
        return metadataNamespace;
    }

    public void setMetadataNamespace(String metadataNamespace)
    {
        this.metadataNamespace = metadataNamespace;
    }
}
