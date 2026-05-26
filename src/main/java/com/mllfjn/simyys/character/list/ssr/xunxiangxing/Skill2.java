package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "缚梦明香";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, level >= 4 ? 2 : 3, 0, 2);
        if (level >= 5) {
            belongTo.bp.atBattleStart(this::useWithoutCost);
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t开启维持3回合的明香境
                √\t\t在明香境中,除自身外非召唤物友方回合结束后,寻香行获得1层心香
                √\t\t寻香行攻击敌方时有40%基础概率附加缚魂香
                √\t\t且敌方防御每比寻香行低1%,寻香行造成的伤害提升1%
                √\tlv2-释放3时,维持效果增加1回合
                √\tlv3-缚魂香每层降低敌方防御10%
                √\tlv4-消耗鬼火减少1点
                √\tlv5-先机:释放2
                
                √\t缚魂香:每次获得时永久降低携带者5%基础防御,此效果可叠加5层
                √\t\t累计3层时,消耗当前层数转化成失神并对敌方目标造成攻击231%间接伤害
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        StatusHuanJing.install(this, getBelongTo());

        return Optional.empty();
    }

    static class StatusXinXiang extends Status implements Displayable {
        private static final String StatusName = "心香";

        private int stack;

        public StatusXinXiang(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        }

        static void addStack(Character character) {
            character.getStatus(StatusXinXiang.class)
                    .ifPresentOrElse(
                            status -> status.stack++,
                            () -> character.addStatus(new StatusXinXiang(character))
                    );
        }

        @Override
        public String getDisplayText() {
            if (stack == 0) {
                return null;
            }
            return StatusName + stack;
        }
    }
}
