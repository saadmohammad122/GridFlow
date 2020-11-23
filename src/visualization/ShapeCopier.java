package visualization;

import javafx.scene.shape.*;

public class ShapeCopier {

    public static Shape copyShape(Shape shape) {
        if (shape instanceof Line) {
            return copyLine((Line)shape);
        } else if (shape instanceof Rectangle) {
            return copyRectangle((Rectangle)shape);
        } else if (shape instanceof Arc) {
            return copyArc((Arc)shape);
        } else if (shape instanceof QuadCurve) {
            return copyQuadCurve((QuadCurve)shape);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Line copyLine(Line line) {
        return new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    private static Rectangle copyRectangle(Rectangle rectangle) {
        return new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private static Arc copyArc(Arc arc) {
        Arc copy = new Arc(arc.getCenterX(), arc.getCenterY(), arc.getRadiusX(), arc.getRadiusY(),
                arc.getStartAngle(), arc.getLength());
        // apply transforms to copy
        arc.getTransforms().forEach(transform -> copy.getTransforms().add(transform));
        return copy;
    }

    private static QuadCurve copyQuadCurve(QuadCurve qc) {
        return new QuadCurve(qc.getStartX(), qc.getStartY(), qc.getControlX(), qc.getControlY(),
                qc.getEndX(), qc.getEndY());
    }
}
