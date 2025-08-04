package com.mllfjn.simyys.starter.info;

import java.io.Serializable;
import java.util.List;

public record CharacterInfo(String name, String speed, String baseAttack, String yuHunAttack, String team, String hp,
                            String defense, String critRate, String critPower, String effectHitRate,
                            String effectResistRate, List<String> special) implements Serializable {
}
