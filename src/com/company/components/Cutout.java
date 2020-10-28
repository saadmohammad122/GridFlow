package com.company.components;

import com.company.geometry.Point;

public class Cutout extends Device implements ILockable {

    public Cutout(String name, Point position, Component inComponent, Component outComponent) {
        super(name, position, inComponent, outComponent);
    }
}
