package com.mllfjn.simyys.determinant;

import com.mllfjn.simyys.character.Character;

public interface ForbidIncrease {
    default boolean effective(Character comeFrom) {
        return true;
    }
}
