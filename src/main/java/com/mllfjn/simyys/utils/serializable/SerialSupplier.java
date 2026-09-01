package com.mllfjn.simyys.utils.serializable;

import java.io.Serializable;

public interface SerialSupplier<T> extends Serializable {
    SerialSupplier<Boolean> ALWAYS_TRUE = () -> true;
    SerialSupplier<Boolean> ALWAYS_FALSE = () -> false;

    T get();
}
