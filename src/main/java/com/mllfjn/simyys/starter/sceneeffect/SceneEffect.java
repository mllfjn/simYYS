package com.mllfjn.simyys.starter.sceneeffect;

import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.collections.SerializableObservableList;

import java.util.function.Consumer;

public enum SceneEffect {
    NULL("选择场景", null),
    HUN_TU("魂土", HunTu::addCharacterProperty),
    HUN_WANG("魂王", HunWang::addCharacterProperty),
    ;
    private final String sceneName;
    private final Consumer<SerializableObservableList<PropertiesHolder>> addCharacter;

    SceneEffect(String sceneName, Consumer<SerializableObservableList<PropertiesHolder>> addCharacter) {
        this.sceneName = sceneName;
        this.addCharacter = addCharacter;
    }

    public String getSceneName() {
        return sceneName;
    }

    public Consumer<SerializableObservableList<PropertiesHolder>> getAddCharacter() {
        return addCharacter;
    }

    @Override
    public String toString() {
        return sceneName;
    }
}
