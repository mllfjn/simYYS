package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.list.ssr.shenwuyue.StatusRuMeng;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.ConditionalReduceCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.list.YiNianHuo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TraversalOrderManager {
    private final static Map<Class<? extends ConditionalReduceCost>, Integer> CRC_MAP = Map.of(
            StatusRuMeng.class, 0,
            YiNianHuo.clazz, 1
    );
    private final static Comparator<ConditionalReduceCost> CRC_COMPARATOR = Comparator
            .comparingInt(crc -> CRC_MAP.get(crc.getClass()));
    public static double getAttribute(Attribute attribute, double base, List<Status> statuses) {
        for (Status status : statuses) {
            if (status instanceof AttributeModifier a && a.isAffectAttribute(attribute)) {
                base += a.getInfluence(attribute);
            }
        }
        return base < 0 ? 0 : base;
    }

    public static List<ConditionalReduceCost> getConditionalReduceCosts(List<Status> statuses) {
        return statuses.stream()
                .filter(status -> status instanceof ConditionalReduceCost)
                .map(status -> (ConditionalReduceCost) status)
                .sorted(CRC_COMPARATOR)
                .toList();
    }
}
