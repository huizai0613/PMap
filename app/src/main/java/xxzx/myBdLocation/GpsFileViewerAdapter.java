package xxzx.myBdLocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.maps.model.LatLng;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ReadFileClass;
import xxzx.publicClass.geometryJson.WKT;

/**
 * Created by Daniel on 12/29/2014.
 */
public class GpsFileViewerAdapter extends RecyclerView.Adapter<GpsFileViewerAdapter.RecordingsViewHolder> {

    private static final String LOG_TAG = "FileViewerAdapter";

    List<RecordingItem> items;
    Context mContext;
    LinearLayoutManager llm;

    DecimalFormat df = new DecimalFormat("0.000");

    public GpsFileViewerAdapter(Context context,LinearLayoutManager linearLayoutManager) {
        super();
        mContext = context;
        llm = linearLayoutManager;
        this.readGpsFolder();
    }

    @Override
    public void onBindViewHolder(final RecordingsViewHolder holder, int position) {

        final  RecordingItem item = getItem(position);
        long itemDuration = item.getLength();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        holder.vName.setText(item.getName());
        //holder.vLength.setText(String.format("%02d:%02d", minutes, seconds));
        holder.imageView.setBackground(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_gpsline));
        holder.vLength.setText(df.format(item.getmSize() / 1024.0)+"KB");
        holder.vDateAdded.setText(getDateString(item.getName()));

        // define an on click listener to open PlaybackFragment
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ArrayList<String> entrys = new ArrayList<String>();
                    entrys.add("查看路线");
                    //entrys.add("分享好友");
                    entrys.add("删除路线");

                    final CharSequence[] items = entrys.toArray(new CharSequence[entrys.size()]);

                    // File delete confirm
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("路径操作");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                //renameFileDialog(holder.getPosition());
                                //转换到地图界面
                                String wkt=getGPSLineWKT(getItem(holder.getPosition()).getFilePath());
                                IntentPublicMapState.Intent(mContext,MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_showgeometry, wkt);

                            } else if(item==1){
                                deleteFileDialog(holder.getPosition());
                            }else if (item == 2) {

                            }
                        }
                    });
                    builder.setCancelable(true);
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                return false;
            }
        });
    }

    @Override
    public RecordingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_item_gpsline_card_view, parent, false);

        mContext = parent.getContext();

        return new RecordingsViewHolder(itemView);
    }

    public static class RecordingsViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vLength;
        protected TextView vDateAdded;
        protected View cardView;
        protected ImageView imageView;

        public RecordingsViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.file_name_text);
            vLength = (TextView) v.findViewById(R.id.file_length_text);
            vDateAdded = (TextView) v.findViewById(R.id.file_date_added_text);
            cardView = v.findViewById(R.id.card_view);
            imageView= (ImageView)v.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public RecordingItem getItem(int position) {

        return items.get(position);
    }


    public void remove(int position) {
        //remove item from database, recyclerview and storage
        //delete file from storage
        File file = new File(getItem(position).getFilePath());
        file.delete();

        Toast.makeText(
                mContext,
                String.format("删除文件成功", getItem(position).getName()),
                Toast.LENGTH_SHORT
        ).show();

        items.remove(getItem(position));
        notifyItemRemoved(position);
    }

    public void deleteFileDialog (final int position) {
        // File delete confirm
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(mContext);
        confirmDelete.setTitle("删除文件");
        confirmDelete.setMessage("确定删除该文件么？");
        confirmDelete.setCancelable(true);
        confirmDelete.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            //remove item from database, recyclerview, and storage
                            remove(position);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "exception", e);
                        }
                        dialog.cancel();
                    }
                });
        confirmDelete.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = confirmDelete.create();
        alert.show();
    }


    /**
     * 获得GPS文件夹的gps文件
     */
    private void readGpsFolder(){
        items=new ArrayList<RecordingItem>();
        File file=new File(MyString.gps_folder_path);
        File flist[] = file.listFiles();

        if(flist==null){
            return;
        }

        for (int i = 0; i < flist.length; i++) {
            if (flist[i].getName().endsWith(".txt")) {
                RecordingItem recordingItem=new RecordingItem();
                recordingItem.setFilePath(flist[i].getPath());
                recordingItem.setName(flist[i].getName());

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date date = df.parse(flist[i].getName().replace(".txt", " ").trim());
                    recordingItem.setmDate(date);

                    File gps_file = new File(flist[i].getPath());
                    recordingItem.setmSize(gps_file.length());
                    items.add(recordingItem);

                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }

        //排序
        Comparator comp = new SortComparator();
        Collections.sort(items, comp);
    }


    private String getDateString(String str) {
        String strs = str.replace(".txt", " ");
        String[] dateStr = strs.split(" ");
        StringBuilder bb = new StringBuilder("");

        String[] _date = dateStr[0].split("-");
        bb.append(_date[0]);
        bb.append("年");
        bb.append(_date[1]);
        bb.append("月");
        bb.append(_date[2]);
        bb.append("日");

        bb.append("   ");
        bb.append(dateStr[1]);

        return bb.toString();
    }


    /**
     * 读取GPS文件转换为WKT
     * @param path
     * @return
     */
    private String getGPSLineWKT(String path) {
        String value="";
        //读取GPS文件
        String str = ReadFileClass.getString(path);
        String[] ss = str.trim().split(";");
        List<LatLng> list = new ArrayList<>();
        for (String s : ss) {
            String[] _s = s.split(",");
            LatLng latlng = new LatLng(Double.valueOf(_s[1]), Double.valueOf(_s[0]));
            list.add(latlng);
        }
        //转换WKT
        value = WKT.PointListToPolylineWKT(list);
        return value;
    }



    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            RecordingItem a = (RecordingItem) lhs;
            RecordingItem b = (RecordingItem) rhs;

            int result=a.getmDate().compareTo(b.getmDate());
            if(result==0)
                return 0;
            else if(result<0)
                return -1;
            else
                return 1;

        }
    }




}
