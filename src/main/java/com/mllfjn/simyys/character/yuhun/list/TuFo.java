package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

import java.util.List;

public class TuFo extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "涂佛";

    private StatusTFListener status;

    @Override
    public void init(Character character) {
        super.init(character);
        status = new StatusTFListener(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }


    // 回合结束时，若本回合普攻或无法动作，使全体友方提升15%效果抵抗、伤害，维持2回合。自身提升双倍
    //  预期实现方式:回合开始时开始检测,如果使用技能则标记为false,如果true则回合结束后生效
    static class StatusTFListener extends Status implements StatusRunnable {
        private boolean isDetecting = false;

        public StatusTFListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND // 回合开始时开始检测
                    || (isDetecting && trigger == Trigger.USED_SKILL)  // 如果释放了技能,则停止检测
                    || (isDetecting && trigger == Trigger.AFTER_ROUND); // 如果回合结束时还处于检测状态,则生效
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                isDetecting = true;
            } else if (trigger == Trigger.USED_SKILL) {
                isDetecting = false;
            } else {
                StatusTF.install(belongTo);
            }
            return false;
        }
    }

    static class StatusTF extends Status implements AttributeModifier, Displayable {

        public StatusTF(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.WEI_CHI, 2);
        }

        public static void install(Character character) {
            List<Character> list = new CharacterFinder(character)
                    .filterTeammate()
                    .getList();

            for (Character target : list) {
                target.getStatus(StatusTF.class).ifPresentOrElse(
                        status -> status.setDuration(2),
                        () -> target.addStatus(new StatusTF(character, target))
                );
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE || attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            if (from == belongTo) {
                return 30;
            } else {
                return 15;
            }
        }

        @Override
        public String getDisplayText() {
            return YuHunName + getDuration();
        }
    }
}
