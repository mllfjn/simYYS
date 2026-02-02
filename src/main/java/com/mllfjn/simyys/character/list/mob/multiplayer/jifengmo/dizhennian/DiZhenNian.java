package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.list.mob.multiplayer.InfoDisplay;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.CiTiao6DouHun;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.CiTiaoManager;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusBoss;
import com.mllfjn.simyys.character.status.instance.StatusCanNotChoose;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.collections.StringGroup;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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

    private final MultiStageManager multiStageManager = new MultiStageManager(this);
    public final DisplayDamageRecord display = new DisplayDamageRecord(this);

    private StatusBuff.BuffType buffType;
    private Character hongNing;

    private boolean beforeHouZi = true;
    private boolean canNingShi = false;


    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("175");
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("999999999");
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

        addStatus(new StatusBoss(this));

        String ciTiao = propertiesHolder.propertiesMap.get(JI_FENG_MO_CI_TIAO_KEY).getString();
        CiTiaoManager.installCiTiao(ciTiao, this);

        // 添加转阶段事件
        addStage();

        // 皮糙肉厚
        // 斗魂15万,其他25万
        addStatus(new StatusPiCaoRouHou(this, (ciTiao != null && ciTiao.equals(CiTiao6DouHun.CiTiaoName)) ? 150000 : 250000));

        // 给对面放一个妖琴
        bp.addCharacter(new SpecialYaoQin(bp, 1 - team));

        // 添加伤害显示器
        addStatus(display);

        // 先机召唤4只海坊主
        bp.atBattleStart(() -> {
            for (StatusBuff.BuffType type : StatusBuff.BuffType.values()) {
                bp.addCharacter(new SpecialHaiFangZhu(bp, team, this, type));
            }
        });
    }

    private void addStage() {
        // 第一次转阶段
        multiStageManager.addStage(() -> {
            // 召唤一个猴子
            this.bp.addCharacter(new HouZi(this.bp, this));
            // 自己进入无法选中状态
            addStatus(new StatusCanNotChoose(this, this));
            // 如果在凝视立即吐出光球
            getStatus(SkillNingShi.StatusNingShiRecordDamage.class)
                    .ifPresent(SkillNingShi.StatusNingShiRecordDamage::delete);
            canNingShi = false;
        });

        // 第二次转阶段
        multiStageManager.addStage(() -> {
            // 删除猴子
            for (Character character : this.bp.situation.characters) {
                if (character instanceof HouZi) {
                    character.die();
                }
            }
            // 自己回到可选中状态
            removeStatus(StatusCanNotChoose.class);
            // 凝视变为光球后下一回合立即释放
            beforeHouZi = false;
            canNingShi = true;
        });
    }

    @Override
    protected EventHandler<MouseEvent> getEventHandler() {
        return multiStageManager.getEventHandler();
    }

    @Override
    protected InfoDisplay getInfoDisplay() {
        return display;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "8000";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        addSkill(new SkillNingShi(this));
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(SkillNingShi.skillID) || tryUseSkill(2) || tryUseSkill(1);
    }

    private void setBuffType(StatusBuff.BuffType buffType) {
        if (this.buffType == null) {
            this.buffType = buffType;
            List<Character> list = new CharacterFinder(this)
                    .filterTeammate()
                    .filter(character -> character instanceof SpecialHaiFangZhu)
                    .getList();

            for (Character character : list) {
                bp.removeCharacter(character);
            }

            bp.addActionListener(this, event -> {
                if (event instanceof EventActionDone) {
                    canNingShi = true;
                    getSkill(SkillNingShi.skillID).ifPresent(skill -> skill.use(bp));
                    return true;
                }
                return false;
            });
        }
    }

    StatusBuff.BuffType getBuffType() {
        return buffType;
    }

    boolean isBeforeHouZi() {
        return beforeHouZi;
    }

    Character getHongNing() {
        return hongNing;
    }

    void setHongNing(Character hongNing) {
        this.hongNing = hongNing;
    }

    boolean canNingShi() {
        return canNingShi;
    }

    static class StatusBuffSetter extends Status implements StatusRunnable {
        private final StatusBuff.BuffType buffType;

        public StatusBuffSetter(Character from, Character belongTo, StatusBuff.BuffType buffType) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.buffType = buffType;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.DIE;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            ((DiZhenNian) from).setBuffType(buffType);
            return false;
        }
    }
}
