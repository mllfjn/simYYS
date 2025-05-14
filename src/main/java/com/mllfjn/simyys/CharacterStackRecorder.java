package com.mllfjn.simyys;


import com.mllfjn.simyys.character.Character;

import java.io.Serializable;
import java.util.List;

public class CharacterStackRecorder implements Serializable {
        public List<Character> characters;
        public Character characterActing;
        public CharacterStackRecorder(List<Character> characters, Character characterActing) {
            this.characters = characters;
            this.characterActing = characterActing;
        }
}
