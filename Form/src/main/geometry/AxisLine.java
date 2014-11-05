package main.geometry;

import java.util.HashMap;

/**
 * Created by chrno on 10/20/14.
 */
public class AxisLine extends Line {
    private String description;
    private Point descriptionPoint;

    public AxisLine(String description, Point descriptionPoint) {
        this.description = description;
        this.descriptionPoint = descriptionPoint;
    }

    public AxisLine(double x1, double y1, double x2, double y2, String description, Point descriptionPoint) {
        super(x1, y1, x2, y2);
        this.description = description;
        this.descriptionPoint = descriptionPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getDescriptionPoint() {
        return descriptionPoint;
    }

    public void setDescriptionPoint(Point descriptionPoint) {
        this.descriptionPoint = descriptionPoint;
    }
}
