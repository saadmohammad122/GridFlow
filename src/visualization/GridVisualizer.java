package visualization;

import construction.canvas.GridCanvas;
import domain.Grid;
import domain.components.*;

public class GridVisualizer {

    public static final double UNIT = 20;
    public static final double STROKE_WIDTH = 1.5;
    public static final double ENERGY_STROKE_WIDTH = 4;

    private final Grid grid;
    private final GridCanvas canvas;

    public GridVisualizer(Grid grid, GridCanvas canvas) {
        this.grid = grid;
        this.canvas = canvas;
    }

    public void displayGrid() {
        clearGrid();

        for (Component component : grid.getComponents()) {
            canvas.addComponentIcon(component.getComponentIcon());
        }
    }

    public void clearGrid() {
        canvas.getChildren().clear();
    }

}
