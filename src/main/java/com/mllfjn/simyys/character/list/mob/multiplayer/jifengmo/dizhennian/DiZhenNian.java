package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.CiTiao6DouHun;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;

import java.util.List;

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

public class DiZhenNian extends CharacterJiFengMoBase {
    public static final String CharacterName = "地震鲶";

    private StatusBuff.BuffType buffType;
    private Character hongNing;

    private boolean beforeHouZi = true;
    private boolean canNingShi = false;


    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        // 给对面放一个妖琴
        // 特殊:为了实现妖琴锁技能和红绿标,如果敌方有一个100速的妖琴,则将其替换为特殊妖琴,并继承其锁定
        SpecialYaoQin.add(bp, team);

        addSkill(new Skill1(this));
        Skill2 skill2 = new Skill2(this);
        addSkill(skill2);
        // 皮糙肉厚
        // 斗魂15万,其他25万
        String ciTiao = propertiesHolder.propertiesMap.get(PropertyKey.JI_FENG_MO_CI_TIAO_KEY).getString();
        addSkill(new Skill3(this,
                (ciTiao != null && ciTiao.equals(CiTiao6DouHun.CiTiaoName)) ? 150000 : 250000));
        addSkill(new Skill4(this, skill2));
        addSkill(new Skill5(this));
        addSkill(new Skill6(this));

        // 先机召唤4只海坊主
        bp.addPriorityMove(this, () -> {
            for (StatusBuff.BuffType type : StatusBuff.BuffType.values()) {
                this.bp().addCharacter(new SpecialHaiFangZhu(this.bp(), team, this, type));
            }
        });
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "175";
    }

    @Override
    protected void addStage(MultiStageManager multiStageManager) {
        // 第一次转阶段
        multiStageManager.addStage(() -> {
            // 召唤一个猴子
            this.bp.addCharacter(new HouZi(this));
            // 自己进入无法选中状态
            addStatus(new StatusUnselectable(this, this));
            // 如果在凝视立即吐出光球
            getStatus(Skill4.StatusNingShiRecordDamage.class)
                    .ifPresent(Skill4.StatusNingShiRecordDamage::delete);
            canNingShi = false;
        });

        // 猴子死亡会自行进入下一阶段
        // TODO第二次转阶段是鸟居
    }

    void houZiDie() {
        // 自己回到可选中状态
        removeStatus(StatusUnselectable.class);
        // 凝视变为光球后下一回合立即释放
        beforeHouZi = false;
        canNingShi = true;

        // 清除敌方身上的地震鲶增益
        List<Character> list = new CharacterFinder(this, true)
                .filterEnemy()
                .getList();
        for (Character character : list) {
            character.removeStatus(StatusBuff.class);
        }
    }

    @Override
    protected void addOwnSkills() {

    }

    @Override
    protected boolean useSkillAuto() {
        // 凝视,波浪翻涌,尾鳍攻击
        return tryUseSkill(4) || tryUseSkill(5) || tryUseSkill(1);
    }

    void setBuffType(StatusBuff.BuffType buffType) {
        if (this.buffType == null) {
            this.buffType = buffType;
            List<Character> list = new CharacterFinder(this)
                    .filterTeammate()
                    .filter(character -> character instanceof SpecialHaiFangZhu)
                    .getList();

            for (Character character : list) {
                character.die();
            }

            bp.addActionListener(new BattleActionListener(this) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        canNingShi = true;
                        getSkill(4).ifPresent(skill -> skill.use(bp));
                        return true;
                    }
                    return false;
                }
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
}
