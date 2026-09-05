package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamKilledCharacter;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "龙胆·绽";
    private static final double[] InitLDLLHp = new double[]{0, 2.4, 2.8, 3.2, 3.6, 3.6};

    private final StatusWLXR statusWLXR;

    private CharacterLDLL characterLDLL;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
        statusWLXR = new StatusWLXR(belongTo);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, () -> summon(0, false));
        }
        Status.of(SkillName + "击杀监听", belongTo)
                .retainAfterDie()
                .retainAfterChangeWave()
                .runOn(Trigger.KILLED_CHARACTER, param -> {
                    Character c = ((ParamKilledCharacter) param).getCharacter();
                    if (c.team != belongTo.team && !c.isSummon()) {
                        Skill2.this.summon(c.getLocation(), true);
                    }
                }).addTo();
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
        summon(0, false);
        return Optional.empty();
    }

    private void summon(double location, boolean forceChangeLocation) {
        if (characterLDLL == null || !characterLDLL.alive) {
            characterLDLL = new CharacterLDLL(((XueYuQian) getBelongTo()), InitLDLLHp[getLevel()], location);
            getBelongTo().bp.addCharacter(characterLDLL);
        } else {
            characterLDLL.repeatSummon(location, forceChangeLocation);
        }
    }
}
