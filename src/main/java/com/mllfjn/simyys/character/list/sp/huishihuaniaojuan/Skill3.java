package com.mllfjn.simyys.character.list.sp.huishihuaniaojuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill3 extends Skill {
    private static final double[] CHUAN_DAO_RATIO = new double[]{0, 0.1, 0.15, 0.15, 0.2, 0.2};
    private static final double[] INCREASE_QUN_TI = new double[]{0, 0, 0, 1.1, 1.1, 1.2};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, "绘梦浮生", 2, 0, 3);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return getBelongTo().bp().canSummon(getBelongTo().team) && super.canUse(bp);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();

        Character target = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .filterSummon(false)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        new CharacterFuShengHuaYing(((HuiShiHuaNiaoJuan) getBelongTo()),
                target, CHUAN_DAO_RATIO[level], INCREASE_QUN_TI[level]
        );

        return Optional.of(target);
    }

    static class CharacterFuShengHuaYing extends CharacterSummonBase {
        public CharacterFuShengHuaYing(HuiShiHuaNiaoJuan owner, Character target,
                                       double chuanDaoRatio, double increaseQunTiDamage
        ) {
            BattlePane bp = owner.bp();
            super(bp, "浮生画影", owner.team);
            bp.addCharacter(this);
            bp.situation.canNotChangeLocation(this);
            bp.situation.unSelectable(this);

            forceSetMaxHp(owner.getHp(), true);

            new StatusZhuMo(owner, target, chuanDaoRatio, increaseQunTiDamage);
        }

        class StatusZhuMo extends Status {
            private final Skill damageSkill = Skill.getInstance("逐墨");
            private Skill attackFromSkill;

            protected StatusZhuMo(HuiShiHuaNiaoJuan from, Character belongTo,
                                  double chuanDaoRatio, double increaseQunTiDamage
            ) {
                super("逐墨", from, belongTo);
                displayNameAndDuration();
                from.replaceSkill3Special(this);
                duration(StatusDurationType.WEI_CHI, 4);
                StatusAdder<Status> adder = from.bp().addStatusAdder(c -> {
                    if (c.team != from.team) {
                        return Status.of("逐墨受到伤害监听", from, c)
                                .runOn(Trigger.AFTER_ATTACK, param -> {
                                    AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                                    if (attackInfo.getAttackType() != AttackType.CHUAN_DAO) {
                                        attack(attackInfo.getSkill(),
                                                chuanDaoRatio * attackInfo.getTraceableNumber().getNumber()
                                        );
                                    }
                                });
                    } else if (increaseQunTiDamage > 0) {
                        return Status.of("逐墨群体增伤", from, c)
                                .runOn(Trigger.WHEN_ATTACK, param -> {
                                    AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                                    if (attackInfo.getAttackType() == AttackType.QUN_TI) {
                                        attackInfo.getTraceableNumber().mul(increaseQunTiDamage, "逐墨群体增伤");
                                    }
                                });
                    } else {
                        return null;
                    }
                });
                beforeDelete(() -> {
                    CharacterFuShengHuaYing.this.die();
                    adder.deleteAndRemove();
                    from.backToNormal();
                });
                addTo();
            }

            void attack(Skill fromSkill, double num) {
                if (attackFromSkill == null) {
                    attackFromSkill = fromSkill;
                    attackFromSkill.addSkillEndListener(() -> {
                        damageSkill.useDone();
                        attackFromSkill = null;
                    });
                }
                from.doInteractiveNoAutoUseDone(damageSkill, interactive -> {
                            AttackInfo chuanDaoAttack = AttackInfo.createChuanDaoAttack(from, damageSkill, belongTo, num);
                            chuanDaoAttack.setLimit(from.getMaxHp() * 12);
                            interactive.attack(chuanDaoAttack);
                        }
                );
            }
        }
    }
}
