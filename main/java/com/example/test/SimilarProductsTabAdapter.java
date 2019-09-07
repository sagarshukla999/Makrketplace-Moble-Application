package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.content.ContextCompat.startActivity;


public class SimilarProductsTabAdapter extends RecyclerView.Adapter<SimilarProductsTabAdapter.ImageViewHolder> {

    private JSONArray Allresults = null;
    public SimilarProductsTabAdapter(JSONArray Allresults ){
        this.Allresults = Allresults;
        Log.d("resultadapter", Allresults.toString());
    }
    private Context mContext;
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similarcard, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        mContext=parent.getContext();
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder viewHolder, int i) {

        //String Order = viewHolder.Order.getSelectedItem().toString();
        //Log.d("Order",Order);
        JSONObject obj=null;
        try {
            obj = Allresults.getJSONObject(i);
            String imgurl=obj.getString("imageURL");
            Picasso.get().load(imgurl).into(viewHolder.photo);
            String buyItNowPrice=obj.getString("Price");
            String shippingCost=obj.getString("shippingCost");
            String timeLeft=obj.getString("Days");
            String title=obj.getString("Name");
            //String viewItemURL=obj.getString("viewItemURL");

            if(shippingCost.equals("0.00")||shippingCost.equals("0")||shippingCost.equals("0.0")){
                viewHolder.shipping.setText("Free Shipping");
            }
            else{
                viewHolder.shipping.setText("$"+shippingCost);
            }
            if(timeLeft.equals('0')||timeLeft.equals("1")){
                viewHolder.days.setText(timeLeft+" Day Left" );
            }else{
                viewHolder.days.setText(timeLeft+" Days Left" );
            }

            viewHolder.cost.setText("$"+buyItNowPrice);
            if(title.length()>60){
                viewHolder.title.setText(title.substring(0,60).toUpperCase()+"...");
            }
            else{
                viewHolder.title.setText(title.toUpperCase());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject finalObj = obj;
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String viewItemURL= null;
                try {
                    viewItemURL = finalObj.getString("viewItemURL");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(viewItemURL));
                mContext.startActivity(browserIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Allresults.length();
    }



    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView title;
        TextView shipping;
        TextView days;
        TextView cost;
        CardView cardview;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            shipping=itemView.findViewById(R.id.shipping);
            days=itemView.findViewById(R.id.days);
            cost=itemView.findViewById(R.id.cost);
            photo=itemView.findViewById(R.id.imageid);
            cardview=itemView.findViewById(R.id.cv);
        }

    }


}



