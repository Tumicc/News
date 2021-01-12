package com.tumi.ala.news.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
//import com.example.ts.news.Adapter.CategoryAdapter;
import com.tumi.ala.news.Adapter.NewsAdapter;
import com.tumi.ala.news.Bean.News;
import com.tumi.ala.news.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment_bak extends Fragment implements NewsAdapter.CallBack {
    private static final String TAG = "MainFragment_bak";

    private View view;

    private final List<String> categoryList = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ListView listView;

    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_bak, container, false);
        initView();

        newsAdapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(newsAdapter);

        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "该新闻已删除！", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        newsAdapter.notifyDataSetChanged();
    }

}
