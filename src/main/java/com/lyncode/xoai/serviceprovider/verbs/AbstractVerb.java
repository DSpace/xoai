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
 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.verbs;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.core.Parameters;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public abstract class AbstractVerb {
    private Parameters parameters;
    private OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config;

    public AbstractVerb(Parameters parameters, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config) {
        this.parameters = parameters;
        this.config = config;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> getServiceProvider() {
        return config;
    }
}
