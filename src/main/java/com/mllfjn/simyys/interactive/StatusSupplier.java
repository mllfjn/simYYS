package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.status.Status;

public interface StatusSupplier {
    Status get(Character from, Character to);
}
