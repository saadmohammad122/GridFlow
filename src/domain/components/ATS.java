package domain.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.ComponentType;
import construction.history.ComponentMemento;
import construction.history.MementoType;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIconCreator;
import visualization.componentIcons.DeviceIcon;

import java.util.List;
import java.util.UUID;

public class ATS extends Device {

    public ATS(String name, Point position) {
        super(name, position);
        createComponentIcon();
    }

    public ATS(JsonNode node) {
        super(UUID.fromString(node.get("id").asText()), node.get("name").asText(),
                Point.fromString(node.get("pos").asText()), node.get("angle").asDouble(),
                node.get("namepos").asBoolean());
        createComponentIcon();
    }

    public ATS(ATSSnapshot snapshot) {
        super(UUID.fromString(snapshot.id), snapshot.name, snapshot.pos, snapshot.angle, snapshot.namepos);
        createComponentIcon();
    }

    protected void createComponentIcon() {
        DeviceIcon icon = ComponentIconCreator.getATSIcon(getPosition(),isInWireEnergized());
        icon.setDeviceEnergyStates(false, false);
        icon.setComponentIconID(getId().toString());
        icon.setAngle(getAngle(), getPosition());
        icon.setComponentName(getName(), isNameRight());
        setComponentIcon(icon);
    }

    @Override
    public ComponentMemento makeSnapshot() {
        return new ATSSnapshot(getId().toString(), getName(), getAngle(), getPosition(), getInWireID().toString(), getOutWireID().toString(), isNameRight());
    }

    @Override
    public void updateComponentIcon() {
        System.out.println(isInWireEnergized());
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setDeviceEnergyStates(isInWireEnergized(), isOutWireEnergized());
    }

    @Override
    public void updateComponentIconName() {
        DeviceIcon icon = (DeviceIcon)getComponentIcon();
        icon.setComponentName(getName(), isNameRight());
    }

    @Override
    public ComponentType getComponentType() { return ComponentType.TRANSFORMER; }
}

class ATSSnapshot implements ComponentMemento {
    String id;
    String name;
    double angle;
    Point pos;
    String inNodeId;
    String outNodeId;
    boolean namepos;

    public ATSSnapshot(String id, String name, double angle, Point pos, String inNodeId, String outNodeId, boolean namepos) {
        this.id = id;
        this.name = name;
        this.angle = angle;
        this.pos = pos.copy();
        this.inNodeId = inNodeId;
        this.outNodeId = outNodeId;
        this.namepos = namepos;
    }

    @Override
    public Component getComponent() {
        return new ATS(this);
    }

    @Override
    public List<String> getConnectionIDs() {
        return List.of(inNodeId, outNodeId);
    }
}
