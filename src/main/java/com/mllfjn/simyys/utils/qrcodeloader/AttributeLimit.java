package com.mllfjn.simyys.utils.qrcodeloader;

import java.util.Random;

public class AttributeLimit {
    private final float min;
    private final float max;

    public AttributeLimit(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float get(float defaultMin, float defaultMax, Random random) {
        if (min != -1) {
            if (min == max) {
                return min;
            } else {
                return random.nextFloat(min, max);
            }
        } else {
            return random.nextFloat(defaultMin, defaultMax);
        }
    }
}
