package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.PreventDie;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "回梦";
    private static final int[] recordPercents = {0, 30, 40, 50, 60, 60};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 2, 3);
        if (level < 5) {
            setCoolDown(3);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int recordPercent = recordPercents[getLevel()];
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        for (Character character : list) {
            character.addStatus(new StatusRecordHp(belongTo, character, recordPercent));
        }
        return Optional.empty();
    }

    private class StatusRecordHp extends Status implements PreventDie {
        private final double recordHp;

        public StatusRecordHp(Character from, Character belongTo, int recordPercent) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            duration(StatusDurationType.WEI_CHI, 2);
            recordHp = belongTo.getHp() * recordPercent / 100;
        }

        @Override
        public void beforeDelete() {
            double needRecovery = recordHp - belongTo.getHp();
            if (needRecovery > 0) {
                from.doInteractive(interactive ->
                        interactive.recovery(Skill3.this, belongTo, needRecovery)
                );
            }
        }

        @Override
        public void preventDie(double excessDamage) {
            delete();
        }

        @Override
        public String getName() {
            return SkillName;
        }
    }
}
