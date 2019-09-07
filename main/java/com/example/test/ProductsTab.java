package com.example.test;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProductsTab extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.products_tab, container, false);



        String itemdetails=((ProductDetails)getActivity()).itemdetails();
        Log.d("The result is",itemdetails);


        JSONObject obj1 = null;
        JSONObject obj;
        try {
            obj1 = new JSONObject(itemdetails);
            obj = obj1.getJSONObject("Item");

            if(obj.has("PictureURL")) {
                JSONArray PictureURL = obj.getJSONArray("PictureURL");
                Log.d("URL", PictureURL.toString());
                String url1=PictureURL.getString(0);
//                LinearLayout l=view.findViewById(R.id.img);
//                for(int i=0;i<PictureURL.length();i++){
//                    ImageView imgview=new ImageView(getContext());
//                    Picasso.get().load(PictureURL.getString(i)).into(imgview);
//                    l.addView(imgview);
//                }
                ImageView imgview=view.findViewById(R.id.img);
                Picasso.get().load(url1).into(imgview);
            }else{
                //displayNone
            }
            //Picasso.get().load(imgurl).into(viewHolder.photo);
//            for(int i=0;i<PictureURL.length();i++){
//
//                Log.d("URL",PictureURL.getString(i));
//            }

            if(obj.has("CurrentPrice")) {
                String Price = obj.getJSONObject("CurrentPrice").get("Value").toString();
                TextView P = (TextView) view.findViewById(R.id.cost);
                P.setText("$"+Price);
                TextView P1 = (TextView) view.findViewById(R.id.price);
                P1.setText("$"+Price);
            }

            //Log.d("CurrentPrice",Price);

            if(obj.has("Subtitle")) {
                String Subtitle = obj.getString("Subtitle");
                TextView sub = (TextView) view.findViewById(R.id.subtitle);
                sub.setText(Subtitle);
            }
            else{
                LinearLayout sub = (LinearLayout) view.findViewById(R.id.SubtitleLayout);
                sub.setVisibility(View.GONE);
            }
            //Log.d("Subtitle",Subtitle);


            String Title=obj.getString("Title");
            Log.d("Title",Title);
            TextView t = (TextView) view.findViewById(R.id.title);
            t.setText(Title);

            //String shippingInfo=((ProductDetails)getActivity()).ShippingInfo();
            Log.d("HelloShipping",((ProductDetails)getActivity()).ShippingInfo());
            JSONObject shippingInfoJson=new JSONObject(((ProductDetails)getActivity()).ShippingInfo());
            //if(shippingInfoJson.getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")) {
              String shippingInfo = shippingInfoJson.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__").toString();
                if(shippingInfo.equals("0.00")||shippingInfo.equals("0")||shippingInfo.equals("0.0")){
                    TextView s = (TextView) view.findViewById(R.id.shipping);
                    s.setText("With Free Shipping");
                }
                else{
                    TextView s = (TextView) view.findViewById(R.id.shipping);
                    s.setText("With $"+shippingInfo);
                }
            //}
            //else{
              //  TextView sub = (TextView) view.findViewById(R.id.shipping);
                //sub.setVisibility(View.GONE);
            //}



            String Brand=null;

            if(obj.has("ItemSpecifics")){
                JSONArray Specification=obj.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
                for(int i=0;i<Specification.length();i++){
                    String spec=Specification.getJSONObject(i).getJSONArray("Value").getString(0);
                    String name=Specification.getJSONObject(i).getString("Name");

                    String s1 = spec.substring(0, 1).toUpperCase();
                    spec = s1 + spec.substring(1);
                    //specification1.append("\u2022 "+spec+"\n");
                    if(name.equals("Brand")){
                        Brand=spec;
                    }
                    Log.d("Specification",name);
                    Log.d("Specification",spec);
                }
                if(Brand!=null) {
                    TextView s = (TextView) view.findViewById(R.id.brand);
                    s.setText(Brand);
                }
                else{
                    LinearLayout sub1 = (LinearLayout) view.findViewById(R.id.BrandLayout);
                    sub1.setVisibility(View.GONE);

                }
                TextView specification1 = (TextView) view.findViewById(R.id.specification);
                specification1.append("\u2022 "+Brand+"\n");
                for(int i=0;i<Specification.length();i++){
                    String spec=Specification.getJSONObject(i).getJSONArray("Value").getString(0);
                    String name=Specification.getJSONObject(i).getString("Name");

                    String s1 = spec.substring(0, 1).toUpperCase();
                    spec = s1 + spec.substring(1);

                    if(!name.equals("Brand")){
                        specification1.append("\u2022 "+spec+"\n");
                    }
                }
            }
            else {
                TextView sub = (TextView) view.findViewById(R.id.specification);
                sub.setVisibility(View.GONE);
                LinearLayout sub1 = (LinearLayout) view.findViewById(R.id.Specification);
                sub1.setVisibility(View.GONE);
            }

            //TextView specification1 = (TextView) view.findViewById(R.id.specification);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

}