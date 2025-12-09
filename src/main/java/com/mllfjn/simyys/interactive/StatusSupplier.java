package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;

import java.util.function.BiFunction;

public interface StatusSupplier {
    Status get(Character from, Character to);
}
