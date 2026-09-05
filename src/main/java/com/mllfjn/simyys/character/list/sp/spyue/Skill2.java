package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "炎崖";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 2);
        if (level >= 5) {
            belongTo.bp().addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        getBelongTo().addStatusOrChange(
                StatusYSHJAdder.class,
                status -> status.duration(3),
                () -> new StatusYSHJAdder(getBelongTo(), getLevel() >= 2 ? 25 : 15, getLevel() >= 3)
        );

        if (getLevel() >= 4) {
            List<Character> list = new CharacterFinder(getBelongTo())
                    .filterEnemy()
                    .getList();
            for (Character character : list) {
                StatusZhiKao.addStack(getBelongTo(), character, this, 3);
            }
        }

        return Optional.empty();
    }

    private class StatusYSHJAdder extends Status {
        public StatusYSHJAdder(Character character, double zengSHang, boolean reduce) {
            super("御神火界幻境", character);
            duration(StatusDurationType.WEI_CHI, 3);

            StatusAdder<Status> adder = character.bp().addStatusAdder(c ->
                    new StatusYSHJ(character, c, zengSHang, reduce)
            );
            beforeDelete(adder::deleteAndRemove);
        }

        private class StatusYSHJ extends Status {
            public StatusYSHJ(Character from, Character belongTo, double zengShang, boolean reduce) {
                super("御神火界", from, belongTo);
                runOn(Trigger.AFTER_ACTION, _ ->
                        StatusZhiKao.addStack(from, belongTo, Skill2.this, 1)
                );
                if (from.team == belongTo.team) {
                    runOn(Trigger.WILL_USE_SKILL, _ -> {
                        if (!belongTo.isHaveStatus(StatusYSHJZengShang.class)) {
                            belongTo.addStatus(new StatusYSHJZengShang(from, belongTo, zengShang));
                        }
                    });
                    if (reduce && CharacterFactory.isFireCharacter(belongTo)) {
                        forceChangeSkillCost(-1);
                    }
                }
            }

            private static class StatusYSHJZengShang extends Status {
                public StatusYSHJZengShang(Character from, Character belongTo, double zengShang) {
                    super("御神火界增伤", from, belongTo);
                    duration(StatusDurationType.CHI_XU, belongTo.isInRound() ? 0 : 1);
                    attribute(Attribute.ZENG_SHANG, zengShang);
                }
            }
        }
    }
}

