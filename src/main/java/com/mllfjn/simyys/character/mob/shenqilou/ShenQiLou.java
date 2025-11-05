package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterIcon;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.collections.StringGroup;
import com.mllfjn.simyys.trigger.EventRoundDone;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ShenQiLou extends Character {
    public static final String CharacterName = "蜃气楼";
    private int stage = 1;
    public ShenQiLou() {}

    public void changeStage() {
        stage++;
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

        map.put(PropertyKey.JI_FENG_MO_CI_TIAO_KEY, new PropertySelectSingle(StringGroup.JI_FENG_MO_CI_TIAO));
        return map;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "8000";
    }

    @Override
    protected CharacterIcon createCharacterIcon(CharacterIcon.onClickHandler onClickHandler) {
        return new CharacterIcon(this, onClickHandler, event -> {
            ContextMenu menu = new ContextMenu();
            MenuItem item1 = new MenuItem("跳过当前回合切换阶段");
            MenuItem item2 = new MenuItem("该回合行动后切换阶段");
            item1.setOnAction(e -> {
                this.bp.next(true);
                this.changeStage();
            });

            item2.setOnAction(e -> {
                this.bp.addActionTrigger(this, te -> {
                    if (te instanceof EventRoundDone rd) {
                        this.changeStage();
                        return true;
                    }
                    return false;
                });
            });

            menu.getItems().addAll(item1, item2);
            menu.show(characterIcon, event.getScreenX(), event.getScreenY());
        });
    }

    @Override
    public void addOwnSkills() {
        skills.add(new Skill1(this));
        skills.add(new Skill4TODO(this));
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(4);
    }
}
