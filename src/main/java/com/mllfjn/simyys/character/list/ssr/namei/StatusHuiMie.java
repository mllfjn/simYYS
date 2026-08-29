package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Trigger;
import javafx.scene.paint.Color;

public class StatusHuiMie extends Status {
    private static final String text = "毁灭";
    // 毁灭等级
    private int stack = 1;
    // 免死是否可以触发
    private boolean canTrigger = true;
    // 剩余可以触发免死的次数
    private int times = 3;

    public StatusHuiMie(NaMei from, Character belongTo, int level, boolean awakening) {
        super(text, from, belongTo);
        type(StatusType.BUFF, StatusForm.YIN_JI);

        // 我方场上至多存在1个此效果
        from.getStatus(StatusNaMeiFlag.class).ifPresent(status -> status.statusHuiMie.delete());

        display(() -> {
            StringBuilder sb = new StringBuilder();
            // 毁灭6
            sb.append(text).append(stack);
            // 免死生效
            if (isPreventDie()) {
                sb.append(Status.DELIMITER).append("金盾");
            }

            return sb.toString();
        }, Color.ORANGE);
        runOn(Trigger.BEFORE_ROUND, _ -> {
            if (stack < 6) {
                stack++;
            }

            if (!canTrigger && times > 0) {
                canTrigger = true;
            }

            if (stack == 6 && times == 0) {
                removeAction(Trigger.BEFORE_ROUND);
            }
        });
        // 降低防御(stack - 1) * 100，这里算的是+防御，所以负的
        attribute(Attribute.DEFENCE, _ -> 100.0 * (1 - stack));
        // 造成伤害时无视100点防御
        attribute(Attribute.IGNORE_DEFENCE, _ -> {
            // lv4-友方每因毁灭降低100点防御,额外无视40点防御,至多额外无视200点防御
            return 100.0 + (level >= 4 ? (stack - 1) * 40 : 0);
        });
        // 那美攻击时无视护甲的状态
        StatusNaMeiFlag naMeiFlag = new StatusNaMeiFlag(from, this, awakening);
        from.addStatus(naMeiFlag);
        // 在毁灭移除时同时移除那美身上的无视防御
        beforeDelete(naMeiFlag::delete);
        // 免死
        // 只有那美自身在场时可以触发
        preventDie(
                () -> canTrigger && from.alive,
                _ -> {
                    canTrigger = false;
                    belongTo.setHp(1);
                    times--;
                }
        );
    }

    static class StatusNaMeiFlag extends Status {
        private final StatusHuiMie statusHuiMie;

        public StatusNaMeiFlag(NaMei naMei, StatusHuiMie huiMie, boolean awakening) {
            super("那美自身无视防御", naMei);
            this.statusHuiMie = huiMie;
            // 觉醒-伊邪那美攻击时,也可触发友方目标当前毁灭的防御无视效果
            if (awakening) {
                attribute(Attribute.IGNORE_DEFENCE, _ ->
                        huiMie.getAttribute(Attribute.IGNORE_DEFENCE, null)
                );
            }
        }
    }
}

