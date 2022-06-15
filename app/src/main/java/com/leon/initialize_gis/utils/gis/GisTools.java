package com.leon.initialize_gis.utils.gis;

import static com.leon.initialize_gis.helpers.MyApplication.getContext;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

public class GisTools {

    public static Graphic createGraphicTextPoint(final double latitude, final double longitude, String text) {

        final Point textPoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        final TextSymbol symbol = new TextSymbol(14, text, Color.rgb(230, 0, 0),
                TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.TOP);
        return new Graphic(textPoint, symbol);
    }

    public static Graphic createGraphicPicturePoint(final double latitude, final double longitude, int id) {
        final BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), id);
        final PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        final Point graphicPoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        return new Graphic(graphicPoint, symbol);
    }

    public static Graphic createGraphicPicturePoint(final Point graphicPoint, int id) {
        final BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), id);
        final PictureMarkerSymbol symbol = new PictureMarkerSymbol(drawable);
        return new Graphic(graphicPoint, symbol);
    }

    public static Point getPoint(final Point point) {
        final String latLong = String.valueOf(CoordinateFormatter.toLatitudeLongitude(point,
                CoordinateFormatter.LatitudeLongitudeFormat.DECIMAL_DEGREES, 10));
        return new Point(CoordinateFormatter.fromLatitudeLongitude(latLong, null).getX(),
                CoordinateFormatter.fromLatitudeLongitude(latLong, null).getY());
    }
}
