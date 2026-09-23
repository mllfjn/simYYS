package com.mllfjn.simyys.starter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record CharacterNameAndTeam(String name, int team) implements Serializable {
    @Override
    public @NotNull String toString() {
        return "队伍" + team + "-" + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterNameAndTeam(String name1, int team1)) {
            return team1 == team && name1.equals(name);
        }
        return false;
    }
}
