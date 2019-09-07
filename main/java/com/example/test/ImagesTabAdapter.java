package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImagesTabAdapter extends RecyclerView.Adapter<ImagesTabAdapter.ImageViewHolder> {

    private JSONArray Allresults = null;
    public ImagesTabAdapter(JSONArray Allresults ){
        this.Allresults = Allresults;
        Log.d("resultadapter", Allresults.toString());
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagecard, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder viewHolder, int i) {

        JSONObject obj=null;
        try {
            obj = Allresults.getJSONObject(i);
            String imgurl=obj.getString("link");
            Picasso.get().load(imgurl).into(viewHolder.photo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Allresults.length();
    }



    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.imageid);
        }

    }


}
