package com.tumi.ala.news.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.tumi.ala.news.Activity.ShowNewsActivity;
import com.tumi.ala.news.Adapter.NewsAdapter;
import com.tumi.ala.news.Bean.News;
import com.tumi.ala.news.LoadListView;
import com.tumi.ala.news.R;
import com.tumi.ala.news.Utils.HttpUtils;
//import com.example.ts.news.Utils.MyBitmapUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SportFragment extends Fragment implements LoadListView.ILoadListener,
        LoadListView.RLoadListener, NewsAdapter.CallBack {
    final String url = "http://api.tianapi.com/tiyu/?key=7bb2c75d5a41707558da9986c15af2a7&num=30";

    private View view;
    private LoadListView mListView;
    private List<News> newsList;
    private NewsAdapter adapter;

    public SportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);
        setupViews();
        if (!HttpUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), "当前没有可以使用的网络，请检查网络设置！", Toast.LENGTH_SHORT).show();

        } else {
            initNews();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowNewsActivity.class);
                intent.putExtra("title", newsList.get(i - mListView.getHeaderViewsCount()).getNews_title());
                intent.putExtra("url", newsList.get(i - mListView.getHeaderViewsCount()).getNews_url());
                intent.putExtra("date", newsList.get(i - mListView.getHeaderViewsCount()).getDate());
                intent.putExtra("author", newsList.get(i - mListView.getHeaderViewsCount()).getAuthor_name());
                intent.putExtra("pic_url", newsList.get(i - mListView.getHeaderViewsCount()).getNews_picurl());
                startActivity(intent);
            }
        });
        return view;
    }

    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON(jsonData);
            }
        }).start();
    }


    private void parseJSONWithGSON(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");
            for (int i = 0; i < 10; i++) {
                JSONObject json_news = jsonArray.getJSONObject(i);
                String imgUrl = json_news.getString("picUrl");
                Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
                String title = json_news.getString("title");
                String date = json_news.getString("ctime");
                String author_name = json_news.getString("description");
                String url = json_news.getString("url");

                News news = new News(bitmap, title, url, imgUrl, date, author_name);
                newsList.add(news);

            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONWithGSON_Refresh(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

            JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(30) + 1);
            String imgUrl = json_news.getString("picUrl");
            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
            String title = json_news.getString("title");
            String date = json_news.getString("ctime");
            String author_name = json_news.getString("description");
            String url = json_news.getString("url");

            News news = new News(bitmap, title, url, date, imgUrl, author_name);
            newsList.add(0, news);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONWithGSON_Load(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

            JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(30) + 1);
            String imgUrl = json_news.getString("picUrl");
            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
            String title = json_news.getString("title");
            String date = json_news.getString("ctime");
            String author_name = json_news.getString("description");
            String url = json_news.getString("url");

            News news = new News(bitmap, title, url, imgUrl, date, author_name);
            newsList.add(news);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initNewDatas() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON_Load(jsonData);

            }
        }).start();

    }

    private void initRefreshDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON_Refresh(jsonData);
            }
        }).start();
    }

    private void setupViews() {

        mListView = view.findViewById(R.id.lv_main);
        //上拉加载接口
        mListView.setInterface(this);
        mListView.setReflashInterface(this);
        newsList = new ArrayList<News>();
        adapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        mListView.setAdapter(adapter);


    }

    //实现onLoad()方法。
    @Override
    public void onLoad() {
        //添加延时效果模拟数据加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initNewDatas();//得到新数据
                mListView.loadCompleted();
            }
        }, 2000);
    }

    //  实现的刷新方法
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRefreshDatas();//得到新数据
                mListView.reflashComplete();
            }
        }, 2000);

    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "该新闻已删除！", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        adapter.notifyDataSetChanged();
    }

}