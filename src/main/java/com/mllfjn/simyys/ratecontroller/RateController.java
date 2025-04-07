package com.mllfjn.simyys.ratecontroller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class RateController {
    public static boolean[] weatherOrNot(List<String> names, List<Double> rates, String effect, boolean rateControl) {
        int size = names.size();
        Return[] returns = new Return[size];

        if (rateControl) {
            new RateControlDialog(names, rates, effect, returns);
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
                result[i] = new Random().nextDouble() * 100 < rates.get(i);
            }
        }

        return result;
    }
}
