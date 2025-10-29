package com.mllfjn.simyys.state.determinant;

import com.mllfjn.simyys.character.Character;

public interface IgnoreActionIncrease {
    default boolean effective(Character from) {
        return true;
    }
}
