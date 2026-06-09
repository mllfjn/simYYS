package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

public class GuiLingGeJi extends YuHun implements YuHunUnfullMark, YuHunAfterCauseAttack {
    public static final String YuHunName = "鬼灵歌姬";

    private SkillGLGJ skill;

    private StatusCountRecord status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        skill = new SkillGLGJ(character);
    }

    private StatusCountRecord record() {
        if (status == null) {
            status = new StatusCountRecord(character);
            character.addStatus(status);
        }
        return status;
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        if (record().count < 5) {
            record().count++;
            attackInfo.addNote(YuHunName + "计数:" + record().count);
        } else {
            record().count = 0;
            attackInfo.addNote("本次攻击触发" + YuHunName);
            Character target = attackInfo.getTarget();
            skill.triggerSkill = attackInfo.getSkill();
            AttackInfo aInfo = AttackInfo.createRealAttack(character, skill, target,
                    Math.min(target.getMaxHp() * 0.2, character.getAttack() * 2.55)
            );
            interactive.attack(aInfo);
            yuHunEffect();
        }
    }

    private class StatusCountRecord extends Status {
        private int count = 0;

        public StatusCountRecord(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void beforeDelete() {
            GuiLingGeJi.this.status = null;
        }
    }

    private static class SkillGLGJ extends Skill {
        Skill triggerSkill;

        SkillGLGJ(Character character) {
            super(character, -1, 0, 0, -1);
        }

        @Override
        public String getName() {
            return YuHunName;
        }

        @Override
        public void addSkillEndListener(Runnable runnable) {
            triggerSkill.addSkillEndListener(runnable);
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            return Optional.empty();
        }
    }
}
