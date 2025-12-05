package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.EffectInfo;

import java.util.ArrayList;
import java.util.List;

public class SkillRecord {
    private final int skillId;
    private final int realCost;
    private final List<AttackInfo> attackInfos = new ArrayList<>();
    private final List<EffectInfo> effectInfos = new ArrayList<>();

    public SkillRecord(int skillId, int realCost) {
        this.skillId = skillId;
        this.realCost = realCost;
    }
}
