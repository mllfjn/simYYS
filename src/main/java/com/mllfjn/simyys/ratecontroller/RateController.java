package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;

import java.util.*;

public class RateController {
    public static boolean[] weatherOrNot(String title, String effect, List<Character> targets, boolean rateControl, RateGetter rateGetter) {
        // 首先根据传入的getter算法计算出每个的概率
        // 其中<=0的,直接算NO, >=100的直接算YES
        // 剩下概率性的, 如果rateControl为true,调用RateControlDialog得出结果
        // 如果结果是default,则随机

        int size = targets.size();
        Return[] returns = new Return[size];
        double[] rates = new double[size];
        Map<Character, Integer> map = new HashMap<>();

        for (int i = 0; i < size; i++) {
            rates[i] = rateGetter.get(targets.get(i));
            if (rates[i] <= 0) {
                returns[i] = Return.NO;
            } else if (rates[i] >= 100) {
                returns[i] = Return.YES;
            } else {
                if (rateControl) {
                    map.put(targets.get(i), i);
                } else {
                    returns[i] = Return.DEFAULT;
                }
            }
        }

        if (!map.isEmpty()) {
            new RateControlDialog(title, effect, map, rates, returns);
        }

        boolean[] result = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (returns[i] == Return.YES) {
                result[i] = true;
            } else if (returns[i] == Return.NO) {
                result[i] = false;
            } else if (returns[i] == Return.DEFAULT) {
                result[i] = new Random().nextDouble() * 100 < rates[i];
            }
        }

        return result;
    }

    public static boolean[] baoJi(String skillName, Character owner, List<Character> targets, boolean rateControl) {
        return weatherOrNot("暴击控制-" + skillName, "暴击", targets, rateControl, (character) ->
                owner.getCritRate()
        );
    }

    public static boolean[] mingZhong(String stateName, Character owner, List<Character> targets, int base, boolean rateControl) {
        return weatherOrNot("命中控制-" + stateName, "命中", targets, rateControl,(character) ->
                base * (100 + owner.getEffectHitRate()) / (100 + character.getEffectResistRate())
        );
    }



    /*public static boolean[] weatherOrNot(String title, String[] names, double[] rates, String effect, boolean rateControl) {
        // 传入rates省略百分号，即实际概率等于 rate * 0.01
        int size = names.length;
        Return[] returns = new Return[size];

        if (rateControl) {
            new RateControlDialog(title, names, rates, effect, returns);
        } else {
            Arrays.fill(returns, Return.DEFAULT);
        }

        boolean[] result = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (returns[i] == Return.YES) {
                result[i] = true;
            } else if (returns[i] == Return.NO) {
                result[i] = false;
            } else if (returns[i] == Return.DEFAULT) {
                result[i] = new Random().nextDouble() * 100 < rates[i];
            }
        }

        return result;
    }
    public static boolean[] baoJi(Character owner, List<Character> targets, boolean rateControl) {
        int size = targets.size();
        String[] names = new String[size];
        double[] rates = new double[size];

        for (int i = 0; i < size; i++) {
            names[i] = targets.get(i).name;
            rates[i] = owner.getCritRate();
        }

        return weatherOrNot("暴击控制", names, rates, "暴击", rateControl);
    }

    public static boolean[] mingZhong(Character owner, List<Character> targets, int base, boolean rateControl) {
        int size = targets.size();
        String[] names = new String[size];
        double[] rates = new double[size];

        for (int i = 0; i < size; i++) {
            names[i] = targets.get(i).name;
            rates[i] = base * (1 + owner.getEffectHitRate()) / (1 + targets.get(i).getEffectResistRate());
        }

        return weatherOrNot("命中控制", names, rates, "命中", rateControl);
    }*/
}
