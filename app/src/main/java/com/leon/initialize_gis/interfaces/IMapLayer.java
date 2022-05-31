package com.leon.initialize_gis.interfaces;

import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.leon.initialize_gis.enums.MapType;

public interface IMapLayer {
    String getMapUrl(TileKey tileKey);

    WebTiledLayer createLayer(MapType layerType);

    WebTiledLayer createLayer();
}
