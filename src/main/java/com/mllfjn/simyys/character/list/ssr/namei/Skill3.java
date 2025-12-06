package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.EffectInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    public static final String SkillName = "灭世之舞";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 目标首先为红标,其次是生命最高单位
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(CharacterFinder.Property.HP, CharacterFinder.Criteria.MAX);

        List<Character> enemy = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getList();
        // 没有测试是先上凋零还是先造成伤害,这里猜测是先凋零

        // 这种对不同单位的概率不同的情况好像不太常见,就不写通用逻辑了
        if (getLevel() >= 4) {
            List<Character> mob = new ArrayList<>();
            List<Character> notMob = new ArrayList<>();
            for (Character character : enemy) {
                if (character.isMob()) {
                    mob.add(character);
                } else {
                    notMob.add(character);
                }
            }
            if (!mob.isEmpty()) {
                addDiaoLing((NaMei) getBelongTo(), interactive, mob, true);
            }
            if (!notMob.isEmpty()) {
                addDiaoLing((NaMei) getBelongTo(), interactive, notMob, false);
            }
        } else {
            addDiaoLing((NaMei) getBelongTo(), interactive, enemy, false);
        }

        // 攻击敌方全体造成攻击120%伤害
        interactive.attack(this, enemy, 120, AttackType.QUN_TI);
        // 攻击指定敌方目标造成攻击120%伤害
        interactive.attack(this, target, 120, AttackType.DAN_TI);
        // TODO 并额外使其获得恍惚

        return Optional.of(target);
    }

    private void addDiaoLing(NaMei naMei, Interactive interactive, List<Character> list, boolean isMob) {
        StringBuilder sb = new StringBuilder();
        sb.append(StatusDiaoLing.text);
        if (isMob) {
            sb.append("-对怪物释放");
        }

        // 并有25%基础概率施加凋零,持续1回合
        // lv4-对怪物释放时,施加凋零的基础概率提升至80%
        // lv3-释放时,施加的凋零持续事件增至2回合
        EffectInfo[] infos = interactive.effect(sb.toString(), list, isMob ? 80 : 25, true
                , (from, to) -> new StatusDiaoLing(from, to, getLevel() >= 3 ? 2 : 1));

        // lv2-凋零命中时施加沉沦,持续1回合
        if (getLevel() >= 2) {
            for (int i = 0; i < infos.length; i++) {
                if (infos[i].isHit()) {
                    list.get(i).addStatus(new StatusChenLun(naMei, list.get(i), getLevel()));
                }
            }
        }

    }
}
