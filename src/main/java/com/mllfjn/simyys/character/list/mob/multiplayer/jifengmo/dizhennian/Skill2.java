package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "光球";

    public Skill2(Character belongTo) {
        super(belongTo, -1, 0, 0, 2);
    }

    void tuGuangQiu(Skill4 skill4, Character target) {
        DiZhenNian diZhenNian = (DiZhenNian) getBelongTo();
        if (diZhenNian.isBeforeHouZi()) {
            skill4.setCooling(2);
        }

        target.getStatus(Skill4.StatusNingShiRecordDamage.StatusDZNBuffsDebuff.class).ifPresentOrElse(
                statusDebuff -> {
                    // 已经有了虚弱状态
                    int duration = statusDebuff.getDuration();
                    AttackInfo info = AttackInfo
                            .createRealAttack(target, this, target, duration * 0.3 * target.getMaxHp());
                    info.setCanThroughShield(true);
                    target.doInteractive(interactive -> interactive.attack(info));
                    // 红凝
                    target.addStatus(new Skill4.StatusNingShiRecordDamage.StatusHongNing(diZhenNian, target));
                },
                () -> target.addStatus(new Skill4.StatusNingShiRecordDamage.StatusDZNBuffsDebuff(diZhenNian, target)));
        log(target);
        Optional<StatusBuff> oSBuff = target.getStatus(StatusBuff.class);
        if (oSBuff.isEmpty()) {
            target.addStatus(new StatusBuff(diZhenNian, target, diZhenNian.getBuffType(), 7));
        }
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return false;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
