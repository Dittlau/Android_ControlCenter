package dittlau.ControlCenter;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


// Switch buttons:
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.view.View.OnClickListener;


import android.net.ConnectivityManager;
import java.util.HashMap;
import java.util.Map;

// Volley for HTTP
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;


// JSON stuff
import org.json.JSONArray;
import org.json.JSONObject;

public class Lights extends Fragment {

    public int app_progress = 50; // Replace with initial value sent from HUE
    public int server_progress = 00; // Replace with initial value sent from HUE

    private Switch light1_on_off;
    private Switch light2_on_off;
    private Switch light3_on_off;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        View view = inflater.inflate(R.layout.fragment_lights, container, false);


        setupLightButtons(view);

        setupSeekBars(view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Lights");

    }



    //////////////////////////////////////////
    //           Handle light buttons       //
    //////////////////////////////////////////

    public View setupLightButtons(View view) {

        // Button 1

        light1_on_off = (Switch) view.findViewById(R.id.light1_on_off);
        //set the switch to ON
        light1_on_off.setChecked(true);
        //attach a listener to check for changes in state
        light1_on_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    sendGET(getResources().getString(R.string.IP_arduino) + "/lights/1/on/");
                }else{
                    sendGET(getResources().getString(R.string.IP_arduino) + "/lights/1/off/");
                }
            }
        });

        // Button 2

        light2_on_off = (Switch) view.findViewById(R.id.light2_on_off);
        //set the switch to ON
        light2_on_off.setChecked(true);
        //attach a listener to check for changes in state
        light2_on_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){

                    JSONPUT(getString(R.string.IP_hue) + getString(R.string.Hue_light_command) + "/1/state", "on", true);
                }else{
                    JSONPUT(getString(R.string.IP_hue) + getString(R.string.Hue_light_command) + "/1/state", "on", false);
                }
            }
        });

        // Button 3

        light3_on_off = (Switch) view.findViewById(R.id.light3_on_off);
        // set the switch to ON
        light3_on_off.setChecked(true);
        // attach a listener to check for changes in state
        light3_on_off.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JSONPUT4(getString(R.string.IP_hue) + getString(R.string.Hue_light_command) + "/1/state", "on", true);
                }else{
                    JSONPUT4(getString(R.string.IP_hue) + getString(R.string.Hue_light_command) + "/1/state", "on", false);
                }
            }
        });



        return view;
    }

    //////////////////////////////////////////
    //           Handle seekbars       //
    //////////////////////////////////////////

    public View setupSeekBars(View view) {

        SeekBar seekBarLiving = (SeekBar) view.findViewById(R.id.light_seekbar1);
        final TextView progressAppText = (TextView) view.findViewById(R.id.progress_app_value);
        final TextView progressServerText = (TextView) view.findViewById(R.id.progress_server_value);

        // set the animation of progress on bar
        seekBarLiving.setProgress(app_progress);

        // set the value from app
        progressAppText.setText(String.valueOf(app_progress));

        // set the value from server
        progressAppText.setText(String.valueOf(server_progress));


        // main seekbar function
        seekBarLiving.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                app_progress = progresValue;
                progressAppText.setText(String.valueOf(app_progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressServerText.setText(String.valueOf(server_progress));
            }

        });
       return view;
    }



    //////////////////////////////////////////
    //           Handle WIFI calls          //
    //////////////////////////////////////////

    public boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check for network connections
        if (networkInfo != null && networkInfo.isConnected()) {
            // Toast.makeText(getActivity(), " Connected ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), " Not Connected ", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public void sendGET(String url) {
        checkInternetConnection();
        Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();

// Formulate the request and handle the response.
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                    }
                });

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }

    public void JSONGET(String url) {
        checkInternetConnection();
        Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();

// Formulate the request and handle the response.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", error.getMessage());
                    }
                });

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }

    // working method, without correct response
    public void JSONPUT(String url, String param, Boolean value) {
        checkInternetConnection();
        Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();

        // define parameters to put
        Map<String, Boolean> postParam = new HashMap<String, Boolean>();
        postParam.put(param, value);

        // Formulate the request and handle the response.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                    }
                });

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }

    //non-working example
    public void JSONPUT2(String url, String param, Boolean value) {
        checkInternetConnection();
        String fullUrl2 = url + "?" + param + "=" + value;

        Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();


        // Formulate the request and handle the response.
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray> (){

            @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                    }
                });

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }

    // StringRequest example
    public void JSONPUT3(String url, String param, Boolean value) {
        checkInternetConnection();
        //String fullUrl2 = url + "?" + param + "=" + value;

        Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();

        // define parameters to put
        Map<String, Boolean> postParam = new HashMap<String, Boolean>();
        postParam.put(param, value);

        // Formulate the request and handle the response.
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("on", "1");
                return params;
            };

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }


    // get example with parameters in url
    public void JSONPUT4(String url, String param, Boolean value) {
        checkInternetConnection();
        String fullUrl = url + "?" + param + "=" + "1";

        Toast.makeText(getActivity(), fullUrl, Toast.LENGTH_LONG).show();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        //Toast.makeText(getActivity(), String.valueOf(err), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error:", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", error.getMessage());
                    }
                });

        MyVolley.getInstance(getActivity()).addToRequestQueue(request);

    }
}