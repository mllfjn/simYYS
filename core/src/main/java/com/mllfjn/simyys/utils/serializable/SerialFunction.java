package com.mllfjn.simyys.utils.serializable;

import java.io.Serializable;
import java.util.function.Function;

public interface SerialFunction<T, R> extends Serializable {
    R apply(T t);
}
