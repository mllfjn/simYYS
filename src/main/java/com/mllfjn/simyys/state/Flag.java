package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;

public class Flag extends State{
    public Flag(Character from, Character belongTo, String name) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.name = name;
    }
    @Override
    public void setName() {

    }
}
