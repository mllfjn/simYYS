package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusDeepFrozen;
import com.mllfjn.simyys.character.status.instance.StatusFrozen;
import com.mllfjn.simyys.character.yuhun.list.XueYouHun;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.StatusSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "暴风雪";
    private static final int[] multiplier = new int[]{0, 30, 33, 33, 36, 36};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        // 已经有冰冻的单位
        ArrayList<Character> charactersWithFrozen = new ArrayList<>();

        for (Character character : list) {
            if (character.isHaveStatus(StatusFrozen.class)) {
                charactersWithFrozen.add(character);
            }
        }

        interactive.effect(this, charactersWithFrozen, 30, false,
                new StatusSupplier("再次攻击冰冻", StatusFrozen.class, (from, to) -> {
                    if (to.isMob()) {
                        to.getStatus(StatusFrozen.class).orElseThrow().setDuration(2);
                    } else {
                        to.removeStatus(StatusFrozen.class);
                        StatusDeepFrozen.install(belongTo, to);
                    }
                })
        );

        ArrayList<Character> charactersWithoutReduceSpeed = new ArrayList<>();
        ArrayList<Character> charactersMobWithReduceSpeed = new ArrayList<>();
        ArrayList<Character> charactersNotMobWithReduceSpeed = new ArrayList<>();

        for (Character character : list) {
            if (XueYouHun.isHaveReduceSpeed(character)) {
                if (character.isMob()) {
                    charactersMobWithReduceSpeed.add(character);
                } else {
                    charactersNotMobWithReduceSpeed.add(character);
                }
            } else {
                charactersWithoutReduceSpeed.add(character);
            }
        }

        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);

            // 无减速,非怪物,8%基础概率冰冻
            interactive.effect(this, charactersWithoutReduceSpeed, 8, true,
                    StatusFrozen.getSupplier(1)
            );

            // 有减速,怪物,基础概率25%
            interactive.effect(this, charactersMobWithReduceSpeed, 25, true,
                    StatusFrozen.getSupplier(1)
            );

            // 有减速,非怪物,最终概率提升10%
            interactive.effect(this, charactersNotMobWithReduceSpeed, 8, 10, true,
                    StatusFrozen.getSupplier(1)
            );

        }

        if (getLevel() >= 3) {
            interactive.effect(this, list, getLevel() >= 5 ? 16 : 8, true,
                    StatusReduceSpeed.getSupplier()
            );
        }

        return Optional.empty();
    }
}
