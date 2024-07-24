package ru.otus.hw.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Chrysalis {

    private String id;

    private int weight;

    private int length;

    private int diameter;

    private ChrysalisColour color;

    public Chrysalis(int weight, int length, int diameter, ChrysalisColour color) {
        this.id = UUID.randomUUID().toString();
        this.weight = weight;
        this.length = length;
        this.diameter = diameter;
        this.color = color;
    }
}
