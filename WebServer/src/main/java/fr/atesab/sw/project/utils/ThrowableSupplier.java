package fr.atesab.sw.project.utils;

@FunctionalInterface
public interface ThrowableSupplier<T> {
    T get() throws Exception;
}