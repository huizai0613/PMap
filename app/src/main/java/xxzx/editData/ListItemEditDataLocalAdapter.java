package xxzx.editData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import xxzx.ImageView.NoScrollGridAdapter;
import xxzx.ImageView.NoScrollGridView;
import xxzx.activity.ImagePagerActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyString;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemEditDataLocalAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<EditDataInfo> items;
    private List<Integer> isselectedList;
	private boolean isSelected = false;

	public ListItemEditDataLocalAdapter(Context ctx, ArrayList<EditDataInfo> items, List<Integer> _selectedList) {
		mInflater = LayoutInflater.from(ctx);
		this.mContext = ctx;
		this.items = items;
		this.isselectedList = _selectedList;
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_edit_local_data, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			//holder.cbox_selected = (CheckBox) convertView.findViewById(R.id.cbox_selected);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);

			//holder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
			//holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			holder.rl_relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_relativeLayout);
			holder.imgbtn_select = (ImageButton) convertView.findViewById(R.id.imgbtn_select);

			holder.imgbtn_photo = (ImageButton) convertView.findViewById(R.id.imgbtn_photo);
			//holder.imgbtn_location = (ImageButton) convertView.findViewById(R.id.imgbtn_location);
			holder.imgbtn_delete = (ImageButton) convertView.findViewById(R.id.imgbtn_delete);

			holder.imgbtn_photo.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_edit_local_picture));
			holder.imgbtn_location.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_edit_local_location));
			holder.imgbtn_delete.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_edit_local_delete));
			//holder.imgbtn_select.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_list_item_unselect));

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final EditDataInfo taskInfo = items.get(position);

		String title = taskInfo.getTitle().trim();
		if(title.length()>8){
			title = title.substring(0,8)+"...";
		}

		String des = taskInfo.getDescribtion().trim();

		holder.tv_title.setText(title);
		holder.tv_content.setText(des);
		holder.tv_date.setText(taskInfo.getDate());

        //如果为空。该控件不显示
		if(des.equals("")){
			holder.tv_content.setVisibility(View.GONE);
		}


		final ArrayList<String> imageUrls = taskInfo.getImgs();

		List<String> list = new ArrayList<String>();
		//检测该照片是否存在
		for (String name : imageUrls) {
			File file = new File(MyString.edit_photo_folder_path + "/" + name);
			//如果存在，则删除
			if (!file.exists()) {
				list.add(name);
			}
		}
		//不存在时，不在加载
		for (String name : list) {
			imageUrls.remove(name);
			Toast.makeText(this.mContext, "文件：" + name + " 不存在！", Toast.LENGTH_SHORT).show();
		}

		imageUrls.clear();
		if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		} else {
			holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls,MyString.edit_photo_folder_path));
		}


		// 点击回帖九宫格，查看大图
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});

		holder.imgbtn_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isSelected){
					isSelected=false;
					//holder.imgbtn_select.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_list_item_unselect));

				}

			}
		});






		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER,MyString.edit_photo_folder_path);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHolder {
		private CheckBox cbox_selected;
		private TextView tv_title;
		private TextView tv_content;
		private ImageButton imgbtn_photo;
		private ImageButton imgbtn_location;
		private ImageButton imgbtn_delete;
		private RelativeLayout rl_relativeLayout;
		private ImageButton imgbtn_select;
		private TextView tv_date;
		private Button btn_edit;
		private NoScrollGridView gridview;
	}
}
