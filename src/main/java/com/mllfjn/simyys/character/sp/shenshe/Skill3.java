package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill3 extends Skill {
    public static final String SkillName = "审判仪式";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        // 蛇神姿态下可释放
        return super.canUse(bp) && getBelongTo().isHaveState(StateSheShen.class);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        ShenShe shenShe = (ShenShe) getBelongTo();
        Interactive interactive = shenShe.getInteractive();
        StateSheShen sheShen= shenShe.getState(StateSheShen.class).orElseThrow();

        // 破除一把天羽羽斩的镇压
        shenShe.poChuZhenYa();

        // 指定1位除自身外友方式神附加堕化
        List<Character> teammateShiShen = CharacterFinder.findTeammateShiShen(shenShe, bp.situation.characters);
        // 去除已经有堕化的和自己
        teammateShiShen.removeIf(teammate -> teammate.isHaveState(StateDuoHua.class) || teammate == shenShe);
        if (!teammateShiShen.isEmpty()) {
            Character target;
            if (teammateShiShen.size() == 1) {
                // 如果只有一个目标了,直接给
                target = teammateShiShen.get(0);
            } else {
                // 如果绿标可以放,给绿标,否则随机
                target = bp.situation.getAutoTo(shenShe.team)
                        .filter(teammateShiShen::contains)
                        .orElseGet(() -> RateController.choose(SkillName, teammateShiShen, teammate -> teammate.name, bp.isControlRate, bp.calc));
            }
            lastUsedTarget = target;
            target.addState(new StateDuoHua(shenShe, target));
        }

        // 若累计破除5把天羽羽斩,解放强大的邪力,将神堕之力(2)替换位蛇神之噬
        if (!shenShe.isZhenYa()) {
            getBelongTo().removeSkill(2);
            getBelongTo().addSkill(new Skill2Special(shenShe));
        }

        // 剩余20%生命次数
        int times = (int) (shenShe.getHp() / shenShe.getMaxHp() / 0.2);

        // lv3-释放时蛇神每剩余20%生命,为自身提升8%行动条
        if (getLevel() >= 3) {
            interactive.increaseLocation(shenShe, 8 * times);
        }
        // lv5-释放时抽取封存的友方式神攻击，总值不超过自身初始攻击100%
        if (getLevel() == 5) {
            StateAddAttack.addAttack(shenShe, sheShen.getAttack());
        }

        int multiplier = 188;
        // lv4-释放时蛇神每剩余20%生命,本次伤害系数增加25%
        if (getLevel() >= 4) {
            multiplier += 25 * times;
        }
        // 对敌方全体造成攻击188%伤害
        interactive.attack(SkillName, CharacterFinder.findEnemy(shenShe, bp.situation.characters), multiplier, AttackType.QUN_TI);


        // 回到本体
        sheShen.backToNormal();
        // lv2-释放时蛇神每剩余20%生命,为自身恢复12%生命
        if (getLevel() >= 2) {
            interactive.recovery(shenShe, shenShe.getMaxHp() * 0.12 * times);
        }
    }
}
