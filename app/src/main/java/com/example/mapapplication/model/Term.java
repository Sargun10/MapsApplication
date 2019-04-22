package com.example.mapapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * model data for term.
 */
public class Term {
    @SerializedName("offset")
    private int offset;

    @SerializedName("value")
    private String value;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
