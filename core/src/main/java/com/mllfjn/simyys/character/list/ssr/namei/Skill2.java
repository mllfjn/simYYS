package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "神赐之吻";
    private boolean useFront = false;
    private final boolean awakening;

    private int times = 3;
    private StatusAdder<?> adder;

    public Skill2(NaMei naMei, boolean awakening, int level) {
        super(naMei, level, 2, 0, 2);
        this.awakening = awakening;

        // lv5-先机:对攻击攻击最高的友方式神无消耗释放神赐之吻(2)
        if (level >= 5) {
            naMei.bp.addPriorityMove(naMei, this::useFront);
        }

        // lv2-当场上非召唤物友方目标首次剩余1点生命时，速度提升50%，持续2回合(每个目标至多生效1次,单次战斗累计至多生效3次)
        if (level >= 2) {
            adder = naMei.bp.addStatusAdder(c ->
                    c.team == naMei.team && !c.isSummon()
                            ? new StatusHpChangeListener(naMei, c, level >= 3)
                            : null
            );
        }
    }

    public void useFront() {
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
        // 先机时只能给攻击最高的友方式神,无视绿标,其他时候可以给除自身以外的任意友方包括阴阳师
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

    class StatusHpChangeListener extends Status {
        public StatusHpChangeListener(Character from, Character belongTo, boolean increaseCritPower) {
            super(SkillName + "血量监听", from, belongTo);
            runOn(Trigger.HP_CHANGE, _ -> {
                if (belongTo.getHp() == 1) {
                    belongTo.addStatus(new StatusNaMeiBuff(from, belongTo, increaseCritPower));
                    if (Skill2.this.times == 1) {
                        Skill2.this.adder.deleteAndRemove();
                        delete();
                    } else {
                        Skill2.this.times--;
                    }
                }
            });
        }
    }

    static class StatusNaMeiBuff extends Status {
        public StatusNaMeiBuff(Character from, Character belongTo, boolean increaseCritPower) {
            super(SkillName + "1血BUFF", from, belongTo);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.SPEED, _ -> belongTo.getInitSpeed() * 0.5);
            if (increaseCritPower) {
                attribute(Attribute.CRIT_POWER, 50.0);
            }
        }
    }
}
