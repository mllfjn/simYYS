package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RateController {
    public static boolean[] weatherOrNot(String title, String[] names, double[] rates, String effect, boolean rateControl) {
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
    }
}
