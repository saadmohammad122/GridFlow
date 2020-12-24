package domain.components;

import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.UUID;

public class Switch extends Device implements ICloseable, ILockable {

    private boolean closed;
    private boolean closedByDefault;
    private boolean locked;


    public Switch(String name, Point position, boolean closedByDefault) {
        super(name, position);
        this.closedByDefault = closedByDefault;
        this.closed = closedByDefault;
        this.locked = false;
        setDimensions();
    }

    public Switch(String name, Point position, boolean closedByDefault, UUID id, double angle, Wire inWire, Wire outWire, boolean closed, boolean locked) {
        super(name, position, id, angle, inWire, outWire);
        this.closed = closed;
        this.closedByDefault = closedByDefault;
        this.locked = locked;
        setDimensions();
    }

    public void setDimensions() {
        this.getDimensions().setWidth(2);
        this.getDimensions().setHeight(3);
    }

    public void toggleLocked() {
        locked = !locked;
    }

    @Override
    protected boolean checkClosed() {
        return closed;
    }

    @Override
    public ComponentIcon getComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(getPosition(), isClosed(), isClosedByDefault());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setBoundingRect(getDimensions(), getPosition());
        icon.setAngle(getAngle(), getPosition());
        return icon;
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
