package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;
import com.mllfjn.simyys.character.yuhun.YuHun;

public class QingNvFang extends YuHun {
    public static final String YuHunName = "青女房";

    public static Class<? extends Status> getStatusClass() {
        return StatusQNFBF.class;
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        character.addStatus(new StatusQNFListener(character));
    }

    static class StatusQNFListener extends Status {

        public StatusQNFListener(Character character) {
            super(YuHunName, character);
            preventDie(() -> !belongTo.isYuHunSeal(), _ -> {
                belongTo.bp.interactive.addYuHunEffectLog(belongTo, QingNvFang.YuHunName);

                // 受到致命伤害时,移除所有状态和印记
                belongTo.deleteStatusIf(status ->
                        status.statusForm == StatusForm.ZHUANG_TAI || status.statusForm == StatusForm.YIN_JI
                );
                // 恢复100%生命
//            belongTo.doInteractive(interactive -> interactive.recovery(belongTo, belongTo.getMaxHp()));
                // 因为游戏里触发没有显示,所以不用interactive直接改生命
                belongTo.setHp(belongTo.getMaxHp());
                // 并使自身冰封1回合
                belongTo.addStatus(new StatusQNFBF(belongTo));

                delete();
            });
        }
    }

    static class StatusQNFBF extends Status implements RejectAllStatuses {

        public StatusQNFBF(Character character) {
            super("青女房冰封", character);
            displayName();
            attribute(Attribute.DEFENCE, _ -> belongTo.getInitDefense());
            // 若冰封结束时仍存活,则再次恢复100%生命
            runOn(Trigger.BEFORE_ROUND, _ -> {
                belongTo.doInteractive(
                        interactive -> interactive.recovery(Skill.getInstance(QingNvFang.YuHunName),
                                belongTo, belongTo.getMaxHp()
                        )
                );
                delete();
            });
        }
    }
}
