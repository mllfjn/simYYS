package com.mllfjn.simyys.ratecontroller;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.EffectInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RateController implements Serializable {
    public static final Random random = new Random();

    /**
     *
     * @param title         标题栏文本
     * @param event         事件名称
     * @param targets       目标
     * @param stringGetter  获取目标文本
     * @param calc          总概率计算器
     * @param controlGetter 概率控制获取
     * @param rateGetter    获取概率
     * @param resultHandler 结果处理
     */
    public static <T> void whetherOrNot(String title, String event, List<T> targets
            , Function<T, String> stringGetter, RateCalc calc, Function<RateCalc, Boolean> controlGetter
            , Function<T, Double> rateGetter, BiConsumer<Integer, Boolean> resultHandler) {
        // 首先根据传入的getter算法计算出每个的概率
        // 其中<=0的,直接算false, >=100的直接算true
        // 剩下概率性的, 如果rateControl为true,调用RateControlDialog得出结果
        // 如果结果null,则随机

        int size = targets.size();
        Boolean[] returns = new Boolean[size];
        double[] rates = new double[size];

        int count = 0;
        for (int i = 0; i < size; i++) {
            rates[i] = rateGetter.apply(targets.get(i));
            if (rates[i] <= 0) {
                returns[i] = false;
            } else if (rates[i] >= 100) {
                returns[i] = true;
            } else {
                if (controlGetter.apply(calc)) {
                    count++;
                }
            }
        }

        if (count > 0) {
            new RateControlDialog(title, event, targets, stringGetter, rates, returns, count, calc);
        }

        for (int i = 0; i < size; i++) {
            if (returns[i] == null) {
                returns[i] = random.nextDouble() * 100 < rates[i];
            }
            resultHandler.accept(i, returns[i]);
        }
    }

    public static void baoJi(String skillName, Character owner, RateCalc calc
            , List<Character> targets, AttackInfo... attackInfos) {
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            if (attackInfos[i].canCrit() && attackInfos[i].getCrit() == null) {
                list.add(targets.get(i));
            }
        }

        whetherOrNot("暴击控制：" + owner.name + "-" + skillName, "暴击", list, Character::getName
                , calc, RateCalc::isControlCrit, character -> owner.getCritRate()
                , (i, crit) -> attackInfos[i].setCrit(crit));
    }

    public static EffectInfo[] mingZhong(String statusName, Character owner, List<Character> targets
            , int baseRate, boolean calHit, RateCalc calc) {
        EffectInfo[] infos = new EffectInfo[targets.size()];
        whetherOrNot("命中控制：" + owner.name + "-" + statusName, "命中"
                , targets, Character::getName, calc, RateCalc::isControlEffectHit
                , character -> {
                    if (calHit) {
                        return baseRate * (100 + owner.getEffectHitRate()) / (100 + character.getEffectResistRate());
                    } else {
                        return (double) baseRate;
                    }
                }
                , (i, hit) -> {
                    EffectInfo info = new EffectInfo();
                    info.setHit(hit);
                    infos[i] = info;
                });
        return infos;
    }

    public static <T> T choose(String title, List<T> list, Function<T, String> stringGetter
            , RateCalc calc) {
        if (list.size() == 1) {
            return list.get(0);
        }

        if (calc.isControlChoose()) {
            ChooseDialog<T> dialog = new ChooseDialog<>(title + "选取", list, stringGetter);
            Optional<T> result = dialog.showAndWait();
            if (result.isPresent()) {
                calc.change(1.0 / list.size());
                return result.get();
            }
        }
        return list.get(random.nextInt(list.size()));
    }

}