package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.qiling.QiLingFactory;
import com.mllfjn.simyys.character.propertygetter.*;

public class TengYuanDaoZhang extends Character {
    public static final String CharacterName = "藤原道长";

    private static final String[] SkillNames = new String[]{
            Skill1.SkillName, Skill2.SkillName, Skill3.SkillName,
            Skill4.SkillName, Skill5.SkillName, Skill6.SkillName,
            Skill7.SkillName, Skill8.SkillName};
    private static final String[] DefaultSkillLevels = new String[]{
            "5", "0", "0",
            "5", "5", "5",
            "1", "6"};
    private static final String[] DefaultShuYin = new String[]{
            "0", "0", "0",
            "0", "0", "0",
            "0", "0"};

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

        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("142");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("3126");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("27140");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("806");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");

        map.put(PropertyKey.QI_LING_KEY, new PropertySelectSingle(QiLingFactory.QI_LING));

        ((PropertyCheck) map.get(PropertyKey.GENERAL_YYS_KEY)).setValue(true);

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
        int[] skill7 = ((PropertyMultiInput) map.get(Skill7.SkillName)).getValuesInt();
        int[] skill8 = ((PropertyMultiInput) map.get(Skill8.SkillName)).getValuesInt();

        addSkill(new Skill1(this, skill1[0]));
        if (skill2[0] > 0) addSkill(new Skill2(this, skill2[0]));
        if (skill3[0] > 0) addSkill(new Skill3(this, skill3[0]));
        if (skill4[0] > 0) addSkill(new Skill4(this, skill4[0]));
        if (skill5[0] > 0) addSkill(new Skill5(this, skill5[0]));
        if (skill6[0] > 0) addSkill(new Skill6(this, skill6[0]));
        if (skill7[0] > 0) addSkill(new Skill7(this));
        if (skill8[0] > 0) addSkill(new Skill8(this, skill8[0]));

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

    @Override
    protected String getDefaultBaseAttack() {
        return "3256";
    }

    @Override
    protected void addOwnSkills() {

    }
}
