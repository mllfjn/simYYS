package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

// √     创造除一个继承自身90%的属性且不可被攻击的影分身
// √     源博雅每攻击3次,影分身将对同一目标释放源博雅上次攻击相同的技能 (skill1、2、5可以触发)
// √     lv2-释放后提升自身30%暴击
// √     lv3-继承的属性提升至100%
// √     lv4-释放后提升自身30点速度
// √     lv5-释放后提升自身70%行动条
// √     术印:影分身造成的伤害额外提升阴阳师攻击的20%
class Skill4 extends Skill {
    static final String SkillName = "秘术·影分身";

    private final int extraMultiplier;

    private boolean exist;

    public Skill4(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 4);

        extraMultiplier = 20 * shuYin;
    }


    @Override
    public boolean canUse(BattlePane bp) {
        return !exist && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        BoYa belongTo = ((BoYa) getBelongTo());
        int level = getLevel();

        if (level >= 2) {
            Status status = Status.of(SkillName + "提升属性", belongTo)
                    .type(StatusType.BUFF, StatusForm.ZHUANG_TAI)
                    .attribute(Attribute.CRIT_RATE, 30)
                    .addTo();
            if (level >= 4) {
                status.attribute(Attribute.SPEED, 30);
                if (level >= 5) {
                    belongTo.doInteractive(interactive ->
                            interactive.increaseLocation(belongTo, 70));
                }
            }
        }

        exist = true;
        belongTo.setYinFenShen(new YinFenShen(belongTo, level >= 3 ? 1 : 0.9, extraMultiplier));

        return Optional.empty();
    }

    static class YinFenShen extends CharacterSummonBase {
        private static final String CharacterName = "影分身";

        private final int extraMultiplier;

        private int times = 0;

        public YinFenShen(Character owner, double coefficient, int extraMultiplier) {
            super(owner.bp, CharacterName, owner.team);
            this.extraMultiplier = extraMultiplier;

            this.setInitBaseAttack(owner.getInitBaseAttack() * coefficient);
            this.setInitAdditionAttack(owner.getInitAdditionAttack() * coefficient);
            this.setInitCritRate(owner.getInitCritRate() * coefficient);
            this.setInitCritPower(owner.getInitCritPower() * coefficient);
        }

        public void usedSkill(YingFenShenCopy skill, Character target) {
            if (times == 2) {
                doInteractive(interactive ->
                        skill.copy(this, target, interactive, extraMultiplier)
                );
                times = 0;
            }
            times++;
        }
    }
}
