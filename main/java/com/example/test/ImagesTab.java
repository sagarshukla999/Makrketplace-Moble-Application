package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImagesTab extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

//    private int[] images={
//            R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4
//    };

    private JSONArray Allresults = null;

    private ImagesTabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String googleimage=((ProductDetails)getActivity()).googleimages();
        Log.d("googleimages",googleimage);
        try {
            JSONObject googleimages=new JSONObject(googleimage);
            Allresults=googleimages.getJSONArray("items");
            //for(int i=0;i<imagearr.length();i++){
              //  Log.d("googleImage",imagearr.getJSONObject(i).getString("link"));
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.images_tab, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new SearchResultAdapter(Allresults);
        adapter = new ImagesTabAdapter(Allresults);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}