package com.mllfjn.simyys.starter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record CharacterNameAndTeam(String name, int team) implements Serializable {
    @Override
    public @NotNull String toString() {
        return "队伍" + team + "-" + name;
    }
}
