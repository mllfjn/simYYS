package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Trigger;

import java.util.List;

class StatusChi extends Status {
    static final String StatusName = "尘缘·赤";

    public StatusChi(DaYuan from, Character belongTo, int level) {
        super(StatusName, from, belongTo);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
        // 回合后立即获得1个回合
        runOn(Trigger.AFTER_ROUND, _ -> {
            // 无法连续触发此效果
            if (!belongTo.isHaveStatus(StatusChiNewRound.class)) {
                // 回合结束后立即获得1个回合,且新回合释放妖术技能消耗的鬼火减少2点(已包含在StatusChiNewRound中)
                belongTo.doInteractive(interactive -> interactive.getNewRound(belongTo));
                belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                    @Override
                    public boolean onBattleAction(BattleEvent event) {
                        if (event instanceof EventActionDone) {
                            belongTo.addStatus(new StatusChiNewRound(from, belongTo));
                            return true;
                        }
                        return false;
                    }
                });

            }
        });
        // lv2 拥有尘缘的目标回合开始时立即获得1点鬼火
        if (level >= 2) {
            runOn(Trigger.BEFORE_ROUND, _ -> belongTo.bp.gainGuiHuo(belongTo, 1));
        }
        display(() -> {
            // 同时附加赤和青会变成虹
            if (belongTo.isHaveStatus(StatusQing.class)) {
                return "尘缘·虹";
            } else {
                return StatusName;
            }
        });
    }
}

// 该状态标记目标处于尘缘·赤获得的新回合中,新回合释放妖术技能消耗的鬼火减少2点
class StatusChiNewRound extends Status {
    public StatusChiNewRound(DaYuan from, Character belongTo) {
        super("赤标记", from, belongTo);
        duration(StatusDurationType.CHI_XU, 1);
        forceChangeSkillCost(-2);
    }
}

class StatusQing extends Status {
    static final String StatusName = "尘缘·青";

    public StatusQing(DaYuan from, Character belongTo, int level) {
        super(StatusName, from, belongTo);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);

        // 回合开始时驱散自身以外的全体友方1个减益状态与控制效果
        runOn(Trigger.BEFORE_ROUND, _ -> {
            List<Character> teammate = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterSelf()
                    .getList();
            for (Character character : teammate) {
                character.dispelDeBuff(1);
            }
            // 并使自身减伤提升50%
            Status.of(StatusQing.StatusName + "减伤", from, belongTo)
                    .attribute(Attribute.JIAN_SHANG, 50.0)
                    .duration(StatusDurationType.WEI_CHI, 1)
                    .addTo();
            // lv2-回合开始时立即获得1点鬼火
            // 和赤一起加的时候由赤来回火
            if (level >= 2 && !belongTo.isHaveStatus(StatusChi.class)) {
                belongTo.bp.gainGuiHuo(belongTo, 1);
            }
        });
        display(() -> {
            // 同时附加赤和青会变成虹，由赤来显示
            if (belongTo.isHaveStatus(StatusChi.class)) {
                return null;
            } else {
                return StatusName;
            }
        });
    }
}