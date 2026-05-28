package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.instance.StatusDieHandler;
import com.mllfjn.simyys.utils.SerializableRunnable;

import java.util.ArrayList;
import java.util.List;

public class StageSummonManager {
    private final List<Character> summonList = new ArrayList<>(5);

    private SerializableRunnable dieHandler;

    public void addSummon(Character summon) {
        summonList.add(summon);
        if (dieHandler != null) {
            summon.addStatus(new StatusDieHandler(summon, dieHandler));
        }
    }

    public void clear() {
        for (Character character : summonList) {
            character.die();
        }
    }
}
