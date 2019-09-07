package com.example.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.ui.main.SectionsPagerAdapter2;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetails extends AppCompatActivity {
    RequestQueue queue;

    private String googleimages;
    private String itemdetails;
    private String similaritems;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Hello","World");
        queue = Volley.newRequestQueue(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        final SectionsPagerAdapter2 sectionsPagerAdapter = new SectionsPagerAdapter2(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);






        ////////////////
        String itemdetailsurl = getIntent().getStringExtra("itemdetailsurl");
        Log.d("Yo",itemdetailsurl);
        JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, itemdetailsurl,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("ItemDetails", response.toString());
                        itemdetails=response.toString();
                        String url="";
                        String title="";
                        String price="";
                        try {
                            JSONObject x=new JSONObject(itemdetails);
                             url = response.getJSONObject("Item").getString("ViewItemURLForNaturalSearch");
                             title = response.getJSONObject("Item").getString("Title");
                             price = response.getJSONObject("Item").getJSONObject("CurrentPrice").getString("Value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String furl=url;
                        final String ftitle=title;
                        final String fprice=price;
                        ImageView fbbutton = findViewById(R.id.fbbutton);
                        fbbutton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String fb="https://www.facebook.com/sharer.php?display=page&u="+Uri.encode(furl)+"&quote=Buy "+Uri.encode(ftitle)+" for $"+
                                        Uri.encode(fprice)+" from Ebay! &hashtag="+Uri.encode("#CSCI571Spring2019Ebay");
                                //String tweet= "https://twitter.com/intent/tweet?text=Check out "+placeDetails.getName()+" Located at "+placeDetails.getFormatted_address()+". Website:  "+placeDetails.getWebsite()+" . "+"TravelAndEntertainmentSearch";
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse(fb));
                                startActivity(viewIntent);

                            }

                        });



                        String imagesurl = getIntent().getStringExtra("imagesurl");
                        Log.d("Yo",imagesurl);
                        JsonObjectRequest getRequest2 = new JsonObjectRequest(Request.Method.GET, imagesurl,null,
                                new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("Images", response.toString());
                                        googleimages=response.toString();
                                        final String similarurl = getIntent().getStringExtra("similarurl");
                                        Log.d("Yo",similarurl);
                                        JsonObjectRequest getRequest3 = new JsonObjectRequest(Request.Method.GET, similarurl,null,
                                                new Response.Listener<JSONObject>()
                                                {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("similar", response.toString());
                                                        similaritems=response.toString();
                                                        View v1 = findViewById(R.id.loading);
                                                        v1.setVisibility(View.GONE);
                                                        View v2 = findViewById(R.id.view_pager);
                                                        v2.setVisibility(View.VISIBLE);

                                                        viewPager.setAdapter(sectionsPagerAdapter);
                                                        final TabLayout tabs = findViewById(R.id.tabs);
                                                        tabs.setupWithViewPager(viewPager);


                                                        tabs.getTabAt(0).setIcon(R.drawable.information_variantactive);
                                                        tabs.getTabAt(1).setIcon(R.drawable.truck_delivery);
                                                        tabs.getTabAt(2).setIcon(R.drawable.google);
                                                        tabs.getTabAt(3).setIcon(R.drawable.equal);


                                                        android.support.v7.widget.Toolbar toolbar = null;
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                                            toolbar = (Toolbar) findViewById(R.id.toolbarid);
                                                            if(ftitle.length()>42){
                                                                toolbar.setTitle(ftitle.substring(0,42)+"...");
                                                            }
                                                            else{
                                                                toolbar.setTitle(ftitle);
                                                            }

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

                                                        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                            @Override
                                                            public void onTabSelected(TabLayout.Tab tab) {
                                                                int position = tab.getPosition();
                                                                Log.d("activetab",position+"");
                                                                if(position==0){
                                                                    tabs.getTabAt(0).setIcon(R.drawable.information_variantactive);
                                                                    tabs.getTabAt(1).setIcon(R.drawable.truck_delivery);
                                                                    tabs.getTabAt(2).setIcon(R.drawable.google);
                                                                    tabs.getTabAt(3).setIcon(R.drawable.equal);
                                                                }
                                                                else if(position==1){
                                                                    tabs.getTabAt(0).setIcon(R.drawable.producttab);
                                                                    tabs.getTabAt(1).setIcon(R.drawable.truck_deliveryactive);
                                                                    tabs.getTabAt(2).setIcon(R.drawable.google);
                                                                    tabs.getTabAt(3).setIcon(R.drawable.equal);
                                                                }
                                                                else if(position==2){
                                                                    tabs.getTabAt(0).setIcon(R.drawable.producttab);
                                                                    tabs.getTabAt(1).setIcon(R.drawable.truck_delivery);
                                                                    tabs.getTabAt(2).setIcon(R.drawable.googleactive);
                                                                    tabs.getTabAt(3).setIcon(R.drawable.equal);
                                                                }
                                                                else if(position==3){
                                                                    tabs.getTabAt(0).setIcon(R.drawable.producttab);
                                                                    tabs.getTabAt(1).setIcon(R.drawable.truck_delivery);
                                                                    tabs.getTabAt(2).setIcon(R.drawable.google);
                                                                    tabs.getTabAt(3).setIcon(R.drawable.equalactive);
                                                                }

                                                            }

                                                            @Override
                                                            public void onTabUnselected(TabLayout.Tab tab) {
                                                            }

                                                            @Override
                                                            public void onTabReselected(TabLayout.Tab tab) {
                                                            }
                                                        });
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
                                        queue.add(getRequest3);
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
                        queue.add(getRequest2);
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
        queue.add(getRequest1);


        /////////

    }

    public static String sagar="yo";
    public String itemdetails(){
        return  itemdetails;
    }
    public String similaritems(){
        return  similaritems.toString();
    }
    public String googleimages(){
        return  googleimages.toString();
    }

    public String ShippingInfo(){
        String xg = getIntent().getStringExtra("ShippingInfo");
        return  xg;
    }


}