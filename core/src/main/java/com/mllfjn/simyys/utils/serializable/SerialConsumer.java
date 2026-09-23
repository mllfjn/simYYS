package com.mllfjn.simyys.utils.serializable;

import java.io.Serializable;
import java.util.function.Consumer;

public interface SerialConsumer<T> extends Serializable {
    void accept(T t);
}
