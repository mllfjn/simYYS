package com.mllfjn.simyys.utils.qrcodeloader;

import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

import java.util.Map;

public abstract class CharacterData {
    public int hero_id;
    public int star; // 星级
    public int level; // 等级
    public boolean awake; // 觉醒

    public CharacterData() {
    }

    public abstract void get(PropertiesHolder ph, Map<LoadAttribute, Float> maxAttributeMap);
}
