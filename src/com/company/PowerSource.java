package com.company;

import processing.core.PConstants;

import java.util.ArrayList;

public class PowerSource extends Component {

    public PowerSource(Main mainSketch, int id, String name, String type, char orientation, int normalState,
                       int xPos, int yPos, int length,
                       String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {

        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        this.getInNode().setEnergized(true); // sets power source inNode energized no matter what
    } // END Constructor #0

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

       // Draw bottom energy line if present
        if (getOutNode().isEnergized()) {
            drawLine(1, 3);
        }

        // Draw energy box if present
        if ((getInNode().isEnergized() && getCurrentState() == 0) ||
                getOutNode().isEnergized() && getCurrentState() == 0) {
            drawBox( 4, 5, 6, 7, 2);
        }

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_THIN * scale;
        float unit = mainSketch.UNIT * scale;

        int x = calcPos((int) (getInNode().getCoord().getxPos()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getyPos()), scale, panY);

        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

      // Draw bottom black line regardless
        drawLine(1, 3);

        // Draw box but fill with green or red depending on state open/closed
        if (getCurrentState() == 0) mainSketch.fill(255, 0, 0); // red
        else mainSketch.fill(0, 255, 0); // green
        drawBox(4, 5, 6, 7, 2);

        // Place name label
        mainSketch.fill(0); // black
        drawText(2, getLabel(), getTextAnchor(), getLabelOrientation());

    } // END renderLines()

    // Establishes all of the grid coordinates needed to draw this object at the correct orientation
    public void calcDrawingCoords() {

        ArrayList<Coord> coords = new ArrayList<>();
        float x = this.getInNode().getCoord().getxPos();
        float y = this.getInNode().getCoord().getyPos();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        coords.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        coords.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Coord coord;
        coord = new Coord(x, y + 1f); // Element #2 - Anchor for text/label
        coords.add(coord);
        coord = new Coord(x, y + 2f); // Element #3 - Top of bottom line
        coords.add(coord);
        coord = new Coord(x - 1f, y); // Element #4 - top left corner of box
        coords.add(coord);
        coord = new Coord(x - 1f, y + 2f); // Element #5 - bottom left corner of box
        coords.add(coord);
        coord = new Coord(x + 1f, y); // Element #6 - top right corner of box
        coords.add(coord);
        coord = new Coord(x + 1f, y + 2f); // Element #7 - bottom right corner of box
        coords.add(coord);

        // This next step determines if the coordinates have to be rotated, rotates them, and
        // then executes the setDs() method for this component.
        switch (this.getOrientation()) {
            case 'U':
                setDs(rotateDwgCoords(coords, 180));
                break;
            case 'L':
                setDs(rotateDwgCoords(coords, 90));
                break;
            case 'R':
                setDs(rotateDwgCoords(coords, 270));
                break;
            default:
                setDs(coords);
                break;
        } // END switch (orientation)

        // Update the location of the outNode
        this.getOutNode().setCoord(getDs().get(1));

        // Establish the click coordinates
        this.setClickCoords(4, 7);


    } // END calcDrawingCoords

}  // END public class PowerSource
