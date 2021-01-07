package baseui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Grid;
import domain.components.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridFileManager {
    private Grid grid;
    private ObjectMapper mapper;

    public GridFileManager() {
        grid = new Grid();
        mapper = new ObjectMapper();
    }

    public void saveGrid(String path) throws IOException {
        ObjectNode gridNode = mapper.createObjectNode();
        ArrayNode components = mapper.createArrayNode();

        grid.getComponents().forEach(c -> components.add(c.getObjectNode(mapper)));
        gridNode.put("components", components);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), gridNode);
    }

    public void loadGrid(String path) throws IOException {
        JsonParser parser = mapper.getFactory().createParser(new File(path));
        ObjectNode gridNode = mapper.readTree(parser);
        parser.close();

        grid.clearComponents();
        ArrayNode JSONComponents = (ArrayNode) gridNode.get("components");

        // create components
        for (JsonNode componentJSON : JSONComponents) {
            Component component = switch (componentJSON.get("type").asText()) {
                case "Breaker" -> new Breaker(componentJSON);
                case "Cutout" -> new Cutout(componentJSON);
                case "Jumper" -> new Jumper(componentJSON);
                case "PowerSource" -> new PowerSource(componentJSON);
                case "Switch" -> new Switch(componentJSON);
                case "Transformer" -> new Transformer(componentJSON);
                case "Turbine" -> new Turbine(componentJSON);
                case "Wire" -> new Wire(componentJSON);
                default -> throw new UnsupportedOperationException();
            };
            grid.addComponent(component);
        }

        // connect components
        for (int i = 0; i < grid.getComponents().size(); i++) {
            Component component = grid.getComponents().get(i);
            JsonNode componentJSON = JSONComponents.get(i);
            component.setConnections(getConnectionsList(componentJSON));
        }
    }

    private List<Component> getConnectionsList(JsonNode node) {
        List<Component> connections = new ArrayList<>();
        switch (node.get("type").asText()) {
            case "Breaker", "Cutout", "Jumper", "Switch", "Transformer" -> {
                connections.add(grid.getComponent(node.get("inWire").asText()));
                connections.add(grid.getComponent(node.get("outWire").asText()));
            }
            case "PowerSource" -> {
                connections.add(grid.getComponent(node.get("outWire").asText()));
            }
            case "Turbine" -> {
                connections.add(grid.getComponent(node.get("outWire1").asText()));
                connections.add(grid.getComponent(node.get("outWire2").asText()));
            }
            case "Wire" -> {
                ArrayNode jsonConnections = (ArrayNode)node.get("connections");
                jsonConnections.forEach(jsonConnection ->
                    connections.add(grid.getComponent(jsonConnection.asText()))
                );
            }
            default -> throw new UnsupportedOperationException();
        }
        return connections;
    }

    public Grid getGrid() {
        return grid;
    }

}