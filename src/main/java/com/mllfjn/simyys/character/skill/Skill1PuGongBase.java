package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Skill1PuGongBase extends Skill {
    protected static final int[] multiplierGeneral = new int[]{0, 100, 105, 110, 115, 125};


    private final List<Skill1PuGongBase> xieZhanSkills = new ArrayList<>();

    public Skill1PuGongBase(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    public Character getTarget() {
        return new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);
    }

    public void usePrivate(Interactive interactive, Character target) {
        // 造成攻击（系数）伤害
        interactive.attackTypical(this, target, multiplierGeneral[getLevel()], AttackType.DAN_TI);
    }

    public boolean canXieZhan(Skill skill) {
        if (skill instanceof Skill1PuGongBase s1) {
            // 如果该技能已经协战过了，返回false
            return !s1.xieZhanSkills.contains(this);
        }
        return true;
    }

    public void xieZhan(Skill skill, Character target) {
        if (skill instanceof Skill1PuGongBase s1) {
            // 如果该技能已经协战过了，直接返回
            if (s1.xieZhanSkills.contains(this)) {
                return;
            }

            s1.xieZhanSkills.add(this);
        }

        getBelongTo().doInteractive(interactive -> usePrivate(interactive, target));
        log(target);
        useDone();
    }

    @Override
    public final Optional<Character> usePrivate(BattlePane bp) {
        throw new RuntimeException("错误的普攻方式");
    }

    @Override
    public void use(BattlePane bp) {
        Character target = getTarget();
        Character belongTo = getBelongTo();

        usePrivate(belongTo.getInteractive(), target);

        // 消息记录
        log(target);

        belongTo.statusRun(Trigger.USE_PU_GONG, new ParamUseSkill(this, target));

        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .getList();

        for (Character character : list) {
            if (RateController.xieZhan(this, character)) {
                character.xieZhan(this, target);
            }

        }

        useDone();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return true;
    }

    @Override
    protected void useDone() {
        super.useDone();
        xieZhanSkills.clear();
    }
}
