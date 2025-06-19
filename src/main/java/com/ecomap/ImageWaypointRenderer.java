package com.ecomap;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ImageWaypointRenderer implements WaypointRenderer<ImageWaypoint> {

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, ImageWaypoint wp) {
        GeoPosition pos = wp.getPosition();
        Point2D point = map.getTileFactory().geoToPixel(pos, map.getZoom());

        BufferedImage img = wp.getImage();

        // ESCALAR a imagem (exemplo: 32x32 px)
        int desiredSize = 70; // ou 24 se você quiser ainda menor
        Image scaledImg = img.getScaledInstance(desiredSize, desiredSize, Image.SCALE_SMOOTH);

        int x = (int) point.getX() - desiredSize / 2;
        int y = (int) point.getY() - desiredSize / 2;

        g.drawImage(scaledImg, x, y, null);


}
}
