package com.mllfjn.simyys.character.status.determinant;

public interface IgnoreDebuff {
    default boolean ignoreDebuffEffective() {
        return true;
    }
}
