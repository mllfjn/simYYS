package com.mllfjn.simyys.character;

import com.mllfjn.simyys.state.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributeCounter {
    private static final List<List<String>> zengShang = new ArrayList<>();
    static {
        zengShang.add(Arrays.asList( // 一般增伤

        ));
    }

    public static double getZengShang(Character character) {
        List<State> states = character.getStates();
        double[] add = new double[zengShang.size()];
        for (State state : states) {
            if (state.isAffectAttribute(Attribute.ZENG_SHANG)) {
                String name = state.name;
                for (int i = 0; i < zengShang.size(); i++) {
                    if (zengShang.get(i).contains(name)) {
                        add[i] += state.getInfluence(Attribute.ZENG_SHANG);
                    }
                }
            }
        }

        double rt = 1;
        for (double a : add) {
            rt *= (1 + a);
        }
        return rt;
    }

    public static double getGeneralAttribute(Attribute attribute, double base, List<State> states) {
        for (State state : states) {
            if (state.isAffectAttribute(attribute)) {
                base += state.getInfluence(attribute);
            }
        }
        return base < 0 ? 0 : base;
    }
}
