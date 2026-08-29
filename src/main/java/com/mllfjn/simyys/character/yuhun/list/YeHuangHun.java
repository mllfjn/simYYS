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
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YeHuangHun extends YuHun implements YuHunSealResponse, YuHunUnfullMark {
    public static final String YuHunName = "夜荒魂";
    private StatusYHHListener status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusYHHListener(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusYHHListener extends Status implements StatusRunnable {
        private boolean counting = false;
        private final Set<Character> effected = new HashSet<>();

        public StatusYHHListener(Character character) {
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
                    int speed = 15 * Math.min(6, effected.size());
                    List<Character> targets = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .filterSummon(false)
                            .getList();

                    for (Character target : targets) {
                        target.addStatus(new StatusYHH(from, target, speed));
                    }

                    effected.clear();
                    YeHuangHun.this.yuHunEffect();
                }
            }
            return false;
        }

        static class StatusYHH extends Status implements AttributeModifier {
            private final int speed;

            public StatusYHH(Character from, Character belongTo, int speed) {
                super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                this.speed = speed;

                duration(StatusDurationType.WEI_CHI, 1);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.SPEED;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return speed;
            }
        }
    }
}
