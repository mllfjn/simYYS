package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.list.mob.multiplayer.StatusRecordDamage;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;

class Skill5 extends PassiveSkill {
    private static final String SkillName = "阴界召唤";
    private static final String[] Names = new String[]{"斗", "牛", "虚", "危", "室", "壁"};

    public Skill5(Character belongTo) {
        super(belongTo, -1, 5);
    }

    void summonNormal() {
        Character belongTo = getBelongTo();
        StatusHKLZHBuff statusHKLZHBuff = new StatusHKLZHBuff(belongTo, 5);
        belongTo.addStatus(statusHKLZHBuff);

        final BattlePane bp = belongTo.bp;
        final int team = belongTo.team;
        // 部下15万生命(估测)
        for (int i = 0; i < 5; i++) {
            CharacterBX c = new CharacterBX(bp, "部下", team, 150000);
            c.setStatusHKLZHBuff(statusHKLZHBuff);
            bp.addCharacter(c);
            if (i == 2) {
                c.addStatus(new StatusModifyAttribute(c, c, StatusType.SPECIAL, StatusForm.SPECIAL) {
                    @Override
                    public boolean isAffectAttribute(Attribute attribute) {
                        return attribute == Attribute.JIAN_SHANG;
                    }

                    @Override
                    public double getInfluence(Attribute attribute, StatusModifyParam param) {
                        return 400;
                    }
                });
            }
        }
    }

    void summonSpecial() {
        HuangKuLou huangKuLou = ((HuangKuLou) getBelongTo());
        StatusHKLZHBuff statusHKLZHBuff = new StatusHKLZHBuff(huangKuLou, 2);
        huangKuLou.addStatus(statusHKLZHBuff);
        BattlePane bp = huangKuLou.bp;
        int team = huangKuLou.team;

        ArrayList<CharacterBX> characters = new ArrayList<>();
        for (String name : Names) {
            CharacterBX characterBX = new CharacterBX(bp, name, team, 99999999);
            characterBX.getCharacterIcon().setEventHandlerContainer(new ClearHpHandler(characterBX));
            characters.add(characterBX);
            bp.addCharacter(characterBX);
        }
        List<CharacterBX> charactersTrue = RateController
                .choose("真假话", characters, Character::getName, bp.calc, 2);

        for (CharacterBX character : charactersTrue) {
            character.setStatusHKLZHBuff(statusHKLZHBuff);
            character.addStatus(new StatusHKLRecordDamage(character, huangKuLou.getInfoDisplay()));
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusHKLRecordDamage extends StatusRecordDamage implements Displayable {
        private final DisplayDamageRecord infoDisplay;

        public StatusHKLRecordDamage(Character character, DisplayDamageRecord infoDisplay) {
            super(character);
            this.infoDisplay = infoDisplay;
        }

        @Override
        protected void addDamage(double damage) {
            infoDisplay.addDamage(damage);
        }

        @Override
        public String getDisplayText() {
            return "真";
        }
    }

    static class CharacterBX extends CharacterSummonBase {
        private BXDieHandler statusHKLZHBuff;

        public CharacterBX(BattlePane bp, String name, int team, double hp) {
            super(bp, name, team);
            // 352防御(nga),100速度(纯猜),2000攻击(纯猜)
            setMaxHp(hp, true);
            setInitDefense(352);
            setInitSpeed(100);
            setInitBaseAttack(2000);
            setMob(1, 1);
        }

        @Override
        protected void addOwnSkills() {
            addSkill(new Skill1PuGongBase(this, 1) {
                @Override
                public String getName() {
                    return "怨灵一击";
                }
            });
        }

        private void setStatusHKLZHBuff(BXDieHandler statusHKLZHBuff) {
            this.statusHKLZHBuff = statusHKLZHBuff;
        }

        @Override
        protected void dieHandle() {
            if (statusHKLZHBuff != null) {
                statusHKLZHBuff.bxDie();
            }
        }
    }

    private interface BXDieHandler {
        void bxDie();
    }

    static class StatusHKLZHBuff extends Status implements AttributeModifier, BXDieHandler {
        private int count;

        public StatusHKLZHBuff(Character character, int count) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.count = count;
        }

        @Override
        public void bxDie() {
            if (count == 1) {
                delete();
            } else {
                count--;
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG || attribute == Attribute.JIAN_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            if (attribute == Attribute.ZENG_SHANG) {
                return 40;
            } else {
                return 70;
            }
        }
    }
}
