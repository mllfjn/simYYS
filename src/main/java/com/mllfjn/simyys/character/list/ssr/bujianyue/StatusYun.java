package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class StatusYun extends Status implements Displayable {
    static final String StatusName = "峦纹·云";

    private final Skill2 skill2;
    private int stack = 1;

    private StatusYun(Character character, Skill2 skill2) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.skill2 = skill2;
    }

    static void addStack(Character character, Skill2 skill2) {
        character.addStatusOrChange(StatusYun.class, StatusYun::addStack,
                () -> new StatusYun(character, skill2)
        );
    }

    void addStack() {
        if (stack < 5) {
            stack++;
        }
    }

    void consumeStack() {
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        for (Character character : list) {
            character.addStatusOrChange(StatusIncreaseNonCrit.class,
                    status -> status.add(stack, skill2.getNonCritIncreaseDuration()),
                    () -> new StatusIncreaseNonCrit(belongTo, character, stack, skill2.getNonCritIncreaseDuration())
            );
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    static class StatusIncreaseNonCrit extends Status implements InfluenceDamageWhenAttack {
        private int stack;

        public StatusIncreaseNonCrit(Character from, Character belongTo, int stack, int duration) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.stack = stack;
            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        void add(int addStack, int duration) {
            stack = Math.min(stack + addStack, 5);
            setDuration(duration);
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            if (!attackInfo.isCrit()) {
                double maxIncrease = from.getInitDefense() * 60;
                double increase = attackInfo.getTraceableNumber().getNumber() * 0.03 * stack;
                if (increase <= maxIncrease) {
                    attackInfo.getTraceableNumber().mul(1 + stack * 0.03, StatusName);
                } else {
                    attackInfo.getTraceableNumber().add(maxIncrease, StatusName);
                }
            }
        }
    }
}
