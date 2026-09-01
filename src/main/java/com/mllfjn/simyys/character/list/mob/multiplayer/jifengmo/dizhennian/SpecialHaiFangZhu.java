package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.sr.haifangzhu.HaiFangZhu;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;

class SpecialHaiFangZhu extends HaiFangZhu {
    public SpecialHaiFangZhu(BattlePane bp, int team, DiZhenNian diZhenNian, StatusBuff.BuffType type) {
        this.name = CharacterName;
        this.bp = bp;
        this.team = team;
        this.setMob(3, 3);

        this.setInitDefense(352);
        this.forceSetMaxHp(64720, true);
        this.setInitBaseAttack(3483);
        this.setInitSpeed(100);
        this.setInitCritPower(150);

        skill1Level = 5;
        skill3Level = 5;
        fillSkills();

        addStatus(new StatusBuff(diZhenNian, this, type, 0));
        Status.of("海坊主-设置加成属性", diZhenNian, this)
                .runOn(Trigger.DIE, _ -> diZhenNian.setBuffType(type))
                .addTo();
    }
}
