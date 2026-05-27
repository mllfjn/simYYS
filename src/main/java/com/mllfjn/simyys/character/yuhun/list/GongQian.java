package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;

public class GongQian extends YuHun {
    public static final String YuHunName = "共潜";

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);

        character.addStatus(new StatusGQListener(character));
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    class StatusGQListener extends Status implements StatusRunnable {
        private boolean madeAttack = true;

        public StatusGQListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND // 回合开始前置false
                    || (!madeAttack && trigger == Trigger.CAUSE_ATTACK) // 如果没有造成过伤害并且刚造成了伤害
                    || trigger == Trigger.AFTER_ROUND; // 回合结束后驱散
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                madeAttack = false;
            } else if (trigger == Trigger.CAUSE_ATTACK) {
                madeAttack = true;
            } else {
                List<Character> list = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .getList();
                ArrayList<Status> statuses = new ArrayList<>();
                for (Character character : list) {
                    for (Status status : character.getStatuses()) {
                        if (status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                            statuses.add(status);
                        }
                    }
                }
                if (!statuses.isEmpty()) {
                    List<Status> toBeDispel = RateController
                            .choose("共潜驱散负面状态", statuses,
                                    status -> status.belongTo.name + status, bp.calc, madeAttack ? 1 : 3
                            );
                    for (Status status : toBeDispel) {
                        status.delete();
                    }
                    GongQian.this.yuHunEffect();
                }
            }
            return false;
        }
    }
}
