package com.lyncode.xoai.dataprovider.services.impl;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResourceResolver implements ResourceResolver {
    private String basePath;

    public FileResourceResolver(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public InputStream getResource(String path) throws IOException {
        return new FileInputStream(new File(basePath, path));
    }
}
