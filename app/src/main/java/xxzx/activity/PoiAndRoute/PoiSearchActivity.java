package xxzx.activity.PoiAndRoute;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

import java.util.List;

import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.library.VectorDrawable;
import xxzx.myView.ImgButtonHorizontal;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.routePlanning.ListItemPoiResultAdapter;

public class PoiSearchActivity extends BaseToolBarActivity implements ActionMenuView.OnMenuItemClickListener, OnPoiSearchListener {

    private EditText editText_Search;
    private Button btn_search;
    private AppBarLayout appBarLayout = null;
    private ImgButtonHorizontal imgbtn_mapLocation;
    private ImgButtonHorizontal imgbtn_myLocation;
    private ListView lv_poiResult;


    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private EditText editCity;// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_poisearch, menu);
        //menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.showOverflowMenu();
        getLayoutInflater().inflate(R.layout.public_base_toolbar_poi_search, toolbar);

        editText_Search = (EditText) toolbar.findViewById(R.id.editText_Search);
        btn_search = (Button) toolbar.findViewById(R.id.btn_right);
        btn_search.setText("搜索");
        btn_search.setVisibility(View.VISIBLE);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord = editText_Search.getText().toString().trim();
                if ("".equals(keyWord)) {
                    Toast.makeText(PoiSearchActivity.this, "请输入搜索关键字", Toast.LENGTH_LONG).show();
                } else {
                    doSearchQuery();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        lv_poiResult = (ListView) findViewById(R.id.lv_poiResult);
        imgbtn_mapLocation = (ImgButtonHorizontal) findViewById(R.id.imgbtn_mapLocation);
        imgbtn_myLocation = (ImgButtonHorizontal) findViewById(R.id.imgbtn_myLocation);
        imgbtn_myLocation.setImage(VectorDrawable.getDrawable(this, R.drawable.iconfont_mylocation));
        imgbtn_mapLocation.setImage(VectorDrawable.getDrawable(this, R.drawable.iconfont_maplocation));

        imgbtn_myLocation.setOnClick(new ImgbtnOnClickListener());
        imgbtn_mapLocation.setOnClick(new ImgbtnOnClickListener());

        //初始化toolbar
        //this.initToolBar();
    }

    private void initData() {
        //listviewitem点击事件
        lv_poiResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiitem = poiResult.getPois().get(position);
                Intent intent = new Intent();
                intent.putExtra("data", poiitem.getTitle() + ";" + poiitem.getLatLonPoint().getLatitude() + ";" + poiitem.getLatLonPoint().getLongitude());
                intent.setClass(PoiSearchActivity.this, RoutePlanningActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    /**
     * toolbar上面的菜单点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String msg = "";
        switch (menuItem.getItemId()) {
            case R.id.action_search:
                keyWord = editText_Search.getText().toString().trim();
                if ("".equals(keyWord)) {
                    Toast.makeText(PoiSearchActivity.this, "请输入搜索关键字", Toast.LENGTH_LONG).show();
                } else {
                    doSearchQuery();
                }
                break;
        }
        return true;
    }


    /**
     * 按钮点击监听器
     */
    private class ImgbtnOnClickListener implements ImgButtonHorizontal.ICoallBack {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgbtn_myLocation:
                    AMapLocation location = MySingleClass.getInstance().getMyGdLocation();
                    if (location != null) {
                        Intent intent = new Intent();
                        intent.putExtra("data", "我的位置;" + location.getLatitude() + ";" + location.getLongitude());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(PoiSearchActivity.this, "无法获取定位信息，请稍等……", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgbtn_mapLocation:
                    IntentPublicMapState.Intent(PoiSearchActivity.this, MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_selectpoi, "");
                    finish();
                    break;
            }
        }
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        //获取当前所在的城市
        AMapLocation location = MySingleClass.getInstance().getMyGdLocation();
        String cityname = "";
        if (location != null) {
            String city = location.getCity();
            if (city != null) {
                cityname = city;
            }
        }
        query = new PoiSearch.Query(keyWord, "", cityname);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        //填充数据
                        lv_poiResult.setAdapter(new ListItemPoiResultAdapter(PoiSearchActivity.this, poiItems));

//                        aMap.clear();// 清理之前的图标
//                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(PoiSearchActivity.this, "没有查询到结果", Toast.LENGTH_SHORT).show();
                        //ToastUtil.show(PoiKeywordSearchActivity.this, R.string.no_result);
                    }
                }
            } else {
                Toast.makeText(PoiSearchActivity.this, "没有查询到结果", Toast.LENGTH_SHORT).show();
            }
        } else if (rCode == 27) {
            Toast.makeText(PoiSearchActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
        } else if (rCode == 32) {
            Toast.makeText(PoiSearchActivity.this, "key错误", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PoiSearchActivity.this, "其他错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(this, infomation, Toast.LENGTH_LONG).show();
    }


}
