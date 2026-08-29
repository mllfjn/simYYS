package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.StatusSupplier;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "刀剑乱舞";

    public Skill3(Character belongTo) {
        super(belongTo, 0, 2, 4, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> targets = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        // 同时玩家有75%概率获得减防效果,两回合内减少50%防御
        interactive.effect(this, targets, 75, true, StatusDefense.getSupplier());
        // 对所有玩家造成2段伤害
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);

        // 当场上有部下时,邀战所有部下
        List<Character> bx = new CharacterFinder(belongTo)
                .filterTeammate()
                .filter(c -> c instanceof Skill5.CharacterBX)
                .getList();
        if (!bx.isEmpty()) {
            for (Character character : bx) {
                character.xieZhan(this, RateController
                        .choose(character.name + "攻击目标", targets, Character::getName, bp.calc)
                );
            }
        }
        return Optional.empty();
    }

    static class StatusDefense extends Status implements AttributeModifier, Displayable {
        private static final String StatusName = "破防";

        public StatusDefense(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusDefense.class, (from, to) ->
                    to.getStatus(StatusDefense.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 2) {
                                    status.setDuration(2);
                                }
                            },
                            () -> to.addStatus(new StatusDefense(from, to))
                    )
            );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitDefense() * 0.5;
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }
    }
}
