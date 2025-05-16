package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;

public class JsonSaver {
    final CharacterInfo[] characterInfo;
    final SkillChangeInfo[] skillChangeInfo;
    final FlagChangeInfo[] flagChangeInfo;
    public JsonSaver(CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
    }
}
