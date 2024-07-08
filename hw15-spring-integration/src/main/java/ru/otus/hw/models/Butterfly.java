package ru.otus.hw.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Butterfly {

    private String id;

    private int weight;

    private int length;

    private ButterflyColour color;

    public Butterfly(int weight, int length, ButterflyColour color) {
        this.id = UUID.randomUUID().toString();
        this.weight = weight;
        this.length = length;
        this.color = color;
    }
}
