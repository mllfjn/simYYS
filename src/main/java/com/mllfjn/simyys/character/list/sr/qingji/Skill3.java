package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.EffectInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "焚身之火";
    private static final int[] multiplier = new int[]{0, 36, 38, 40, 42, 42};

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        int multi = multiplier[getLevel()];

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, list, multi, AttackType.QUN_TI);
            EffectInfo[] effectInfos = interactive.effect(this, list, 100, 0, true,
                    StatusPoisoning.getSupplier(3, 5)
            );
            for (EffectInfo effectInfo : effectInfos) {
                if (effectInfo.isHit()) {
                    skill2.madePoisoning(effectInfo.getTarget());
                }
            }
        }
        for (Character character : list) {
            StatusFenHuo.install(belongTo, character, this, getLevel() >= 5 ? 99 : 66);
        }
        return Optional.empty();
    }
}
