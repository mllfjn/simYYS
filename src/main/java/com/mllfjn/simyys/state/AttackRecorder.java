package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.customnode.TextFlowLog;

import java.util.ArrayList;
import java.util.List;

public class AttackRecorder extends State{
    public static final String privateName = "攻击记录";
    List<NumberRecorder> list = new ArrayList<>();
    public AttackRecorder(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    public NumberRecorder getLast() {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    public class NumberRecorder {
        public final Character from;
        public final TextFlowLog.TextType type;
        public final double number;
        public NumberRecorder(Character from, TextFlowLog.TextType type, double number) {
            this.from = from;
            this.type = type;
            this.number = number;
        }
    }

}
