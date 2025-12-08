package com.mllfjn.simyys.character.status.determinant;

import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.AttackInfo;

public interface InfluenceDamageBeingAttack {

    void doInfluenceBeingAttack(AttackType attackType, AttackInfo attackInfo);
}
