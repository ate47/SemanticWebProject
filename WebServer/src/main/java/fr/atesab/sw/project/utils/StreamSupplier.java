package fr.atesab.sw.project.utils;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface StreamSupplier {
    InputStream get() throws IOException;
}
