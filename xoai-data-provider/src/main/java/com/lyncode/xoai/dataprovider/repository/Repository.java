/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider.repository;

import com.lyncode.xoai.dataprovider.filter.FilterResolver;
import com.lyncode.xoai.services.api.ResumptionTokenFormat;

public class Repository {
    private FilterResolver filterResolver;

    public static Repository repository () {
        return new Repository();
    }

    private RepositoryConfiguration configuration;
    private ItemRepository itemRepository;
    private SetRepository setRepository;
    private ResumptionTokenFormat resumptionTokenFormatter;

    public RepositoryConfiguration getConfiguration() {
        return configuration;
    }

    public Repository withConfiguration(RepositoryConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public Repository withItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        return this;
    }

    public SetRepository getSetRepository() {
        return setRepository;
    }

    public Repository withSetRepository(SetRepository setRepository) {
        this.setRepository = setRepository;
        return this;
    }

    public ResumptionTokenFormat getResumptionTokenFormatter() {
        return resumptionTokenFormatter;
    }

    public Repository withResumptionTokenFormatter(ResumptionTokenFormat resumptionTokenFormatter) {
        this.resumptionTokenFormatter = resumptionTokenFormatter;
        return this;
    }

    public FilterResolver getFilterResolver() {
        return filterResolver;
    }
}
