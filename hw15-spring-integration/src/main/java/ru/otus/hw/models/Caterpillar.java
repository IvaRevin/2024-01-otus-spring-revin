package ru.otus.hw.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Caterpillar {

    private String id;

    private int weight;

    private int length;

    private int diameter;

    private CaterpillarColour color;

    public Caterpillar(int weight, int length, int diameter, CaterpillarColour color) {
        this.id = UUID.randomUUID().toString();
        this.weight = weight;
        this.length = length;
        this.diameter = diameter;
        this.color = color;
    }
}
