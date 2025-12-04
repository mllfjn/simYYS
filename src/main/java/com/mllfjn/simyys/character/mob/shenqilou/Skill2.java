package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

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
    public void usePrivate(BattlePane bp) {
        ShenQiLou shenQiLou = ((ShenQiLou) getBelongTo());
        Interactive interactive = shenQiLou.getInteractive();
        // 对群体目标造成当前生命40%的伤害,没测试,猜测是真伤
        // 降低敌方群体15%暴击率
        List<Character> target = CharacterFinder.findEnemy(shenQiLou, bp.situation.characters);
        for (Character character : target) {
            interactive.attack(character, AttackType.ZHEN_SHI, AttackInfo.createRealAttack(
                    shenQiLou, character, (from, to) -> to.getHp() * 0.4
            ));
            character.addStatus(new StatusReduceCritRate(shenQiLou, character, 15));
        }

    }
}
