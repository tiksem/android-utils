package com.dbbest.android.image;

public enum ProcessorExtra {
    OUTPUT_PATH("output_path");

    private String extraName;

    private ProcessorExtra(String name) {
        this.extraName = name;
    }

    public String getExtraName() {
        return extraName;
    }
}
