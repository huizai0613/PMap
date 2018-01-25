package xxzx.spatialite.TableStruct;

/**
 * Created by ch on 2016/4/1.
 */
public class ChannelDangerTableColumn {

    public static String RowId = "ROWID";// 数据库的行号

    public static String DangerName = "DANGERNAME";// 名称

    public static String PowerName = "POWERNAME";// 所属电力线名称

    public static String ChannelName = "CHANNELNAME";// 所属通道名称

    public static String ChannelObjectId = "CHANNELOBJECTID";// 所属通道编号

    public static String DangerType = "DANGERTYPE";// 类型

    public static String  DangerMark = "DANGERMARK";//隐患点类型

    public static String Version = "VERSION"; //版本更新 0表示原始的，1标示更新的

    public static String  DangerLevel = "DANGERLEVEL"; //危险级别

    public static String  UserName = "USERNAME"; //巡查人员

    public static String  PicsJson = "PICSJSON"; //图片序列

    public static String KeyID="KEYID"; //和服务器对应

    public static String DateTime = "DATE";// 时间

    public static String Geometry = "Geometry";// 图形

    public static String ImgNum = "IMGNUM"; //图片数量

    public static String SpotMark = "SPOTMARKS";//现场巡视情况

    public static String HuiLuType = "HUILUTYPE";//隐患点回路类型

}
