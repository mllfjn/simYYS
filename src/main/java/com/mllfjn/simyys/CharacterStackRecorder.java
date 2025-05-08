package com.mllfjn.simyys;


import com.mllfjn.simyys.character.Character;

import java.io.Serializable;
import java.util.List;

public class CharacterStackRecorder implements Serializable {/*
    List<CharacterRecorderInfo> characterRecorderInfo = new ArrayList<>();
    Character characterActing;
    public Recorder(List<Character> characters, Character characterActing) {
        for (Character character : characters) {
            characterRecorderInfo.add(new CharacterRecorderInfo(character));
        }
        this.characterActing = characterActing;
    }

    public void recover(BattlePane battlePane) {
        for (CharacterRecorderInfo info : characterRecorderInfo) {
            info.recover();
        }
        battlePane.characterActing = characterActing;
    }*/

        public List<Character> characters;
        public Character characterActing;
        public CharacterStackRecorder(List<Character> characters, Character characterActing) {
            this.characters = characters;
            this.characterActing = characterActing;
        }
}
