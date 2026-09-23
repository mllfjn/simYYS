package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class SkillWuJianShaLu extends Skill {
    private static final String SkillName = "无间杀戮";

    private final Skill2 skill2;

    private boolean replaced;

    public SkillWuJianShaLu(Character belongTo, Skill2 skill2) {
        super(belongTo, -1, 0, 0, 2);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    void replace() {
        if (!replaced) {
            ((AXiuLuo) getBelongTo()).skillWuJianShaLu = this;
            getBelongTo().removeSkill(skill2);
            getBelongTo().addSkill(this, true);
            replaced = true;
        }
    }

    void beforeRound() {
        AXiuLuo belongTo = (AXiuLuo) getBelongTo();
        StatusLiXing statusLiXing = belongTo.statusLiXing;
        if (statusLiXing.getStack() < 9) {
            Character target = new CharacterFinder(belongTo)
                    .filterSelf()
                    .getRandom();
            belongTo.doInteractive(interactive -> {
                interactive.attackTypical(this, target, 263, AttackType.DAN_TI);
                List<Character> list = new CharacterFinder(belongTo)
                        .filterSelf()
                        .filter(c -> c != target)
                        .getList();
                interactive.attackTypical(this, list, 113, AttackType.QUN_TI);
            });
            log(target);
            useDone();
        } else {
            backToNormal();
        }
    }

    void backToNormal() {
        AXiuLuo belongTo = (AXiuLuo) getBelongTo();
        belongTo.skillWuJianShaLu = null;
        belongTo.removeSkill(this);
        belongTo.addSkill(skill2, true);
        replaced = false;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
