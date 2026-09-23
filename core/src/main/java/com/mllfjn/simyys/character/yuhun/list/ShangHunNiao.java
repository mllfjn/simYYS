package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.EventCharacterDie;

public class ShangHunNiao extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "伤魂鸟";
    private BattleActionListener listener;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);

        listener = new BattleActionListener(character) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventCharacterDie ed && !ed.getCharacter().isMob()) {
                    Character belongTo = getBelongTo();
                    belongTo.doInteractive(
                            interactive -> interactive.healTypical(Skill.getInstance(YuHunName), belongTo, 20));
                    // 并提升20%伤害
                    StatusShangHunNiao.addStack(belongTo);

                    yuHunEffect();
                }
                return false;
            }
        };
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        getBelongTo().bp.addActionListener(listener);
    }

    @Override
    public void disable() {
        getBelongTo().bp.removeActionListener(listener);
    }


    static class StatusShangHunNiao extends Status {
        private int stack = 1;

        public StatusShangHunNiao(Character character) {
            super(YuHunName, character, character, StatusType.BUFF, StatusForm.YIN_JI);
            display(() -> YuHunName + stack);
            attribute(Attribute.ZENG_SHANG, _ -> 20.0 * stack);
        }

        public static void addStack(Character character) {
            character.getStatus(StatusShangHunNiao.class)
                    .ifPresentOrElse(
                            status -> {
                                if (status.stack < 6) {
                                    status.stack++;
                                }
                            },
                            () -> character.addStatus(new StatusShangHunNiao(character))
                    );
        }
    }
}

