package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.instance.StatusBoss;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class CharacterBieGuanSiTang extends CharacterSummonBase {
    static final String CharacterName = "别馆私汤";
    private static final Skill skill = Skill.getInstance("温泉疗愈");

    private final MaoChuan maoChuan;
    private final double puGongDamageIncrease;
    private final Skill2 skill2;
    private final StatusAdder<?> adder;

    public CharacterBieGuanSiTang(MaoChuan maoChuan, double puGongDamageIncrease, Skill2 skill2) {
        super(maoChuan.bp, CharacterName, maoChuan.team);
        this.isSummon = true;
        this.maoChuan = maoChuan;
        this.puGongDamageIncrease = puGongDamageIncrease;
        this.skill2 = skill2;

        maoChuan.setBieGuanSiTang(this);
        forceSetMaxHp(5.5 * maoChuan.getAttack(), true);
        setInitDefense(maoChuan.getDefence());

        adder = bp.addStatusAdder(c ->
                c.team == this.team
                        ? new StatusPuGongDamageIncrease(this, c)
                        : null
        );

        addStatus(new StatusBoss(this));

        // 行动条固定在70
        forceSetLocation(70);
        bp.situation.canNotChangeLocation(this);
    }

    private void wenQuanLiaoYu(Character target, Interactive interactive) {
        // 基础恢复攻击98%
        double recovery = maoChuan.getAttack() * 0.98;
        // 如果自己双倍
        if (skill2.selfDouble && target == maoChuan) {
            recovery *= 2;
        }
        // 执行恢复
        double finalRecovery = recovery;
        interactive.recovery(skill, target, finalRecovery);
        // 并有概率驱散
        if (RateController.otherWhether(CharacterBieGuanSiTang.CharacterName, "驱散",
                bp.calc, skill2.rate
        )) {
            target.dispelDeBuffPrioritizeCrowdControl(1);
        }
    }

    void wenQuanLiaoYu(Character target) {
        this.doInteractive(interactive -> wenQuanLiaoYu(target, interactive));
    }

    void refresh() {
        doInteractive(interactive -> {
            interactive.recovery(skill, this, getMaxHp());
            List<Character> list = new CharacterFinder(this)
                    .filterTeammate()
                    .getList();
            for (Character character : list) {
                wenQuanLiaoYu(character);
            }
        });
    }

    @Override
    protected void dieHandle() {
        maoChuan.setBieGuanSiTang(null);
        adder.deleteAndRemove();
    }

    class StatusPuGongDamageIncrease extends Status implements InfluenceDamageWhenAttack {

        public StatusPuGongDamageIncrease(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            if (attackInfo.getSkill() instanceof Skill1PuGongBase) {
                attackInfo.getTraceableNumber().mul(CharacterBieGuanSiTang.this.puGongDamageIncrease, CharacterName);
            }
        }
    }
}
