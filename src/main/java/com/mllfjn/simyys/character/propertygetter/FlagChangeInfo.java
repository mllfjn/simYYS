package com.mllfjn.simyys.character.propertygetter;

import java.io.Serializable;

public class FlagChangeInfo implements Serializable {
    public FlagType flagType;
    public int target;

    public FlagChangeInfo(FlagType flagType, int target) {
        this.flagType = flagType;
        this.target = target;
    }


    public enum FlagType {
        GREEN("绿标"),
        RED("红标");
        public final String type;

        FlagType(String type) {
            this.type = type;
        }


        @Override
        public String toString() {
            return type;
        }
    }
}
