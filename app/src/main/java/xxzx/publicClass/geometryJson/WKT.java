package xxzx.publicClass.geometryJson;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mulin on 2015/11/8.
 */
public class WKT {

    /**
     * 点 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getPOINTWktToJson(String wkt, int wkid) {

        String[] strHead = wkt.split("\\(");
        String strContent = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strResult = strContent.split(" ");

        PointObject pointObject = new PointObject();
        pointObject.setX(Double.parseDouble(strResult[0]));
        pointObject.setY(Double.parseDouble(strResult[1]));

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        pointObject.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(pointObject);

    }

    /**
     * 多点 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getMULTIPOINTWktToJson(String wkt, int wkid) {

        MultiIPointObject multiIPointObject = new MultiIPointObject();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(\\(");
        String strMiddle = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strMiddles = strMiddle.split(",");

        List<Double[]> list = new ArrayList<Double[]>();

        for (int i = 0; i < strMiddles.length; i++) {

            if (i == 0) {

                String item = strMiddles[i].substring(0,
                        strMiddles[i].length() - 1);
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            } else if (i == strMiddles.length) {

                String item = strMiddles[i]
                        .substring(1, strMiddles[i].length());
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            } else {

                String strItem = strMiddles[i].trim();
                String item = strItem.substring(1, strItem.length() - 1);
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            }

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        multiIPointObject.setPoints(list);
        multiIPointObject.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(multiIPointObject);

    }

    /**
     * 线 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getLINESTRINGWktToJson(String wkt, int wkid) {

        LineStringObject lineStringObject = new LineStringObject();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();
        List<Double[]> list = new ArrayList<Double[]>();

        String[] strHead = wkt.split("\\(");
        String strContent = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strResult = strContent.split(",");

        for (int i = 0; i < strResult.length; i++) {

            String itme = strResult[i].trim();
            String[] items = itme.split(" ");
            Double[] listResult = new Double[]{Double.parseDouble(items[0]),
                    Double.parseDouble(items[1])};
            list.add(listResult);

        }

        lists.add(list);

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        lineStringObject.setPaths(lists);
        lineStringObject.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(lineStringObject);

    }

    /**
     * 多线 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getMULTILINESTRINGWktToJson(String wkt, int wkid) {

        MultLinesStringObject lineStringObject = new MultLinesStringObject();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);

        String[] strList = strHead[1].split("\\),\\(");

        for (int i = 0; i < strList.length; i++) {

            String item = strList[i].trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");

            List<Double[]> list = new ArrayList<Double[]>();

            for (int j = 0; j < items.length; j++) {

                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");

                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};

                list.add(listResult);

            }

            lists.add(list);

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        lineStringObject.setRings(lists);
        lineStringObject.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(lineStringObject);

    }


    /**
     * 面 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getPOLYGONWktToJson(String wkt, int wkid) {

        PolygonObject polygonObject = new PolygonObject();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);

        String[] strList = strHead[1].split("\\), \\(");

        for (int i = 0; i < strList.length; i++) {

            String item = strList[i].trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");

            List<Double[]> list = new ArrayList<Double[]>();

            for (int j = 0; j < items.length; j++) {

                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");

                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};

                list.add(listResult);

            }

            lists.add(list);

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        polygonObject.setRings(lists);
        polygonObject.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(polygonObject);
    }


    /**
     * 多面 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    public static String getMULTIPOLYGONWktToJson(String wkt, int wkid) {

        PolygonObject polygonObject = new PolygonObject();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);
        ToTailWkt = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strHeads = ToTailWkt.split("\\(", 2);

        String[] strList = strHeads[1].split("\\), \\(");

        if (strList.length == 1) {

            for (int i = 0; i < strList.length; i++) {

                String item = strList[i].trim();
                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");

                List<Double[]> list = new ArrayList<Double[]>();

                for (int j = 0; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");

                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};

                    list.add(listResult);

                }

                lists.add(list);

            }

        } else {

            for (int i = 0; i < strList.length; i++) {

                String item = strList[i].trim();
                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");

                List<Double[]> list = new ArrayList<Double[]>();

                for (int j = 1; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");

                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};

                    list.add(listResult);
                }
                lists.add(list);
            }

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        polygonObject.setRings(lists);
        polygonObject.setSpatialReference(spatialReference);
        Gson gson = new Gson();
        return gson.toJson(polygonObject);
    }


    /**
     * 將geometryJson轉換為wkt
     *
     * @param geometryJson
     * @return
     */
    public static String PolygonGeometryJsonToWKT(String geometryJson) {
        try {
            JSONObject json = new JSONObject(geometryJson);
            JSONArray rings = json.getJSONArray("rings");
            StringBuilder wkt = new StringBuilder("POLYGON(");
            for (int i = 0; i < rings.length(); i++) {//遍历JSONArray
                StringBuilder builder = new StringBuilder("(");
                JSONArray ring = rings.getJSONArray(i);
                for (int j = 0; j < ring.length(); j++) {//遍历JSONArray
                    JSONArray pts = ring.getJSONArray(j);
                    String x = pts.getString(0);
                    String y = pts.getString(1);
                    builder.append(x.toString());
                    builder.append(" ");
                    builder.append(y.toString());
                    if (j < ring.length() - 1) {
                        builder.append(",");
                    }
                }
                builder.append(")");
                wkt.append(builder);
            }
            return wkt.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * @param geometryJson
     */
    public static String PointGeometryJsonToWKT(String geometryJson) {
        try {
            JSONObject json = new JSONObject(geometryJson);
            StringBuilder wkt = new StringBuilder("POINT(");
            wkt.append(json.get("x").toString());
            wkt.append(" ");
            wkt.append(json.get("y").toString());
            return wkt.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 將geometryJson轉換為wkt
     *
     * @param geometryJson
     * @return
     */
    public static String PolylineGeometryJsonToWKT(String geometryJson) {
        try {
            JSONObject json = new JSONObject(geometryJson);
            JSONArray rings = json.getJSONArray("paths");
            StringBuilder wkt = new StringBuilder("LINESTRING(");
            for (int i = 0; i < rings.length(); i++) {//遍历JSONArray
                StringBuilder builder = new StringBuilder("(");
                JSONArray ring = rings.getJSONArray(i);
                for (int j = 0; j < ring.length(); j++) {//遍历JSONArray
                    JSONArray pts = ring.getJSONArray(j);
                    String x = pts.getString(0);
                    String y = pts.getString(1);
                    builder.append(x.toString());
                    builder.append(" ");
                    builder.append(y.toString());
                    if (j < ring.length() - 1) {
                        builder.append(",");
                    }
                }
                builder.append(")");
                wkt.append(builder);
            }
            return wkt.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String PointListToPointWKT(List<LatLng> pts) {
        if (pts.size() == 0) {
            return "";
        } else {
            try {
                StringBuilder wkt = new StringBuilder("POINT(");
                wkt.append(String.valueOf(pts.get(0).longitude));
                wkt.append(" ");
                wkt.append(String.valueOf(pts.get(0).latitude));
                wkt.append(")");
                return wkt.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    public static String PointListToPointWKT(LatLng latlng) {
        try {
            StringBuilder wkt = new StringBuilder("POINT(");
            wkt.append(String.valueOf(latlng.longitude));
            wkt.append(" ");
            wkt.append(String.valueOf(latlng.latitude));
            wkt.append(")");
            return wkt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String PointListToPolylineWKT(List<LatLng> pts) {
        try {
            StringBuilder wkt = new StringBuilder("LINESTRING(");
            for (int i = 0; i < 1; i++) {//遍历JSONArray
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < pts.size(); j++) {
                    String x = String.valueOf(pts.get(j).longitude);
                    String y = String.valueOf(pts.get(j).latitude);
                    builder.append(x.toString());
                    builder.append(" ");
                    builder.append(y.toString());
                    if (j < pts.size() - 1) {
                        builder.append(",");
                    }
                }
                //builder.append(")");
                wkt.append(builder);
            }
            wkt.append(")");
            return wkt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String PointListToPolygonWKT(List<LatLng> pts) {
        try {
            StringBuilder wkt = new StringBuilder("POLYGON(");
            for (int i = 0; i < 1; i++) {//遍历JSONArray
                StringBuilder builder = new StringBuilder("(");
                for (int j = 0; j < pts.size(); j++) {//遍历JSONArray
                    String x = String.valueOf(pts.get(j).longitude);
                    String y = String.valueOf(pts.get(j).latitude);
                    builder.append(x.toString());
                    builder.append(" ");
                    builder.append(y.toString());
                    builder.append(",");
                }

                builder.append(String.valueOf(pts.get(0).longitude));
                builder.append(" ");
                builder.append(String.valueOf(pts.get(0).latitude));

                builder.append(")");
                wkt.append(builder);
            }
            wkt.append(")");
            return wkt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public  static List<LatLng> wktToPtsList(String wkt){
        List<LatLng> list = new ArrayList<>();
        if(wkt.contains("POINT")){
            return POINTWktToPtsList(wkt);
        }else if(wkt.contains("LINESTRING")){
            return POLYLINEWktToPtsList(wkt);
        }else if(wkt.contains("POLYGON")){
            return POLYGONWktToPtsList(wkt);
        }else{
            return list;
        }
    }


    public static List<LatLng> POINTWktToPtsList(String wkt) {
        List<LatLng> list = new ArrayList<>();
        try {
            String[] strHead = wkt.split("\\(");
            String strContent = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strResult = strContent.split(" ");

            LatLng latlng = new LatLng(Double.parseDouble(strResult[1]), Double.parseDouble(strResult[0]));
            list.add(latlng);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public static List<LatLng> POLYLINEWktToPtsList(String wkt) {
        List<LatLng> list = new ArrayList<LatLng>();
        try {
            String[] strHead = wkt.split("\\(");
            String strContent = strHead[1].substring(0, strHead[1].length() - 1);
            String[] strResult = strContent.split(",");

            for (int i = 0; i < strResult.length; i++) {

                String itme = strResult[i].trim();
                String[] items = itme.split(" ");
                LatLng latlng = new LatLng(Double.parseDouble(items[1]), Double.parseDouble(items[0]));
                list.add(latlng);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return list;
        }
    }


    public static List<List<LatLng>> MUTILINEWktToPtsList(String wkt) {
        List<List<LatLng>> list = new ArrayList<>();
        try {
            String ToTailWkt = wkt.substring(0, wkt.length() - 3);
            String[] strHead = ToTailWkt.split("\\(\\(");

            String[] strList = strHead[1].split("\\), \\(");

            for (int i = 0; i < strList.length; i++) {

                String item = strList[i].trim();
//                if(i==0) {
//                    item = item.substring(1, item.length());
//                }else if(i==strList.length-1){
//                    item = item.substring(0, item.length()-1);
//                }else{
//                    item = item.substring(0, item.length());
//                }

                String[] items = item.split(",");

                List<LatLng> li = new ArrayList<LatLng>();
                for (int j = 0; j < items.length; j++) {

                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");

                    LatLng latlng = new LatLng(Double.parseDouble(jItems[1]), Double.parseDouble(jItems[0]));
                    li.add(latlng);
                }

                list.add(li);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return list;
        }
    }


    public static List<LatLng> POLYGONWktToPtsList(String wkt) {

        List<LatLng> list = new ArrayList<LatLng>();
        try {
        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);

        String[] strList = strHead[1].split("\\), \\(");

        for (int i = 0; i < strList.length; i++) {

            String item = strList[i].trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");

            for (int j = 0; j < items.length; j++) {

                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");

                LatLng latlng = new LatLng(Double.parseDouble(jItems[1]), Double.parseDouble(jItems[0]));
                list.add(latlng);
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return list;
        }
    }


}
