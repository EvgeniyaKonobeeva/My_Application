package com.example.appwithfragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class MyFragment extends android.app.Fragment{
    private View view;
    private int n = 10;
    private Button conBut;
    private myOnClickListener listener;
    private Activity activity;
    private ArrayList<String> photoUrls;
    private RecyclerView recyclerView;
    List<ListContent> list;

    interface myOnClickListener{
        void doAction(String object);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listener = (myOnClickListener) this.getActivity();
        activity = this.getActivity();
        view = inflater.inflate(R.layout.fragment, null);

        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("PROCESS", "connection established");
            new LoadFromFlickrTasc().setParams(n, 1).execute();
        } else {
            Log.d("ERROR 0", "connection error");
        }


        //conBut = (Button)view.findViewById(R.id.getConnectionBut);
        //conBut.setOnClickListener(new MyListener());

        String[] text = new String[n];
        for(int i = 0; i< n; i++){
            text[i] = "text " + (i+1);
        }
        list = new ArrayList();
        ListContent lc;
        for(int i = 0; i < n; i++){
            lc = new ListContent(text[i]);
            list.add(lc);
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.rl);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(new RLAdapter(list));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                listener.doAction("element " + (position+1));
            }
        });



        //recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        return view;

    }



    class ConnectionToFlickr implements Runnable {


        private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
                "&format=json&nojsoncallback=1";

        private String key = "b14e644ffd373999f625f4d2ba244522";
        private String secret = "4610da2f4d81bcb9";

        private String per_page = "&per_page=";
        private String page = "&page=";
        private String date = "&date=2016-08-02";

        private String resultStr;

        public ConnectionToFlickr(int per_page, int page) {

            StringBuilder sb = new StringBuilder(protocol);
            sb.append(this.page + page);
            sb.append(this.per_page + per_page);
            sb.append(date);

            protocol = sb.toString();
        }

        public ConnectionToFlickr() {
        }


        @Override
        public void run() {
            try {
                final URL url = new URL(protocol);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                JSONArray jsa = getJSONInfo(connection).getJSONObject("photos").getJSONArray("photo");


                String query2 = "https://farm[farm_id].staticflickr.com/[server_id]/[ID]_[id_secret].jpg";
                photoUrls = new ArrayList<>();
                for (int i = 0; i < jsa.length(); i++) {
                    JSONObject obj = jsa.getJSONObject(i);

                    String farmId = obj.getString("farm");
                    String serverId = obj.getString("server");
                    String id = obj.getString("id");
                    String secret = obj.getString("secret");

                    String qq = query2.replace("[farm_id]", farmId).replace("[server_id]", serverId).replace("[ID]",id).replace("[id_secret]", secret);


                    photoUrls.add(qq);
                    Log.d("RESULT", qq);

                }

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        for(ListContent lc : list){
                            lc.setImRes(photoUrls.get(count++));
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });


            } catch (MalformedURLException mue) {
                Log.d("ERROR 1", mue.getMessage());
            } catch (IOException ioe) {
                Log.d("ERROR 2", ioe.getMessage());
            } catch (JSONException jse) {
                Log.d("ERROR 3", jse.toString());
            }
        }

        public JSONObject getJSONInfo(HttpURLConnection connection) throws IOException, JSONException {

            InputStream is = connection.getInputStream();
            StringBuffer buf = new StringBuffer();
            String photosData;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((photosData = reader.readLine()) != null) {
                buf.append(photosData);
            }

            //Log.d("STRING 1", buf.toString());
            is.close();
            connection.disconnect();
            return new JSONObject(buf.toString());

        }
    }
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("PROCESS", "connection established");
                new LoadFromFlickrTasc().setParams(n, 1).execute();
            } else {
                Log.d("ERROR 0", "connection error");
            }
        }
    }


    class LoadFromFlickrTasc extends AsyncTask<Void, Integer, Void>{
        private String protocol = " https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=b14e644ffd373999f625f4d2ba244522" +
                "&format=json&nojsoncallback=1";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(protocol);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                JSONArray jsa = getJSONInfo(connection).getJSONObject("photos").getJSONArray("photo");

                String query2 = "https://farm[farm_id].staticflickr.com/[server_id]/[ID]_[id_secret].jpg";
                photoUrls = new ArrayList<>();
                for (int i = 0; i < jsa.length(); i++) {
                    JSONObject obj = jsa.getJSONObject(i);

                    String farmId = obj.getString("farm");
                    String serverId = obj.getString("server");
                    String id = obj.getString("id");
                    String secret = obj.getString("secret");

                    String qq = query2.replace("[farm_id]", farmId).replace("[server_id]", serverId).replace("[ID]", id).replace("[id_secret]", secret);


                    photoUrls.add(qq);
                }

            }catch (IOException ioe){

            }catch (JSONException je){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int count = 0;
            for(ListContent lc : list){
                lc.setImRes(photoUrls.get(count++));
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        public LoadFromFlickrTasc setParams(int per_page, int pages){
            StringBuilder sb = new StringBuilder(protocol);
            sb.append("&per_page=" + per_page);
            sb.append("&page=" + pages);
            protocol = sb.toString();
            return this;
        }

        public JSONObject getJSONInfo(HttpURLConnection connection) throws IOException, JSONException {

            InputStream is = connection.getInputStream();
            StringBuffer buf = new StringBuffer();
            String photosData;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((photosData = reader.readLine()) != null) {
                buf.append(photosData);
            }

            //Log.d("STRING 1", buf.toString());
            is.close();
            connection.disconnect();
            return new JSONObject(buf.toString());

        }
    }

}
