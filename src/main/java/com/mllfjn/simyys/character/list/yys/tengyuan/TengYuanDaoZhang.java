package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;

public class TengYuanDaoZhang extends Character {
    public static final String CharacterName = "藤原道长";

    public static final String[] SkillNames = new String[]{
            Skill1.SkillName, Skill2.SkillName, Skill3.SkillName,
            Skill4.SkillName, Skill5.SkillName, Skill6.SkillName,
            Skill7.SkillName, Skill8.SkillName};
    public static final String[] DefaultSkillLevels = new String[]{
            "5", "0", "0",
            "5", "5", "5",
            "1", "6"};
    public static final String[] DefaultShuYin = new String[]{
            "0", "0", "0",
            "0", "0", "0",
            "0", "0"};

    private final StatusLvYin statusLvYin;

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        PropertyKey.addYYSSkill(map, SkillNames, DefaultSkillLevels, DefaultShuYin);

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skill1 = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        int[] skill2 = ((PropertyMultiInput) map.get(Skill2.SkillName)).getValuesInt();
        int[] skill3 = ((PropertyMultiInput) map.get(Skill3.SkillName)).getValuesInt();
        int[] skill4 = ((PropertyMultiInput) map.get(Skill4.SkillName)).getValuesInt();
        int[] skill5 = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        int[] skill6 = ((PropertyMultiInput) map.get(Skill6.SkillName)).getValuesInt();
//        int[] skill7 = ((PropertyMultiInput) map.get(Skill7.SkillName)).getValuesInt();
        int[] skill8 = ((PropertyMultiInput) map.get(Skill8.SkillName)).getValuesInt();

        addSkills(
                new Skill1(this, skill1[0]),
                new Skill2(this, skill2[0]),
                new Skill3(this, skill3[0]),
                new Skill4(this, skill4[0]),
                new Skill5(this, skill5[0]),
                new Skill6(this, skill6[0]),
                new Skill7(this),
                new Skill8(this, skill8[0])
        );
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
            statusLvYin.addStack(1);
            return true;
        } else {
            return super.isUncontrollable();
        }
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "";
    }

    @Override
    protected void addOwnSkills() {

    }
}
