package xxzx.spatialite.TableStruct;

/**
 * Created by ch on 2016/4/1.
 */
public class ChannelDangerTableDataClass extends BaseDangerTableDataClass{

    public String ChannelName = "";// 所属通道名称
    public int ChannelObjectId = -1; //所属通道编号

    public String Geometry="";


    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public int getChannelObjectId() {
        return ChannelObjectId;
    }

    public void setChannelObjectId(int channelObjectId) {
        ChannelObjectId = channelObjectId;
    }

    public String getGeometry() {
        return Geometry;
    }

    public void setGeometry(String geometry) {
        Geometry = geometry;
    }
}
