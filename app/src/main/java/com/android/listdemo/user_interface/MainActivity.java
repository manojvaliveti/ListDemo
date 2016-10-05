package com.android.listdemo.user_interface;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.listdemo.R;
import com.android.listdemo.adapter.RecyclerAdapter;
import com.android.listdemo.constant.Constant;
import com.android.listdemo.model.JsonModel;
import com.android.listdemo.model.Row;
import com.android.listdemo.service_interface.RetrofitService;
import com.android.listdemo.utill.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private RetrofitService retrofitService;
    private View mToolbarView;
    private ActionBar actionBar;
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ConnectionDetector cd;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private List<Row> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initComponent();
    }

    private void initComponent() {
        // set Actionbar
        setActionBar();

        // Initialize connection detector to check internet connection
        cd = new ConnectionDetector(mContext);

        // init all views
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(false);



        // initialize to retrofit service instance
        retrofitService = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService.class);

        // call to api to retrieve data from server
        if (cd.isConnectingToInternet()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            getDataFromServer();
        } else {
            Toast.makeText(mContext, "Please check network connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setActionBar() {
        mToolbarView = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        setTitle("ListDemo");
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        Toast.makeText(mContext, "clicked pos " + tag, Toast.LENGTH_SHORT).show();
    }

    /// refresh list when pull down
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getDataFromServer();
    }

    // call server api
    private void getDataFromServer() {
        retrofitService.getListData().enqueue(new Callback<JsonModel>() {
            @Override
            public void onResponse(Call<JsonModel> call, Response<JsonModel> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    dataList = response.body().getRows();
                    if (mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                    adapter = new RecyclerAdapter(mContext, dataList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(mContext, response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonModel> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
