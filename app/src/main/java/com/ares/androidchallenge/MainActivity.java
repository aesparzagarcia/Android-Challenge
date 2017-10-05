package com.ares.androidchallenge;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private final static String url = "https://api.chucknorris.io/jokes";
    private final static String categories = "/categories";
    private final static String random = "/random?category={category}";

    ListView listView;
    MainAdapter adapter;
    private List<String> jokes;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jokes = new ArrayList<>();
        this.setUpDialog();

        listView = (ListView) findViewById(R.id.jokes_list);

        getChuckCategoriesInfo();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getChuckRandomCategoryInfo(jokes.get(i));
            }
        });
    }

    private void setUpDialog(){
        // Config loading
        MaterialDialog.Builder progressBuilder = new MaterialDialog.Builder(this)
                .content("Cargando")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .progress(true, 0);

        progressDialog = progressBuilder.build();
    }

    private void getChuckCategoriesInfo(){
        progressDialog.show();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONArray json = new JSONArray(response.toString());
                            for(int i = 0; i < json.length(); i++){
                                jokes.add(json.get(i).toString());
                            }

                            adapter = new MainAdapter(getApplicationContext(), jokes);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"That didn't work! " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getChuckRandomCategoryInfo(final String category){
        progressDialog.show();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+random.replace("{category}",category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String joke = jsonObject.getString("value");
                            onShowPopUp(category.toUpperCase(), joke);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"That didn't work! " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void onShowPopUp(String category, String joke){
        new MaterialDialog.Builder(this)
                .title(category)
                .content(joke)
                .positiveText("Ok")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }
}
