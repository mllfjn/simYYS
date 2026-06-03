package com.mllfjn.simyys.utils;

import java.io.Serializable;
import java.util.function.Function;

public interface SerialFunction<T, R> extends Function<T, R>, Serializable {
}
