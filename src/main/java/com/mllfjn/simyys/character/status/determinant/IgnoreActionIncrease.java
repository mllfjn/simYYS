package com.mllfjn.simyys.character.status.determinant;

import com.mllfjn.simyys.character.Character;

public interface IgnoreActionIncrease {

    /**
     * @deprecated 使用{@link com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange}替代
     * @param from 拉条发起者
     * @return return true if 免疫拉条
     */
    @Deprecated
    default boolean effective(Character from) {
        return true;
    }
}
