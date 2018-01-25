package xxzx.myData.dataInput;

import android.content.Context;

import com.amap.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ReadFileClass;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/1/18.
 */
public class DataJsonPointClass extends BasePointClass {



    public DataJsonPointClass(String folderPath, String fileName, Context mContext){
        super(folderPath,fileName,mContext);
    }

    /**
     * 读取txt文件
     * @param path
     */
    @Override
    protected void readData(String path) {
        String currentTime = sdf.format(new Date());

        try {
            String value = ReadFileClass.getString(path);
            JSONObject jsonObject = new JSONObject(value);

            //JSONArray dataArray = jsonObject.getJSONArray("data");

            //线路表
            if (!jsonObject.isNull("paths")) {
                JSONObject plinePathsObject = jsonObject.getJSONObject("paths");
                JSONObject geometryObject = plinePathsObject.getJSONObject("geometry");
                JSONArray pathsArray = geometryObject.getJSONArray("paths");


                StringBuilder multilineString = new StringBuilder("MULTILINESTRING(");

                //每个paths
                for (int i = 0; i < pathsArray.length(); i++) {

                    JSONArray poisArray = pathsArray.getJSONArray(i);

                    StringBuilder pathString = new StringBuilder("(");

                    for (int j = 0; j < poisArray.length(); j++) {
                        JSONArray latlngArray = poisArray.getJSONArray(j);
                        String lngObject = (String) latlngArray.getString(0);
                        String latObject = (String) latlngArray.getString(1);

                        pathString.append(lngObject.toString());
                        pathString.append(" ");
                        pathString.append(latObject.toString());

                        if (j >= poisArray.length() - 1) {
                            pathString.append(")");
                        } else {
                            pathString.append(",");
                        }
                    }

                    multilineString.append(pathString);

                    if (i >= pathsArray.length() - 1) {
                        multilineString.append(")");
                    } else {
                        multilineString.append(",");
                    }
                }

                plineTableDataClass = new PowerlineTableDataClass();

                plineTableDataClass.setPowerName(this.powerName);
                String username = MySingleClass.getInstance().getUser().getmName();
                plineTableDataClass.setUserName(username);
                plineTableDataClass.setIsSelect(0);
                plineTableDataClass.setDateTime(currentTime);
                plineTableDataClass.setGeometry(multilineString.toString());
            }


            //杆塔表
            if (!jsonObject.isNull("ganta")) {
                JSONArray gantaArray = jsonObject.getJSONArray("ganta");
                for (int i = 0; i < gantaArray.length(); i++) {
                    JSONObject gantaObject = gantaArray.getJSONObject(i);
                    String polename = gantaObject.getString("name");
                    String polegeo = gantaObject.getString("geo");
                    int poleobjectid = gantaObject.getInt("id");
                    int gtcounts = gantaObject.isNull("gtcounts") ? 0 : gantaObject.getInt("gtcounts");


                    PoleTableDataClass pole = new PoleTableDataClass();
                    pole.setDateTime(currentTime);
                    pole.setGeometry(polegeo);
                    pole.setPowerName(this.powerName);
                    pole.setIsSelect(0);
                    pole.setPoleName(polename);
                    pole.setPoleNumber(i);
                    pole.setPoleObjectId(poleobjectid);
                    pole.setDangerCount(gtcounts);

                    this.poleList.add(pole);
                }

            }

            //通道表
            if (!jsonObject.isNull("tongdao")) {
                JSONArray tongdaoArray = jsonObject.getJSONArray("tongdao");
                for (int i = 0; i < tongdaoArray.length(); i++) {

                    JSONObject tongdaoObject = tongdaoArray.getJSONObject(i);

                    String chnname = tongdaoObject.getString("name");
                    String chngeo = tongdaoObject.getString("geo");
                    int chnobjectid = tongdaoObject.getInt("id");
                    int gtcounts = tongdaoObject.isNull("tdcounts") ? 0 : tongdaoObject.getInt("tdcounts");


                    ChannelTableDataClass chn = new ChannelTableDataClass();
                    chn.setDateTime(currentTime);
                    chn.setGeometry(chngeo);
                    chn.setPowerName(this.powerName);
                    chn.setIsSelect(0);
                    chn.setChannelName(chnname);
                    chn.setChannelNumber(i);
                    chn.setChannelObjectId(chnobjectid);
                    chn.setDangerCount(gtcounts);

                    this.chnList.add(chn);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
