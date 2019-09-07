package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WishListTab extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private JSONArray Allresults = null;
    private String url = null;

    private SearchResultAdapter adapter;

    RequestQueue queue;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //final String keyword = view.getStringExtra("Keyword");
        //queue = Volley.newRequestQueue(getApplicationContext());

        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.wishlist_layout, container, false);
        layoutManager = new GridLayoutManager(getContext(), 2);
        Log.d("wishlist",MainActivity.wishListItems.toString());
        JSONObject x=MainActivity.wishListItems;
        Iterator keys = x.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            JSONObject value = null;
            try {
                value = x.getJSONObject((String) key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Allresults.put(value);
        }


        if(Allresults!=null){
            View nodata = view.findViewById(R.id.nodata);
            nodata.setVisibility(View.GONE);
            View data = view.findViewById(R.id.data);
            data.setVisibility(View.VISIBLE);
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new SearchResultAdapter(Allresults);
            //adapter1 = new SearchResultAdapter(Allresults);
            recyclerView.setAdapter(adapter);
        }
        else{
            View nodata = view.findViewById(R.id.nodata);
            nodata.setVisibility(View.VISIBLE);
            View data = view.findViewById(R.id.data);
            data.setVisibility(View.GONE);
        }

        return view;
    }

}
