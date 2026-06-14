package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "水宿山行";

    final Skill2 skill2;
    StatusJieJieContainer status;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        if (status != null) {
            status.refresh();
        } else {
            status = new StatusJieJieContainer(getBelongTo(), getLevel());
            getBelongTo().addStatus(status);
        }
        return Optional.empty();
    }

    class StatusJieJieContainer extends Status {
        private final StatusAdder<StatusJieJieEffect> adder;

        final boolean reduceCritDamage;
        final boolean increaseNonCritDamage;
        final boolean increaseAttack;

        public StatusJieJieContainer(Character character, int level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.WEI_CHI, 3);
            adder = character.bp.addStatusAdder(c ->
                    c.team == character.team
                            ? new StatusJieJieEffect(Skill3.this, this, character, c)
                            : null
            );
            reduceCritDamage = level >= 2;
            increaseNonCritDamage = level >= 3;
            increaseAttack = level >= 4;
        }

        void refresh() {
            setDuration(3);
            for (StatusJieJieEffect statusJieJieEffect : adder.getList()) {
                statusJieJieEffect.lastSkill = null;
            }
        }

        @Override
        public void beforeDelete() {
            adder.deleteAndRemove();
            Skill3.this.status = null;
        }

    }

}
