package com.mllfjn.simyys.character.status.determinant;

public interface PreventDie {
    default boolean preventDieEffective() {
        return true;
    }

    void preventDie(double excessDamage);

    String getName();
}
