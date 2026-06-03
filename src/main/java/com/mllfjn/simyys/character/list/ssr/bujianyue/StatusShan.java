package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.HealInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;

class StatusShan extends Status implements Displayable {
    static final String StatusName = "峦纹·山";

    private int stack = 1;

    private StatusShan(Character character) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    static void addStack(Character character) {
        character.addStatusOrChange(StatusShan.class, StatusShan::addStack, () -> new StatusShan(character));
    }

    void addStack() {
        if (stack < 5) {
            stack++;
        }
    }

    void consumeStack(Skill1 skill, Interactive interactive) {
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        dispel(list);

        double initDefense = belongTo.getInitDefense();
        interactive.heal(skill, list, c -> {
            HealInfo healInfo = HealInfo.createHeal(belongTo, skill, c, initDefense);
            healInfo.setMultiplier(stack * 100);
            return healInfo;
        });
    }

    private void dispel(List<Character> list) {
        ArrayList<Status> statuses = new ArrayList<>();
        ArrayList<Status> crowedControls = new ArrayList<>();

        for (Character character : list) {
            for (Status status : character.getStatuses()) {
                if (status.statusType == StatusType.DEBUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                    if (status instanceof CrowdControl) {
                        crowedControls.add(status);
                    } else {
                        statuses.add(status);
                    }
                }
            }
        }

        int canDispel = stack;
        if (!crowedControls.isEmpty()) {
            List<Status> choose = RateController.choose(StatusName + "驱散-优先控制效果", crowedControls,
                    Status::toString, belongTo.bp.calc, canDispel
            );
            for (Status status : choose) {
                status.delete();
            }
            if (canDispel > choose.size()) {
                canDispel -= choose.size();
            } else {
                // 这里只有=一种情况，不可能小于
                return;
            }
        }

        if (!statuses.isEmpty()) {
            List<Status> choose = RateController.choose(StatusName + "驱散", statuses,
                    Status::toString, belongTo.bp.calc, canDispel
            );
            for (Status status : choose) {
                status.delete();
            }
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
