package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Interactive;

public class DuoLuoZhiJian extends Character {
    public static final String CharacterName = "堕落之剑";
    private final ShenShe shenShe;

    public DuoLuoZhiJian(ShenShe shenShe, Character character, BattlePane bp, boolean xianJi) {
        this.isSummon = true;
        this.shenShe = shenShe;
        name = CharacterName;
        setBattlePane(bp);
        // 生命为神堕八岐大蛇攻击的310%
        double hp = shenShe.getAttack() * 3.1;
        setMaxHp(hp, true);
        // 其余属性继承原式神
        this.setInitBaseAttack(character.getInitBaseAttack());
        this.setInitAdditionAttack(character.getInitAdditionAttack());
        this.setInitDefense(character.getInitDefense());
        this.setInitSpeed(character.getInitSpeed());
        this.setInitCritRate(character.getInitCritRate());
        this.setInitCritPower(character.getInitCritPower());
        this.setInitEffectHitRate(character.getInitEffectHitRate());
        this.setInitEffectResistRate(character.getInitEffectResistRate());

        if (xianJi) {
            character.die();
            // TODO 献祭:视同被击败,被献祭的友方无法触发复活效果,无法被复活
        }

        bp.addCharacter(this);
        StateShenSheJianShang.add(shenShe);
    }

    @Override
    public void dieHandle() {
        StateShenSheJianShang.reduce(shenShe);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return null;
    }

    @Override
    public void addOwnSkills() {

    }

    @Override
    public void round(boolean skip) {
        Interactive interactive = getInteractive();
        // 行动时恢复20%生命
        interactive.recovery(this, getMaxHp() * 0.2);
        // 获得1点鬼火
        bp.gainGuiHuo(this, 1);
        // 并为神堕八岐大蛇提升15%行动条
        interactive.increaseLocation(shenShe, 15);
    }

    @Override
    public boolean controllable() {
        return false;
    }
}
