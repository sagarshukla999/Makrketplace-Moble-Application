package com.example.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShippingTab extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shipping_tab, container, false);
        String itemdetai=((ProductDetails)getActivity()).itemdetails();
        //Log.d("The result is",itemdetails);

        JSONObject obj1 = null;
        JSONObject itemdetails= null;
        try {
            obj1 = new JSONObject(itemdetai);
            itemdetails = obj1.getJSONObject("Item");
            //itemDetails.Item.Seller.FeedbackScore
            //itemDetails.Item.Seller.PositiveFeedbackPercent
            //itemDetails.Item.Seller.FeedbackRatingStar
            //itemDetails.Item.Storefront.StoreName
            //itemDetails.Item.Storefront.StoreURL
            String FeedbackScore=itemdetails.getJSONObject("Seller").getString("FeedbackScore");
            String PositiveFeedbackPercent=itemdetails.getJSONObject("Seller").getString("PositiveFeedbackPercent");
            String FeedbackRatingStar=itemdetails.getJSONObject("Seller").getString("FeedbackRatingStar");

           if(itemdetails.has("Storefront")) {
               String StoreName = itemdetails.getJSONObject("Storefront").getString("StoreName");
               final String StoreURL = itemdetails.getJSONObject("Storefront").getString("StoreURL");
               String htmlString="<u>"+StoreName+"/u>";
               Log.d("StoreURL",StoreURL);
               TextView storename = view.findViewById(R.id.storeNameValue);
               storename.setText(Html.fromHtml(htmlString));

               storename.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StoreURL));
                       getContext().startActivity(browserIntent);
                   }
               });
           }
           else{
               TextView storename = view.findViewById(R.id.storeNameValue);
               storename.setVisibility(View.GONE);
           }
//            Log.d("FeedbackScore",FeedbackScore);
//            Log.d("PositiveFeedbackPercent",PositiveFeedbackPercent);
//            Log.d("FeedbackRatingStar",FeedbackRatingStar);
//            Log.d("StoreName",StoreName);


            TextView feedbackscore = view.findViewById(R.id.feedbackScoreValue);
            feedbackscore.setText(FeedbackScore);
            CircularScoreView circularScoreView = (CircularScoreView) view.findViewById(R.id.popularityValue);
            Float x=Float.parseFloat(PositiveFeedbackPercent);
            int a = Math.round(x);
            circularScoreView.setScore(a);
            String color = FeedbackRatingStar.replace("Shooting","");
            ImageView image = (ImageView) view.findViewById(R.id.feedbackStarValue);
            if(Integer.parseInt(FeedbackScore)>=10000){
                image.setImageResource(R.drawable.star_circle);
            }else{
                image.setImageResource(R.drawable.star_circle_outline);
            }
            //TextView PositiveFeedbackPercen = getView().findViewById(R.id.popularityValue);
            //PositiveFeedbackPercen.setText(PositiveFeedbackPercent);


            //selectedItemDetails.shippingInfo[0].shippingServiceCost
            //selectedItemDetails.shippingInfo[0].handlingTime
            //condition[].conditionDisplayName[0]
            //Log.d("shippingInfo",shippingInfo);

            JSONObject shippingInfoJson=new JSONObject(((ProductDetails)getActivity()).ShippingInfo());
            String shippingServiceCost=shippingInfoJson.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__").toString();
            String handlingTime=shippingInfoJson.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("handlingTime").getString(0);
            String Condition=shippingInfoJson.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
            String GlobalShipping=itemdetails.getString("GlobalShipping");
            Log.d("shippingServiceCost",shippingServiceCost);
            Log.d("handlingTime",handlingTime);
            Log.d("Condition",Condition);
            Log.d("GlobalShipping",GlobalShipping);
            TextView shippingServiceCos = view.findViewById(R.id.shippingCostValue);
            if(shippingServiceCost.equals("0.00")||shippingServiceCost.equals("0")||shippingServiceCost.equals("0.0")){
                shippingServiceCos.setText("Free Shipping");
            }
            else{
                shippingServiceCos.setText("$"+shippingServiceCost);
            }

            if(GlobalShipping.equals("true")){
                GlobalShipping="Yes";
            }
            else{
                GlobalShipping="No";
            }

            if(handlingTime.equals("0")||handlingTime.equals("1")){
                TextView handlingTimeValu = view.findViewById(R.id.handlingTimeValue);
                handlingTimeValu.setText(handlingTime+"day");
            }
            else{
                TextView handlingTimeValu = view.findViewById(R.id.handlingTimeValue);
                handlingTimeValu.setText(handlingTime+"days");
            }

            TextView globalShippingValu = view.findViewById(R.id.globalShippingValue);
            globalShippingValu.setText(GlobalShipping);

            TextView conditionValu = view.findViewById(R.id.conditionValue);
            conditionValu.setText(Condition);


            //itemDetails.Item.ReturnPolicy.ReturnsAccepted
            //itemDetails.Item.ReturnPolicy.ReturnsWithin
            //ShippingCostPaidBy
             //       Refund
            String ReturnsAccepted=itemdetails.getJSONObject("ReturnPolicy").getString("ReturnsAccepted");
            String ReturnsWithin=itemdetails.getJSONObject("ReturnPolicy").getString("ReturnsWithin");
            String ShippedBy=itemdetails.getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy");
            String RefundMode=itemdetails.getJSONObject("ReturnPolicy").getString("Refund");

            Log.d("ReturnsAccepted",ReturnsAccepted);
            Log.d("ReturnsWithin",ReturnsWithin);
            Log.d("ShippedBy",ShippedBy);
            Log.d("RefundMode",RefundMode);

            TextView ReturnsAccepte = view.findViewById(R.id.policyValue);
            ReturnsAccepte.setText(ReturnsAccepted);
            TextView returnsWithinValu = view.findViewById(R.id.returnsWithinValue);
            returnsWithinValu.setText(ReturnsWithin);
            TextView refundModeValu = view.findViewById(R.id.refundModeValue);
            refundModeValu.setText(RefundMode);
            TextView shippedByValu = view.findViewById(R.id.shippedByValue);
            shippedByValu.setText(ShippedBy);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}