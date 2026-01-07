package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

//      将受到伤害的40%延迟至自身下个回合开始前结算(延迟结算伤害不致命)
//      [释放]对敌方目标释放时,将其锁入时之缝,持续1回合
// √          对友方目标释放时,使其进入时之隙
// √              若目标为自身,获得1层时之辉并冷却1回合
// √              若目标为其他友方,使其获得2层时之辉
// √     lv2-友方在时之隙中伤害提升20%
//      lv3-时之缝持续时间提升至2回合
// √     lv4-友方在时之隙中的伤害提升增至40%
// √     lv5-先机:使初始攻击最高的友方式神获得1层时之辉
//      时之缝:减益,印记:无法受到行动条改变效果,无法在回合外释放妖术与普攻
// √     时之辉:增益,印记:至多2层,友方场上至多存在1个此效果
// √          卑弥呼释放[Skill3]后,消耗1层时之辉,使携带者进入时之隙
// √          携带者回合开始前,消耗1层时之辉,使此回合转化为时之隙
// √     时之隙:不计入回合统计,视为回合外行动


class Skill2 extends Skill {
    public static final String SkillName = "溯回时隙";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);

        belongTo.bp.addActionListener(belongTo, event -> {
            if (event instanceof EventBattleStart) {
                Character target = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

                StatusShiZhiHui.get(belongTo, target, 1, level);
                return true;
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
        // 自动状态下目标好像只会是友方
        // 优先绿标,其次是包括自身和阴阳师在内的所有友方中攻击最高的单位
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        StatusShiZhiXi.enter(belongTo, target, getLevel());

        if (target == belongTo) {
            setCoolDown(1);
            StatusShiZhiHui.get(belongTo, target, 1, getLevel());
        } else {
            StatusShiZhiHui.get(belongTo, target, 2, getLevel());
        }

        return Optional.of(target);
    }

    @Override
    public void pastRound() {
        super.pastRound();
        setCoolDown(0);
    }

}
