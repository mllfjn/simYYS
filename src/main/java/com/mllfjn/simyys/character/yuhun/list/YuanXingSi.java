package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YuanXingSi extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "元兴寺";

    private StatusYXSListener status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusYXSListener(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusYXSListener extends Status implements StatusRunnable {
        private final Set<Character> effected = new HashSet<>();

        private boolean counting = false;

        public StatusYXSListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND
                    || (counting && trigger == Trigger.MAKING_CROWD_CONTROL)
                    || trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 回合开始时开始计算
            // 造成伤害时加一层并且计入已生效名单
            // 回合结束时添加状态并且清除名单
            if (trigger == Trigger.BEFORE_ROUND) {
                counting = true;
            } else if (trigger == Trigger.MAKING_CROWD_CONTROL && param instanceof ParamAddCrowdControl pac) {
                effected.add(pac.getEffectInfo().getTarget());
            } else {
                counting = false;
                if (!effected.isEmpty()) {
                    int count = effected.size();
                    List<Character> targets = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .filterSummon(false)
                            .getList();

                    for (Character target : targets) {
                        StatusYXS.add(belongTo, target, count);
                    }

                    effected.clear();
                    YuanXingSi.this.yuHunEffect();
                }
            }
            return false;
        }

        static class StatusYXS extends Status implements AttributeModifier, Displayable {
            private int stack;

            public StatusYXS(Character from, Character belongTo) {
                super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }

            public static void add(Character from, Character belongTo, int addStack) {
                // 首先遍历查看剩余回合数2和1的元兴寺状态
                // 其中剩余2回合的一定比剩余1回合的添加的晚,所以如果找到了剩余2回合的,说明后面没有其他元兴寺状态了
                // 如果找到剩余1回合并且有8层,说明不需要继续了
                StatusYXS statusD2 = null;
                StatusYXS statusD1 = null;
                for (Status status : belongTo.getStatuses()) {
                    if (status instanceof StatusYXS y) {
                        if (y.getDuration() == 2) {
                            statusD2 = y;
                            break;
                        } else {
                            if (y.stack == 8) {
                                return;
                            }
                            statusD1 = y;
                        }
                    }
                }

                // 剩余回合2可以有的层数:8-剩余1的层数
                int duration2CanAdd;
                if (from == belongTo) {
                    duration2CanAdd = statusD2 == null ? 8 : 8 - statusD2.stack;
                    statusD2 = null;
                } else {
                    duration2CanAdd = statusD1 == null ? 8 : 8 - statusD1.stack;
                }

                if (duration2CanAdd > 0) {
                    if (statusD2 != null) {
                        statusD2.stack = Math.min(statusD2.stack + addStack, duration2CanAdd);
                    } else {
                        StatusYXS statusYXS = new StatusYXS(from, belongTo);
                        statusYXS.stack = Math.min(addStack, duration2CanAdd);
                        if (from == belongTo) {
                            statusYXS.setDuration(3);
                        }
                        belongTo.addStatus(statusYXS);
                    }
                }
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.ZENG_SHANG;
            }

            @Override
            public double getInfluence(Attribute attribute) {
                return 5 * stack;
            }

            @Override
            public String getDisplayText() {
                return YuHunName + stack + "-" + getDuration();
            }
        }
    }
}
