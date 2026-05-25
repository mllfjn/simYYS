package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "无垢莲华";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);
        belongTo.addStatus(new StatusHuanJingListener(belongTo, level >= 3));
    }

    @Override
    public String getSkillDesc() {
        return """
                所有PVP相关的内容都没有做
                √\t回合开始时,若自身未处于无法动作,开启王之盛宴
                \t[释放]以100%基础概率对敌方目标施加金莲,持续1回合
                \tlv2-被施加金莲的敌方回合中降低80%初始攻击
                √\tlv3-若幻境未开启,回合结束时提升自身50%行动条
                \tlv4-被施加金莲的敌方回合中若进行普攻,友方获得1点鬼火
                \tlv5-金莲持续时间增加至2回合
                √\t\t对怪物施加时,使额外伤害增加帝释天攻击70%
                
                \t金莲:(要操控直接去操控,不写这个了)
                \t金莲可为帝释天抵挡被单次技能施加的控制效果,之后存在时间减少1回合;
                \t金莲在帝释天受到致命伤害时移除,抵挡伤害并使其恢复至最大生命值5%
                √\t对怪物施加会使其受到伤害时,额外受到1次该伤害40%的真实伤害,不触发帝释天御魂效果,持续至战斗结束
                √\t帝释天最多对1名敌人施加金莲,且自身不会受到此效果影响
                
                √\t王之盛宴:幻境中,每有1名敌方式神或阴阳师,全体友方速度降低3%
                √\t敌方回合开始时,行动条最前端友方提升30%行动条
                √\t幻境在帝释天死亡后关闭
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        DiShiTian belongTo = ((DiShiTian) getBelongTo());
        int level = getLevel();
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .filter(character -> !(character instanceof DiShiTian))
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        StatusJinLian status;

        if (target.isMob()) {
            status = new StatusJinLianForMob(belongTo, target, level >= 5, this);
        } else {
            status = new StatusJinLianNormal(belongTo, target);
        }

        target.addStatus(status);
        belongTo.addJinLian(status);

        return Optional.of(target);
    }

    static class StatusHuanJingListener extends Status implements StatusRunnable {
        private final boolean isIncreaseLocation;

        public StatusHuanJingListener(Character character, boolean isIncreaseLocation) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.isIncreaseLocation = isIncreaseLocation;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (trigger == Trigger.BEFORE_ROUND && !belongTo.isUncontrollable())
                    || (isIncreaseLocation && trigger == Trigger.AFTER_ROUND);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                ((DiShiTian) belongTo).openHuanJing();
                return true;
            } else {
                belongTo.doInteractive(interactive ->
                        interactive.increaseLocation(belongTo, 50)
                );
            }
            return false;
        }
    }
}
