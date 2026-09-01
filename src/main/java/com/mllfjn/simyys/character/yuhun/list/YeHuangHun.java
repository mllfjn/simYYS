package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
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

    class StatusYHHListener extends Status {
        private final Set<Character> effected = new HashSet<>();

        public StatusYHHListener(Character character) {
            super(YuHunName + "监听", character);
            // 回合开始时开始监听控制
            runOn(Trigger.BEFORE_ROUND, _ -> enableAction(Trigger.MAKING_CROWD_CONTROL));
            // 造成控制时加一层并且计入已生效名单
            runOnAndDisable(Trigger.MAKING_CROWD_CONTROL, param ->
                    effected.add(((ParamAddCrowdControl) param).getEffectInfo().getTarget())
            );
            // 回合结束时添加状态并且清除名单
            runOn(Trigger.AFTER_ROUND, _ -> {
                if (!effected.isEmpty()) {
                    List<Character> targets = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .filterSummon(false)
                            .getList();

                    for (Character target : targets) {
                        StatusYHH.addStack(from, target, effected.size());
                    }

                    effected.clear();
                    YeHuangHun.this.yuHunEffect();
                }
                disableAction(Trigger.MAKING_CROWD_CONTROL);
            });
        }

        static class StatusYHH extends Status {
            private int stack;

            private StatusYHH(Character from, Character belongTo) {
                super(YuHunName, from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);

                duration(StatusDurationType.WEI_CHI, 1);
                addTo();
            }

            static void addStack(Character from, Character belongTo, int stack) {
                StatusYHH statusYHH = belongTo.getStatus(StatusYHH.class)
                        .orElseGet(() -> new StatusYHH(from, belongTo));
                statusYHH.stack = Math.min(6, statusYHH.stack + stack);
                statusYHH.attribute(Attribute.SPEED, 15 * statusYHH.stack);
            }
        }
    }
}
