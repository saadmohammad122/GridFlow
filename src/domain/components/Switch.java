package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import construction.history.MementoType;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;
import visualization.componentIcons.SourceIcon;

import java.util.List;
import java.util.UUID;

public class Switch extends Closeable {


    public Switch(String name, Point position, boolean closedByDefault) {
        super(name, position, closedByDefault);
        createComponentIcon();
    }

    public Switch(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("closedByDefault").asBoolean(), node.get("closed").asBoolean(), node.get("locked").asBoolean());
        createComponentIcon();
    }

    public Switch(SwitchSnapshot snapshot) {
        super(UUID.fromString(snapshot.id), snapshot.name, snapshot.pos, snapshot.angle, snapshot.closedByDefault, snapshot.closed, snapshot.locked);
        createComponentIcon();
    }

    private void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getSwitchIcon(getPosition(), isClosed(), isClosedByDefault(), isLocked());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setComponentName(getName());
        icon.setAngle(getAngle(), getPosition());
        setComponentIcon(icon);
    }

    @Override
    public void updateComponentIcon() {
        DeviceIcon icon = (DeviceIcon) getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    @Override
    public void updateComponentIconName() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setComponentName(getName());
    }

    @Override
    public ComponentType getComponentType() { return ComponentType.SWITCH; }

    @Override
    public void toggleState() {
        Point oldNamePos = this.getComponentIcon().getCurrentNamePos();
        boolean oldActiveLeft = this.getComponentIcon().getActiveLeft();
        toggleClosed();
        createComponentIcon();
        this.getComponentIcon().setComponentNamePosition(oldNamePos);
        this.getComponentIcon().setCurrentNamePos(oldNamePos);
        this.getComponentIcon().setActiveLeft(oldActiveLeft);
    }

    @Override
    public void toggleLockedState() {
        toggleLocked();
        createComponentIcon();
    }

    @Override
    public ComponentMemento makeSnapshot() {
        return new SwitchSnapshot(getId().toString(), getName(), getAngle(), getPosition(), isClosed(), isClosedByDefault(), isLocked(), getInWireID().toString(), getOutWireID().toString());
    }
}

class SwitchSnapshot implements ComponentMemento {
    String id;
    String name;
    double angle;
    Point pos;
    boolean closed;
    boolean closedByDefault;
    boolean locked;
    String inNodeId;
    String outNodeId;

    public SwitchSnapshot(String id, String name, double angle, Point pos, boolean closed, boolean closedByDefault, boolean locked, String inNodeId, String outNodeId) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.closed = closed;
        this.closedByDefault = closedByDefault;
        this.locked = locked;
        this.inNodeId = inNodeId;
        this.outNodeId = outNodeId;
    }

    @Override
    public Component getComponent() {
        return new Switch(this);
    }

    @Override
    public List<String> getConnectionIDs() {
        return List.of(inNodeId, outNodeId);
    }
}
