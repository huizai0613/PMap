package xxzx.spatialite.TableStruct;

/**
 * Created by ch on 2016/4/1.
 */
public class PoleDangerTableDataClass extends BaseDangerTableDataClass {

    public String PoleName = "";// 所属杆塔名称
    public int PoleObjectId = -1; //所属杆塔编号

    public String getPoleName() {
        return PoleName;
    }

    public void setPoleName(String poleName) {
        PoleName = poleName;
    }

    public int getPoleObjectId() {
        return PoleObjectId;
    }

    public void setPoleObjectId(int poleObjectId) {
        PoleObjectId = poleObjectId;
    }
}
