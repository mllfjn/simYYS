package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.trigger.Trigger;

import java.util.*;

public class TuZhiZhu extends YuHun implements YuHunUnfullMark {
    public static final String YuHunName = "土蜘蛛";

    @Override
    public String getName() {
        return YuHunName;
    }

    static class StatusTuZhiZhu extends Status implements Displayable, Runnable, AttributeModifier {
        private final TuZhiZhuRecord[] records = new TuZhiZhuRecord[3];
        private int count = 0;

        private StatusTuZhiZhu(Character belongTo) {
            super(null, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        public static void enable(Character from, Character target, double num) {
            Optional<StatusTuZhiZhu> OptionalTu = target.getStatus(StatusTuZhiZhu.class);
            // 如果目标已经有三层土了，直接返回
            if (OptionalTu.isPresent() && OptionalTu.get().getCount() == 3) {
                return;
            }
            // 如果目标身上没有土蜘蛛状态,new一个
            StatusTuZhiZhu statusTu = OptionalTu.orElseGet(() -> {
                StatusTuZhiZhu statusTuZhiZhu = new StatusTuZhiZhu(target);
                target.addStatus(statusTuZhiZhu);
                return statusTuZhiZhu;
            });

            // 附加土蜘蛛
            statusTu.add(from, num);
        }

        private void add(Character from, double num) {
            records[count++] = new TuZhiZhuRecord(from, num);
        }

        public int getCount() {
            return count;
        }

        @Override
        public String getText() {
            return "土" + getCount();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND_FIRST;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp) {
            for (TuZhiZhuRecord record : records) {
                record.from.doInteractive(interactive ->
                        interactive.attack(belongTo, AttackType.JIAN_JIE, Info.createJianJieAttack(
                                (owner, target) -> record.num, record.from, belongTo)));
            }
            return true;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -belongTo.getInitSpeed() * 0.1 * count;
        }

        record TuZhiZhuRecord(Character from, double num) {
        }
    }
}
