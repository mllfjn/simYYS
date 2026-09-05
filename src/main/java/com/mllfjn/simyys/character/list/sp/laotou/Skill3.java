package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.character.status.StatusDurationType;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "委以重任";
    private final int[] multiplier = new int[]{0, 14, 16, 18, 20, 20};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        LaoTou belongTo = (LaoTou) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        int level = getLevel();

        // 二技能需要确认是否释放过三
        belongTo.addStatus(new StatusUse3Flag(belongTo));
        // 恢复非召唤物友方目标生命上限(系数)的生命
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSummon(false)
                .filterSelf()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        interactive.recovery(this, target, belongTo.getMaxHp() * multiplier[level] / 100);

        target.getStatus(StatusYuHunTransfer.class).ifPresentOrElse(statusYHT -> {
            // 若目标未处于控制效果,则使其获得新的回合并在该回合结束后移除御魂转移效果
            if (!target.isUnderCrowdControl()) {
                interactive.getNewRound(target);
                statusYHT.duration(StatusDurationType.CHI_XU, 1);
            } else {
                // 对处于御魂转移效果的目标再次释放时,驱散或解除其所有控制效果
                target.removeAllCrowControl();
            }
        }, () -> {
            // 有50%概率将自身的御魂4件套效果转移给目标,维持一回合(至多转移1种效果,TODO且总御魂造成伤害提升至多为140%)
            getFirstFullYuHun().ifPresent(yuHun -> {
                if (level >= 5) {
                    doTransfer(target, yuHun);
                } else {
                    if (RateController.otherWhether(SkillName + "-向[" + target.getName() + "]转移御魂"
                            , "转移", bp.calc, 50)) {
                        doTransfer(target, yuHun);
                    }

                }
            });
        });
        return Optional.of(target);
    }

    private void doTransfer(Character target, Equip yuHun) {
        target.addStatus(new StatusYuHunTransfer(getBelongTo(), target, yuHun));
    }

    private Optional<Equip> getFirstFullYuHun() {
        for (Equip equip : getBelongTo().getYuHunSet()) {
            if (equip instanceof YuHunUnfullMark) {
                continue;
            }
            return Optional.of(equip);
        }
        return Optional.empty();
    }
}