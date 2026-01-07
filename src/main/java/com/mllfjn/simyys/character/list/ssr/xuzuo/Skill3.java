package com.mllfjn.simyys.character.list.ssr.xuzuo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.r.chounv.CaoRen;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

// √     对敌方目标造成6次伤害，每次对敌方目标造成攻击60%伤害
//      敌方目标的最大生命每比须佐之男高10%,本次伤害提升3%,最多不超过50%
// √     lv2-伤害提升最多不超过65%
// √     lv3-每次造成的伤害增加至70%
// √     lv4-伤害提升最多不超过80%
// √     lv5-每次造成的伤害增加至80%

class Skill3 extends Skill {
    public static final String SkillName = "天雷万象";

    private final int multiplier;
    private final int maxZengShang;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);

        int baseMultiplier = (level >= 5 ? 80 : level >= 3 ? 70 : 60);
        int baseMaxZengShang = (level >= 4 ? 80 : level >= 2 ? 65 : 50);

        Optional<Skill> oSkill2 = belongTo.getSkill(2);
        if (oSkill2.isPresent()) {
            if (oSkill2.get().getLevel() >= 5) {
                baseMultiplier += 20;
                baseMaxZengShang += 20;
            }
        }

        multiplier = baseMultiplier;
        maxZengShang = baseMaxZengShang;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        // 优先打红标,如果红标不存在,则优先打草人,无草人则打生命最高的单位
        // 这个逻辑似乎只有须佐有,就不写通用逻辑了
        Character target = null;

        CharacterFinder allEnemyFinder = new CharacterFinder(belongTo).filterEnemy();
        Character auto = allEnemyFinder.getAuto();
        if (auto != null) {
            // 优先红标
            target = auto;
        } else {
            List<Character> enemy = allEnemyFinder.getList();
            for (Character character : enemy) {
                if (character instanceof CaoRen) {
                    // 否则草人
                    target = character;
                    break;
                }
            }

            // 再没有就找生命最高的
            if (target == null) {
                target = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .get(Attribute.HP, CharacterFinder.Criteria.MAX);
            }
        }

        // 敌方目标的最大生命每比须佐之男高10%,本次伤害提升3%
        int times = (int) (((target.getMaxHp() / belongTo.getMaxHp()) - 1) * 10);
        int zengShang = Math.min(times * 3, maxZengShang);

        StatusSkill3ZengShang status = new StatusSkill3ZengShang(belongTo, zengShang);
        belongTo.addStatus(status);

        for (int i = 0; i < 6; i++) {
            interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);
        }

        belongTo.removeStatus(status);

        return Optional.of(target);
    }

    static class StatusSkill3ZengShang extends Status implements AttributeModifier {
        private final double zengShang;

        public StatusSkill3ZengShang(Character character, double zengShang) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.zengShang = zengShang;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return zengShang;
        }
    }
}
