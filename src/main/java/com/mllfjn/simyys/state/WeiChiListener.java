package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;

public class WeiChiListener extends State{
    public static final String privateName = "维持";
    private final State content;
    public WeiChiListener(Character belongTo, Character comeFrom, State content) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
        this.content = content;
    }

    @Override
    public void setName() {
        name = privateName;
    }
}
