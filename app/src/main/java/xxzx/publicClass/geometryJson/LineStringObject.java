package xxzx.publicClass.geometryJson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mulin on 2015/11/8.
 */
public class LineStringObject {
    private List<List<Double[]>> paths;
    private HashMap<String, Integer> spatialReference;

    public List<List<Double[]>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<Double[]>> paths) {
        this.paths = paths;
    }

    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
