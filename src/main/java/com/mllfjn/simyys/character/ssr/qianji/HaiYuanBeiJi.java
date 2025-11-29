package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.status.*;
import com.mllfjn.simyys.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.trigger.battleevent.EventUseGuiHuo;

import java.util.List;

public class HaiYuanBeiJi extends Character {
    public static final String CharacterName = "海原贝戟";
    private final QianJi qianJi;
    private final StatusChaoSheng chaoSheng;

    public HaiYuanBeiJi(QianJi qianJi, BattlePane bp, int level) {
        this.isSummon = true;
        this.name = CharacterName;
        this.qianJi = qianJi;

        // 标记千姬已经放下锤子,并且移除免控
        qianJi.setHaiYuanBeiJi(this);
        qianJi.removeStatus(QianJi.StatusQianJiIgnoreDebuff.class);
        // 在bp中添加角色和鬼火监听器
        setBattlePane(bp);
        bp.addCharacter(this);

        // 该状态同时完成免疫debuff和无法行动
        chaoSheng = new StatusChaoSheng(this, level);
        addStatus(chaoSheng);
        // 继承千姬100%的防御
        setInitDefense(qianJi.getInitDefense());
        // 生命值是千姬攻击力的550%
        setMaxHp(qianJi.getAttack() * 5.5, true);
        // 放下锤子后开始记录悲歌层数
        qianJi.addStatus(new StatusBeiGe(qianJi));

        bp.addActionListener(this, event -> {
            if (event instanceof EventUseGuiHuo eg && eg.getTeam() == team) {
                chaoSheng.addStack(eg.getNum(), this.bp);
            }
            return false;
        });
    }

    @Override
    protected String getDefaultBaseAttack() {
        return null;
    }

    @Override
    public void addOwnSkills() {

    }

    @Override
    public void dieHandle() {
        qianJi.setHaiYuanBeiJi(null);
        qianJi.addStatus(new QianJi.StatusQianJiIgnoreDebuff(qianJi));
        Skill3_2.removeBeiGeAndChangeSkill(qianJi);
    }

    public void addChaoSheng(int count) {
        chaoSheng.addStack(count, bp);
    }

    @Override
    public boolean controllable() {
        return false;
    }
}

class StatusChaoSheng extends Status implements Displayable, IgnoreDebuff {
    private final static String StatusName = "潮声";
    private int stack;

    public StatusChaoSheng(Character character, int level) {
        super(character, character, StatusType.BUFF, StatusForm.YIN_JI);

        // lv5-海原贝戟被召唤时,立刻获得3层潮声
        if (level == 5) {
            stack = 3;
        }
    }

    public void addStack(int count, BattlePane bp) {
        stack += count;
        while (stack >= 7) {
            stack -= 7;
            List<Character> teammate = CharacterFinder.findTeammate(belongTo, bp.situation.characters);
            for (Character character : teammate) {
                StatusQianJiZengShang.addStack(character);
            }
        }
    }


    @Override
    public String getText() {
        return StatusName + stack;
    }
}

class StatusQianJiZengShang extends Status implements AttributeModifier, Displayable {
    // 这个状态游戏里没显示来源,不知道会不会有影响
    private int stack = 0;

    public StatusQianJiZengShang(Character character) {
        super(null, character, StatusType.BUFF, StatusForm.YIN_JI);
    }

    public static void addStack(Character character) {
        character.getStatus(StatusQianJiZengShang.class)
                .or(() -> character.addStatus(new StatusQianJiZengShang(character)))
                .ifPresent(StatusQianJiZengShang::addStack);
    }

    public void addStack() {
        if (stack < 5) {
            stack++;
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ZENG_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return stack * 15;
    }

    @Override
    public String getText() {
        return "金剑" + stack;
    }
}