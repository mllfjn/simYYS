package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.HealInfo;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "归山之路";
    private static final int[] multipliers = new int[]{0, 200, 220, 220, 240, 240};

    private final StatusRongXin statusRongXin;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 2, 0, 3);
        statusRongXin = new StatusRongXin(belongTo, skill2);
        belongTo.bp().addStatusAdder(c -> {
            if (c.team == belongTo.team && CharacterFactory.isFireCharacter(c)) {
                return Status.of(SkillName + "释放妖术监听器", belongTo, c)
                        .runOn(Trigger.WILL_USE_SKILL, _ -> statusRongXin.addStack(1));
            }
            return null;
        });
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        statusRongXin.addStack(2);
        List<Character> list = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getList();
        heal(list, multipliers[getLevel()]);
        return Optional.empty();
    }

    private class StatusRongXin extends Status {
        private int stack;
        private final Skill3Special skill3Special;

        private StatusRongXin(Character character, Skill2 skill2) {
            super("熔芯", character);
            skill3Special = new Skill3Special(character, skill2);
            type(StatusType.GENERAL, StatusForm.YIN_JI);
            display(() -> getName() + stack);
            retainAfterDie();
            retainAfterChangeWave(() -> stack = 0);
        }

        void addStack(int addStack) {
            this.stack += addStack;
            Character character = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
            Skill3.this.heal(List.of(character), 200);
            if (stack == 8) {
                stack = 0;
                skill3Special.usePrivate(belongTo.bp());
            }
        }
    }

    private void heal(List<Character> targets, int multiplier) {
        double defense = getBelongTo().getInitDefense();
        getBelongTo().doInteractive(interactive ->
                interactive.heal(this, targets, c -> {
                    HealInfo healInfo = new HealInfo(getBelongTo(), this, c, defense);
                    healInfo.setMultiplier(multiplier);
                    return healInfo;
                })
        );
        if (getLevel() >= 3) {
            for (Character target : targets) {
                target.addStatusOrChange(
                        StatusGSZLHealBuff.class,
                        status -> status.duration(2),
                        () -> new StatusGSZLHealBuff(getBelongTo(), target, getLevel() >= 5)
                );
            }
        }
    }

    private static class StatusGSZLHealBuff extends Status {
        public StatusGSZLHealBuff(Character from, Character belongTo, boolean attack) {
            super(SkillName + "BUFF", from, belongTo);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.DEFENCE, _ -> 0.25 * from.getInitDefense());
            if (attack) {
                attribute(Attribute.ATTACK, _ ->
                        Math.min(belongTo.getInitAttack() * 0.25, from.getInitDefense() * 1.6)
                );
            }
        }
    }
}
