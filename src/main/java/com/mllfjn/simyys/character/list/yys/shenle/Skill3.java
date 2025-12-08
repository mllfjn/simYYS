package com.mllfjn.simyys.character.list.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.battleevent.EventBattleStart;

import java.util.Optional;

class Skill3 extends Skill {
    public static final String SkillName = "通灵·疾风";
    public static final int[] multiplier = new int[]{0, 10, 15, 20, 25, 30};

    private final int shuYin;
    private boolean mobBattle = false;

    public Skill3(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 1, 2);
        this.shuYin = shuYin;
        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventBattleStart) {
                mobBattle = belongTo.bp.isMobBattle(belongTo);
                if (mobBattle) {
                    // 且不进入冷却
                    setCoolDown(0);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        // 增加其(系数)攻击
        int multi = multiplier[getLevel()];
        if (mobBattle) {
            multi += 8 * shuYin;
        }

        target.addStatus(new StatusJF(belongTo, target, multi));

        if (mobBattle) {
            // 与怪物战斗时,使目标获得1个回合
            belongTo.getInteractive().getNewRound(target);
        } else {
            // 与阴阳师战斗时,交换神乐与目标的行动条位置
            belongTo.setLocation(target.getLocation());
            target.setLocation(100);
        }

        return Optional.of(target);
    }

    public static class StatusJF extends Status implements AttributeModifier {
        private final int percent;

        public StatusJF(Character from, Character belongTo, int percent) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
            this.percent = percent;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return belongTo.getInitBaseAttack() * percent / 100;
        }
    }
}
