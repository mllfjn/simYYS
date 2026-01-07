package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.list.QingNvFang;

import java.util.List;
import java.util.Optional;

class Skill2Special extends Skill {
    public static final String SkillName = "蛇神之噬";

    public Skill2Special(Character belongTo) {
        super(belongTo, 0, 0, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveStatus(StatusZhongYan.class);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        ShenShe shenShe = (ShenShe) getBelongTo();
        // 展开终焉审判幻境,幻境中,神堕八岐大蛇免疫减益和 TODO 放逐
        shenShe.addStatus(new StatusZhongYan(shenShe));
        // 夺取阴阳师位进行接下来的战斗
        Character yys = new CharacterFinder(shenShe)
                .filterTeammate()
                .filterYYS(true)
                .getFirst();
        if (yys != null) {
            yys.die();
        }
        // 获得新回合
        shenShe.doInteractive(interactive -> interactive.getNewRound(shenShe));
        // 将审判仪式(3)替换位终焉裁决
        getBelongTo().removeSkill(3);
        getBelongTo().addSkill(new Skill3Special(shenShe));
        // 并在原地召唤1把堕落之剑
        new DuoLuoZhiJian(shenShe, shenShe, bp, false);
        // 将剩余非召唤物的友方目标献祭为堕落之剑,并夺取其6%的初始攻击
        // 既然献祭之后立马就死而且不能复活,那就没必要减了直接加上就行
        List<Character> teammateShiShen = new CharacterFinder(shenShe)
                .filterTeammate()
                .filterShiShen()
                .filterSelf()
                // 这里不知道为什么会青女房会例外,免疫减益的不止这一个啊
                .filter(character -> !character.isHaveStatus(QingNvFang.getStatusClass()))
                .getList();
        for (Character character : teammateShiShen) {
            new DuoLuoZhiJian(shenShe, character, bp, true);
            StatusAddAttack.addAttack((ShenShe) getBelongTo(), character.getInitAttack() * 0.06);
        }
        return Optional.empty();
    }
}
