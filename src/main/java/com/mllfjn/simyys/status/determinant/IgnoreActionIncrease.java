package com.mllfjn.simyys.status.determinant;

import com.mllfjn.simyys.character.Character;

public interface IgnoreActionIncrease {

    /**
     * @param from 拉条发起者
     * @return return true if 免疫拉条
     */
    default boolean effective(Character from) {
        return true;
    }
}
