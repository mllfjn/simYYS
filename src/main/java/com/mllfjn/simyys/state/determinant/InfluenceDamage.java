package com.mllfjn.simyys.state.determinant;

import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;

public interface InfluenceDamage {
    boolean effective(AttackType attackType);
    void doInfluence(AttackType attackType, Info info);
}
