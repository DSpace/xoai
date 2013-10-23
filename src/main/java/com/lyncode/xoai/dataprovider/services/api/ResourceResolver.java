package com.lyncode.xoai.dataprovider.services.api;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceResolver {
    InputStream getResource(String path) throws IOException;
}
