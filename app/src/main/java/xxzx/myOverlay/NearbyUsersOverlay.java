package xxzx.myOverlay;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.myBdLocation.AroudFriends.NbUserInfo;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;

/**
 * Created by ch on 2016/2/9.
 */
public class NearbyUsersOverlay extends MyBaseOverlay {

    //记录图标
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    //记录标注数据
    private ArrayList<Text> mPoiTexts = new ArrayList<Text>();

    private List<NbUserInfo> mUserList;

    private String OverlayName="NearbyUsersOverlay";

    public List<LatLng> pois = new ArrayList<>();

    public NearbyUsersOverlay() {

    }

    /**
     * 传入点数据
     * @param users
     */
    public void setmNbUsers(List<NbUserInfo> users){
        this.mUserList = users;
    }



    @Override
    public void addToMap(AMap amap) {
        //首先移除该图层上的所有信息
        this.removeFromMap();

        //添加点
        for (int i = 0; i < mUserList.size(); i++) {

            Marker marker = amap.addMarker(new MarkerOptions().position(mUserList.get(i).location)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_nearby_user))
                    .title(MyString.maker_title_nearby_user)
                    .snippet("DefaultMarker"));

            marker.setObject(mUserList.get(i));
            mPoiMarks.add(marker);

            Text text = amap.addText(GdGeometryOptions.getTextOptions(mUserList.get(i).location, mUserList.get(i).name));
            text.setFontSize(36);
            text.setBackgroundColor(Color.WHITE);
            text.setFontColor(Color.RED);
            text.setAlign(Text.ALIGN_LEFT, Text.ALIGN_TOP);

            mPoiTexts.add(text);

            pois.add(mUserList.get(i).location);

        }
    }


    public int getMarkerCount(){
        return mPoiMarks.size();
    }

    @Override
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }

        for (Text text : mPoiTexts) {
            text.remove();
        }

        mPoiMarks.clear();
        mPoiTexts.clear();

    }


    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

}
