package com.mllfjn.simyys.hit;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;

public interface StateSupplier {
    State get(Character belongTo, Character comeFrom);
}
