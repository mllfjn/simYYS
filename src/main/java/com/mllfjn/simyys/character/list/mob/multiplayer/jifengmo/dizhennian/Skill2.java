package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.interactive.AttackInfo;
import javafx.scene.paint.Color;

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

        target.getStatus(StatusDZNReduceDefense.class).ifPresentOrElse(
                statusDebuff -> {
                    // 已经有了虚弱状态
                    int duration = statusDebuff.getDuration();
                    AttackInfo info = AttackInfo
                            .createRealAttack(target, this, target, duration * 0.3 * target.getMaxHp());
                    info.setCanThroughShield(true);
                    target.doInteractive(interactive -> interactive.attack(info));
                    // 红凝
                    Status.of("红凝", diZhenNian, target)
                            .display(() -> "红凝", Color.RED)
                            .addTo();
                    diZhenNian.setHongNing(target);
                },
                () -> target.addStatus(new StatusDZNReduceDefense(diZhenNian, target)));
        log(target);
        Optional<StatusBuff> oSBuff = target.getStatus(StatusBuff.class);
        if (oSBuff.isEmpty()) {
            target.addStatus(new StatusBuff(diZhenNian, target, diZhenNian.getBuffType(), 7));
        }
    }

    static class StatusDZNReduceDefense extends Status {
        public StatusDZNReduceDefense(Character from, Character belongTo) {
            super("地震鲶-减防", from, belongTo);
            duration(StatusDurationType.CHI_XU, 7);
            attribute(Attribute.DEFENCE, _ -> -belongTo.getInitDefense());
            displayNameAndDuration();
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
