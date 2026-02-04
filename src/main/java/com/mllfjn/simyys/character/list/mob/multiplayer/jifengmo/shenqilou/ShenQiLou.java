package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;

public class ShenQiLou extends CharacterJiFengMoBase {
    public static final String CharacterName = "蜃气楼";

    public ShenQiLou() {}

    @Override
    protected void addStage(MultiStageManager multiStageManager) {
        // 第一次转阶段
        multiStageManager.addStage(() -> {
            // 使用"极·虾兵蟹将"召唤五只小怪
            // 游戏内信息显示是叫"蜃气楼大将"

            // 如果处于下潜状态,上浮
        });

        // 第二次转阶段,小怪死后或者主动跳过进入
        multiStageManager.addStage(() -> {
            // 召唤五个纸人
        });

        // 第三次转阶段,召唤镜像,一个一定假,其他2真2假
        multiStageManager.addStage(() -> {

        });

        // 第四次转阶段,该阶段每次行动前回满火,BOSS进行下潜状态,不再使用技能
        // 由于蜃气楼可以重复多次所有阶段,执行完后再重新添加阶段
        multiStageManager.addStage(() -> {

            addStage(multiStageManager);
        });
    }

    @Override
    public void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this));
        addSkill(new Skill4(this));
        addSkill(new SkillPassive1(this));
        addSkill(new SkillPassive2(this));
    }

    @Override
    protected boolean useSkillAuto() {
        // 只要带盾，直接放4.钳鳌重击
        // 如果没盾有3火,优先放3.绀连击,在冷却放2.蜃气爆弹
        return tryUseSkill(4) || tryUseSkill(3) || tryUseSkill(2);
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "200";
    }
}
