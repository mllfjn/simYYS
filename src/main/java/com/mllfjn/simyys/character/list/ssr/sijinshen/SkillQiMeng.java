package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SkillQiMeng extends Skill {
    private static final String SkillName = "启蒙";

    private final List<Character> zSZHCarriers = new ArrayList<>();
    private final double coefficient;
    private final double maxDamage;
    private final List<AttackInfo> attackInfos = new ArrayList<>();

    private Skill skill;

    public SkillQiMeng(Character belongTo, double coefficient) {
        super(belongTo, -1, -1, 0, -1);
        this.coefficient = coefficient;
        maxDamage = belongTo.getInitAttack() * 24;

        belongTo.bp().addStatusAdder(c ->
                c.team == belongTo.team && c != belongTo && CharacterFactory.FIRE_CHARACTER.contains(c.getClass())
                        ? new StatusQMCauseAttackListener(this, belongTo, c)
                        : null
        );
    }

    void start(Skill skill) {
        this.skill = skill;
        getBelongTo().bp().interactive.qiMeng[getBelongTo().team] = this;
    }

    void log() {
        getBelongTo().statusRun(Trigger.WILL_USE_SKILL, new ParamUseSkill(this, null, 0));
        log(null);
    }

    void addCharacter(Character belongTo) {
        zSZHCarriers.add(belongTo);
    }

    void removeCharacter(Character belongTo) {
        zSZHCarriers.remove(belongTo);
    }

    void addAttackInfo(AttackInfo attackInfo) {
        // 歌姬不触发,原因不太确定
        if (attackInfo.getAttackType() != AttackType.ZHEN_SHI) {
            attackInfos.add(attackInfo);
        }
    }

    @Override
    public void useDone() {
        takeAction();
        skill = null;
        getBelongTo().bp().interactive.qiMeng[getBelongTo().team] = null;
        super.useDone();
    }

    public void takeAction() {
        if (!attackInfos.isEmpty()) {
            Character belongTo = getBelongTo();
            belongTo.doInteractive(interactive -> {
                for (AttackInfo attackInfo : attackInfos) {
                    if (attackInfo.getTarget().alive) {
                        interactive.attack(AttackInfo
                                .createGuDingAttack(belongTo, this, attackInfo.getTarget(),
                                        Math.min(maxDamage, attackInfo.getTraceableNumber().getNumber() * coefficient)
                                )
                        );
                    }
                }
            });
            attackInfos.clear();
        }
    }

    public void check(Skill skill) {
        if (skill == this.skill) {
            takeAction();
        }
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
