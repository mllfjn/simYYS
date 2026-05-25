package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "以毒攻毒";
    private boolean enable = false;

    private final StatusXieDuSpecialOnXieNv status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        status = new StatusXieDuSpecialOnXieNv(belongTo);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t引爆蝎毒造成伤害时,自身恢复伤害20%的生命
                √\t敌方每有1层蝎毒,提升自身20%攻击
                √\t引爆蝎毒恢复生命溢出时,将溢出恢复量的100%平均分配给其他友方
                 \tlv2-蝎毒使目标减疗40%
                √\tlv3-引爆蝎毒恢复比例提升至30%
                 \tlv4-蝎毒超过1层时,每层额外使目标减疗10%
                √\tlv5-陷入5层蝎毒的敌方目标受到间接伤害回合后,向其他敌方溅射该伤害60%的间接伤害(此伤害不超过蝎女攻击上限800%)
                """;
    }

    @Override
    public void enable() {
        enable = true;
        getBelongTo().addStatus(status);
    }

    @Override
    protected void disable() {
        enable = false;
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    public boolean canCount() {
        return enable && getLevel() >= 5;
    }

    class StatusXieDuSpecialOnXieNv extends Status implements StatusRunnable, AttributeModifier {
        private static final Skill SKILL = Skill.getInstance(SkillName);

        public StatusXieDuSpecialOnXieNv(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.CAUSE_ATTACK) {
                InteractiveInfo info = ((ParamAttackInfo) param).getAttackInfo();
                Skill skill = info.getSkill();
                if (skill == StatusXieDu.SKILL
                        && !info.isCancel()
                        && info.getTarget() == ((XieNv) belongTo).getXieDu().belongTo) {
                    double number = info.getTraceableNumber().getNumber();
                    if (number > 0) {
                        double maxRecovery = number * (Skill2.this.getLevel() >= 3 ? 0.3 : 0.2);
                        double loseHP = belongTo.getMaxHp() - belongTo.getHp();
                        belongTo.doInteractive(interactive -> {
                            if (maxRecovery <= loseHP) {
                                interactive.recovery(SKILL, belongTo, maxRecovery);
                            } else {
                                interactive.recovery(SKILL, belongTo, loseHP);
                                List<Character> list = new CharacterFinder(belongTo)
                                        .filterTeammate()
                                        .filterSelf()
                                        .getList();
                                if (!list.isEmpty()) {
                                    double averageOverflow = (maxRecovery - loseHP) / list.size();
                                    for (Character character : list) {
                                        interactive.recovery(SKILL, character, averageOverflow);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            return false;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            StatusXieDu xieDu = ((XieNv) belongTo).getXieDu();
            if (xieDu != null) {
                return belongTo.getInitAttack() * 0.2 * xieDu.getStack();
            } else {
                return 0;
            }
        }
    }
}
