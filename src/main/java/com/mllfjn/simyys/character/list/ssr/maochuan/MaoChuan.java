package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

/**
 * 分别测试猫川自己和队友上述情况下经过温泉：
 * 1、跑条经过温泉
 * 2、拉条经过温泉
 * 3、推条经过温泉
 * 4、神乐PVP疾风目标在温泉以前
 * 5、妖琴获得回合目标在温泉以前
 * 6、开局设置位置
 * 测试结果
 * 1、有效
 * 2、有效
 * 3、有效
 * 4、疾风的目标有效，但是神乐自己无效
 * 5、有效
 * 6、无效！
 * 另：注意行动时会设置行动条为0，此时不触发
 */
public class MaoChuan extends CharacterShiShenBase {
    public static final String CharacterName = "猫川";

    private CharacterBieGuanSiTang bieGuanSiTang;

    boolean isBieGuanSiTangExist() {
        return bieGuanSiTang != null;
    }

    void setBieGuanSiTang(CharacterBieGuanSiTang bieGuanSiTang) {
        this.bieGuanSiTang = bieGuanSiTang;
    }

    CharacterBieGuanSiTang getBieGuanSiTang() {
        return bieGuanSiTang;
    }

    @Override
    public double getLocationWhenGettingOrder() {
        if (bieGuanSiTang != null && skill3Level >= 3 && getLocation() < 70) {
            return getLocation() + 30;
        } else {
            return super.getLocationWhenGettingOrder();
        }
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
        return "3377";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
