package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.ConditionalReduceCost;
import com.mllfjn.simyys.character.status.ForceChangeCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.list.HaiYueHuoYu;

import java.util.ArrayList;
import java.util.List;

public class SkillCostResult {
    private List<ConditionalReduceCost> conditionalReduceCostList;
    private HaiYueHuoYu haiYueHuoYu;

    private final int finalCost;

    public SkillCostResult(Skill skill) {
        Character belongTo = skill.getBelongTo();

        int mustUse = skill.getCost();
        // 强制增加或减少消耗 比如猛火、SP千
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof ForceChangeCost rc) {
                mustUse += rc.getChange();
            }
        }

        // 可选额外消耗:海月火玉
        belongTo.getYuHun(HaiYueHuoYu.class)
                .ifPresent(yueHuoYu -> haiYueHuoYu = yueHuoYu);

        int optionalUse = haiYueHuoYu == null ? 0 : HaiYueHuoYu.EXTRA_USE;
        int maxUse = mustUse + optionalUse;

        // 如果加上了海月还是不消耗鬼火,直接设0返回
        if (maxUse <= 0) {
            finalCost = 0;
            return;
        }

        conditionalReduceCostList = new ArrayList<>();
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof ConditionalReduceCost crc) {
                conditionalReduceCostList.add(crc);
                maxUse -= crc.getReduce();
                if (maxUse <= 0) {
                    finalCost = 0;
                    return;
                }
            }
        }

        if (belongTo.bp.canUseGuiHuo(belongTo, maxUse)) {
            finalCost = maxUse;
        } else {
            finalCost = maxUse - optionalUse;
        }
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void reallyUse() {
        if (conditionalReduceCostList != null && !conditionalReduceCostList.isEmpty()) {
            for (ConditionalReduceCost conditionalReduceCost : conditionalReduceCostList) {
                conditionalReduceCost.enable();
            }
        }
        if (haiYueHuoYu != null) {
            haiYueHuoYu.enable();
        }
    }
}
