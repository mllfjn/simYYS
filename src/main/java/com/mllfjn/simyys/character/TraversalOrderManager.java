package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.list.ssr.namei.StatusHuiMie;
import com.mllfjn.simyys.character.list.ssr.shenwuyue.StatusRuMeng;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.ConditionalReduceCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.yuhun.list.YiNianHuo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TraversalOrderManager {
    private final static Map<Class<? extends ConditionalReduceCost>, Integer> CRC_MAP = Map.of(
            StatusRuMeng.class, 1,
            YiNianHuo.clazz, 2
    );
    private final static Comparator<ConditionalReduceCost> CRC_COMPARATOR =
            Comparator.comparingInt(crc -> CRC_MAP.get(crc.getClass()));

    private final static Map<Class<? extends PreventDie>, Integer> PD_MAP = Map.of(
            StatusHuiMie.class, 1
    );
    private final static Comparator<PreventDie> PD_COMPARATOR =
            Comparator.comparingInt(pd -> PD_MAP.getOrDefault(pd.getClass(), Integer.MAX_VALUE));

    public static double getAttribute(Attribute attribute, double base, List<Status> statuses) {
        for (Status status : statuses) {
            if (status instanceof AttributeModifier a && a.isAffectAttribute(attribute)) {
                base += a.getInfluence(attribute, null);
            }
        }
        return base < 0 ? 0 : base;
    }

    public static double getActualDefense(Character attacker, Character target, AttackType attackType) {
        double defense = target.getInitDefense();
        AttributeModifier.StatusModifyParam param = new AttributeModifier.StatusModifyParam(target, attackType);
        for (Status status : target.getStatuses()) {
            if (status instanceof AttributeModifier a && a.isAffectAttribute(Attribute.DEFENCE)) {
                defense += a.getInfluence(Attribute.DEFENCE, param);
            }
        }

        for (Status status : attacker.getStatuses()) {
            if (status instanceof AttributeModifier a && a.isAffectAttribute(Attribute.IGNORE_DEFENCE)) {
                defense -= a.getInfluence(Attribute.IGNORE_DEFENCE, param);
            }
        }

        return defense < 0 ? 0 : defense;
    }

    public static double getProbabilityAtLeastOne(Attribute attribute, List<Status> statuses) {
        double noEventProbability = 100;
        for (Status status : statuses) {
            if (status instanceof AttributeModifier am && am.isAffectAttribute(attribute)) {
                noEventProbability *= (100 - am.getInfluence(attribute, null)) / 100;
            }
        }
        return 100 - noEventProbability;
    }

    public static List<ConditionalReduceCost> getConditionalReduceCosts(List<Status> statuses) {
        return statuses.stream()
                .filter(status -> status instanceof ConditionalReduceCost)
                .map(status -> (ConditionalReduceCost) status)
                .sorted(CRC_COMPARATOR)
                .toList();
    }

    public static boolean preventDie(InteractiveInfo interactiveInfo, double excessDamage, List<Status> statuses) {
        ArrayList<PreventDie> preventDies = new ArrayList<>();
        for (Status status : statuses) {
            if (status instanceof PreventDie pd && pd.preventDieEffective()) {
                preventDies.add(pd);
            }
        }
        preventDies.sort(PD_COMPARATOR);
        for (PreventDie pd : preventDies) {
            pd.preventDie(excessDamage);
            interactiveInfo.getTraceableNumber().addTrace("(" + pd.getName() + "免死生效)");
            interactiveInfo.setCancel(true);
            return true;
        }
        return false;
    }
}
