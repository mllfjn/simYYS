package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill2 extends Skill {
    public static final String SkillName = "汐梦";
    private static final int[] multiplier = new int[]{0, 172, 185, 198, 211, 211};

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);

        // 非召唤物的敌方回合结束后,千姬增加10%的行动条
        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventRoundDone erd
                    && !erd.getCharacter().isSummon() // 非召唤物
                    && erd.getCharacter().team != belongTo.team // 敌方
            ) {
                belongTo.doInteractive(interactive -> interactive.increaseLocation(belongTo, 10));
            }
            return false;
        });
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);
        // 对敌方目标造成攻击(系数)伤害
        interactive.attack(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        // 若海原贝戟在场,额外附加汐梦
        if (((QianJi) getBelongTo()).getHaiYuanBeiJi() != null) {
            target.addStatus(new StatusXiMeng(getBelongTo(), target));
        }
        return Optional.of(target);
    }
}
