package xxzx.publicClass.geometryJson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mulin on 2015/11/8.
 */
public class MultLinesStringObject {

    private List<List<Double[]>> rings;
    private HashMap<String, Integer> spatialReference;

    public List<List<Double[]>> getRings() {
        return rings;
    }

    public void setRings(List<List<Double[]>> rings) {
        this.rings = rings;
    }

    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}
