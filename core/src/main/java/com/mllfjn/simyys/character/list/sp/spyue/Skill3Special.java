package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill3Special extends Skill {
    private static final String SkillName = "崩山炙落";
    private final Skill2 skill2;

    private boolean used;

    public Skill3Special(Character belongTo, Skill2 skill2) {
        super(belongTo, -1, 0, 0, 3);
        this.skill2 = skill2;

        Status.of(SkillName + "伤害上限", belongTo)
                .runOn(Trigger.WHEN_ATTACK, param -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                    attackInfo.setLimit(belongTo.getInitAttack() * 20);
                })
                .retainAfterDie()
                .retainAfterChangeWave()
                .addTo();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        belongTo.statusRun(Trigger.WILL_USE_SKILL, new ParamUseSkill(this, null, 0));
        List<Character> list = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getList();
        double defense = belongTo.getDefence();
        belongTo.doInteractive(interactive -> {
            for (int i = 0; i < 3; i++) {
                interactive.attack(this, list, c -> {
                    AttackInfo attackInfo = new AttackInfo(belongTo, this, c, AttackType.QUN_TI, defense);
                    attackInfo.setMultiplier(600);
                    return attackInfo;
                });
            }
        });
        for (Character character : list) {
            StatusZhiKao.addStack(belongTo, character, skill2, 5);
        }
        if (!used) {
            used = true;
            List<Character> teammate = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            for (Character character : teammate) {
                Status.of(SkillName + "增伤", belongTo, character)
                        .attribute(Attribute.ZENG_SHANG, 30)
                        .addTo();
            }
        }

        log(null);
        return Optional.empty();
    }
}
