package com.company.components;

import com.company.geometry.Point;

public class Transformer extends Device {

    public Transformer(String name, Point position, Component inComponent, Component outComponent) {
        super(name, position, inComponent, outComponent);
    }
}
