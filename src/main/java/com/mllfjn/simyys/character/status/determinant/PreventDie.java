package com.mllfjn.simyys.character.status.determinant;

public interface PreventDie {
    default boolean effective() {
        return true;
    }

    void preventDie(double excessDamage);

    String getName();
}
