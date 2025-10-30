package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.determinant.IgnoreChangeMaxHp;
import com.mllfjn.simyys.state.determinant.IgnoreDebuff;
import com.mllfjn.simyys.state.determinant.InfluenceDamage;
import com.mllfjn.simyys.state.determinant.PreventDie;

// 无法改变生命上限,免疫减益和 TODO 放逐
public class StateSheShen extends State implements IgnoreChangeMaxHp, IgnoreDebuff, PreventDie, InfluenceDamage {
    public static final String privateName = "蛇神";
    private final double originalMaxHp;
    private final double originalHp;

    private final double attack;

    private final int level;

    public StateSheShen(Character character, int skillLevel, double attack) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);

        originalHp = character.getHp();
        originalMaxHp = character.getMaxHp();

        this.attack = attack;
        this.level = skillLevel;

        // 生命为神堕八岐大蛇攻击200%
        character.setMaxHp(character.getAttack() * skillLevel == 1 ? 2 : 3.8);
        character.setHp(character.getMaxHp());
    }

    public double getAttack() {
        return attack;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void action() {
        backToNormal();

        // lv4-蛇神被击败时,自身提升100点速度,持续1个回合
        if (level >= 4) {
            belongTo.addState(new StateSheShenSpeed(belongTo));
        }
    }

    public void backToNormal() {
        // 当蛇神通过审判仪式破除天羽羽斩镇压或受到致命伤害时,本体重新回到场上
        delete();
        belongTo.setMaxHp(originalMaxHp);
        belongTo.setHp(originalHp);
    }


    @Override
    public boolean effective(AttackType attackType, Character character) {
        // lv3-蛇神受到的群体伤害下降30%
        return level >=3 && attackType == AttackType.QUN_TI;
    }

    @Override
    public void doInfluence(AttackType attackType, Info info) {
        info.getTraceableNumber().mul(0.7, "蛇神");
    }
}

class StateSheShenSpeed extends State implements AttributeModifier {
    public StateSheShenSpeed(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
        this.setSettleType(StateSettleType.CHI_XU, 1);
    }

    @Override
    public void setName() {
        name = "蛇神加速100";
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 100;
    }
}
