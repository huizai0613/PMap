package xxzx.ImageView;

import java.io.File;
import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import xxzx.activity.R;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;

public class NoScrollGridAdapter extends BaseAdapter {

	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<String> imageUrls;
	/** 图片所存储的文件夹路径 */
	private String imgfolderPath="";
	public NoScrollGridAdapter(Context ctx, ArrayList<String> urls,String folderpath) {
		this.ctx = ctx;
		this.imageUrls = urls;
		this.imgfolderPath=folderpath;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.gridview_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

		LayoutParams para;
		para = imageView.getLayoutParams();

		int reqWidth=para.width;
		int reqHeight= para.height;

		String filepath = imgfolderPath + "/" + imageUrls.get(position);
		File file=new File(filepath);
		if(!file.exists()){
			ToastUtil.show(ctx,"图片不存在！"+filepath);
			return view;
		}

		Bitmap bp = decodeSampledBitmapFromFd(imgfolderPath + "/"+ imageUrls.get(position),reqWidth,reqHeight);
		imageView.setImageBitmap(bp);
		return view;
	}


	// 从sd卡上加载图片
	private Bitmap decodeSampledBitmapFromFd(String pathName,
												   int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
											 int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private Bitmap createScaleBitmap(Bitmap src, int dstWidth,
											int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

}
