package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

public class BoYa extends CharacterYYSBase {
    public static final String CharacterName = "源博雅";
    public static final String[] SkillNames = new String[]{
            Skill1.SkillName, Skill2.SkillName, Skill3.SkillName,
            Skill4.SkillName, Skill5.SkillName, Skill6.SkillName,
            Skill7.SkillName, Skill8.SkillName};
    public static final String[] DefaultSkillLevels = new String[]{
            "5", "0", "5",
            "5", "5", "5",
            "5", "1"};
    public static final String[] DefaultShuYin = new String[]{
            "0", "0", "3",
            "0", "0", "0",
            "0", "0"};

    private Skill4.YinFenShen yinFenShen;
    private Skill8 skill8;

    Optional<Skill8> getSkill8() {
        return Optional.of(skill8);
    }

    Optional<Skill4.YinFenShen> getYinFenShen() {
        return Optional.ofNullable(yinFenShen);
    }

    void setYinFenShen(Skill4.YinFenShen yinFenShen) {
        this.yinFenShen = yinFenShen;
    }

    @Override
    protected boolean useSkillAuto() {
        if (yinFenShen == null) {
            Optional<Skill> oSkill4 = getSkill(4);
            if (oSkill4.isPresent()) {
                return oSkill4.get().tryUse(bp);
            }
        }

        return tryUseSkill(3);
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        // 博雅初始暴击20
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("20");

        PropertyKey.addYYSSkill(map, SkillNames, DefaultSkillLevels, DefaultShuYin);

        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        PropertiesMap map = propertiesHolder.propertiesMap;

        int[] skill = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skill[0], skill[1]));

        skill = ((PropertyMultiInput) map.get(Skill2.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill2(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill3.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill3(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill4.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill4(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill5(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill6.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill6(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill7.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill7(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill8.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            skill8 = new Skill8(this, skill[0], skill[1]);
            addSkill(skill8);
        }
    }

    @Override
    public void reset(BattlePane bp) {
        super.reset(bp);
        if (yinFenShen != null) {
            yinFenShen.bp = bp;
        }
    }
}
