package xxzx.HttpJsonReadClass;

import android.content.Context;
import android.util.SparseIntArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;

/**
 * Created by ch on 2017/1/16.
 */
public class JsonChnDanger {
    private Context mContext;
    private String plineName;
    private String chnName;
    private ChannelDangerTableOpt chnDangerTableOpt;
    private DangerSgTableOpt dangerSgTableOpt;

    public JsonChnDanger(Context context,String plineName,String chnName) {
        this.mContext=context;
        this.plineName = plineName;
        this.chnName = chnName;
        this.chnDangerTableOpt = new ChannelDangerTableOpt();
        this.dangerSgTableOpt = new DangerSgTableOpt();
    }

    public void readDangerJson(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String message = json.getString("message");

            if (message.equals("succeed")) {
                JSONArray imagenum = json.getJSONArray("imagenum");
                JSONArray data = json.getJSONArray("data");
                //施工详细信息
                JSONArray sg = json.getJSONArray("shigong");
                //获得隐患点的图片数量
                List<Integer> imgNumList = this.getImgNumList(imagenum);

                //创建事务
                this.chnDangerTableOpt.BEGIN();
                //获得keyid与rowid对应的关系，用于细节存储
                SparseIntArray keyid_Rowid = this.checkInsertDangerTable(data, imgNumList);
                boolean result_boolean = this.checkInsertDangerSgTable(sg, keyid_Rowid);
                if(result_boolean){
                    //完成事务
                    this.chnDangerTableOpt.COMMIT();
                    ToastUtil.show(this.mContext, "写入数据库完成！");
                }else{
                    //回滚事务
                    this.chnDangerTableOpt.ROLLBACK();
                    ToastUtil.show(this.mContext,"写入数据库错误，回滚事务！");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得图片数量列表
     *
     * @param data
     * @return
     */
    private List<Integer> getImgNumList(JSONArray data) {
        List<Integer> imgNumList = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); i++) {
                imgNumList.add(data.getJSONObject(i).getInt("ImageNum"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return imgNumList;
        }

    }

    /**
     * 插入隐患表
     *
     * @param data
     * @param imgnumlist
     * @return
     */
    private SparseIntArray checkInsertDangerTable(JSONArray data, List<Integer> imgnumlist) {
        //获得keyid与rowid对应的关系，用于细节存储
        SparseIntArray keyid_Rowid = new SparseIntArray();

        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject danger = data.getJSONObject(i);

                ChannelDangerTableDataClass tableDataClass = new ChannelDangerTableDataClass();
                tableDataClass.setDangerType(danger.getInt("Type"));
                //tableDataClass.setVersion(0);
                tableDataClass.setDangerMark("");
                tableDataClass.setSpotMark(danger.getString("SpotMarks") == null ? "" : danger.getString("SpotMarks"));
                tableDataClass.setDateTime(danger.getString("Checkdate").replace("T", " "));
                tableDataClass.setDangerLevel(danger.getInt("State"));
                tableDataClass.setKeyID(danger.getInt("KeyId"));
                tableDataClass.setDangerName(danger.getString("Marks") == null ? "没有标题" : danger.getString("Marks"));
                tableDataClass.setPicsJson("");
                tableDataClass.setChannelName(danger.getString("Tdname"));
                tableDataClass.setChannelObjectId(danger.getInt("Tdid"));
                tableDataClass.setUserName(danger.getString("Username"));
                tableDataClass.setPowerName(this.plineName);
                tableDataClass.setImgNum(imgnumlist.get(i));
                String geometryValue = "";
                //判断隐患图形是否为空
                if(danger.getString("Shape").equals("")){
                    geometryValue = "";
                }else{
                    JSONObject Shape = danger.getJSONObject("Shape");
                    geometryValue = Shape.getString("Value");
                }
                tableDataClass.setGeometry(geometryValue);
                int rowid = chnDangerTableOpt.insert(tableDataClass);

                if(rowid!=-1) {
                    keyid_Rowid.put(tableDataClass.getKeyID(), rowid);
                }

                //下面代码以后点击更新有用
//                    //首先查询当前数据库中有没有相同KeyID的隐患信息
//                    List<ChannelDangerTableDataClass> channelDangers =
//                            chnDangerTableOpt.getRowFormKeyID(tableDataClass.getKeyID());
//
//                    //如果存在的数据
//                    if(channelDangers.size()>0){
//                        for (ChannelDangerTableDataClass channeldanger :channelDangers) {
//                            //如果存在的数据为原始数据，本地没有修改的数据,先删除
//                            if(channeldanger.getVersion()== DangerVersion.DANGER_VERSION_INIT) {
//                                chnDangerTableOpt.delete(channeldanger.getRowId());
//                                chnDangerTableOpt.insert(tableDataClass);
//                            }
//                        }
//                    }else {//如果不存在相同KeyID的数据，将该条数据插入数据库
//                        chnDangerTableOpt.insert(tableDataClass);
//                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return keyid_Rowid;
        }
    }

    /**
     * 读取并写入隐患点细节表
     */
    private boolean checkInsertDangerSgTable(JSONArray data, SparseIntArray keyid_Rowid) {

        for (int i = 0; i < data.length(); i++) {

            DangerSgTableDataClass tableDataClass = new DangerSgTableDataClass();

            try {
                JSONObject danger = data.getJSONObject(i);
                int Faultid = danger.getInt("Faultid");
                int dangerrowid = keyid_Rowid.get(Faultid, -1);
                if (dangerrowid == -1) {
                    return false;
                }

                tableDataClass.setDangerRowId(dangerrowid);
                tableDataClass.setGuank1(danger.getString("Guank1") == null ? "" : danger.getString("Guank1"));
                tableDataClass.setGuank2(danger.getString("Guank2") == null ? "" : danger.getString("Guank2"));
                tableDataClass.setGuank3(danger.getString("Guank3") == null ? "" : danger.getString("Guank3"));
                tableDataClass.setGuank4(danger.getString("Guank4") == null ? "" : danger.getString("Guank4"));
                tableDataClass.setGuank5(danger.getString("Guank5") == null ? "" : danger.getString("Guank5"));
                tableDataClass.setKeeper(danger.getString("Keeper") == null ? "" : danger.getString("Keeper"));
                tableDataClass.setKeeperphone(danger.getString("Keeperphone") == null ? "" : danger.getString("Keeperphone"));
                tableDataClass.setSgdepartment(danger.getString("Sgdepartment") == null ? "" : danger.getString("Sgdepartment"));
                tableDataClass.setSgcontact(danger.getString("Sgcontact") == null ? "" : danger.getString("Sgcontact"));
                tableDataClass.setSgphone(danger.getString("Sgphone") == null ? "" : danger.getString("Sgphone"));
                tableDataClass.setYwcontact(danger.getString("Ywcontact") == null ? "" : danger.getString("Ywcontact"));
                tableDataClass.setYwphone(danger.getString("Ywphone") == null ? "" : danger.getString("Ywphone"));
                tableDataClass.setLocation(danger.getString("Location") == null ? "" : danger.getString("Location"));
                tableDataClass.setPlineName(this.plineName);
                tableDataClass.setChnName(this.chnName);

                tableDataClass.setDangerRowId(dangerrowid);


            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            //插入数据
            int rowid = this.dangerSgTableOpt.insert(tableDataClass);
            if (rowid == -1) {
                return false;
            }
        }
        return true;
    }
}
