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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SimilarProductsTab extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private JSONArray Allresults = null;
    private JSONArray DefaultArray;
    private SimilarProductsTabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String similaritem=((ProductDetails)getActivity()).similaritems();
        Log.d("similaritemsYOYO",similaritem);
        JSONArray NewResultsArray=new JSONArray();
        try {
            JSONObject similaritems=new JSONObject(similaritem);
            Allresults=similaritems.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item");



            for(int i=0;i<Allresults.length();i++){
                JSONObject jo = new JSONObject();
                jo.put("imageURL", Allresults.getJSONObject(i).getString("imageURL"));
                jo.put("Price", Allresults.getJSONObject(i).getJSONObject("buyItNowPrice").getString("__value__"));
                jo.put("shippingCost", Allresults.getJSONObject(i).getJSONObject("shippingCost").getString("__value__"));
                String timeLeft=Allresults.getJSONObject(i).getString("timeLeft");
                String NtimeLeft=timeLeft.substring(timeLeft.indexOf("P")+1, timeLeft.indexOf("D"));
                jo.put("Days", NtimeLeft);
                jo.put("Name", Allresults.getJSONObject(i).getString("title"));
                jo.put("viewItemURL", Allresults.getJSONObject(i).getString("viewItemURL"));
                NewResultsArray.put(jo);
            }
            DefaultArray=NewResultsArray;


        } catch (JSONException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.similarproducts_tab, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new SimilarProductsTabAdapter(NewResultsArray);
        //recyclerView.setAdapter(adapter);

        final JSONArray NewResults=null;
        final Spinner sortBy = rootView.findViewById(R.id.SortBy);

        final Spinner orderBy = rootView.findViewById(R.id.Order);

        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String text = sortBy.getSelectedItem().toString();
                final String order = orderBy.getSelectedItem().toString();

                if(text.equals("Default")){
                    orderBy.setEnabled(false);
                    orderBy.setClickable(false);
                    orderBy.setAlpha(0.5f);
                    adapter = new SimilarProductsTabAdapter(DefaultArray);
                    recyclerView.setAdapter(adapter);
                    //orderBy.setEnabled(false);
                }
                else{
                    orderBy.setEnabled(true);
                    orderBy.setAlpha(1f);
                    //orderBy.setVisibility(View.GONE);
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < DefaultArray.length(); i++) {
                        try {
                            jsonValues.add(DefaultArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort( jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private final String KEY_NAME = text;

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = (String) a.get(KEY_NAME);
                                valB = (String) b.get(KEY_NAME);
                            }
                            catch (JSONException e) {
                                //do something
                            }

                            if(order.equals("Ascending")){
                                return valA.compareTo(valB);
                            }
                            else{
                                return -valA.compareTo(valB);
                            }

                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    JSONArray updatedArray=new JSONArray();
                    for (int i = 0; i < jsonValues.size(); i++) {
                        updatedArray.put(jsonValues.get(i));
                    }
                    adapter = new SimilarProductsTabAdapter(updatedArray);
                    recyclerView.setAdapter(adapter);
                }



                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        orderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String text = sortBy.getSelectedItem().toString();
                final String order = orderBy.getSelectedItem().toString();

                if(text.equals("Default")){
                    orderBy.setEnabled(false);
                    orderBy.setClickable(false);
                    orderBy.setAlpha(0.5f);
                    adapter = new SimilarProductsTabAdapter(DefaultArray);
                    recyclerView.setAdapter(adapter);

                }
                else{
                    orderBy.setEnabled(true);
                    orderBy.setAlpha(1f);
                    //orderBy.setVisibility(View.GONE);
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < DefaultArray.length(); i++) {
                        try {
                            jsonValues.add(DefaultArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort( jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private final String KEY_NAME = text;

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = (String) a.get(KEY_NAME);
                                valB = (String) b.get(KEY_NAME);
                            }
                            catch (JSONException e) {
                                //do something
                            }

                            if(order.equals("Ascending")){
                                return valA.compareTo(valB);
                            }
                            else{
                                return -valA.compareTo(valB);
                            }

                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    JSONArray updatedArray=new JSONArray();
                    for (int i = 0; i < jsonValues.size(); i++) {
                        updatedArray.put(jsonValues.get(i));
                    }
                    adapter = new SimilarProductsTabAdapter(updatedArray);
                    recyclerView.setAdapter(adapter);
                }



                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //adapter.notifyDataSetChanged();

        return rootView;
    }
}