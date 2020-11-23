package model.components;

import model.geometry.Point;
import visualization.components.ComponentIcon;
import visualization.components.ComponentIconCreator;
import visualization.components.DeviceIcon;

public class Switch extends Device implements IToggleable, ILockable {

    boolean closed;
    boolean locked;

    public Switch(String name, Point position, boolean closed) {
        super(name, position);
        this.closed = closed;
        this.locked = false;
    }

    public void toggleState() {
        closed = !closed;
    }

    public boolean getState() {
        return closed;
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
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(getPosition());
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
        return icon;
    }
}
