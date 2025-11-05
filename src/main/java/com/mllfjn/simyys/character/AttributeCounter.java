package com.mllfjn.simyys.character;

import com.mllfjn.simyys.state.AttributeModifier;
import com.mllfjn.simyys.state.State;

import java.util.ArrayList;
import java.util.List;

public class AttributeCounter {
    /*private static final List<List<String>> zengShang = new ArrayList<>();
    static {
        // 一般增伤
        zengShang.add(List.of());
    }

    public static double getZengShang(Character character) {
        List<State> states = character.getStates();
        double[] add = new double[zengShang.size()];
        for (State state : states) {
            if (state instanceof AttributeModifier a && a.isAffectAttribute(Attribute.ZENG_SHANG)) {

                for (int i = 0; i < zengShang.size(); i++) {
                    if (zengShang.get(i).contains(state.name)) {
                        add[i] += a.getInfluence(Attribute.ZENG_SHANG);
                        break;
                    }
                }
            }
        }

        double rt = 1;
        for (double a : add) {
            rt *= (1 + a);
        }
        return rt;
    }*/

    public static double getGeneralAttribute(Attribute attribute, double base, List<State> states) {
        for (State state : states) {
            if (state instanceof AttributeModifier a && a.isAffectAttribute(attribute)) {
                base += a.getInfluence(attribute);
            }
        }
        return base < 0 ? 0 : base;
    }
}
