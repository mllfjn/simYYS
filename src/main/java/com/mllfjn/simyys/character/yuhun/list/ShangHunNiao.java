package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.EventCharacterDie;

public class ShangHunNiao extends YuHun implements YuHunSealResponse {
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


    static class StatusShangHunNiao extends Status implements AttributeModifier, Displayable {
        private int stack;

        public StatusShangHunNiao(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        }

        public static void addStack(Character character) {
            character.getStatus(StatusShangHunNiao.class)
                    .or(() -> character.addStatus(new StatusShangHunNiao(character)))
                    .ifPresent(status -> {
                        if (status.stack < 6) {
                            status.stack++;
                        }
                    });
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 20 * stack;
        }

        @Override
        public String getDisplayText() {
            return "伤" + stack;
        }
    }
}

