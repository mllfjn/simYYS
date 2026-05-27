package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;


public class HuangKuLou extends CharacterJiFengMoBase {
    public static final String CharacterName = "荒骷髅";

    private Skill5 skill5;

    @Override
    protected void addStage(MultiStageManager multiStageManager) {
        // 75%血召唤部下
        multiStageManager.addStage(() -> {
            skill5.summonNormal();
            setHpWithoutTrigger(getMaxHp() * 0.75);
        });
        // 50%血真假话
        multiStageManager.addStage(() -> {
            skill5.summonSpecial();
            // 自己进入无法选中状态
            addStatus(new StatusUnselectable(this, this));
            setHpWithoutTrigger(getMaxHp() * 0.5);
        });
        // 25%血召唤部下
        multiStageManager.addStage(() -> {
            skill5.summonNormal();
            setHpWithoutTrigger(getMaxHp() * 0.25);
        });
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(2) // 毒雾
                || tryUseSkill(3) // 刀剑
                || tryUseSkill(4) // 花海
                || tryUseSkill(6); // 刀锋
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this));
        addSkill(new Skill4(this));
        skill5 = new Skill5(this);
        addSkill(skill5);
        addSkill(new Skill6(this));
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "190";
    }

}
