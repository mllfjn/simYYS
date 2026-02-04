package com.mllfjn.simyys.character.list.yys.qingming;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;
import java.util.Optional;

public class QingMing extends CharacterYYSBase {
    public static final String CharacterName = "晴明";
    private static final String[] SkillNames = new String[]{Skill1.SkillName, Skill5.SkillName, Skill8.SkillName};
    private static final String[] DefaultSkillLevels = new String[]{"5", "5", "5"};
    private static final String[] DefaultShuYin = new String[]{"0", "3", "3"};

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

        int[] skill = ((PropertyMultiInput) map.get(Skill1.SkillName)).getValuesInt();
        addSkill(new Skill1(this, skill[0], skill[1]));

        skill = ((PropertyMultiInput) map.get(Skill5.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill5(this, skill[0], skill[1]));
        }

        skill = ((PropertyMultiInput) map.get(Skill8.SkillName)).getValuesInt();
        if (skill[0] > 0) {
            addSkill(new Skill8(this, skill[0], skill[1]));
        }
    }

    @Override
    protected boolean useSkillAuto() {
        // 如果有星技能并且自己身上没有星状态，放星
        Optional<Skill> oSkill8 = getSkill(8);
        if (oSkill8.isPresent()) {
            if (getStatus(Skill8.StatusXing.class).isEmpty()) {
                return tryUseSkill(8);
            }
        }
        // 如果不放星，看对面身上是否有灭，如果都没有则放灭
        Optional<Skill> oSkill5 = getSkill(5);
        if (oSkill5.isPresent()) {
            boolean have = false;
            List<Character> list = new CharacterFinder(this)
                    .filterEnemy()
                    .getList();
            for (Character character : list) {
                if (character.isHaveStatus(Skill5.StatusMie.class)) {
                    have = true;
                    break;
                }
            }
            if (!have) {
                return tryUseSkill(5);
            }
        }

        return false;
    }
}
