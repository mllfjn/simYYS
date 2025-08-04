package com.mllfjn.simyys.starter.info;

import java.io.Serializable;

public record SkillChangeInfo(String name, String timesToAct, String lockSkill) implements Serializable {
}
