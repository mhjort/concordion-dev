package org.concordion.api;

import java.io.IOException;
import java.io.InputStream;

public interface Target {

    void write(Resource resource, String s) throws IOException;

    void copyTo(Resource resource, InputStream inputStream) throws IOException;

    void delete(Resource resource) throws IOException;
}