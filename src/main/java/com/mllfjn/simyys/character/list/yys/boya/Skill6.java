package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;

// √     先机:施放
// √     对生命最高的敌方目标造成100%的伤害
// √     lv2-伤害增加至110%
// √     lv3-伤害增加至120%
// √     lv4-伤害增加至130%
// √     lv5-伤害增加至140%,该次伤害不触发
//      敌方全体的御魂被动效果
// √     术印:伤害额外提升40%
class Skill6 extends PassiveSkill {
    public static final String SkillName = "秘术·豹袭";

    private final int multiplier;

    public Skill6(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 6);
        multiplier = 90 + level * 10 + shuYin * 40;
        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventBattleStart) {
                use();
                return true;
            }
            return false;
        });
    }

    private void use() {
        Character belongTo = getBelongTo();
        int level = getLevel();
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .get(Attribute.HP, CharacterFinder.Criteria.MAX);

        belongTo.doInteractive(interactive -> {
            AttackInfo interactiveInfo = AttackInfo
                    .createTypicalAttack(belongTo, this, target, multiplier, AttackType.DAN_TI);

            if (level >= 5) {
                interactiveInfo.setCalYuHun(false);
            }

            interactive.attack(interactiveInfo);
        });

        log(target);
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }
}
