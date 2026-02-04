package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusRejectAllStatusesInstance;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

public class DuoLuoZhiJian extends Character {
    public static final String CharacterName = "堕落之剑";
    private final ShenShe shenShe;

    public DuoLuoZhiJian(ShenShe shenShe, Character character, BattlePane bp, boolean xianJi) {
        this.isSummon = true;
        this.shenShe = shenShe;
        name = CharacterName;
        reset(bp);
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

        // 拒绝所有状态
        this.addStatus(new StatusRejectAllStatusesInstance(this));

        if (xianJi) {
            character.getStatus(StatusDuoHua.class).ifPresent(StatusDuoHua::disable);
            character.die();
            // TODO 献祭:视同被击败,被献祭的友方无法触发复活效果,无法被复活
        }

        bp.addCharacter(this);
        StatusShenSheJianShang.add(shenShe);

        fillSkills();
    }

    @Override
    public void dieHandle() {
        StatusShenSheJianShang.reduce(shenShe);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return null;
    }

    @Override
    public void addOwnSkills() {
        addSkill(new DLZJSkill1(this));
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }



    static class DLZJSkill1 extends Skill {

        public DLZJSkill1(Character belongTo) {
            super(belongTo, 0, 0, 0, 1);
        }

        @Override
        public String getName() {
            return DuoLuoZhiJian.CharacterName;
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            DuoLuoZhiJian belongTo = ((DuoLuoZhiJian) getBelongTo());
            Interactive interactive = belongTo.getInteractive();
            // 行动时恢复20%生命
            interactive.recovery(Skill.getInstance(DuoLuoZhiJian.CharacterName), belongTo, belongTo.getMaxHp() * 0.2);
            // 获得1点鬼火
            bp.gainGuiHuo(belongTo, 1);
            // 并为神堕八岐大蛇提升15%行动条
            interactive.increaseLocation(belongTo.shenShe, 15);
            return Optional.empty();
        }
    }
}

