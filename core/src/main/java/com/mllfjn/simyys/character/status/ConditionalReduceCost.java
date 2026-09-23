package com.mllfjn.simyys.character.status;

public interface ConditionalReduceCost {
    int getMaxReduce();

    void enable(int usedCount);
}
