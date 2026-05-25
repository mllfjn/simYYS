package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class Skill5 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "破损";

    private int count = 3;

    public Skill5(TuZhiZhu belongTo, Skill7 skill7) {
        super(belongTo, -1, 5);

        CharacterSummonBase characterTui = new CharacterSummonBase(belongTo.bp, "土蜘蛛-腿", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            {
                setInitDefense(686);
            }

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                partDie();
                belongTo.skill6.summon();
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.SPEED;
                            }

                            @Override
                            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                                return -50;
                            }
                        });
            }
        };
        CharacterSummonBase characterBei = new CharacterSummonBase(belongTo.bp, "土蜘蛛-背", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            {
                setInitDefense(774);
            }

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                partDie();
                belongTo.skill6.summon();
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.DEFENCE;
                            }

                            @Override
                            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                                return belongTo.getInitDefense() * -0.3;
                            }
                        });
            }
        };
        CharacterSummonBase characterQian = new CharacterSummonBase(belongTo.bp, "土蜘蛛-钳", belongTo.team) {
            private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

            {
                setInitDefense(778);
            }

            @Override
            protected EventHandler<MouseEvent> getEventHandler() {
                return clearHpHandler.getEventHandler();
            }

            @Override
            protected void dieHandle() {
                partDie();
                belongTo.addStatus(
                        new StatusModifyAttribute(belongTo, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL) {
                            @Override
                            public boolean isAffectAttribute(Attribute attribute) {
                                return attribute == Attribute.ATTACK;
                            }

                            @Override
                            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                                return belongTo.getInitAttack() * -0.3;
                            }
                        });
                belongTo.skill6.clearSpider();
                for (int i = 0; i < 3; i++) {
                    belongTo.bp.addCharacter(new CharacterSummonBase(belongTo.bp, "白茧", belongTo.team) {
                        {
                            skill7.tZZReduceEnable();
                            forceSetMaxHp(99999999, true);
                        }

                        private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

                        @Override
                        protected EventHandler<MouseEvent> getEventHandler() {
                            return clearHpHandler.getEventHandler();
                        }

                        @Override
                        protected void dieHandle() {
                            skill7.tZZReduceDisable();
                        }
                    });
                }
            }
        };

        characterTui.forceSetMaxHp(9999999999L, true);
        characterBei.forceSetMaxHp(9999999999L, true);
        characterQian.forceSetMaxHp(9999999999L, true);

        belongTo.bp.atBattleStart(() -> {
            belongTo.addStatus(new StatusUnselectable(belongTo, belongTo));
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
                √\t\t并召唤白茧
                \t应该不至于打不死白茧吧?
                \t终于知道土蜘蛛为什么开局会掉血了!
                \t\t土蜘蛛召唤三个部位类似先机,并且有生效顺序.如果博雅先加载,那么豹子就会扑本体;如果土蜘蛛先加载,豹子就扑部位
                """;
    }

    void partDie() {
        if (count == 1) {
            TuZhiZhu belongTo = (TuZhiZhu) getBelongTo();
            belongTo.removeStatus(StatusUnselectable.class);
            belongTo.canChangeStage = true;

            return;
        }
        count--;
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
