package com.ecomap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;

import java.awt.*;

public interface WaypointRenderer<T extends Waypoint> {
    void paintWaypoint(Graphics2D g, JXMapViewer map, T wp);
}
