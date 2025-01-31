package com.mllfjn.simyys;

import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import javafx.stage.Stage;

public class BattlePane {
    CharacterInfo[] characterInfo;
    SkillChangeInfo[] skillChangeInfo;
    FlagChangeInfo[] flagChangeInfo;
    public BattlePane(Stage stage, CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
    }
}
