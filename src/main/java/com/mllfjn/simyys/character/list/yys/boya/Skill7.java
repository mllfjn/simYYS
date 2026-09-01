package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;

// √     阵亡时，提升全体队友60%的伤害和20点速度，持续1回合
// √     lv2-额外提升30点速度
// √     lv3-伤害提升增至80%
// √     lv4-速度提升增至40点
// √     lv5-持续回合数+1
//      术印:增加的伤害额外提升20%,增加的速度额外提升10点 (注:该效果目前无效)

class Skill7 extends PassiveSkill {
    static final String SkillName = "悲声哀嚎";
    private static final int[] baseZengShang = new int[]{0, 60, 60, 80, 80, 80};
    private static final int[] baseSpeed = new int[]{0, 20, 30, 30, 40, 40};

    private final int zengShang;
    private final int speed;

    public Skill7(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 7);
//        this.zengShang = baseZengShang[level] + 20 * shuYin;
        this.zengShang = baseZengShang[level];
        this.speed = baseSpeed[level];

        Status.of(SkillName + "阵亡监听", belongTo)
                .runOn(Trigger.DIE, _ -> Skill7.this.use())
                .addTo();
    }

    private void use() {
        Character belongTo = getBelongTo();
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        for (Character character : list) {
            Status.of(SkillName, belongTo, character)
                    .type(StatusType.BUFF, StatusForm.ZHUANG_TAI)
                    .duration(StatusDurationType.CHI_XU, getLevel() >= 5 ? 2 : 1)
                    .attribute(Attribute.ZENG_SHANG, zengShang)
                    .attribute(Attribute.SPEED, speed)
                    .displayNameAndDuration()
                    .addTo();
        }

        log(null);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}