package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "洪福降临";

    private final Status status;

    public Skill2(LaoTou laoTou, int level) {
        super(laoTou, level, 2);
        this.status = Status.of(SkillName, laoTou);
        status.runOn(Trigger.AFTER_ROUND, _ -> {
            // 自身回合结束时，击退全体敌方目标10%行动条
            // lv2-击退行动条效果提升至15%
            laoTou.doInteractive(interactive -> {
                List<Character> enemy = new CharacterFinder(laoTou)
                        .filterEnemy()
                        .getList();
                for (Character character : enemy) {
                    interactive.decreaseLocation(character, level >= 2 ? 15 : 10);
                }

                // 若回合中释放过委以重任,则开始打盹
                if (laoTou.isHaveStatus(StatusUse3Flag.class)) {
                    laoTou.addStatus(new StatusDaDun(laoTou, level >= 4));
                    // lv4-打盹额外获得1点鬼火
                    if (level >= 4) {
                        laoTou.bp.gainGuiHuo(laoTou, 1);
                    }
                }

                // lv3-回合结束时额外获得2点鬼火
                if (level >= 3) {
                    laoTou.bp.gainGuiHuo(laoTou, 2);
                }
            });
        });
        // lv5-战斗开始后,自身首次受到伤害时开始打盹
        if (level >= 5) {
            Status.of("首次伤害打盹", laoTou)
                    .runOn(Trigger.AFTER_ATTACK, _ ->
                            laoTou.bp.addActionListener(new BattleActionListener(laoTou) {
                                @Override
                                public boolean onBattleAction(BattleEvent event) {
                                    if (event instanceof EventActionDone) {
                                        laoTou.addStatus(new StatusDaDun(laoTou, true));
                                        return true;
                                    }
                                    return false;
                                }
                            })).addTo();
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }
}


