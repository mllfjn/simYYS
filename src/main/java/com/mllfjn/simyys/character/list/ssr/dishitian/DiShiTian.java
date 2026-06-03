package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class DiShiTian extends CharacterShiShenBase {
    public static final String CharacterName = "帝释天";

    private int enemyCount;

    private StatusJinLian jinLian;

    @Override
    protected boolean useSkillAuto() {
        if (jinLian == null && tryUseSkill(2)) {
            return true;
        }
        return tryUseSkill(3);
    }

    void addJinLian(StatusJinLian newJinLian) {
        if (jinLian != null) {
            jinLian.delete();
        }
        jinLian = newJinLian;
    }

    void removeJinLian() {
        jinLian = null;
    }

    StatusJinLian getJinLian() {
        return jinLian;
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3109";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }

    // 该状态在帝释天队友身上,降低其速度
    static class StatusReduceSpeed extends Status implements AttributeModifier {

        public StatusReduceSpeed(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return ((DiShiTian) from).enemyCount * belongTo.getInitSpeed() * -0.03;
        }
    }

    // 该状态在帝释天敌方身上,回合开始时拉帝释天这把的单位
    static class StatusIncreaseLocation extends Status implements StatusRunnable {

        public StatusIncreaseLocation(DiShiTian from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            from.enemyCount++;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND || trigger == Trigger.DIE;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                Character target = new CharacterFinder(from)
                        .filterTeammate()
                        .get(Attribute.LOCATION, CharacterFinder.Criteria.MAX);
                from.doInteractive(interactive -> interactive.increaseLocation(target, 30));
            } else {
                ((DiShiTian) from).enemyCount--;
            }
            return false;
        }
    }
}
