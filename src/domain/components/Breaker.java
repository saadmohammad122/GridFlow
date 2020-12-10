package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

public class Breaker extends Device implements ICloseable, ICloneable, IPairable {

    private Voltage voltage;
    private boolean closed;
    private boolean closedByDefault;

    public Breaker(String name, Point position, Voltage voltage, boolean closedByDefault) {
        super(name, position);
        this.voltage = voltage;
        this.closed = closedByDefault;
        this.closedByDefault = closedByDefault;
        this.setUnitWidth(2);
        switch(voltage) {
            case KV12:
                this.setUnitHeight(4);
                break;

            case KV70:
                this.setUnitHeight(3);
                break;
        }

    } @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon;
        if (voltage == Voltage.KV12) {
            icon = ComponentIconCreator.get12KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
            icon.setComponentName(getName());
            icon.setBoundingRect(getPosition(), this.getUnitWidth(), this.getUnitHeight(), -0.5, -0.5);
        } else {
            icon = ComponentIconCreator.get70KVBreakerIcon(getPosition(), isClosed(), isClosedByDefault());
            icon.setComponentName(getName());
            icon.setBoundingRect(getPosition(), this.getUnitWidth(), this.getUnitHeight(), -0.5, -0.5);
        }
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        return icon;
    }

    public Voltage getVoltage() {
        return voltage;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isClosedByDefault() {
        return closedByDefault;
    }

    @Override
    public void toggle() {
        closed = !closed;
    }
}
