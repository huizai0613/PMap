package xxzx.myData.dataInput;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.amap.api.maps.model.LatLng;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by ch on 2016/1/18.
 */
public class DataKmlPointClass extends BasePointClass {

    //public Polyline polyline;//线数据

    public DataKmlPointClass(String folderPath,String fileName,Context mContext){
        super(folderPath,fileName,mContext);
    }
    /**
     * 读取txt文件
     * @param path
     */
    @Override
    public void readData(String path)
    {
        try {
            InputStream instream = new FileInputStream(path);

            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(instream, "UTF-8"); //为Pull解释器设置要解析的XML数据
            int event = pullParser.getEventType();

            boolean isLabel=false;
            while (event != XmlPullParser.END_DOCUMENT){
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        //persons = new ArrayList<Person>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Placemark".equals(pullParser.getName())){
                            isLabel=true;
                        }
                        if ("name".equals(pullParser.getName())){
                            String name = pullParser.nextText();
                            //存储标签
                            if(isLabel){
                                ptLabels.add(name);
                            }
                        }
                        if ("coordinates".equals(pullParser.getName())){
                            String ss = String.valueOf(pullParser.nextText());

                            String[] strArray=ss.split(",");
                            if(strArray.length==3){
                                //经纬度转化为墨卡托平面坐标
                                LatLng pt=new LatLng(Double.valueOf(strArray[1]),Double.valueOf(strArray[0]));
                                this.Pts.add( this.convert84togcj02(pt));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("Placemark".equals(pullParser.getName())){
                            isLabel=false;
                        }
                        break;
                }
                event = pullParser.next();
            }

            //填充数据
            this.getPoleTableRow();
            this.getChannelTableRow();
            this.getPoleTableRow();
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("InputDataTxtItemView", "The File doesn't not exist.");
        }
        catch (XmlPullParserException e){
            Log.d("InputDataTxtItemView", e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("InputDataTxtItemView", e.getMessage());
        }
    }

}
