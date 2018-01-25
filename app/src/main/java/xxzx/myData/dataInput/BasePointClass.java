package xxzx.myData.dataInput;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/1/18.
 */
public abstract class BasePointClass {
    public String powerName;
    protected List<LatLng> Pts;
    protected List<Integer> tdCountsList;
    protected List<Integer> gtCountsList;
    protected ArrayList<String> ptLabels;
    protected Context mContext;

    public PowerlineTableDataClass plineTableDataClass = null;
    public List<PoleTableDataClass> poleList = null;
    public List<ChannelTableDataClass> chnList = null;


    //通过SimpleDateFormat获取24小时制时间
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public BasePointClass(String folderPath,String fileName, Context mContext) {
        this.mContext = mContext;

        this.Pts = new ArrayList<>();
        this.ptLabels = new ArrayList<>();
        this.poleList = new ArrayList<>() ;
        this.chnList = new ArrayList<>() ;
        this.tdCountsList = new ArrayList<>();
        this.gtCountsList = new ArrayList<>();

        String path = folderPath + "/" + fileName;
        this.powerName = getFileName(fileName);
        this.readData(path);
    }

    protected LatLng convert84togcj02(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter(mContext);
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    /**
     * 获得线路名称，去掉后缀
     *
     * @param filename
     * @return
     */
    private String getFileName(String filename) {
        int end = filename.lastIndexOf(".");
        if (end != -1) {
            return filename.substring(0, end).trim();
        } else {
            return null;
        }

    }

    protected PowerlineTableDataClass getPowerlineTableRow() {
        plineTableDataClass = new PowerlineTableDataClass();

        String currentTime = sdf.format(new Date());

        plineTableDataClass.setPowerName(this.powerName);
        String username = MySingleClass.getInstance().getUser().getmName();
        plineTableDataClass.setUserName(username);
        plineTableDataClass.setIsSelect(0);
        plineTableDataClass.setDateTime(currentTime);
        plineTableDataClass.setGeometry("");

        return plineTableDataClass;
    }

    protected List<PoleTableDataClass> getPoleTableRow(){

        poleList = new ArrayList<>() ;
        String currentTime = sdf.format(new Date());

        for(int i=0;i<Pts.size();i++){
            PoleTableDataClass pole=new PoleTableDataClass();
            pole.setDateTime(currentTime);
            pole.setGeometry(WKT.PointListToPointWKT(Pts.get(i)));
            pole.setPowerName(this.powerName);
            pole.setIsSelect(0);
            pole.setPoleName(this.ptLabels.get(i));
            pole.setPoleNumber(i);
            pole.setDangerCount(gtCountsList.get(i));
            poleList.add(pole);
        }

        return poleList;
    }


    protected List<ChannelTableDataClass> getChannelTableRow(){

        chnList = new ArrayList<>() ;
        List<LatLng> linePts = new ArrayList<LatLng>();

        String currentTime = sdf.format(new Date());

        if(Pts.size()<2){
            ToastUtil.show(mContext,"该线路杆塔数小于2，不存在通道！");
        }

        for(int i=0;i<Pts.size()-1;i++){
            ChannelTableDataClass channel = new ChannelTableDataClass();
            channel.setDateTime(currentTime);

            linePts.clear();
            linePts.add(Pts.get(i));
            linePts.add(Pts.get(i + 1));

            channel.setGeometry(WKT.PointListToPolylineWKT(linePts));
            channel.setIsSelect(0);
            channel.setPowerName(this.powerName);

            String pre=this.ptLabels.get(i).substring(this.powerName.length());
            String next=this.ptLabels.get(i + 1).substring(this.powerName.length());
            channel.setChannelName(this.powerName + pre + "-" + next);
            channel.setChannelNumber(i);
            channel.setDangerCount(tdCountsList.get(i));

            chnList.add(channel);
        }
        return chnList;
    }


    protected abstract void readData(String path);

}
