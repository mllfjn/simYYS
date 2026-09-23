package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.TraversalOrderManager;
import com.mllfjn.simyys.character.status.ConditionalReduceCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.list.HaiYueHuoYu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillCostResult {
    private Map<ConditionalReduceCost, Integer> conditionalReduceCostIntegerMap;
    private HaiYueHuoYu haiYueHuoYu;

    private final int finalCost;

    public SkillCostResult(Skill skill) {
        Character belongTo = skill.getBelongTo();

        int mustUse = skill.getCost();
        // 强制增加或减少消耗 比如猛火、SP千
        for (Status status : belongTo.getStatuses()) {
            mustUse += status.getForceChangeSkillCost();
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

        List<ConditionalReduceCost> conditionalReduceCosts = TraversalOrderManager
                .getConditionalReduceCosts(belongTo.getStatuses());
        if (conditionalReduceCosts.isEmpty()) {
            if (belongTo.bp.canUseGuiHuo(belongTo, maxUse)) {
                finalCost = maxUse;
            } else {
                finalCost = mustUse;
            }
            return;
        }
        conditionalReduceCostIntegerMap = new HashMap<>();
        for (ConditionalReduceCost crc : conditionalReduceCosts) {
            int maxReduce = crc.getMaxReduce();
            if (maxReduce <= 0) {
                continue;
            }
            if (maxReduce >= maxUse) {
                conditionalReduceCostIntegerMap.put(crc, maxUse);
                finalCost = 0;
                return;
            } else {
                conditionalReduceCostIntegerMap.put(crc, maxReduce);
                maxUse -= maxReduce;
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
        if (conditionalReduceCostIntegerMap != null && !conditionalReduceCostIntegerMap.isEmpty()) {
            conditionalReduceCostIntegerMap.forEach(ConditionalReduceCost::enable);
        }
        if (haiYueHuoYu != null) {
            haiYueHuoYu.enable();
        }
    }
}
