package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class SearchResult extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

//    private int[] images={
//            R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4
//    };

    private JSONArray Allresults = null;
    private JSONObject Response = null;
    private String url = null;

    private SearchResultAdapter adapter;

    RequestQueue queue;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        Log.d("Hello",url);
        final String keyword = getIntent().getStringExtra("Keyword");
        queue = Volley.newRequestQueue(getApplicationContext());
        //jonObj=getSearchResults();
        //getSearchResults();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        layoutManager = new GridLayoutManager(this, 2);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        Response=response;


                        JSONObject jsonObj = Response;


                        try {
                            //jsonObj = new JSONObject(xg);
                            JSONArray a1 = (JSONArray) jsonObj.get("findItemsAdvancedResponse");
                            JSONObject a2 = a1.getJSONObject(0);
                            JSONArray a3 = (JSONArray) a2.get("searchResult");
                            JSONObject a4 = a3.getJSONObject(0);
                            Allresults = (JSONArray) a4.get("item");
                            //JSONArray Allresults = .searchResult[0].item;

                            Log.d("Allresults", Allresults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                        Toolbar toolbar = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            toolbar = (Toolbar) findViewById(R.id.toolbar);
                            toolbar.setTitle("Search Results");
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setDisplayShowHomeEnabled(true);
                            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // back button pressed
                                    Log.d("back","back");
                                    finish();
                                }
                            });

                        }



                        TextView key = findViewById(R.id.keyword);
                        key.setText(" " + keyword);
                        TextView count = findViewById(R.id.totalItems);
                        //count.setText();
                        View v1 = findViewById(R.id.loading);
                        v1.setVisibility(View.GONE);
                        if(Allresults!=null){
                            count.setText(" " + Allresults.length() + " ");
                            View nodata = findViewById(R.id.nodata);
                            nodata.setVisibility(View.GONE);
                            View data = findViewById(R.id.data);
                            data.setVisibility(View.VISIBLE);
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new SearchResultAdapter(Allresults);
                            //adapter1 = new SearchResultAdapter(Allresults);
                            recyclerView.setAdapter(adapter);
                        }
                        else{
                            View nodata = findViewById(R.id.nodata);
                            nodata.setVisibility(View.VISIBLE);
                            View data = findViewById(R.id.data);
                            data.setVisibility(View.GONE);
                        }




//                        View v2 = findViewById(R.id.data);
//                        v2.setVisibility(View.VISIBLE);




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        error.printStackTrace();
                    }
                });
        queue.add(getRequest);

    }

}
