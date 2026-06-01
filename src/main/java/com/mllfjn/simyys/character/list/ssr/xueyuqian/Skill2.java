package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "龙胆·绽";
    private static final double[] InitLDLLHp = new double[]{0, 2.4, 2.8, 3.2, 3.6, 3.6};

    private final StatusWLXR statusWLXR;

    private CharacterLDLL characterLDLL;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
        statusWLXR = new StatusWLXR(belongTo);
    }

    StatusWLXR getStatusWLXR() {
        return statusWLXR;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        if (characterLDLL == null) {
            characterLDLL = new CharacterLDLL(((XueYuQian) getBelongTo()), InitLDLLHp[getLevel()]);
            bp.addCharacter(characterLDLL);
        } else {
            characterLDLL.repeatSummon();
        }
        return Optional.empty();
    }

}
