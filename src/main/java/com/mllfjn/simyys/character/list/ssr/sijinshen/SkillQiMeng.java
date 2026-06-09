package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class SkillQiMeng extends Skill {
    private static final String SkillName = "启蒙";

    private final List<Character> zSZHCarriers = new ArrayList<>();
    private final double coefficient;
    private final double maxDamage;

    public SkillQiMeng(Character belongTo, double coefficient) {
        super(belongTo, -1, -1, 0, -1);
        this.coefficient = coefficient;
        maxDamage = belongTo.getInitAttack() * 24;

        belongTo.bp.addStatusAdder(c ->
                c.team == belongTo.team && c != belongTo && CharacterFactory.FIRE_CHARACTER.contains(c.getClass())
                        ? new StatusQMCauseAttackListener(this, belongTo, c)
                        : null
        );
    }

    void start() {
        getBelongTo().statusRun(Trigger.WILL_USE_SKILL, new ParamUseSkill(this, null, 0));
        log(null);
    }

    void addCharacter(Character belongTo) {
        zSZHCarriers.add(belongTo);
    }

    void removeCharacter(Character belongTo) {
        zSZHCarriers.remove(belongTo);
    }

    void doInteractive(AttackInfo attackInfo) {
        getBelongTo().doInteractive(interactive ->
                interactive.attack(AttackInfo
                        .createGuDingAttack(getBelongTo(), SkillQiMeng.this, attackInfo.getTarget(),
                                Math.min(maxDamage, attackInfo.getTraceableNumber().getNumber() * coefficient)
                        )
                )
        );
    }

    Optional<Character> getMaxAttacker() {
        return zSZHCarriers.stream().max(Comparator.comparing(Attribute.ATTACK.getGetter()));
    }

    boolean isEffective() {
        return !zSZHCarriers.isEmpty();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

}
