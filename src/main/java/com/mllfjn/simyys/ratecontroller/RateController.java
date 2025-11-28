package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Info;

import java.util.*;
import java.util.function.Function;

public class RateController {
    public static <T> boolean[] whetherOrNot(String title, String event, List<T> targets, Function<T, String> stringGetter, boolean rateControl, TotalRateCalc calc, Function<T, Double> rateGetter) {
        // 首先根据传入的getter算法计算出每个的概率
        // 其中<=0的,直接算NO, >=100的直接算YES
        // 剩下概率性的, 如果rateControl为true,调用RateControlDialog得出结果
        // 如果结果是default,则随机

        int size = targets.size();
        Return[] returns = new Return[size];
        double[] rates = new double[size];
        boolean[] tbd = new boolean[size];

        int count = 0;
        for (int i = 0; i < size; i++) {
            rates[i] = rateGetter.apply(targets.get(i));
            if (rates[i] <= 0) {
                returns[i] = Return.NO;
            } else if (rates[i] >= 100) {
                returns[i] = Return.YES;
            } else {
                if (rateControl) {
                    tbd[i] = true;
                    count++;
                } else {
                    returns[i] = Return.DEFAULT;
                }
            }
        }

        if (count > 0) {
            new RateControlDialog(title, event, targets, stringGetter, tbd, rates, returns, count, calc);
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

    public static void baoJi(String skillName, Character owner, boolean rateControl, TotalRateCalc calc, List<Character> targets, Info... infos) {
        List<Character> list = new ArrayList<>();
        for (Info info : infos) {
            if (info.canCrit() && info.getCrit() != null) {

            }
        }
        /*return whetherOrNot("暴击控制：" + owner.name + "-" + skillName, "暴击", targets, Character::getName, rateControl, calc
                , (character) -> {
                    return owner.getCritRate();
                }
        );*/
    }

    public static boolean[] mingZhong(String stateName, Character owner, List<Character> targets, int base, boolean rateControl, TotalRateCalc calc) {
        return whetherOrNot("命中控制：" + owner.name + "-" + stateName, "命中", targets, Character::getName, rateControl, calc, character ->
                base * (100 + owner.getEffectHitRate()) / (100 + character.getEffectResistRate())
        );
    }

    public static <T> T choose(String title, List<T> list, Function<T, String> stringGetter, boolean rateControl, TotalRateCalc calc) {
        if (list.size() == 1) {
            return list.get(0);
        }

        if (rateControl) {
            ChooseDialog<T> dialog = new ChooseDialog<>(title + "选取", list, stringGetter);
            Optional<T> result = dialog.showAndWait();
            if (result.isPresent()) {
                calc.add(1.0 / list.size());
                return result.get();
            }
        }
        return list.get(new Random().nextInt(list.size()));
    }

}