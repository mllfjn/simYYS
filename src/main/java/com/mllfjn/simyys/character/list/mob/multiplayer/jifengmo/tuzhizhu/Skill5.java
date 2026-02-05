package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;
import com.mllfjn.simyys.character.status.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class Skill5 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "破损";

    public Skill5(TuZhiZhu belongTo) {
        super(belongTo, -1, 5);
        CharacterSummonBase characterTui = new CharacterSummonBase(belongTo.bp, "土蜘蛛-腿", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.SPEED;
                            }

                            @Override
                            public double getInfluence(Attribute attribute) {
                                return -50;
                            }
                        });
            }
        };
        CharacterSummonBase characterBei = new CharacterSummonBase(belongTo.bp, "土蜘蛛-背", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.DEFENCE;
                            }

                            @Override
                            public double getInfluence(Attribute attribute) {
                                return belongTo.getInitDefense() * -0.3;
                            }
                        });
            }
        };
        CharacterSummonBase characterQian = new CharacterSummonBase(belongTo.bp, "土蜘蛛-钳", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.ATTACK;
                            }

                            @Override
                            public double getInfluence(Attribute attribute) {
                                return belongTo.getInitAttack() * -0.3;
                            }
                        });
            }
        };

        characterTui.setMaxHp(9999999999L, true);
        characterBei.setMaxHp(9999999999L, true);
        characterQian.setMaxHp(9999999999L, true);

        belongTo.bp.atBattleStart(() -> {
            belongTo.bp.addCharacter(characterTui);
            belongTo.bp.addCharacter(characterBei);
            belongTo.bp.addCharacter(characterQian);
        });
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t右方大腿被破坏时,速度降低50
                √\t中间背部被破坏时,防御降低30%
                √\t左方钳子被破坏时,攻击降低30%
                \t\t并召唤白茧
                \t白茧怎么也不可能打不死对吧
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
