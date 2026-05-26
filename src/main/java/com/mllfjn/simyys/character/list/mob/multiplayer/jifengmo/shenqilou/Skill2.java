package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "蜃气爆弹";

    public Skill2(Character belongTo) {
        super(belongTo, 0, 3, 4, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        ShenQiLou shenQiLou = ((ShenQiLou) getBelongTo());
        Interactive interactive = shenQiLou.getInteractive();
        // 对群体目标造成当前生命40%的伤害,没测试,猜测是真伤
        // 降低敌方群体15%暴击率
        List<Character> target = new CharacterFinder(shenQiLou)
                .filterEnemy()
                .getList();
        for (Character character : target) {
            interactive.attack(AttackInfo.createRealAttack(
                    shenQiLou, this, character, character.getHp() * 0.4)
            );
            character.addStatus(new StatusReduceCritRate(shenQiLou, character, 15));
        }
        return Optional.empty();
    }
}
