package com.mllfjn.simyys.character.propertygetter;

import java.io.Serializable;

public class FlagChangeInfo implements Serializable {
    public final FlagType flagType;
    public int target;

    public FlagChangeInfo(FlagType flagType, int target) {
        this.flagType = flagType;
        this.target = target;
    }


    public enum FlagType {
        GREEN("绿标", 0),
        RED("红标", 1);
        public final String type;
        public final int index;

        FlagType(String type, int index) {
            this.type = type;
            this.index = index;
        }


        @Override
        public String toString() {
            return type;
        }
    }
}
