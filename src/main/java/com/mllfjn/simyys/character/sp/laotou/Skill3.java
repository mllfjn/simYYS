package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateCalc;
import com.mllfjn.simyys.ratecontroller.RateController;
import com.mllfjn.simyys.character.status.StatusDurationType;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    public static final String SkillName = "委以重任";
    private final int[] multiplier = new int[]{0, 14, 16, 18, 20, 20};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        LaoTou belongTo = (LaoTou) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        int level = getLevel();

        // 二技能需要确认是否释放过三
        belongTo.addStatus(new StatusUse3Flag(belongTo));
        // 恢复非召唤物友方目标生命上限(系数)的生命
        List<Character> teammate = CharacterFinder.findTeammateExceptSummon(belongTo, bp.situation.characters);
        teammate.remove(belongTo);
        Character target = CharacterFinder.findPriorAuto(teammate, bp
                , belongTo.team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        lastUsedTarget = target;
        interactive.recovery(target, belongTo.getMaxHp() * multiplier[level] / 100);

        target.getStatus(StatusYuHunTransfer.class).ifPresentOrElse(statusYHT -> {
            // 若目标未处于控制效果,则使其获得新的回合并在该回合结束后移除御魂转移效果
            if (target.controllable()) {
                interactive.getNewRound(target);
                statusYHT.setDurationType(StatusDurationType.CHI_XU, 1);
            } else {
                // 对处于御魂转移效果的目标再次释放时,驱散或解除其所有控制效果
                target.removeAllCrowControl();
            }
        }, () -> {
            // 有50%概率将自身的御魂4件套效果转移给目标,维持一回合(至多转移1种效果,TODO且总御魂造成伤害提升至多为140%)
            getFirstFullYuHun().ifPresent(yuHun -> {
                if (level == 5) {
                    doTransfer(target, yuHun);
                } else {
                    RateController.whetherOrNot(SkillName, "向" + target + "转移御魂", List.of("转移")
                            , item -> item, bp.calc, RateCalc::isControlWhetherOther, item -> 50.0
                            , (s, aBoolean) -> {if (aBoolean) doTransfer(target, yuHun);});
                }
            });
        });
    }

    private void doTransfer(Character target, Class<? extends YuHun> yuHun) {
        target.addStatus(new StatusYuHunTransfer(getBelongTo(), target, yuHun));
    }

    private Optional<Class<? extends YuHun>> getFirstFullYuHun() {
        for (YuHun yuHun : getBelongTo().getYuHunSet()) {
            if (yuHun instanceof YuHunUnfullMark) {
                continue;
            }
            return Optional.of(yuHun.getClass());
        }
        return Optional.empty();
    }
}