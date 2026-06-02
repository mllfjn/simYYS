package com.mllfjn.simyys.character.list.sphudie;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.HealInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "梦引之声";

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 2, 0, 3);
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
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        final int level = getLevel();

        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorAuto(Attribute.INIT_ATTACK, CharacterFinder.Criteria.MAX);

        HealInfo[] healInfos = interactive.heal(this, list, c -> {
            HealInfo healInfo = new HealInfo(belongTo, this, c, belongTo.getMaxHp());
            if (c == target) {
                healInfo.setMultiplier(level >= 2 ? 30 : 20);
            } else {
                healInfo.setMultiplier(15);
            }
            return healInfo;
        });

        if (level >= 3) {
            for (Character character : list) {
                character.replaceStatus(new StatusZengShang(belongTo, character));
            }
            if (level >= 4) {
                StatusMengJian.install(belongTo, target,
                        healInfos[list.indexOf(target)].getTraceableNumber().getNumber() * 0.5, skill2
                );
            }
        }

        return Optional.of(target);
    }

    private static class StatusZengShang extends Status implements AttributeModifier {
        public StatusZengShang(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 20;
        }
    }
}
