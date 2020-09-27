package com.pan1.em;

public enum Group {

    VIP(400, "VIP"),

    GENERAL(200, "GENERAL");

    /**
     * memory
     */
    private int memory;

    /**
     * name
     */
    private String name;

    public int getMemory() {
        return memory;
    }

    public String getName() {
        return name;
    }

    Group(int memory, String name) {
        this.memory = memory;
        this.name = name;
    }

}
