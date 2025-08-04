package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.guihuo.GuiHuo;
import com.mllfjn.simyys.ratecontroller.TotalRateCalc;

import java.io.Serializable;
import java.util.List;

public record CharacterStack(List<Character> characters, Character characterActing, Character[] autoTo, GuiHuo[] guiHuos, double totalRate) implements Serializable {
}
