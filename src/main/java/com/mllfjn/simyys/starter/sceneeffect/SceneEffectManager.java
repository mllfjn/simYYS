package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SceneEffectManager {
    public static final List<String> SCENE_NAME;
    public static final Map<String, Consumer<List<PropertiesHolder>>> SCENE_ADD_CHARACTER;

    static {
        SCENE_NAME = List.of(HunTu.Scene_Name);
        SCENE_ADD_CHARACTER = Map.of(HunTu.Scene_Name, HunTu::addCharacterProperty);
    }
}
