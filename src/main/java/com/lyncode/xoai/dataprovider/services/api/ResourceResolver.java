package com.lyncode.xoai.dataprovider.services.api;

import java.io.InputStream;

public interface ResourceResolver {
    InputStream getResource (String path);
}
