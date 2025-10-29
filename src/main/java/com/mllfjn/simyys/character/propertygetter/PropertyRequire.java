package com.mllfjn.simyys.character.propertygetter;

import javafx.scene.Node;
import javafx.stage.Window;

import java.io.Serializable;

public abstract class PropertyRequire implements Serializable {
    public abstract Node getNode(String desc, Window owner);
    public abstract boolean cover(PropertyRequire pr);


    public double getDouble() {
        return 0;
    }
    public boolean getBoolean() {
        return false;
    }
    public int getInt() {
        return 0;
    }

//    public abstract void toString(StringBuilder sb);
}
