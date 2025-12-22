package com.mllfjn.simyys.character.list.mob.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.mob.jifengmo.citiao.CiTiaoManager;
import com.mllfjn.simyys.character.list.sr.haifangzhu.HaiFangZhu;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.ActionWhenDie;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.instance.StatusBoss;
import com.mllfjn.simyys.collections.StringGroup;

import java.util.List;

import static com.mllfjn.simyys.character.PropertyKey.JI_FENG_MO_CI_TIAO_KEY;

/**
 * 地震鲶相关机制
 * 皮糙肉厚归零前，造成伤害和受到伤害都减少70%
 * 光球造成剩余buff回合数*30%最大生命的穿盾伤害，该伤害吃boss的皮糙肉厚减伤以及不见岳的白字增伤
 * 不见岳只给友方增伤白字，也就是说光球的伤害来源是友方，进一步来说应该是该单位自己
 * 也有可能和破盾500万伤害一样来自阴阳师
 * 这里阴阳师也有可能是站位的"一号位"，下次尝试一下
 * 既然造成伤害的和承受伤害的单位都是自己，那么为什么会吃到BOSS的皮糙肉厚减伤呢
 * 猜测：皮糙肉厚不是地震鲶自身的状态，而是开局时给场上所有单位附加，该状态受到伤害时减少70%，这样可以解释为什么打在草人身上不会减伤
 * 为了验证这个猜测，可以查看海原贝戟是否吃到减伤，也可以看混乱时打在己方的伤害
 * 没办法验证 没找到带千不带缘的阵容 也不可能找到带SP星熊的阵容
 *
 */

public class DiZhenNian extends Character {
    public static final String CharacterName = "地震鲶";

    private StatusBuff.BuffType buffType;

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("175");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("99999999");
        ((PropertyInput) map.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("704");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_RATE_KEY)).setValue("10");
        ((PropertyInput) map.get(PropertyKey.GENERAL_CRIT_POWER_KEY)).setValue("150");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)).setValue("0");
        ((PropertyCheck) map.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyCheck) map.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);

        map.put(JI_FENG_MO_CI_TIAO_KEY, new PropertySelectSingle(StringGroup.JI_FENG_MO_CI_TIAO));
        return map;
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        CiTiaoManager.installCiTiao(propertiesHolder.propertiesMap.get(JI_FENG_MO_CI_TIAO_KEY).getString(), this);

        addStatus(new StatusBoss(this));

        // 给对面放一个妖琴
        bp.addCharacter(new SpecialYaoQin(bp, 1 - team));

        // 先机召唤4只海坊主
        bp.addActionListener(this, event -> {
            if (event instanceof EventBattleStart) {
                for (StatusBuff.BuffType type : StatusBuff.BuffType.values()) {
                    SpecialHaiFangZhu specialHaiFangZhu = new SpecialHaiFangZhu(bp, team);
                    specialHaiFangZhu.addStatus(new StatusBuff(this, specialHaiFangZhu, type, 0));
                    specialHaiFangZhu.addStatus(new StatusBuffSetter(this, specialHaiFangZhu, type));
                    bp.addCharacter(specialHaiFangZhu);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "8000";
    }

    @Override
    protected void addOwnSkills() {

    }

    private void setBuffType(StatusBuff.BuffType buffType) {
        if (this.buffType == null) {
            this.buffType = buffType;
            List<Character> targets = new CharacterFinder(this)
                    .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                    .filter(character -> character.name.equals(HaiFangZhu.CharacterName))
                    .getList();
            for (Character target : targets) {
                bp.removeCharacter(target);
            }
        }
    }

    static class StatusBuffSetter extends Status implements ActionWhenDie {
        private final StatusBuff.BuffType buffType;

        public StatusBuffSetter(Character from, Character belongTo, StatusBuff.BuffType buffType) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.buffType = buffType;
        }

        @Override
        public void action(BattlePane bp) {
            ((DiZhenNian) from).setBuffType(buffType);
        }
    }
}
