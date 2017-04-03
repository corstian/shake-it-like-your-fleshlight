package co.boerman.chaker.common;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Corstian on 29/03/2017.
 */

public class HueHelper {
    public String Endpoint;
    public String Username;
    public int Lights;

    // PUT /api/username/lights/number/state

    private Context _context;

    public HueHelper(Context context) {
        _context = context;
    }

    public void setLight(boolean state, float brightness, float hue, float saturation) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("on", state);
        object.put("bri", brightness);
        object.put("hue", hue);
        object.put("sat", saturation);
        final String requestBody = object.toString();

        for (int i = 0; i < Lights; i++) {
            String url = Endpoint + "/api/" + Username + "/lights/" + i + "/state";

            RequestQueue queue = Volley.newRequestQueue(_context);
            StringRequest request = new StringRequest(
                    Request.Method.PUT,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // We don't give a fuck about it's response. Just change the fucking light.
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // We don't give a fuck about errors either.
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            };

            queue.add(request);
        }
    }
}
