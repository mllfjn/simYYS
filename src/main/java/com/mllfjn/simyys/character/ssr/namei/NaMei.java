package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.trigger.BattleActionListener;
import com.mllfjn.simyys.trigger.BattleActionTrigger;

import java.util.HashSet;
import java.util.Set;

public class NaMei extends Character {
    public static final String privateName = "伊邪那美";
    private int skill1Level;
    private int skill2Level;
    private boolean awakening;

    // 1血增益
    private final Set<Character> effectedCharacters = new HashSet<>();
    private int times = 3;
    public NaMei() {

    }

    @Override
    public PropertiesMap getProperties() {
        PropertiesMap map = super.getProperties();
        ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).setValue("3618");
        map.put(PropertyKey.SKILL_KEY, new PropertyInput().setValue("555"));
        map.put(PropertyKey.JUE_XING_KEY, new PropertyCheck().setValue(true));
        return map;
    }

    @Override
    public void init(PropertiesMap properties, BattlePane bp) {
        super.init(properties, bp);
        int skillLevel = properties.get(PropertyKey.SKILL_KEY).getInt();
        skill1Level = PropertyKey.getSkillLevel(skillLevel, 1);
        skill2Level = PropertyKey.getSkillLevel(skillLevel, 2);
        awakening = properties.get(PropertyKey.JUE_XING_KEY).getBoolean();

        // skill2 lv2-当场上非召唤物友方目标首次剩余1点生命时，速度提升50%，持续2回合(每个目标至多生效1次,单次战斗累计至多生效3次)
        if (skill2Level >= 2) {
            bp.addActionTrigger(this, new BattleActionListener() {
                @Override
                public void onBattleAction(BattleActionTrigger trigger, Character character, BattlePane bp) {
                    if (trigger == BattleActionTrigger.ON_HP_CHANGE) {
                        if (!effectedCharacters.contains(character) && character.getHp() == 1) {
                            effectedCharacters.add(character);
                            character.addState(new StateNaMeiSpeed(NaMei.this, character));
                            // lv3-当场上非召唤物友方目标首次剩余1点生命时,暴击伤害提升50% 限制与lv2相同
                            if (skill2Level >= 3) {
                                character.addState(new StateNaMeiCritPower(NaMei.this, character));
                            }
                        }
                    }
                }

                @Override
                public void response(BattlePane bp) {
                    times--;
                    if (times == 0) {
                        bp.removeActionTrigger(NaMei.this, this);
                    }
                }
            });
        }
    }

    @Override
    public void addSkills() {
        super.addSkills();
        skills.add(new Skill1(this, skill1Level));
        skills.add(new Skill2(this, awakening));
    }

    @Override
    public void useFrontSkill(BattlePane bp) {
        // lv5-先机:对攻击攻击最高的友方式神无消耗释放神赐之吻(2)
        if (skill2Level == 5) {
            ((Skill2) getSkill(2)).useFront(bp);
        }
    }
}

class StateNaMeiSpeed extends State implements AttributeModifier {
    public StateNaMeiSpeed(NaMei from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        setSettleType(StateSettleType.CHI_XU, 2);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return belongTo.getInitSpeed() * 0.4;
    }

    @Override
    public void setName() {
        name = "那美1血加速";
    }
}

class StateNaMeiCritPower extends State implements AttributeModifier {
    public StateNaMeiCritPower(NaMei from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        setSettleType(StateSettleType.CHI_XU, 2);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 50;
    }

    @Override
    public void setName() {
        name = "那美1血加爆伤";
    }
}