package com.mllfjn.simyys.utils.serializable;

import java.io.Serializable;

public interface SerialSupplier<T> extends Serializable {
    T get();
}
