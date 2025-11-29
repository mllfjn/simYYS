package com.mllfjn.simyys.status.determinant;

public interface PreventDie {
    default boolean effective() {
        return true;
    }

    void preventDie();

    String getName();
}
