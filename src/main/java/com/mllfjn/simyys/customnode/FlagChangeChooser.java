package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.character.CharacterFactory;

import java.util.Arrays;
import java.util.stream.Stream;

public class FlagChangeChooser extends LabelChooser{
    /*public FlagChangeLabel() {
        String[] types = new String[CharacterFactory.characterType.length + 1];
        types[0] = "特殊";
        System.arraycopy(CharacterFactory.characterType, 0, types, 1, CharacterFactory.characterType.length);

        String[][] context = new String[CharacterFactory.characterList.length + 1][];
        context[0] = new String[]{"不变", "取消"};
        System.arraycopy(CharacterFactory.characterList, 0, context, 1, CharacterFactory.characterList.length);
        super("不变",types,context);

    }*/
    public FlagChangeChooser() {
        super("不变");
    }

    @Override
    public String[] getTypeText() {
        return Stream.concat(Stream.of("特殊"), Arrays.stream(CharacterFactory.characterType)).toArray(String[]::new);
    }

    @Override
    public String[][] getList() {
        return Stream.concat(Stream.<String[]>of(new String[]{"不变", "取消"}), Arrays.stream(CharacterFactory.characterList)).toArray(String[][]::new);
    }

    @Override
    public String getChooseText() {
        return "选择式神";
    }
}
