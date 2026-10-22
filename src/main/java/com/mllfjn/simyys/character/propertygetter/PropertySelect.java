package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.character.PropertyKey;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class PropertySelect extends PropertyRequire implements Serializable {

    protected final String optionKey;

    protected String value;

    public PropertySelect(String optionKey) {
        this.optionKey = optionKey;
    }

    @Override
    public String getString() {
        return value;
    }

    protected Map<String, List<String>> getOptions() {
        return PropertyKey.SELECT_OPTIONS.get(optionKey);
    }

    protected boolean contains(String newValue) {
        for (List<String> optionsList : getOptions().values()) {
            if (optionsList.contains(newValue)) {
                this.value = newValue;
                return true;
            }
        }
        return false;
    }
}
