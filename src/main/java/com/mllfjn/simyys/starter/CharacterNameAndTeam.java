package com.mllfjn.simyys.starter;

import java.io.Serializable;

public record CharacterNameAndTeam(String name, int team) implements Serializable {
    @Override
    public String toString() {
        return "队伍" + team + "-" + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterNameAndTeam oc) {
            return oc.team == team && oc.name.equals(name);
        }
        return false;
    }
}
