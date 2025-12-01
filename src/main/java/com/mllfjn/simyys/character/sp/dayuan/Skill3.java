package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill3 extends Skill {
    public static final String SkillName = "与世结缘";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        DaYuan daYuan = (DaYuan) getBelongTo();
        int level = getLevel();

        Character target = bp.situation
                // 目标首先是绿标
                .getAutoTo(daYuan.team)
                // 然后是结缘的式神
                .or(() -> daYuan.getStatus(StatusCombined.class).map(status -> status.from))
                // 最后是攻击最高的
                .orElseGet(() -> CharacterFinder.find(bp.situation.characters, daYuan.team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX));

        lastUsedTarget = target;
        Interactive interactive = daYuan.getInteractive();

        // 获得1层神力
        StatusShenLi.addStack(daYuan, 1);
        // 获取神力层数
        int shenLiStack = daYuan.getStatus(StatusShenLi.class).map(StatusShenLi::getStack).orElse(0);
        // 并治疗友方目标生命上限8%的生命
        Info healInfo = heal(interactive, target);
        // 使其有50%概率获得尘缘·赤，有50%概率获得尘缘·青
        // 测试了一下5层以前这里是“非此即彼”的
        boolean chi = false;
        boolean qing = false;

        if (shenLiStack == 5) {
            // 自身神力5层时，同时获得两个效果
            chi = qing = true;
        } else if (target.isHaveStatus(StatusSTChi.class)) {
            // 有胜天之缘赤的单位被施加与世结缘时，必定出现尘缘·赤
            chi = true;
        } else if (target.isHaveStatus(StatusSTQing.class)) {
            // 有胜天之缘青的单位被施加与世结缘时，必定出现尘缘·青
            qing = true;
        } else {
            // 既没有5层神力,也没胜天之缘
            List<String> list = List.of(StatusChi.StatusName, StatusQing.StatusName);
            int i = list.indexOf(RateController.choose(SkillName, list, item -> item, bp.isControlRate, bp.calc));
            if (i == 0) {
                chi = true;
            } else {
                qing = true;
            }
        }
        if (chi) {
            target.addStatus(new StatusChi(daYuan, target, level));
        }
        if (qing) {
            target.addStatus(new StatusQing(daYuan, target, level));
        }

        // lv5 - 若治疗暴击,则额外提升该目标暴击伤害 **必须在治疗目标之后,治疗其他队友之前**
        if (level == 5 && healInfo.isCrit()) {
            target.addStatus(new StatusCritPower(daYuan, target));
        }

        // 自身神力3层及以上时,额外治疗所选目标以外的友方生命上限8%的生命
        if (shenLiStack >= 3) {
            List<Character> targets = CharacterFinder.findTeammate(daYuan, bp.situation.characters);
            targets.remove(target);
            heal(interactive, targets);
        }
    }


    private Info heal(Interactive interactive, Character target) {
        // lv4-若治疗时目标生命为100%，则治疗时额外施加生命上限8%的护盾，判断是否满血的逻辑在install方法内部
        if (getLevel() >= 4) {
            StatusDaYuanShield.install(getBelongTo(), target);
        }
        return interactive.heal(SkillName, target, 8);
    }

    private void heal(Interactive interactive, List<Character> targets) {
        if (getLevel() >= 4) {
            for (Character target : targets) {
                StatusDaYuanShield.install(getBelongTo(), target);
            }
        }
        interactive.heal(SkillName, targets, 8);
    }

    static class StatusCritPower extends Status implements AttributeModifier {
        // 防止from为大缘自身时递归计算
        private boolean counting;

        public StatusCritPower(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            setSettleType(StatusDurationType.WEI_CHI, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER && !counting;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            counting = true;
            double rt = from.getCritPower() * 0.4;
            counting = false;
            return Math.min(rt, 110);
        }
    }
}

