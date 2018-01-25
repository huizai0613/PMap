package xxzx.myData.dataInput;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ch on 2016/1/18.
 */
public class DataTxtPointClass extends BasePointClass {

    public DataTxtPointClass(String folderPath,String fileName,Context mContext){
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
            if (instream != null)
            {
                InputStreamReader inputreader = new InputStreamReader(instream,"GB2312");
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    this.getPointInfo(line.trim());
                }
                instream.close();
            }

            //将点转化为线
//            this.polyline = GeometryTool.CreatePolylineFromPoints(this.Pts);
//            this.envelope=new Envelope();
//            ((Geometry)this.polyline).queryEnvelope(envelope);

            //填充数据
            this.getPoleTableRow();
            this.getChannelTableRow();
            this.getPoleTableRow();
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("InputDataTxtItemView", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("InputDataTxtItemView", e.getMessage());
        }
    }


    /**
     * 解析txt文件的每行数据
     * @param line
     */
    private void getPointInfo(String line){
        if(!line.equals("")){
            String [] strArray = line.split("\\s+");
            if(strArray.length==3){
                //标签
                ptLabels.add(strArray[0].trim());
                LatLng pt=new LatLng(Double.valueOf(strArray[2]),Double.valueOf(strArray[1]));
                //将84坐标转换gcj02
                this.Pts.add( this.convert84togcj02(pt));
            }
        }
    }










}
