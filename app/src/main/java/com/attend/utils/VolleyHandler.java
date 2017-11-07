package com.attend.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.attend.AppController;
import com.attend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ankit Joshi on 04-11-2017.
 */

public class VolleyHandler {
    private static VolleyHandler mInstance = new VolleyHandler();

    //Create a singleton
    public static synchronized VolleyHandler getInstance() {
        return mInstance;
    }

    //Create an object to return the server's response
    public class ApiResult {
        public Integer success;
        public String message;
        public Object data;

        //check what kind of data is returned in the json
        public boolean dataIsArray() {
            return (data != null && data instanceof JSONArray);
        }

        public boolean dataIsObject() {
            return (data != null && data instanceof JSONObject);
        }

        public boolean dataIsInteger() {
            return (data != null && data instanceof Integer);
        }

        //return the data properly casted
        public JSONArray getDataAsArray() {
            if (this.dataIsArray()) {
                return (JSONArray) this.data;
            } else {
                return null;
            }
        }

        public JSONObject getDataAsObject() {
            if (this.dataIsObject()) {
                return (JSONObject) this.data;
            } else {
                return null;
            }
        }

        public Integer getDataAsInteger() {
            if (this.dataIsInteger()) {
                return (Integer) this.data;
            } else {
                return null;
            }
        }

    }

    //create an interface with a callback method
    public interface ApiResponse<T> {
        public void onCompletion(T result);
    }

    //Create a get request with the url to query, and a callback
    public void RequestApi(String url, final ApiResponse<ApiResult> completion) {
        Log.v("Performing request: ", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        try {
                            Integer success = response.getInt("status");
                            res.data = response.getJSONObject("data");
                            res.message = response.getString("message");
                            res.success = success;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            res.success = -1;
                        }
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = -1;
                res.message = displayVolleyResponseError(error);
                completion.onCompletion(res);
            }
        }
        );

        //In case the server is a bit slow and we experience timeout errors﹕ error => com.android.volley.TimeoutError
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        AppController.getInstance().addToRequestQueue(jsonRequest);

    }

    public void RequestApiPOST(String url, final Map<String, String> params, final ApiResponse<ApiResult> completion) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        ApiResult res = new ApiResult();
                        try {
                            JSONObject response = new JSONObject(responseString);
                            Integer success = response.getInt("status");
                            res.success = success;
                            res.data = response.getJSONObject("data");
                            res.message = response.getString("message");
                        } catch (JSONException e) {
                            res.success = -1;
                            e.printStackTrace();
                        }
                        completion.onCompletion(res);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ApiResult res = new ApiResult();
                        res.success = -1;
                        res.message = displayVolleyResponseError(error);
                        completion.onCompletion(res);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        //Added for fb connect which timeout
        strRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        AppController.getInstance().addToRequestQueue(strRequest);
    }

    public void uploadImage(final Bitmap bitmap, String url, final ApiResponse<ApiResult> completion) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                ApiResult res = new ApiResult();
                res.success = -1;
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    res.data = result.getJSONObject("data");
                    res.success = result.getInt("status");
                    res.message = result.getString("message");
                    Log.i("Handler: ", String.valueOf(res.data));
                } catch (JSONException e) {
                    res.success = -1;
                    e.printStackTrace();
                }
                completion.onCompletion(res);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = -1;
                res.message = displayVolleyResponseError(error);
                completion.onCompletion(res);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart("profile.png", getBytesFromBitmap(bitmap), "image/png"));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(multipartRequest);
    }

    // convert from bitmap to byte array
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();
    }

    //TODO: This function is broken, error in retrieving strings
    private String displayVolleyResponseError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            return Resources.getSystem().getString(R.string.error_network_timeout);
        } else if (error instanceof AuthFailureError) {
            return Resources.getSystem().getString(R.string.error_auth_failure);
        } else if (error instanceof ServerError) {
            return Resources.getSystem().getString(R.string.error_server);
        } else if (error instanceof NetworkError) {
            return Resources.getSystem().getString(R.string.error_network);
        } else if (error instanceof ParseError) {
            return Resources.getSystem().getString(R.string.error_parse);
        } else {
            return Resources.getSystem().getString(R.string.error_unknown);
        }
    }

}
