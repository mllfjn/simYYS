package com.mllfjn.simyys.character.list.sr.luoxinfu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusStun;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.EffectInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "噬心食髓";
    private static final int[] multiplier = new int[]{0, 72, 76, 80, 83, 83};

    private final Skill2 skill2;

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
        boolean level5 = getLevel() >= 5;

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);

        double belongToSpeed = belongTo.getSpeed();
        for (Character character : list) {
            skill2.madeAttack(character);
            double speed = character.getSpeed();
            int baseRate = 15;
            if (speed < belongToSpeed) {
                baseRate += (int) ((belongToSpeed - speed) / 3);
            }
            EffectInfo effectInfo = interactive
                    .effect(this, character, baseRate, true, StatusStun.getSupplier(1));
            if (!effectInfo.isHit() && level5) {
                if (!character.isHaveStatus(StatusSXDY.class)) {
                    character.addStatus(new StatusSXDY(belongTo, character));
                }
            }
        }
        return Optional.empty();
    }

    class StatusSXDY extends Status {
        private StatusSXDY(Character from, Character belongTo) {
            super("噬心毒液", from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 1);
            displayName();
            beforeDelete(() -> from.doInteractive(interactive -> {
                AttackInfo attackInfo = AttackInfo
                        .createJianJieAttack(from, Skill3.this, belongTo, from.getAttack());
                double fromSpeed = from.getSpeed();
                double belongToSpeed = belongTo.getSpeed();
                int baseMultiplier = 136;
                if (belongToSpeed < fromSpeed) {
                    int addMultiplier = (int) (fromSpeed - belongToSpeed);
                    baseMultiplier += Math.min(addMultiplier, 50);
                }
                attackInfo.setMultiplier(baseMultiplier);
                interactive.attack(attackInfo);
                Skill3.this.useDone();
            }));
        }
    }
}
