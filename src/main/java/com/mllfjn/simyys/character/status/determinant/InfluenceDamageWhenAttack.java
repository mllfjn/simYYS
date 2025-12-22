package com.mllfjn.simyys.character.status.determinant;

import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

public interface InfluenceDamageWhenAttack {

    void doInfluenceWhenAttack(AttackType attackType, InteractiveInfo interactiveInfo);
}
