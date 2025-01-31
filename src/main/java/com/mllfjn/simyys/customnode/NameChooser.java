package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.character.CharacterFactory;

public class NameChooser extends LabelChooser {

    public NameChooser() {
        super("选择式神");
    }

    @Override
    public String[] getTypeText() {
        return CharacterFactory.characterType;
    }

    @Override
    public String[][] getList() {
        return CharacterFactory.characterList;
    }

    @Override
    public String getChooseText() {
        return "选择式神";
    }
}
