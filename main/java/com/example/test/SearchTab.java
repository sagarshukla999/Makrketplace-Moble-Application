package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SearchTab extends Fragment {
    RequestQueue queue;
    String currentZipcode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_layout, container, false);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

            getCurrentZip();

        CheckBox nearby = (CheckBox) view.findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableLocation(view);
            }

            });


        RadioButton current = (RadioButton) view.findViewById(R.id.current);
        RadioButton enterZipcode = (RadioButton) view.findViewById(R.id.enterZipcode);


        final AutoCompleteTextView autoCompleteTextView =
                view.findViewById(R.id.enteredZipcode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(adapter);

        final EditText enteredZipcode = (EditText)view.findViewById(R.id.enteredZipcode);
        enteredZipcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("Hi","Hi");
                //String zip=enteredZipcode.getText().toString();
                autoFillZipcode(autoCompleteTextView);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        enterZipcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText textzip = (EditText) getView().findViewById(R.id.enteredZipcode);
                RadioButton current = (RadioButton) getView().findViewById(R.id.current);
                current.setChecked(false);
                textzip.setEnabled(true);
                View v6 = getView().findViewById(R.id.zipError);
                v6.setVisibility(View.GONE);
            }

        });

        current.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText textzip = (EditText) getView().findViewById(R.id.enteredZipcode);
                RadioButton enterZipcode = (RadioButton) getView().findViewById(R.id.enterZipcode);
                enterZipcode.setChecked(false);
                textzip.setEnabled(false);
                textzip.getText().clear();
                View v6 = getView().findViewById(R.id.zipError);
                v6.setVisibility(View.GONE);
            }

        });

        Button search = (Button) view.findViewById(R.id.search);
        Button clear = (Button) view.findViewById(R.id.clear);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                searchItems(view);

            }

        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                clearForm(view);


            }

        });



        return view;
    }

    private void getCurrentZip() {
        final String url = "http://ip-api.com/json";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response", response.get("zip").toString());
                            currentZipcode=response.get("zip").toString();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void enableLocation(View view){
        CheckBox c = getView().findViewById(R.id.nearby);
        boolean checked=c.isChecked();

        if (checked) {
            View v1 = getView().findViewById(R.id.miles);
            v1.setVisibility(View.VISIBLE);
            View v2 = getView().findViewById(R.id.textView6);
            v2.setVisibility(View.VISIBLE);
            View v3 = getView().findViewById(R.id.current);
            v3.setVisibility(View.VISIBLE);
            View v4 = getView().findViewById(R.id.enterZipcode);
            v4.setVisibility(View.VISIBLE);
            View v5 = getView().findViewById(R.id.enteredZipcode);
            v5.setVisibility(View.VISIBLE);
            View v6 = getView().findViewById(R.id.zipError);
            v6.setVisibility(View.GONE);
        } else {
            View v1 = getView().findViewById(R.id.miles);
            v1.setVisibility(View.GONE);
            View v2 = getView().findViewById(R.id.textView6);
            v2.setVisibility(View.GONE);
            View v3 = getView().findViewById(R.id.current);
            v3.setVisibility(View.GONE);
            View v4 = getView().findViewById(R.id.enterZipcode);
            v4.setVisibility(View.GONE);
            View v5 = getView().findViewById(R.id.enteredZipcode);
            v5.setVisibility(View.GONE);

            View v6 = getView().findViewById(R.id.zipError);
            v6.setVisibility(View.GONE);
            EditText textzip = (EditText) getView().findViewById(R.id.enteredZipcode);
            RadioButton enterZipcode = (RadioButton) getView().findViewById(R.id.enterZipcode);
            enterZipcode.setChecked(false);
            textzip.setEnabled(false);
            RadioButton current = (RadioButton) getView().findViewById(R.id.current);
            current.setChecked(true);

            EditText m = (EditText) getView().findViewById(R.id.miles);
            m.getText().clear();
            EditText z = (EditText) getView().findViewById(R.id.enteredZipcode);
            z.getText().clear();

        }

    }

    public void searchItems(View view){
        //EditText keyword = (EditText) getView().findViewById(R.id.keyword);
        boolean error1;
        boolean error2=false;
        EditText keyword = (EditText) getView().findViewById(R.id.keyword);
        View v1 = getView().findViewById(R.id.keywordError);
        if (keyword.getText().toString().matches("")) {
            //keyword.setError("Input required");
            v1.setVisibility(View.VISIBLE);
            error1=true;
            Toast.makeText(getContext(),"Please fix all the fields with errors", Toast.LENGTH_SHORT).show();
        }
        else{
            error1=false;
            v1.setVisibility(View.GONE);
        }

        CheckBox c = getView().findViewById(R.id.nearby);
        boolean checked=c.isChecked();

        if (checked) {
            RadioButton enterZipcode = (RadioButton) getView().findViewById(R.id.enterZipcode);
            if(enterZipcode.isChecked()){
                EditText enteredZipcode = (EditText) getView().findViewById(R.id.enteredZipcode);
                View v2 = getView().findViewById(R.id.zipError);
                if (enteredZipcode.getText().toString().matches("")) {
                    v2.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Please fix all the fields with errors", Toast.LENGTH_SHORT).show();
                    error2=true;
                }
                else if(enteredZipcode.getText().toString().length()!=5){
                    v2.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Please fix all the fields with errors", Toast.LENGTH_SHORT).show();
                    error2=true;
                }
                else if (!enteredZipcode.getText().toString().matches("[0-9]+") ){
                    v2.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Please fix all the fields with errors", Toast.LENGTH_SHORT).show();
                    error2=true;
                }
                else{
                    v2.setVisibility(View.GONE);
                    error2=false;
                }
            }
        }

        String[] categoryvalues=new String[]{"AllCategories","550","2984","267","11450","58058","26395","11233","1249"};

        if(error1||error2){return;}

        final String KeywordVal=keyword.getText().toString();
        Spinner Category = (Spinner)getView().findViewById(R.id.Category);
        String CategoryVal = categoryvalues[Category.getSelectedItemPosition()];

        CheckBox unspecified = getView().findViewById(R.id.unspecified);
        String UnspecifiedVal=Boolean.toString(unspecified.isChecked());

        CheckBox used = getView().findViewById(R.id.used);
        String UsedVal=Boolean.toString(used.isChecked());
        CheckBox new_w = getView().findViewById(R.id.new_w);
        String NewVal=Boolean.toString(new_w.isChecked());
        CheckBox local = getView().findViewById(R.id.local);
        String LocalVal=Boolean.toString(local.isChecked());
        CheckBox free = getView().findViewById(R.id.free);
        String FreeVal=Boolean.toString(free.isChecked());

        EditText miles = (EditText) getView().findViewById(R.id.miles);
        String DistanceVal=miles.getText().toString();
        String ZipVal;
        CheckBox nearby = getView().findViewById(R.id.nearby);
        if(nearby.isChecked()){
            RadioButton current = getView().findViewById(R.id.current);
            if(current.isChecked()){
                ZipVal=currentZipcode;
            }
            else{
                EditText enteredZipcode = (EditText) getView().findViewById(R.id.enteredZipcode);
                ZipVal=enteredZipcode.getText().toString();
            }
        }
        else{
            ZipVal=currentZipcode;
        }
        String currentZipVal=currentZipcode;
        final String url = "https://marketplace-1554111139715.appspot.com/itemResult?Distance="+DistanceVal+"&New="+NewVal+"&Shipping=%7B%22Free%22:"+FreeVal+",%22Local%22:"+LocalVal+"%7D&Unspecified="+UnspecifiedVal+"&Used="+UsedVal+"&Zip="+ZipVal+"&category="+CategoryVal+"&currentZip="+currentZipVal+"&keyword="+KeywordVal;

        Intent myIntent = new Intent(getActivity(), SearchResult.class);
        myIntent.putExtra("url", url);
        myIntent.putExtra("Keyword", KeywordVal);
        Log.d("Hi",url);
        startActivity(myIntent);


//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //try {
//                            Log.d("Response", response.toString());
//                            //currentZipcode=response.get("zip").toString();
//                        //}
//                        //catch (JSONException e) {
//                            //e.printStackTrace();
//                        //}
//                        Intent myIntent = new Intent(getActivity(), SearchResult.class);
//                        myIntent.putExtra("response", response.toString());
//                        myIntent.putExtra("Keyword", KeywordVal);
//
//                        //myIntent.putExtra("key", value); //Optional parameters
//                        startActivity(myIntent);
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
    }
    public void clearForm(View view){
        EditText keyword = (EditText) getView().findViewById(R.id.keyword);
        keyword.getText().clear();
        CheckBox unspecified = (CheckBox) getView().findViewById(R.id.unspecified);
        CheckBox used = (CheckBox) getView().findViewById(R.id.used);
        CheckBox new_w = (CheckBox) getView().findViewById(R.id.new_w);
        unspecified.setChecked(false);
        used.setChecked(false);
        new_w.setChecked(false);

        CheckBox local = (CheckBox) getView().findViewById(R.id.local);
        CheckBox free = (CheckBox) getView().findViewById(R.id.free);
        local.setChecked(false);
        free.setChecked(false);

        CheckBox nearby = (CheckBox) getView().findViewById(R.id.nearby);
        nearby.setChecked(false);
        //enableLocation(view);
        Spinner Category=(Spinner) getView().findViewById(R.id.Category);
        Category.setSelection(0,true);
        this.enableLocation(view);

        View v1 = getView().findViewById(R.id.keywordError);
        v1.setVisibility(View.GONE);
    }

    public void autoFillZipcode(final AutoCompleteTextView autocomplete){
        //if(zip.length()<3 || zip.length()>4){return;}
        Log.d("ziplength",Integer.toString(autocomplete.length()));
        //if(zip.length()<3){return;}
        final String url = "https://marketplace-1554111139715.appspot.com/getZipcodes/"+autocomplete.getText().toString();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());

                        JSONArray ja = null;
                        try {
                            ja = (JSONArray) response.get("postalCodes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<String> arr = new ArrayList<String>();
                        for(int i=0;i<ja.length();i++){
                            JSONObject rec = null;
                            try {
                                rec = ja.getJSONObject(i);
                                arr.add(rec.getString("postalCode"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getActivity(), android.R.layout.select_dialog_item, arr);
                        Log.d("Response", "YOYOYO");
                            autocomplete.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //}


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