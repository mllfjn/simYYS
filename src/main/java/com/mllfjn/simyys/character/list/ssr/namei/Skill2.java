package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.battleevent.EventHpChange;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class Skill2 extends Skill {
    private static final String SkillName = "神赐之吻";
    private boolean useFront = false;
    private final boolean awakening;

    // 1血增益
    private final Set<Character> effectedCharacters = new HashSet<>();
    private int times = 3;

    public Skill2(Character belongTo, boolean awakening, int level) {
        super(belongTo, level, 2, 0, 2);
        NaMei naMei = (NaMei) belongTo;
        this.awakening = awakening;

        // lv5-先机:对攻击攻击最高的友方式神无消耗释放神赐之吻(2)
        if (level >= 5) {
            naMei.bp.atBattleStart(() -> useFront(naMei.bp));
        }

        // lv2-当场上非召唤物友方目标首次剩余1点生命时，速度提升50%，持续2回合(每个目标至多生效1次,单次战斗累计至多生效3次)
        if (level >= 2) {
            naMei.bp.addActionListener(naMei, event -> {
                if (event instanceof EventHpChange ec) {
                    if (!effectedCharacters.contains(ec.getCharacter()) && ec.getCharacter().getHp() == 1) {
                        effectedCharacters.add(ec.getCharacter());
                        ec.getCharacter().addStatus(new StatusNaMeiSpeed(naMei, ec.getCharacter()));
                        // lv3-当场上非召唤物友方目标首次剩余1点生命时,暴击伤害提升50% 限制与lv2相同
                        if (level >= 3) {
                            ec.getCharacter().addStatus(new StatusNaMeiCritPower(naMei, ec.getCharacter()));
                        }
                        // 如果3次结束return true删除监听器
                        return times-- == 0;
                    }
                }
                return false;
            });
        }
    }

    public void useFront(BattlePane bp) {
        useFront = true;
        this.useWithoutCost();
        useFront = false;
    }
    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = getTarget();

        // 为自身以外的指定友方目标施加毁灭
        if (!target.isHaveStatus(StatusHuiMie.class)) {
            target.addStatus(new StatusHuiMie((NaMei) getBelongTo(), target, getLevel(), awakening));
        } else { // 若该目标已处于毁灭,则使其额外失去最大生命50%的生命(该效果不致命)
            target.setHp(Math.max(1, target.getHp() - target.getMaxHp() * 0.5));
        }

        return Optional.of(target);
    }

    private Character getTarget() {
        CharacterFinder characterFinder = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterSelf();
        // 先机时只能给攻击最高的友方式神,其他时候可以给除自身以外的任意友方包括阴阳师
        if (useFront) {
            characterFinder.filterYYS(false);
            return characterFinder.get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        } else {
            return characterFinder.getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}


class StatusNaMeiSpeed extends Status implements AttributeModifier {
    public StatusNaMeiSpeed(NaMei from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 2);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return belongTo.getInitSpeed() * 0.5;
    }
}

class StatusNaMeiCritPower extends Status implements AttributeModifier {
    public StatusNaMeiCritPower(NaMei from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 2);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        // 这里没测试是加50还是50%
        return 50;
    }
}
