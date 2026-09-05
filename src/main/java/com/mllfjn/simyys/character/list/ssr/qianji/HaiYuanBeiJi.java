package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.battleevent.EventUseGuiHuo;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

class HaiYuanBeiJi extends CharacterSummonBase {
    private static final String CharacterName = "海原贝戟";
    private static final Skill skill = Skill.getInstance(CharacterName);

    private final QianJi qianJi;
    private final StatusChaoSheng chaoSheng;
    private final StatusAdder<?> adder;

    public HaiYuanBeiJi(QianJi qianJi, BattlePane bp, int level) {
        super(bp, CharacterName, qianJi.team);
        this.qianJi = qianJi;
        this.isSummon = true;

        bp.addCharacter(this);

        // 标记千姬已经放下锤子,并且移除免控
        qianJi.setHaiYuanBeiJi(this);
        qianJi.removeStatus(QianJi.StatusQianJiIgnoreDebuff.class);

        // 该状态同时完成免疫debuff
        chaoSheng = new StatusChaoSheng(this, level);
        addStatus(chaoSheng);
        // 继承千姬100%的防御
        setInitDefense(qianJi.getInitDefense());
        // 生命值是千姬攻击力的550%
        forceSetMaxHp(qianJi.getAttack() * 5.5, true);
        // 放下锤子后开始记录悲歌层数
        qianJi.addStatus(new StatusBeiGe(qianJi));

        // 在场时为友方单位恢复受到伤害30%的生命值,友方单位每使用1点鬼火
        // TODO 有50%的概率
        // 使海原贝戟叠加1层潮声
        // lv3-概率增至100%
        adder = bp.addStatusAdder(c ->
                c.team == this.team
                        ? new StatusRecovery(this, c)
                        : null
        );
        bp.addActionListener(new BattleActionListener(this) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventUseGuiHuo eg && eg.getTeam() == team) {
                    chaoSheng.addStack(eg.getNum());
                }
                return false;
            }
        });

        // lv2-海原贝戟在场时,千姬和海原贝戟受到的伤害降低30%
        if (level >= 2) {
            this.addStatus(new StatusJianShang(this));
            qianJi.addStatus(new StatusJianShang(qianJi));
        }
    }

    @Override
    public void dieHandle() {
        qianJi.setHaiYuanBeiJi(null);
        qianJi.addStatus(new QianJi.StatusQianJiIgnoreDebuff(qianJi));
        qianJi.removeStatus(StatusJianShang.class);
        Skill3_2.removeBeiGeAndChangeSkill(qianJi);
        adder.deleteAndRemove();
    }

    public void addChaoSheng(int count) {
        chaoSheng.addStack(count);
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }

    static class StatusJianShang extends Status {

        public StatusJianShang(Character character) {
            super("千姬减伤", character);
            runOn(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.7, getName())
            );
        }
    }

    static class StatusRecovery extends Status {

        public StatusRecovery(Character from, Character belongTo) {
            super(HaiYuanBeiJi.CharacterName + "受到伤害后恢复", from, belongTo);
            runOn(Trigger.AFTER_ATTACK, param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                double number = attackInfo.getTraceableNumber().getNumber();
                if (number > 0) {
                    from.doInteractive(interactive ->
                            interactive.recovery(skill, belongTo, number * 0.3));
                }
            });
        }
    }
}

class StatusChaoSheng extends Status implements IgnoreDebuff {
    private final static String StatusName = "潮声";
    private int stack;

    public StatusChaoSheng(Character character, int level) {
        super(StatusName, character, character, StatusType.BUFF, StatusForm.YIN_JI);

        // lv5-海原贝戟被召唤时,立刻获得3层潮声
        if (level >= 5) {
            stack = 3;
        }
        display(() -> StatusName + stack);
    }

    public void addStack(int count) {
        stack += count;
        while (stack >= 7) {
            stack -= 7;
            belongTo.bp.gainGuiHuo(belongTo, 3);
            List<Character> teammate = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            for (Character character : teammate) {
                StatusQianJiZengShang.addStack(character);
            }
        }
    }
}

class StatusQianJiZengShang extends Status {
    private int stack = 1;

    public StatusQianJiZengShang(Character character) {
        super("金剑", character, character, StatusType.BUFF, StatusForm.YIN_JI);
        attribute(Attribute.ZENG_SHANG, _ -> 15.0 * stack);
        display(() -> "金剑" + stack);
    }

    public static void addStack(Character character) {
        character.getStatus(StatusQianJiZengShang.class)
                .ifPresentOrElse(
                        StatusQianJiZengShang::addStack,
                        () -> character.addStatus(new StatusQianJiZengShang(character))
                );
    }

    public void addStack() {
        if (stack < 5) {
            stack++;
        }
    }
}