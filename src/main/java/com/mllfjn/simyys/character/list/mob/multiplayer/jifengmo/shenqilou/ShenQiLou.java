package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.CiTiaoManager;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.status.instance.StatusBoss;
import com.mllfjn.simyys.collections.StringGroup;
import com.mllfjn.simyys.battleevent.EventActionDone;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import static com.mllfjn.simyys.character.PropertyKey.JI_FENG_MO_CI_TIAO_KEY;

public class ShenQiLou extends Character {
    public static final String CharacterName = "蜃气楼";
    private int stage = 1;
    private boolean prepareChangeStage = false;

    private transient ContextMenu contextMenu;

    public ShenQiLou() {}

    public void changeStage() {
        stage++;
        prepareChangeStage = false;
        switch (stage) {
            case 2 -> stage2();
            case 3 -> stage3();
        }
    }

    private void stage2() {
        System.out.println("蜃气楼进入二阶段");
    }

    private void stage3() {
        System.out.println("蜃气楼进入三阶段");
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        CiTiaoManager.installCiTiao(propertiesHolder.propertiesMap.get(JI_FENG_MO_CI_TIAO_KEY).getString(), this);

        addStatus(new StatusBoss(this));
    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("200");
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
    protected String getDefaultBaseAttack() {
        return "8000";
    }

    @Override
    protected EventHandler<MouseEvent> getEventHandler() {
        return event -> {
            if (!prepareChangeStage) {
                getContextMenu().show(getCharacterIcon(), event.getScreenX(), event.getScreenY());
            }
        };
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            contextMenu = new ContextMenu();
            MenuItem item1 = new MenuItem("跳过当前回合切换阶段");
            MenuItem item2 = new MenuItem("该回合行动后切换阶段");
            item1.setOnAction(e -> {
                this.bp.skipCharacterAct();
                this.changeStage();
            });

            item2.setOnAction(e -> {
                if (!prepareChangeStage) {
                    this.bp.addActionListener(this, te -> {
                        if (te instanceof EventActionDone) {
                            this.changeStage();
                            return true;
                        }
                        return false;
                    });
                    prepareChangeStage = true;
                }
            });

            contextMenu.getItems().addAll(item1, item2);
        }
        return contextMenu;
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
}
