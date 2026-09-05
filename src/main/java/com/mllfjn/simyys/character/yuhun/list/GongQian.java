package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;

public class GongQian extends Equip {
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

    class StatusGQListener extends Status {
        private boolean madeAttack = false;

        public StatusGQListener(Character character) {
            super(YuHunName, character);
            // 造成伤害监听
            runOnAndDisable(Trigger.CAUSE_ATTACK, _ -> madeAttack = true);
            // 回合开始前开始监听
            runOn(Trigger.BEFORE_ROUND, _ -> {
                madeAttack = false;
                enableAction(Trigger.CAUSE_ATTACK);
            });
            // 回合结束后驱散
            runOn(Trigger.AFTER_ROUND, _ -> {
                List<Character> list = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .getList();
                ArrayList<Status> statuses = new ArrayList<>();
                for (Character c : list) {
                    for (Status status : c.getStatuses()) {
                        if (status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                            statuses.add(status);
                        }
                    }
                }
                if (!statuses.isEmpty()) {
                    List<Status> toBeDispel = RateController
                            .choose("共潜驱散负面状态", statuses,
                                    status -> status.belongTo.name + status, character.bp().calc, madeAttack ? 1 : 3
                            );
                    for (Status status : toBeDispel) {
                        status.delete();
                    }
                    GongQian.this.yuHunEffect();
                }
                disableAction(Trigger.CAUSE_ATTACK);
            });
        }
    }
}
