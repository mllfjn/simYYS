package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.sr.haifangzhu.HaiFangZhu;

class SpecialHaiFangZhu extends HaiFangZhu {
    public SpecialHaiFangZhu(BattlePane bp, int team) {
        this.name = CharacterName;
        this.bp = bp;
        this.team = team;
        this.setMob(3, 3);

        this.setInitDefense(352);
        this.setMaxHp(64720, true);
        this.setInitBaseAttack(3483);
        this.setInitSpeed(100);
        this.setInitCritPower(150);

        skill1Level = 5;
        addSkills();
    }
}
