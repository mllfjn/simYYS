package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.battleevent.EventRoundDone;

public class QianJi extends CharacterShiShenBase {
    public static final String CharacterName = "千姬";
    private HaiYuanBeiJi haiYuanBeiJi = null;

    public QianJi() {

    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2948";
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        // 非召唤物的敌方回合结束后,千姬增加10%的行动条
        bp.addActionListener(this, event -> {
            if (event instanceof EventRoundDone erd
                    && !erd.getCharacter().isSummon() // 非召唤物
                    && erd.getCharacter().team != team // 敌方
            ) {
                QianJi.this.doInteractive(interactive -> interactive.increaseLocation(QianJi.this, 10));
            }
            return false;
        });

        // 千姬免控,插锤子时自动移除,锤子拔掉自动获取
        addStatus(new StatusQianJiIgnoreDebuff(this));
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
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        getSkill3_1();
    }

    public HaiYuanBeiJi getHaiYuanBeiJi() {
        return haiYuanBeiJi;
    }

    public void setHaiYuanBeiJi(HaiYuanBeiJi haiYuanBeiJi) {
        this.haiYuanBeiJi = haiYuanBeiJi;
    }

    public void getSkill3_1() {
        addSkill(new Skill3_1(this, skill3Level));
    }

    @Override
    protected boolean useSkillAuto() {
        // 没锤子的时候插锤子,二技能和拔锤子就不自动放了
        if (haiYuanBeiJi == null) {
            return tryUseSkill(3);
        }
        return false;
    }

    static class StatusQianJiIgnoreDebuff extends Status implements IgnoreDebuff {
        public StatusQianJiIgnoreDebuff(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }
    }
}

