package com.example.mapapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * model data in matched substrings.
 */
public class MatchedSubstring {
    @SerializedName("length")
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @SerializedName("offset")
    private int offset;
}
