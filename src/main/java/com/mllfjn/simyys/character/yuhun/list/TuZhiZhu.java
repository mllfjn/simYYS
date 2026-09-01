package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.character.status.Trigger;

import java.io.Serializable;
import java.util.*;

public class TuZhiZhu extends YuHun implements YuHunUnfullMark {
    public static final String YuHunName = "土蜘蛛";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        character.addStatus(new StatusTZZListener(character));
    }

    class StatusTZZListener extends Status {
        private final Map<Character, Double> map = new LinkedHashMap<>();

        private Skill causeSkill;

        public StatusTZZListener(Character character) {
            super(YuHunName, character);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.CAUSE_ATTACK) {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                if (attackInfo.isCalEffectYuHun()) {
                    Character target = attackInfo.getTarget();
                    // 对怪物造成伤害时
                    if (!target.isMob()) {
                        return false;
                    }

                    double number = attackInfo.getTraceableNumber().getNumber();

                    // 如果没有实际造成伤害，则返回
                    if (number <= 0) {
                        return false;
                    }

                    if (causeSkill == null) {
                        causeSkill = attackInfo.getSkill();
                        causeSkill.addSkillEndListener(() -> {
                            for (Map.Entry<Character, Double> entry : map.entrySet()) {
                                StatusTuZhiZhu.enable(belongTo, entry.getKey(), 0.1 * entry.getValue());
                            }
                            TuZhiZhu.this.yuHunEffect();
                            map.clear();
                            causeSkill = null;
                        });
                    }

                    map.put(target, map.getOrDefault(target, 0.0) + number);
                }
            }
            return false;
        }
    }

    static class StatusTuZhiZhu extends Status {
        private final Skill skill = Skill.getInstance(TuZhiZhu.YuHunName);
        private final TuZhiZhuRecord[] records = new TuZhiZhuRecord[3];
        private int count = 0;

        private StatusTuZhiZhu(Character belongTo) {
            super(null, belongTo);
            display(() -> "土" + getCount());
            attribute(Attribute.SPEED, _ -> -0.1 * count * belongTo.getInitSpeed());
            runOn(Trigger.AFTER_ROUND_FIRST, _ -> {
                for (TuZhiZhuRecord record : records) {
                    if (record == null) {
                        break;
                    }
                    record.from.doInteractive(interactive ->
                            interactive.attack(AttackInfo.createJianJieAttack(record.from, skill, belongTo, record.num))
                    );
                }
                delete();
            });
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

        record TuZhiZhuRecord(Character from, double num) implements Serializable {
        }
    }
}
