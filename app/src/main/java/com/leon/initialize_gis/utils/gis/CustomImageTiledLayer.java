package com.leon.initialize_gis.utils.gis;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ImageTiledLayer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomImageTiledLayer extends ImageTiledLayer {
    private String mainURL = "";

    public CustomImageTiledLayer(TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
    }

    public String getMainURL() {
        return mainURL;
    }

    public void setMainURL(String mainURL) {
        this.mainURL = mainURL;
    }

    @Override
    protected byte[] getTile(TileKey tileKey) {
        int level = tileKey.getLevel();
        int col = tileKey.getColumn();
        int row = tileKey.getRow();
        String requestUrl = mainURL + "&TILECOL=" + col + "&TILEROW=" + row + "&TILEMATRIX=" + (level);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            return getBytes(is);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        return bos.toByteArray();
    }
}
