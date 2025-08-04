package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;

public interface StateSupplier {
    State get(Character to, Character from);
}
