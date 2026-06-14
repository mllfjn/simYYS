package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.sr.huajing.HuaJing;
import com.mllfjn.simyys.character.list.sr.luoxinfu.LuoXinFu;
import com.mllfjn.simyys.character.list.sr.rihefang.RiHeFang;
import com.mllfjn.simyys.character.list.sr.xiazhongshaonv.XiaZhongShaoNv;
import com.mllfjn.simyys.character.list.ssr.axiuluo.AXiuLuo;
import com.mllfjn.simyys.character.list.ssr.bujianyue.StatusJieJieEffect;
import com.mllfjn.simyys.character.list.ssr.dashe.DaShe;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.list.ChuShiLuo;
import com.mllfjn.simyys.character.yuhun.list.GuiLingGeJi;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;

public class HunWang {
    public static void addCharacterProperty(SerializableObservableList<PropertiesHolder> list) {
        // 数据来自https://bbs.nga.cn/read.php?tid=35316684
        PropertiesMap pm;

        // 阿修罗
        pm = CharacterFactory.getProperties(AXiuLuo.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("300000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("425");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("141");
        PropertiesHolder ph = new PropertiesHolder(AXiuLuo.CharacterName, pm);
        ph.setAfterCreateAction(character -> {
            BattlePane bp = character.getBp();
            AttackInfo.LIMIT = 100000;
            bp.addStatusAdder(c ->
                    c.team != character.team
                            ? new StatusHpCheck(c)
                            : null
            );
            character.addStatus(new StatusSplashAttack(character));
        });
        list.add(ph);

        // 络新妇
        pm = CharacterFactory.getProperties(LuoXinFu.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("130000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("320.32");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("132");
        list.add(new PropertiesHolder(LuoXinFu.CharacterName, pm));

        // 匣子
        pm = CharacterFactory.getProperties(XiaZhongShaoNv.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("1");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("120000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("320.32");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("145");
        list.add(new PropertiesHolder(XiaZhongShaoNv.CharacterName, pm));

        // 八岐大蛇
        pm = CharacterFactory.getProperties(DaShe.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("666666");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("390");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("137");
        ph = new PropertiesHolder(DaShe.CharacterName, pm);
        ph.setAfterCreateAction(character -> character.addStatus(new StatusSplashAttack(character)));
        list.add(ph);

        // 化鲸
        pm = CharacterFactory.getProperties(HuaJing.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("44000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("900");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("148");
        ((PropertyCheck) pm.get(PropertyKey.JUE_XING_KEY)).setValue(false);
        ((PropertySelectMulti) pm.get(PropertyKey.YU_HUN_KEY)).setValue(ChuShiLuo.YuHunName);
        list.add(new PropertiesHolder(HuaJing.CharacterName, pm));

        // 日和坊
        pm = CharacterFactory.getProperties(RiHeFang.CharacterName);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("2");
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.SKILL_KEY)).setValue("111");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("198500");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("0");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("152");
        list.add(new PropertiesHolder(RiHeFang.CharacterName, pm));

        // 白蛇
        pm = Character.getDefaultProperties();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("800000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("580");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("135");
        ph = new PropertiesHolder("白蛇", pm);
        ph.setAfterCreateAction(character -> character.addStatus(new StatusSplashAttack(character)));
        list.add(ph);

        // 蛇魔
        pm = Character.getDefaultProperties();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("172000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("570");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("127");
        ph = new PropertiesHolder("蛇魔", pm);
        ph.setAfterCreateAction(character -> character.addStatus(new StatusAddDefense(character)));
        list.add(ph);

        // 蛇魔
        pm = Character.getDefaultProperties();
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_TEAM_KEY)).setValue(true);
        ((PropertyCheck) pm.get(PropertyKey.GENERAL_MOB_KEY)).setValue(true);
        ((PropertyInput) pm.get(PropertyKey.GENERAL_WAVE_KEY)).setValue("3");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_HP_KEY)).setValue("172000");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_DEFENSE_KEY)).setValue("570");
        ((PropertyInput) pm.get(PropertyKey.GENERAL_SPEED_KEY)).setValue("127");
        ph = new PropertiesHolder("蛇魔", pm);
        ph.setAfterCreateAction(character -> character.addStatus(new StatusAddDefense(character)));
        list.add(ph);
    }

    private static class StatusHpCheck extends Status implements InfluenceDamageWhenAttack {
        private boolean addedListener;

        private boolean reduceDamage;

        public StatusHpCheck(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            if (!addedListener) {
                attackInfo.getSkill().addSkillEndListener(() -> {
                    addedListener = false;
                    reduceDamage = false;
                });
                addedListener = true;
                if (belongTo.getHpPercent() < 0.7) {
                    reduceDamage = true;
                }
            }

            if (reduceDamage) {
                attackInfo.getTraceableNumber().mul(0.3, "恶之震慑");
            }

        }
    }

    private static class StatusSplashAttack extends Status implements StatusRunnable {
        private final Skill skill = Skill.getInstance("善之祝福");
        private final BattleActionListener listener = new BattleActionListener(belongTo) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventActionDone) {
                    count = 0;
                    return true;
                }
                return false;
            }
        };

        private int count;

        public StatusSplashAttack(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return count < 7 && trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getAttackType() == AttackType.DAN_TI) {
                Character attacker = attackInfo.getAttacker();
                double splashDamage = attackInfo.getTraceableNumber().getNumber() * 0.2;
                List<Character> list = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSelf()
                        .getList();
                attacker.doInteractive(interactive ->
                        interactive.attack(skill, list, c -> {
                            AttackInfo aInfo = AttackInfo.createGuDingAttack(attacker, skill, c, splashDamage);
                            aInfo.setNotCalYuHun();
                            if (attackInfo.getSkill() instanceof GuiLingGeJi.SkillGLGJ) {
                                attacker.getStatus(StatusJieJieEffect.class).ifPresent(
                                        status -> {
                                            if (status.isCreaseNonCritDamage(aInfo)) {
                                                aInfo.getTraceableNumber().mul(0.8, "不知道为什么歌姬溅射不吃不见岳结界");
                                            }
                                        }
                                );
                            }
                            return aInfo;
                        })
                );
                if (count == 0) {
                    belongTo.bp.addActionListener(listener);
                }
                count++;
            }
            return false;
        }
    }

    private static class StatusAddDefense extends Status implements AttributeModifier {
        public StatusAddDefense(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE && belongTo.getHpPercent() < 0.6;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitDefense() * 0.2;
        }
    }
}
