package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.character.skill.CharacterFinder;

public class ShenShe extends Character {
    public static final String privateName = "神堕八岐大蛇";

    private int skill1Level;
    private int skill2Level;
    private int skill3Level;

    // 共被6把神剑·天羽羽斩镇压，最后一把是篡位，所以计数是5
    private int zhenyaRest = 5;
    public ShenShe() {}

    public boolean isZhenYa() {
        return zhenyaRest > 0;
    }

    public void poChuZhenYa() {
        if (zhenyaRest > 0) {
            zhenyaRest--;
        }
    }

    @Override
    public void useFrontSkill(BattlePane bp) {
        // 二.lv5-先机:释放二
        if (skill2Level == 5) {
            getSkill(2).useWithoutCost(bp);
        }

    }

    @Override
    public void die(BattlePane bp) {
        super.die(bp);

        // 阵亡时将封存的攻击全部归还
        for (Character teammate : CharacterFinder.findTeammateShiShen(this, bp.characters)) {
            teammate.removeState(StateStoreAttack.privateName);
        }
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();

        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("4153");

        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("555"));

        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);

        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill2Level = PropertyKey.getSkillLevel(skillLevel, 2);
        skill3Level = PropertyKey.getSkillLevel(skillLevel, 3);
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2(this, skill2Level));
        skills.add(new Skill3(this, skill3Level));
    }
}
