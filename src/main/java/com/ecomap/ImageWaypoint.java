package com.ecomap;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.image.BufferedImage;

public class ImageWaypoint extends DefaultWaypoint {
    private final BufferedImage image;

    public ImageWaypoint(GeoPosition pos, BufferedImage image) {
        super(pos);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }
}
