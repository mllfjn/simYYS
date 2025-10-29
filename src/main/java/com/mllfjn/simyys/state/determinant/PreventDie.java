package com.mllfjn.simyys.state.determinant;

public interface PreventDie {
    default boolean effective() {
        return true;
    }

    void action();
}
