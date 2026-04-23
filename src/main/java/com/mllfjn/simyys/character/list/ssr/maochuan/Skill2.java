package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "泉涌";
    private static final int[] RATES = new int[]{0, 40, 40, 60, 80, 100};

    final int rate;
    final boolean selfDouble;

    private BattleActionListener listener;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        rate = RATES[level];
        selfDouble = level >= 2;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t友方经过别馆私汤时享受温泉疗愈:恢复猫川攻击98%的生命并有40%概率驱散1个减益状态或控制效果,优先驱散控制效果
                √\tlv2-自身经过别馆私汤时,获得的恢复量翻倍
                √\tlv3-驱散概率提升至60%
                √\tlv4-驱散概率提升至80%
                √\tlv5-驱散概率提升至100%
                """;
    }

    @Override
    public void enable() {
        Character belongTo = getBelongTo();
        listener = belongTo.bp.forEveryone(belongTo, character -> {
            if (character.team == belongTo.team) {
                character.addStatus(new StatusPassByListener(belongTo, character));
            }
        });
    }

    @Override
    protected void disable() {
        if (listener != null) {
            getBelongTo().bp.removeActionListener(getBelongTo(), listener);
            listener = null;
            for (Character character : getBelongTo().bp.situation.teamPane[getBelongTo().team].characters) {
                character.removeStatus(StatusPassByListener.class);
            }
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusPassByListener extends Status implements StatusRunnable {

        public StatusPassByListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.LOCATION_CHANGE && ((MaoChuan) from).isBieGuanSiTangExist();
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamLocationChange plc) {
                // 如果经过了70
                if (plc.oldLocation < 70 && plc.newLocation >= 70) {
                    ((MaoChuan) from).getBieGuanSiTang().wenQuanLiaoYu(belongTo);
                }
            }
            return false;
        }
    }
}
