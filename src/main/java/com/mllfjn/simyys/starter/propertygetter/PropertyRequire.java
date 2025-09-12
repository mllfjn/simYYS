package com.mllfjn.simyys.starter.propertygetter;

import javafx.scene.Node;

import java.io.Serializable;

public abstract class PropertyRequire implements Serializable {

    protected final String desc;
    public PropertyRequire(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public abstract Node getNode();
    public abstract boolean cover(PropertyRequire pr);
    public double getDouble() {
        if (this instanceof PropertyInput in) {
            try {
                return Double.parseDouble(in.getValue());
            } catch (NumberFormatException e) {
                return 0;
            }

        }
        return 0;
    }

    public boolean getBoolean() {
        if (this instanceof PropertyCheck check) {
            return check.getValue();
        }
        return false;
    }

    public int getInt() {
        if (this instanceof PropertyInput in) {
            return Integer.parseInt(in.getValue());
        }
        return 0;
    }

    public void toString(StringBuilder sb) {
        if (!sb.isEmpty()) {
            sb.append("  ");
        }
    }
}
