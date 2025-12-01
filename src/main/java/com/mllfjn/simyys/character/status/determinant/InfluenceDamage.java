package com.mllfjn.simyys.character.status.determinant;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;

public interface InfluenceDamage {
    boolean effective(AttackType attackType, Character character);
    void doInfluence(AttackType attackType, Info info);
}
