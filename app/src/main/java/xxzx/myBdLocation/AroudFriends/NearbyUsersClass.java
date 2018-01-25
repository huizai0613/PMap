package xxzx.myBdLocation.AroudFriends;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import xxzx.login.HttpRequestResult;
import xxzx.myBdLocation.GDLocationUtils;
import xxzx.myBdLocation.MyGdLocationClass;
import xxzx.myOverlay.NearbyUsersOverlay;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;

/**
 * Created by Lenovo on 2017/6/19.
 */

public class NearbyUsersClass {
    //距离，单位米
    private int nearDistance =MyString.nearDistance;
    //时间，单位分钟
    private int nearTime =MyString.nearTime;

    private Context mContext;

    private NearbyUsersOverlay nearbyUsersOverlay;

    private LoadingDialog loadingDialog;

    public NearbyUsersClass(Context context){
        this.mContext = context;
    }


    public void Switch(){

        if(this.nearbyUsersOverlay!=null){
            if(this.nearbyUsersOverlay.getMarkerCount()!=0){
                this.remove();
                return;
            }
        }

        this.download();
    }


    private void download(){

        MySingleClass mySingleClass = MySingleClass.getInstance();
        AMapLocation location = mySingleClass.getMyGdLocation();

        if(location==null){
            ToastUtil.show(this.mContext,"当前没有定位信息，不能查询附近人员");
            return;
        }

        if(this.loadingDialog ==null){
            this.loadingDialog = new LoadingDialog(mContext);
        }

        this.loadingDialog.show();
        MyRunnable myRunnable = new MyRunnable();
        myRunnable.setData(String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()),nearDistance,nearTime);

        new Thread(myRunnable).start();
    }


    private void remove(){
        if(this.nearbyUsersOverlay != null){
            this.nearbyUsersOverlay.removeFromMap();
        }
    }


    private void addUserInfoInMap(String json){
        try {
            JSONArray data = new JSONArray(json);

            List<NbUserInfo> mUserList = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject userObject = data.getJSONObject(i);
                NbUserInfo nbUserInfo = new NbUserInfo();

                nbUserInfo.name = userObject.getString("TrueName");
                nbUserInfo.telephone = userObject.getString("Mobile");

                nbUserInfo.location = new LatLng(userObject.getDouble("ey"),userObject.getDouble("ex"));

                mUserList.add(nbUserInfo);
            }

            if(mUserList.size()==0){
                ToastUtil.show(this.mContext,"附近没有巡查人员在线");
                return;
            }

            if(this.nearbyUsersOverlay == null){
                this.nearbyUsersOverlay = new NearbyUsersOverlay();
            }

            this.nearbyUsersOverlay.setmNbUsers(mUserList);
            AMap amap = MySingleClass.getInstance().getBaseMapViewClass().getMapView().getMap();
            this.nearbyUsersOverlay.addToMap(amap);

            //缩放到范围
            List<LatLng> pois = this.nearbyUsersOverlay.pois;

            AMapLocation location = MySingleClass.getInstance().getMyGdLocation();
            if(location!=null){
                pois.add(new LatLng(location.getLatitude(),location.getLongitude()));
            }

            GdMapTool.zoomToSpan(amap,pois);

        }catch (JSONException e){
            ToastUtil.show(this.mContext,e.toString());
        }
    }


    /**
     * 查询服务端进程
     */
    public class MyRunnable implements Runnable {
        private String lon ;
        private String lat;
        //距离，单位米
        private int nearDistance ;
        //时间，单位分钟
        private int nearTime ;

        public void setData(String lon, String lat,int distace,int minute) {
            this.lon = lon;
            this.lat = lat;
            this.nearDistance = distace;
            this.nearTime = minute;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();
            try {
                if (mySingleClass.getUser() != null) {

                    String url = properties.get("url_nearby_users").toString();

                    String httpPath = String.format(url,this.nearTime,this.nearDistance, this.lon, this.lat);

                    String result = MyHttpRequst.getHttpGetRequst2(httpPath);

                    Message msg = new Message();

                    if(TextUtils.isEmpty(result)){
                        msg.what= HttpRequestResult.ERROR;
                    }else {
                        msg.what= HttpRequestResult.SUCCESS;
                        msg.obj= result;
                    }
                    mHandler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {

            loadingDialog.dismiss();
            if(msg.what == HttpRequestResult.ERROR){
                ToastUtil.show(mContext,"查询错误,请确认网络是连接");
            }else if(msg.what == HttpRequestResult.SUCCESS){
                addUserInfoInMap((String) msg.obj);
            }
        }
    };





}
