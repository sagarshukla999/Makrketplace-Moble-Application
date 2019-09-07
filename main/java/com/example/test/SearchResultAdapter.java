package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.app.PendingIntent.getActivity;
import static android.support.v4.content.ContextCompat.startActivity;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ImageViewHolder> {

    private JSONArray Allresults = null;
    public SearchResultAdapter(JSONArray Allresults ){
        this.Allresults = Allresults;
        Log.d("resultadapter", Allresults.toString());
    }
    RequestQueue queue;
    private Context mContext;
    String finalItemId;
    String finalTitle;
    Intent myIntent;
    JSONObject fshippingInfo;
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        mContext=parent.getContext();
        //view=this.view;
        return imageViewHolder;


    }

    @Override
    public void onBindViewHolder(final ImageViewHolder viewHolder, int i) {

       JSONObject obj=null;
        String itemId=null;
        String Ftitle="";
        try {
            obj = Allresults.getJSONObject(i);

            String imgurl=obj.getJSONArray("galleryURL").getString(0);
//            Log.d("onbindview", imgurl);
            Picasso.get().load(imgurl).into(viewHolder.photo);
            //Picasso.with(mContext).load(imgurl).fit().centerInside().into(viewHolder.photo);
            String title=obj.getJSONArray("title").getString(0);
            finalTitle=title;
            if(obj.getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice")) {
                String sellingStatus = obj.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__").toString();
                viewHolder.photoCost.setText("$"+sellingStatus);
            }
            else{ viewHolder.photoCost.setText("N/A");}
            fshippingInfo=obj;
            if(obj.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")) {
                //Log.d("onbindview", sellingStatus);
                String shippingInfo = obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__").toString();

            if(shippingInfo.equals("0.00")||shippingInfo.equals("0")||shippingInfo.equals("0.0")){
                    viewHolder.photoShipping.setText("Free Shipping");
                }
                else{
                    viewHolder.photoShipping.setText("$"+shippingInfo);
                }
            }
            else{ viewHolder.photoShipping.setText("N/A");}
            //Log.d("onbindview", shippingInfo);
            if(obj.has("postalCode")){
                String postalCode=obj.getJSONArray("postalCode").getString(0);
                viewHolder.zip.setText("Zip: "+postalCode);
            }
            else{ viewHolder.zip.setText("N/A");}

            itemId=obj.getJSONArray("itemId").getString(0);

            //Log.d("onbindview", postalCode);
            if(obj.has("condition")) {
                String Condition = obj.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                viewHolder.photoCondition.setText(Condition);
            }
            else{ viewHolder.photoCondition.setText("N/A");}





            if(title.length()>50){
                viewHolder.photoTitle.setText(title.substring(0,50).toUpperCase()+"...");
                Ftitle=title.substring(0,50).toUpperCase()+"...";
            }
            else{
                viewHolder.photoTitle.setText(title.toUpperCase());
                Ftitle=title;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //viewHolder.photocart.setText("Cart"+i);

        //viewHolder.photoCost.setText("Cost"+i);

        if(MainActivity.wishListItems.has(itemId)){
            viewHolder.addCart.setVisibility(View.GONE);
            viewHolder.removeCart.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.removeCart.setVisibility(View.GONE);
            viewHolder.addCart.setVisibility(View.VISIBLE);
        }


        finalItemId = itemId;
        final JSONObject finalObj = obj;

        //View v2 = view.findViewById(R.id.removeCart);
        //v2.setVisibility(View.VISIBLE);

        final String finalFtitle = Ftitle;
        viewHolder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, finalFtitle+" was added to wishlist", Toast.LENGTH_SHORT).show();
                try {
                    MainActivity.addWishList(finalItemId, finalObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                viewHolder.addCart.setVisibility(View.GONE);
                viewHolder.removeCart.setVisibility(View.VISIBLE);
            }
        });


        viewHolder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, finalFtitle +" was removed from wishlist", Toast.LENGTH_SHORT).show();
                MainActivity.removeWishList(finalItemId);
                viewHolder.removeCart.setVisibility(View.GONE);
                viewHolder.addCart.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagesurl = null;
                try {
                    myIntent = new Intent(mContext, ProductDetails.class);
                    final String itemdetailsurl = "https://marketplace-1554111139715.appspot.com/itemDetails/"+finalItemId;
                    myIntent.putExtra("itemdetailsurl", itemdetailsurl);
                    imagesurl = "https://marketplace-1554111139715.appspot.com/photos/"+ URLEncoder.encode(finalTitle, "utf-8");
                    myIntent.putExtra("imagesurl", imagesurl);
                    final String similarurl = "https://marketplace-1554111139715.appspot.com/getSimilarItems/"+finalItemId;
                    myIntent.putExtra("similarurl", similarurl);
                    myIntent.putExtra("ShippingInfo", fshippingInfo.toString());
                    mContext.startActivity(myIntent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Allresults.length();
    }



    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView addCart;
        ImageView removeCart;
        ImageView photo;
        TextView photoTitle;
        TextView photoCondition;
        CardView cardview;
        TextView photoShipping;
        TextView photoCost;
        TextView zip;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.imageid);
            photoTitle=itemView.findViewById(R.id.imgtext);
            photoCondition=itemView.findViewById(R.id.condition);
            photoCost=itemView.findViewById(R.id.cost);
            photoShipping=itemView.findViewById(R.id.shipping);
            addCart=itemView.findViewById(R.id.addCart);
            removeCart=itemView.findViewById(R.id.removeCart);
            zip=itemView.findViewById(R.id.zip);
            cardview=itemView.findViewById(R.id.cv);
        }

    }

//    private void getItemDetails(final String ShippingInfo) {
//
//
//
//        String imagesurl = null;
//        try {
//            myIntent = new Intent(mContext, ProductDetails.class);
//            final String itemdetailsurl = "https://marketplace-1554111139715.appspot.com/itemDetails/"+finalItemId;
//            myIntent.putExtra("itemdetailsurl", itemdetailsurl);
//            imagesurl = "https://marketplace-1554111139715.appspot.com/photos/"+ URLEncoder.encode(finalTitle, "utf-8");
//            myIntent.putExtra("imagesurl", imagesurl);
//            final String similarurl = "https://marketplace-1554111139715.appspot.com/getSimilarItems/"+finalItemId;
//            myIntent.putExtra("similarurl", similarurl);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, itemdetailsurl,null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("ItemDetails", response.toString());
//                        //String itemDetails=response.toString();
//                        myIntent = new Intent(mContext, ProductDetails.class);
//                        myIntent.putExtra("ItemDetails", response.toString());
//                        Log.d("YOYOYO",ShippingInfo);
//                        myIntent.putExtra("ShippingInfo", ShippingInfo);
//                        getItemImages();
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                        error.printStackTrace();
//                    }
//                });
//        queue.add(getRequest);
//    }
//
//    private void getItemImages() {
//        String imagesurl=null;
//        try {
//            imagesurl = "https://marketplace-1554111139715.appspot.com/photos/"+URLEncoder.encode(finalTitle, "utf-8");
//            myIntent.putExtra("imagesurl", imagesurl);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, imagesurl,null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Images", response.toString());
//                        getSimilarItems();
//                        myIntent.putExtra("Images", response.toString());
//
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                        error.printStackTrace();
//                    }
//                });
//        queue.add(getRequest);
//    }
//
//    private void getSimilarItems() {
//
//        final String similarurl = "https://marketplace-1554111139715.appspot.com/getSimilarItems/"+finalItemId;
//        myIntent.putExtra("similarurl", similarurl);
//
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, similarurl,null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("similar", response.toString());
//                        myIntent.putExtra("similar", response.toString());
//
//                        mContext.startActivity(myIntent);
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                        error.printStackTrace();
//                    }
//                });
//        queue.add(getRequest);
//    }


}
