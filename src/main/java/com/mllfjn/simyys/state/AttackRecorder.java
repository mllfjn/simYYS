package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.customnode.CustomTextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    protected void setName() {
        name = privateName;
    }

    public class NumberRecorder {
        public final Character comeFrom;
        public final CustomTextFlow.NumberType type;
        public final double number;
        public NumberRecorder(Character comeFrom, CustomTextFlow.NumberType type, double number) {
            this.comeFrom = comeFrom;
            this.type = type;
            this.number = number;
        }
    }

}
