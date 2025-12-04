package com.mllfjn.simyys.character.yys;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.battleevent.EventHpChange;
import com.mllfjn.simyys.character.status.Trigger;

public class QiLingFactory {
    public static final String ZhenMuShou = "镇墓兽";
    public static final String HuoLing = "火灵";
    public static void addQiLing(PropertiesMap map, Character character) {
        String s = map.get(PropertyKey.QI_LING_KEY).getString();
        if (s == null || s.isEmpty()) {
            return;
        }
        if (s.equals(ZhenMuShou)) {
            character.bp.addActionListener(character, event -> {
                if (event instanceof EventHpChange hc
                        && hc.getCharacter() == character
                        && character.getHp() < character.getMaxHp() * 0.7) {
                    for (Character target : CharacterFinder.findTeammate(character, character.bp.situation.characters)) {
                        target.addStatus(new StatusQiCritPower(character, target));
                    }
                    return true;
                }
                return false;
            });
        } else if (s.equals(HuoLing)) {
            character.bp.addActionListener(character, event -> {
                if (event instanceof EventBattleStart) {
                    character.bp.gainGuiHuo(character, 2);
                    return true;
                }
                return false;
            });
        }
    }
}

class StatusQiCritPower extends Status implements AttributeModifier, Displayable {

    public StatusQiCritPower(Character from, Character belongTo) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 30;
    }

    @Override
    public String getText() {
        return "契镇";
    }
}

class StatusQiHuoLing extends Status implements Runnable {
    public StatusQiHuoLing(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND_FIRST;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        Interactive interactive = belongTo.getInteractive();
        for (int i = 0; i < 3; i++) {
            interactive.attack("火灵之力"
                    , CharacterFinder.findEnemy(belongTo, bp.situation.characters)
                    , 100, AttackType.QUN_TI);
        }
        return false;
    }
}