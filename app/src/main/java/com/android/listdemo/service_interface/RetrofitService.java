package com.android.listdemo.service_interface;

import com.android.listdemo.constant.Constant;
import com.android.listdemo.model.JsonModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by admin on 03/10/16.
 */

public interface RetrofitService {

    @GET(Constant.GET_DATA_LIST)
    Call<JsonModel> getListData();
}
