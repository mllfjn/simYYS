package com.mllfjn.simyys.starter.info;

import com.google.gson.Gson;

public class InitInfo {
    CharacterInfo[] characterInfo;
    SkillChangeInfo[] skillChangeInfo;
    FlagChangeInfo[] flagChangeInfo;
    public InitInfo(CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
    }
}
