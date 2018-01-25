package xxzx.publicClass.geometryJson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mulin on 2015/11/8.
 */
public class MultiIPointObject {

    private List<Double[]> points;
    private HashMap<String, Integer> spatialReference;

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }

    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
