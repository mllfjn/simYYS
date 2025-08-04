package com.mllfjn.simyys.starter.info;

import java.io.Serializable;

public record FlagChangeInfo(String name, String timesToAct, String redFlag, String greenFlag) implements Serializable {

}
