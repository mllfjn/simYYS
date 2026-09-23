package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.PassiveSkill;

import java.util.HashSet;
import java.util.Set;

class Skill6 extends PassiveSkill {
    private static final String SkillName = "召唤";

    private final Skill7 skill7;

    private final Set<Character> spiderSet = new HashSet<>();

    private int forceSummonTimes = 0;

    public Skill6(Character belongTo, Skill7 skill7) {
        super(belongTo, -1, 6);
        this.skill7 = skill7;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t部位被破坏时,召唤出一群小蜘蛛前来助战
                √\t本体受到一定量伤害时(用转阶段实现),也会召唤一群小蜘蛛前来助战
                """;
    }

    void clearSpider() {
        if (spiderSet.isEmpty()) {
            return;
        }
        Character[] array = spiderSet.toArray(new Character[]{});
        for (Character character : array) {
            character.die();
        }
    }

    void forceSummon() {
        if (spiderSet.isEmpty()) {
            summon();
        } else {
            forceSummonTimes++;
        }
    }

    void summon() {
        // 经测试,先打掉背,杀一个小蜘蛛再打掉腿,不会补充小蜘蛛
        if (!spiderSet.isEmpty()) {
            return;
        }

        if (forceSummonTimes > 0) {
            forceSummonTimes--;
        }

        Character belongTo = getBelongTo();
        for (int i = 0; i < 5; i++) {
            Character spider = new CharacterSummonBase(belongTo.bp, "小蜘蛛", belongTo.team) {
                {
                    forceSetMaxHp(77600, true);
                    setInitDefense(212);
                    setMob(1, 1);
                }

                @Override
                protected void dieHandle() {
                    spiderDie(this);
                }
            };
            belongTo.bp.addCharacter(spider);
            spiderSet.add(spider);
            skill7.tZZReduceEnable();
        }
        log(null);
    }

    private void spiderDie(Character spider) {
        skill7.tZZReduceDisable();
        spiderSet.remove(spider);

        if (forceSummonTimes > 0) {
            summon();
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
