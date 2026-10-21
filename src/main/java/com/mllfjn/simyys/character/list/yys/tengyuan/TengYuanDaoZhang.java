package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.*;

public class TengYuanDaoZhang extends CharacterYYSBase {
    public static final String CharacterName = "藤原道长";

    private final StatusLvYin statusLvYin;

    @Override
    protected boolean useSkillAuto() {
        if (getLvYin().getStack() >= 10) {
            if (tryUseSkill(5)) {
                return true;
            }

        }
        return tryUseSkill(4);
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        map.get(PropertyKey.GENERAL_SPEED_KEY).setValue("142");
        map.get(PropertyKey.GENERAL_DEFENSE_KEY).setValue("806");
        return map;
    }

    @Override
    protected YYSSkill[] getSkill() {
        return new YYSSkill[]{
                new YYSSkill(Skill1.SkillName, 5, 0),
                new YYSSkill(Skill2.SkillName, 0, 0),
                new YYSSkill(Skill3.SkillName, 0, 0),
                new YYSSkill(Skill4.SkillName, 5, 3),
                new YYSSkill(Skill5.SkillName, 5, 3),
                new YYSSkill(Skill6.SkillName, 5, 0),
                new YYSSkill(Skill7.SkillName, 1, 0),
                new YYSSkill(Skill8.SkillName, 6, 0)
        };
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skillLevels = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skillLevels[0], skillLevels[1]));

        skillLevels = ((PropertyMultiInput) map.get(Skill2.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill2(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill3.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill3(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill4.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill4(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill5(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill6.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill6(this, skillLevels[0], skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill7.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill7(this, skillLevels[1]));
        }

        skillLevels = ((PropertyMultiInput) map.get(Skill8.SkillName)).getValuesInt();
        if (skillLevels[0] > 0) {
            addSkill(new Skill8(this, skillLevels[0], skillLevels[1]));
        }
    }

    public TengYuanDaoZhang() {
        statusLvYin = new StatusLvYin(this);
        addStatus(statusLvYin);
    }

    StatusLvYin getLvYin() {
        return statusLvYin;
    }

    @Override
    public boolean isUncontrollable() {
        if (isHaveStatus(Skill4.StatusZouLv.class)) {
            return true;
        } else {
            return super.isUncontrollable();
        }
    }

    @Override
    public void round() {
        if (isHaveStatus(Skill4.StatusZouLv.class)) {
            statusLvYin.addStack(1);
        } else {
            super.round();
        }
    }
}
