package com.mllfjn.simyys.character.list.ssr.tianzhao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "神圣裁决";
    private static final int[] Multipliers = new int[]{0, 72, 79, 86, 93, 100};

    private final Skill2 skill2;

    StatusTianHui statusTianHui;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        int multiplier = Multipliers[getLevel()];
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        for (int i = 0; i < 3; i++) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            interactive.attackTypical(this, list, multiplier, AttackType.QUN_TI);
        }

        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);
        }

        Character tianHuiTarget = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .filterShiShen()
                .get(Attribute.INIT_ATTACK, CharacterFinder.Criteria.MAX);

        if (tianHuiTarget != null) {
            if (statusTianHui != null && tianHuiTarget == statusTianHui.belongTo) {
                statusTianHui.setDuration(2);
            } else {
                if (statusTianHui != null) {
                    statusTianHui.delete();
                }
                tianHuiTarget.addStatus(new StatusTianHui(belongTo, tianHuiTarget, skill2, this));
            }
            interactive.increaseLocation(tianHuiTarget, 20);
        }

        return Optional.of(target);
    }
}
