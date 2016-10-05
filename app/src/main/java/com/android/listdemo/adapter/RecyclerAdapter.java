package com.android.listdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.listdemo.R;
import com.android.listdemo.model.JsonModel;
import com.android.listdemo.model.Row;
import com.android.listdemo.utill.ImageLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 03/10/16.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private View.OnClickListener clickListener;
    private List<Row> dataList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView txtTitle, txtDestcription;
        ImageView imgPicture;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
            txtTitle = (TextView) itemView.findViewById(R.id.textVwName);
            txtDestcription = (TextView) itemView.findViewById(R.id.textVwDescription);
            imgPicture = (ImageView) itemView.findViewById(R.id.imgVwPicture);
        }
    }

    public RecyclerAdapter(Context context,List<Row> dataList, View.OnClickListener clickListener) {
        this.mContext = context;
        this.clickListener = clickListener;
        this.dataList = dataList;
        options = ImageLoading.getDisplayImageOption(0);
        if (!imageLoader.isInited()) {
            this.imageLoader = ImageLoading.initImageLoader(mContext, imageLoader);
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_item, parent, false);
        ViewHolder vhHeader = new ViewHolder(v, viewType);
        return vhHeader;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        String imageUrl = (String) dataList.get(position).getImageHref();
        holder.relativeLayout.setTag(position);
        holder.relativeLayout.setOnClickListener(clickListener);

        //Check condition for title and description is available or not.

        String title = (dataList.get(position).getTitle()==null) ? "No Title Available." : dataList.get(position).getTitle();
        String description = (dataList.get(position).getDescription()==null) ? "No Description Available." : dataList.get(position).getDescription();

        //set title & description
        holder.txtTitle.setText(title);
        holder.txtDestcription.setText(description);

        imageLoader.displayImage(imageUrl, holder.imgPicture, options);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
