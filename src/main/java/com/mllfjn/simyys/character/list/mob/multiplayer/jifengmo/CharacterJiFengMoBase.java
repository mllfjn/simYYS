package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.CiTiaoManager;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.status.instance.StatusBoss;
import com.mllfjn.simyys.collections.StringGroup;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class CharacterJiFengMoBase extends Character {
    public boolean canChangeStage = true;

    private final MultiStageManager multiStageManager = new MultiStageManager(this, () -> this.canChangeStage);
    private final DisplayDamageRecord display = new DisplayDamageRecord(this);

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_SPEED_KEY)).setValue(getJiFengMoSpeed());
        ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).setValue("0");
        ((PropertyInput) map.get(PropertyKey.GENERAL_HP_KEY)).setValue("9999999999");
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
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);

        addStatus(new StatusBoss(this));

        CiTiaoManager.installCiTiao(
                propertiesHolder.propertiesMap.get(PropertyKey.JI_FENG_MO_CI_TIAO_KEY).getString(), this
        );

        // 添加转阶段事件
        addStage(multiStageManager);

        // 添加伤害显示器
        addStatus(display);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "8000";
    }

    @Override
    protected EventHandler<MouseEvent> getEventHandler() {
        return multiStageManager.getEventHandler();
    }

    @Override
    public DisplayDamageRecord getInfoDisplay() {
        return display;
    }

    protected abstract String getJiFengMoSpeed();

    protected abstract void addStage(MultiStageManager multiStageManager);
}
