package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

import java.util.List;

public class TuFo extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "涂佛";

    private StatusTFListener status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
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
    static class StatusTFListener extends Status {
        public StatusTFListener(Character character) {
            super(YuHunName, character);

            // 回合开始时开始检测
            runOn(Trigger.BEFORE_ROUND, _ -> {
                enableAction(Trigger.USED_SKILL);
                enableAction(Trigger.AFTER_ROUND);
            });
            // 如果释放了技能,则停止检测
            runOnAndDisable(Trigger.USED_SKILL, _ -> {
                disableAction(Trigger.USED_SKILL);
                disableAction(Trigger.AFTER_ROUND);
            });
            // 如果回合结束时还处于检测状态,则生效
            runOnAndDisable(Trigger.AFTER_ROUND, _ -> {
                StatusTF.install(belongTo);
                disableAction(Trigger.USED_SKILL);
                disableAction(Trigger.AFTER_ROUND);
            });
        }
    }

    static class StatusTF extends Status {

        public StatusTF(Character from, Character belongTo) {
            super(YuHunName, from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.WEI_CHI, 2);
            int increase = from == belongTo ? 30 : 15;
            attribute(Attribute.EFFECT_RESIST_RATE, increase);
            attribute(Attribute.ZENG_SHANG, increase);
            displayNameAndDuration();
        }

        public static void install(Character character) {
            List<Character> list = new CharacterFinder(character)
                    .filterTeammate()
                    .getList();

            for (Character target : list) {
                target.getStatus(StatusTF.class).ifPresentOrElse(
                        status -> status.duration(2),
                        () -> target.addStatus(new StatusTF(character, target))
                );
            }
        }
    }
}
