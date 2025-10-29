package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class Skill2Special extends Skill {
    public static final String privateName = "蛇神之噬";

    public Skill2Special(Character belongTo) {
        super(belongTo, 0, 0, 0, 2);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        ShenShe shenShe = (ShenShe) getBelongTo();
        // 展开终焉审判幻境,幻境中,神堕八岐大蛇免疫减益和 TODO 放逐
        shenShe.addState(new StateZhongYan(shenShe));
        // 夺取阴阳师位进行接下来的战斗
        bp.characters.removeIf(character -> character.team == shenShe.team && character.isYYS());
        // 获得新回合
        bp.getNewRound(shenShe);
        // 将审判仪式(3)替换位终焉裁决
        ObservableList<Skill> skills = shenShe.getSkills();
        skills.remove(3);
        skills.add(3, new Skill3Special(shenShe));
        // 并在原地召唤1把堕落之剑
        new DuoLuoZhiJian(shenShe, shenShe, bp, false);
        // 将剩余非召唤物的友方目标献祭为堕落之剑,并夺取其6%的初始攻击
        // 既然献祭之后立马就死而且不能复活,那就没必要减了直接加上就行
        for (Character character : CharacterFinder.findTeammateShiShen(shenShe, bp.characters)) {
            new DuoLuoZhiJian(shenShe, character, bp, true);
            StateAddAttack.addAttack((ShenShe) getBelongTo(), character.getInitAttack() * 0.06);
        }
    }
}
