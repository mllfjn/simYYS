package com.mllfjn.simyys.utils;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;
import java.util.List;


public class Recorder implements Serializable {
        public List<Character> characters;
        public Character characterActing;
        public Recorder(List<Character> characters, Character characterActing) {
            this.characters = characters;
            this.characterActing = characterActing;
        }
}
