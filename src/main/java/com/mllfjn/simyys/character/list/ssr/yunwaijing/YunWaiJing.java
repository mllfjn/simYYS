package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.interactive.HealInfo;

public class YunWaiJing extends CharacterShiShenBase {
    public static final String CharacterName = "云外镜";

    private Status typeAttribute;

    private Skill1Yin skill1Yin;
    private Skill2.Skill2Yin skill2Yin;
    private Skill3Yin skill3Yin;

    private Skill1Yang skill1Yang;
    private Skill2.Skill2Yang skill2Yang;
    private Skill3Yang skill3Yang;

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
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
        return "3028";
    }

    @Override
    protected void addOwnSkills() {
        bp().addPriorityMove(this, () -> {
            typeAttribute = Status.of("额外属性", this).retainAfterDie().addTo();
            if (getInitBaseAttack() > getInitDefense() * 5) {
                changeTo(Type.Yin);
            } else {
                changeTo(Type.Yang);
            }
            if (awakening) {
                this.addStatus(new StatusJingDun(this, this));
            }
            setHp(getMaxHp() * 0.5);
        });
    }

    @Override
    public void beHeal(HealInfo healInfo) {
        addStatus(new StatusShield(this, this, healInfo.getTraceableNumber().getNumber())
                .duration(StatusDurationType.CHI_XU, 2));

    }

    void changeTo(Type type) {
        if (type == Type.Yin) {
            typeAttribute.removeAttribute(Attribute.DEFENCE);
            typeAttribute.attribute(Attribute.ATTACK, _ -> getInitDefense() * 2);

            // 删除阳状态的技能
            if (skill1Yang != null) {
                removeSkill(skill1Yang);
                removeSkill(skill2Yang);
                removeSkill(skill3Yang);

                skill3Yang.changeToYin();
            }

            // 添加阴状态技能
            if (skill1Yin == null) {
                skill1Yin = new Skill1Yin(this, skill1Level);
                skill2Yin = new Skill2.Skill2Yin(this);
                skill3Yin = new Skill3Yin(this, skill3Level);
            }
            addSkill(skill1Yin, true);
            addSkill(skill2Yin, true);
            addSkill(skill3Yin, true);
        } else {
            typeAttribute.removeAttribute(Attribute.ATTACK);
            typeAttribute.attribute(Attribute.DEFENCE, _ -> getInitBaseAttack() * 0.1);

            // 删除阴状态的技能
            if (skill1Yin != null) {
                removeSkill(skill1Yin);
                removeSkill(skill2Yin);
                removeSkill(skill3Yin);
            }

            // 添加阳状态技能
            if (skill1Yang == null) {
                skill1Yang = new Skill1Yang(this, skill1Level);
                skill2Yang = new Skill2.Skill2Yang(this);
                skill3Yang = new Skill3Yang(this, skill3Level);
            }
            addSkill(skill1Yang, true);
            addSkill(skill2Yang, true);
            addSkill(skill3Yang, true);

            skill3Yang.changeToYang();
        }
    }

    enum Type {Yin, Yang}
}
