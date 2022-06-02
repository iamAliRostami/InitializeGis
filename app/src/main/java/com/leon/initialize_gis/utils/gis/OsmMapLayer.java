package com.leon.initialize_gis.utils.gis;

import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.leon.initialize_gis.enums.MapType;
import com.leon.initialize_gis.interfaces.IMapLayer;

import java.util.Arrays;
import java.util.List;

public class OsmMapLayer implements IMapLayer {
    private static final List<String> SUB_DOMAINS = Arrays.asList("a", "b", "c");

    @Override
    public String getMapUrl(TileKey tileKey) {

        return null;
    }

    @Override
    public WebTiledLayer createLayer(MapType layerType) {
        return null;
    }

    @Override
    public WebTiledLayer createLayer() {
        // build the web tiled layer from stamen
        // use web tiled layer as Basemap
        return new WebTiledLayer("http://{subDomain}.tile.openstreetmap.org/{level}/{col}/{row}.png", SUB_DOMAINS);
//        return new WebTiledLayer("http://37.191.92.152:7080/PBS/rest/services/GoogleMapsRoad/MapServer/");
    }
}
