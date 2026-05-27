package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.EffectInfo;

public class StatusBiHu extends Status implements StatusRunnable, Displayable {
    private static final String StatusName = "庇护";

    private Skill skill = null;

    public StatusBiHu(Character from, Character belongTo) {
        // 庇护：可抵挡单次技能当中的所有控制效果和TODO 放逐
        super(from, belongTo, StatusType.SPECIAL, StatusForm.YIN_JI);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.ADDING_CROWD_CONTROL;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAddCrowdControl pac) {
            EffectInfo effectInfo = pac.getEffectInfo();
            Skill fromSkill = effectInfo.getSkill();
            // 如果还没生效或者生效的技能就是当前技能，则取消控制
            if (skill == null || skill == fromSkill) {
                effectInfo.setCancel(true);
                if (skill == null) {
                    skill = fromSkill;
                    fromSkill.addSkillEndListener(this::used);
                }
            }
        }
        return false;
    }

    protected void used() {
        delete();
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}
