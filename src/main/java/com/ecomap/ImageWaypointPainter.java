package com.ecomap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.painter.Painter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Set;

public class ImageWaypointPainter implements Painter<JXMapViewer> {
    private Set<ImageWaypoint> waypoints;
    private WaypointRenderer<ImageWaypoint> renderer;

    public void setWaypoints(Set<ImageWaypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public void setRenderer(WaypointRenderer<ImageWaypoint> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        if (waypoints == null || renderer == null) return;

        for (ImageWaypoint wp : waypoints) {
            renderer.paintWaypoint(g, map, wp);
        }
    }
}
