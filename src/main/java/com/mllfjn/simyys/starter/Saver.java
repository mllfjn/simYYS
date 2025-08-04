package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;

import java.io.Serializable;

public class Saver implements Serializable {
    final CharacterInfo[] characterInfo;
    final SkillChangeInfo[] skillChangeInfo;
    final FlagChangeInfo[] flagChangeInfo;
    public Saver(CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
    }
}
