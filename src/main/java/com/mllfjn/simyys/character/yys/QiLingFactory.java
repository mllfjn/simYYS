package com.mllfjn.simyys.character.yys;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.battleevent.EventBattleStart;
import com.mllfjn.simyys.trigger.battleevent.EventHpChange;
import com.mllfjn.simyys.trigger.Trigger;

public class QiLingFactory {
    public static final String ZhenMuShou = "镇墓兽";
    public static final String HuoLing = "火灵";
    public static void addQiLing(PropertiesMap map, Character character) {
        String s = map.get(PropertyKey.QI_LING_KEY).getString();
        if (s == null || s.isEmpty()) {
            return;
        }
        if (s.equals(ZhenMuShou)) {
            character.bp.addActionTrigger(character, event -> {
                if (event instanceof EventHpChange hc
                        && hc.getCharacter() == character
                        && character.getHp() < character.getMaxHp() * 0.7) {
                    for (Character target : CharacterFinder.findTeammate(character, character.bp.situation.characters)) {
                        target.addState(new StateQiCritPower(character, target));
                    }
                    return true;
                }
                return false;
            });
        } else if (s.equals(HuoLing)) {
            character.bp.addActionTrigger(character, event -> {
                if (event instanceof EventBattleStart) {
                    character.bp.gainGuiHuo(character, 2);
                    return true;
                }
                return false;
            });
        }
    }
}

class StateQiCritPower extends State implements AttributeModifier, Displayable {

    public StateQiCritPower(Character from, Character belongTo) {
        super(from, belongTo, StateType.BUFF, StateForm.YIN_JI);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return false;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 0;
    }

    @Override
    public String getText() {
        return "契镇";
    }
}

class StateQiHuoLing extends State implements Runnable {
    public StateQiHuoLing(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND_FIRST;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        Interactive interactive = belongTo.getInteractive();
        for (int i = 0; i < 3; i++) {
            interactive.attack("火灵之力"
                    , CharacterFinder.findEnemy(belongTo, bp.situation.characters)
                    , 100, AttackType.QUN_TI);
        }
        return false;
    }
}