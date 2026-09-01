package com.mllfjn.simyys.character.list.ssr.shiling;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.Interactive;

// √     lv5-若在回合外释放,灼伤敌方,为其附加20%易伤,持续2回合
class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "开锅";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        int level = getLevel();
        if (level >= 5 && !getBelongTo().isInRound()) {
            target.addStatus(new StatusZhuoShang(getBelongTo(), target));
        }
        super.usePrivate(interactive, target);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusZhuoShang extends Status {
        private static final String StatusName = "灼伤";

        public StatusZhuoShang(Character from, Character belongTo) {
            super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            displayNameAndDuration();
            attribute(Attribute.YI_SHANG, 20);
        }
    }
}
