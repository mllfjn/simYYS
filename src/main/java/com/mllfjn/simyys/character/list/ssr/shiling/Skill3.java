package com.mllfjn.simyys.character.list.ssr.shiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;
import java.util.Optional;

// √     为友方目标准备梦想料理,使其进入用餐状态,持续1个回合
// √     用餐状态的友方目标回合结束后吃下食物,结束用餐并进入饱食
// √     无法对自身和已经处于用餐状态的友方目标释放
// √     lv2-消耗鬼火减少1点
// √     lv3-友方吃下食物时,获得庇护,持续1个回合
// √     lv4-友方吃下食物时,为全体友方恢复食灵攻击78%的生命
// √     lv5-敌方每有1个目标,飨食额外附加食灵攻击66%的真实伤害
// √     用餐:回合内造成的伤害减少30%;同时受到的伤害减少40%
// √     饱食:任意3个回合后,若未处于无法动作,发动飨食,对敌方全体造成伤害
// √     飨食:将用餐的友方目标回合内对敌方造成伤害的158%平均分配给所有敌方目标.
// √         该伤害无视防御,不会暴击,不触发携带者以及敌方的御魂,被动,仅受食灵增伤效果影响
// √         对单目标伤害最多不超过友方出场式神初始总攻击的3000%

class Skill3 extends Skill {
    private static final String SkillName = "梦想料理";

    private double xiangShiLimit;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, level >= 2 ? 2 : 3, 0, 3);
        belongTo.bp.addPriorityMove(belongTo, () -> {
            List<Character> targets = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterShiShen()
                    .getList();
            double limit = 0;
            for (Character target : targets) {
                limit += target.getInitAttack();
            }
            xiangShiLimit = limit * 30;
        });
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterSelf()
                .filter(character -> !character.isHaveStatus(StatusYongCan.class))
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        target.addStatus(new StatusYongCan(getBelongTo(), target, getLevel(), xiangShiLimit));

        return Optional.of(target);
    }

    static class StatusYongCan extends Status {
        private static final String StatusName = "用餐";

        private final int level;
        private final double xiangShiLimit;

        private double xiangShiCount;

        public StatusYongCan(Character from, Character belongTo, int level, double xiangShiLimit) {
            super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
            this.level = level;
            this.xiangShiLimit = xiangShiLimit;
            displayName();
            runOn(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.6, StatusName)
            );
            runOn(Trigger.AFTER_ROUND, _ -> {
                eat();
                delete();
            });
            runOn(Trigger.CAUSE_ATTACK, param -> {
                if (belongTo.isInRound()) {
                    xiangShiCount += ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber();
                }
            });
            runOn(Trigger.WHEN_ATTACK, param -> {
                if (belongTo.isInRound()) {
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.7, StatusName);
                }
            });
        }

        private void eat() {
            belongTo.addStatus(new StatusBaoShi(from, belongTo, xiangShiCount, level, xiangShiLimit));
            if (level >= 3) {
                StatusBaoShiBiHu.get(from, belongTo);
            }

            if (level >= 4) {
                Skill skill = Skill.getInstance(SkillName);
                double num = from.getAttack() * 0.78;
                from.doInteractive(interactive -> {
                    List<Character> targets = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .getList();
                    for (Character target : targets) {
                        interactive.recovery(skill, target, num);
                    }
                });
            }
        }

        static class StatusBaoShiBiHu extends StatusBiHu {

            private StatusBaoShiBiHu(Character from, Character belongTo) {
                super(from, belongTo);
            }

            public static void get(Character from, Character belongTo) {
                belongTo.getStatus(StatusBaoShiBiHu.class)
                        .orElseGet(() -> {
                            StatusBaoShiBiHu statusBaoShiBiHu = new StatusBaoShiBiHu(from, belongTo);
                            belongTo.addStatus(statusBaoShiBiHu);
                            return statusBaoShiBiHu;
                        }).refresh();
            }

            public void refresh() {
                duration(StatusDurationType.CHI_XU, 2);
            }
        }
    }

    static class StatusBaoShi extends Status {
        private static final String StatusName = "饱食";

        private final double num;
        private final boolean extraDamage;
        private final double limit;

        private int round = 4;

        public StatusBaoShi(Character from, Character belongTo, double num, int level, double limit) {
            super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
            this.num = num;
            this.extraDamage = level >= 5;
            this.limit = limit;

            Skill2.StatusBaoShiAttack.addStack(from);

            belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        if (round > 0) {
                            round--;
                        }

                        if (round == 0 && !belongTo.isUnderCrowdControl()) {
                            delete();
                            xiangShi();
                            return true;
                        }
                    }
                    return false;
                }
            });
            display(() -> StatusName + round);
        }

        private void xiangShi() {
            List<Character> targets = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();

            int size = targets.size();
            double numForeach = num * 1.58 / size;
            Skill skill = Skill.getInstance("飨食");
            Optional<Skill2.StatusOutRoundInfluence> oStatus = belongTo.getStatus(Skill2.StatusOutRoundInfluence.class);
            double extra;
            if (extraDamage) {
                extra = belongTo.getAttack() * 0.66 * size;
            } else {
                extra = 0;
            }

            belongTo.doInteractive(interactive -> {
                for (Character target : targets) {
                    AttackInfo attackInfo = AttackInfo.createRealAttack(belongTo, skill, target, numForeach);
                    attackInfo.setLimit(limit);

                    oStatus.ifPresent(status ->
                            status.run(Trigger.WHEN_ATTACK, new ParamAttackInfo(attackInfo)));

                    interactive.attack(attackInfo);

                    if (extraDamage) {
                        AttackInfo extraInfo = AttackInfo.createRealAttack(belongTo, skill, target, extra);
                        interactive.attack(extraInfo);
                    }
                }
            });
        }
    }
}
