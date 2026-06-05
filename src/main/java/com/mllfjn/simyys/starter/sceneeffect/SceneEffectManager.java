package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SceneEffectManager {
    public static final List<String> SCENE_NAMES;
    public static final Map<String, Consumer<List<PropertiesHolder>>> SCENE_ADD_CHARACTER;

    static {
        SCENE_NAMES = List.of(
                HunTu.SCENE_NAME,
                HunWang.SCENE_NAME);
        SCENE_ADD_CHARACTER = Map.of(
                HunTu.SCENE_NAME, HunTu::addCharacterProperty

        );
    }
}
