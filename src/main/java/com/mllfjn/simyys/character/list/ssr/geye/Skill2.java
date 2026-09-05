package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "渡灵";

    public Skill2(GeYe belongTo, int level) {
        super(belongTo, level, 0, 0, 2);

        belongTo.addStatus(new StatusUseSkillListener(belongTo));
        belongTo.bp.addStatusAdder(c ->
                c.team == belongTo.team && !c.isSummon() && c != belongTo
                        ? new HeShouContainer(belongTo, c, level >= 3)
                        : null
        );

        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, () ->
                    StatusHZBH.addStack(belongTo, belongTo, 1, false)
            );
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t葛叶释放归源术外的妖术获得1层九尾之力。其他非召唤物友方获得技能合守
                √\t[释放]使指定友方目标提升40%行动条,并获得2层狐族庇护,持续1回合
                √\tlv2-使友方获得3层狐族庇护
                √\tlv3-友方获得狐族庇护时,提升葛叶75点速度,至多3次,持续1回合
                √\tlv4-释放使自身获得1层狐族庇护,持续1回合
                √\tlv5-战斗开始时,获得1层狐族庇护,持续1回合
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();

        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .getAutoOrElseRandom();

        belongTo.getInteractive().increaseLocation(target, 40);
        StatusHZBH.addStack(belongTo, target, level >= 2 ? 3 : 2, level >= 3);

        if (level >= 4) {
            StatusHZBH.addStack(belongTo, belongTo, 1, false);
        }

        return Optional.of(target);
    }

    static class StatusUseSkillListener extends Status {
        public StatusUseSkillListener(Character character) {
            super(SkillName + "释放技能监听", character);
            runOn(Trigger.USED_SKILL, param -> {
                if (!(((ParamUseSkill) param).getSkill() instanceof Skill3)) {
                    if (StatusJiuWei.addStack(belongTo)) {
                        delete();
                    }
                }
            });
        }
    }

    static class HeShouContainer extends Status {
        public HeShouContainer(GeYe from, Character belongTo, boolean isIncreaseSpeed) {
            super(SkillHeShou.SkillName, from, belongTo);
            Skill skill = new SkillHeShou(from, belongTo, isIncreaseSpeed);
            belongTo.addSkill(skill);
            beforeDelete(() -> belongTo.removeSkill(skill));
        }
    }

}
