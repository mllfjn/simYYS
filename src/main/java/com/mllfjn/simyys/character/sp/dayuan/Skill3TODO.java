package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

class Skill3TODO extends Skill {
    public static final String privateName = "与世结缘";

    public Skill3TODO(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        // 目标首先是绿标，然后是结缘的式神，最后是攻击最高的
        Character target;
        if (bp.autoTo[getBelongTo().team] != null) {
            target = bp.autoTo[getBelongTo().team];
        } else if (getBelongTo().getState(StateCombined.privateName) instanceof StateCombined sfc){
            target = sfc.from;
        } else {
            target = CharacterFinder.find(bp.characters, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        }
        lastUsedTarget = target;
        Interactive interactive = getBelongTo().getInteractive(bp);

        // 获得1层神力
        StateShenLi.addStack(getBelongTo(), 1);
        // 并治疗友方目标生命上限8%的生命
        Info heal = interactive.heal(privateName, target, 8);
        // lv5 - 若治疗暴击,则额外提升该目标暴击伤害 **必须在治疗目标之后,治疗其他队友之前**
        if (getLevel() == 5 && heal.getBaoJi()) {
            target.addState(new StateCritPower(getBelongTo(), target));
        }

        int ShenLiStack = ((StateShenLi)getBelongTo().getState(StateShenLi.privateName)).getStack();

        // 自身神力3层及以上时,额外治疗所选目标以外的友方生命上限8%的生命
        if (ShenLiStack >= 3) {
            List<Character> targets = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
            targets.remove(target);
            interactive.heal(privateName, targets, 8);
        }


    }
}
