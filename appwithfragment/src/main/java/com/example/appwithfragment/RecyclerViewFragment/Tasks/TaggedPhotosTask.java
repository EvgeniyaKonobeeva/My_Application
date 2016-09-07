package com.example.appwithfragment.RecyclerViewFragment.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.RecyclerViewFragment.GettingResults;
import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;
import com.example.appwithfragment.RetrofitPack.FlickrAPI;
import com.example.appwithfragment.RetrofitPack.Photo;
import com.example.appwithfragment.RetrofitPack.otherCategories.CategoriesPhoto;
import com.example.appwithfragment.RetrofitPack.otherCategories.Clusters.Cluster;
import com.example.appwithfragment.RetrofitPack.otherCategories.Clusters.ClustersTags;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Евгения on 18.08.2016.
 * таск , который соответствует одному фрагменту,для загрузки данных с сервера
 *
 */
public class TaggedPhotosTask extends AsyncTask<Object, Integer, ArrayList> {
    private static final String errorTag = "ERROR Task";
    private int loadingPhotosPerOnce = 50;
    private boolean photoEnds = false;


    private Map protocol;
    private Map protocolPhoto;
    private GettingResults fragment;
    private String tag;
    private int startPageNum;
    private String baseURL;


    public TaggedPhotosTask(GettingResults fragment, Map protocol, Map protocolPhoto, String tag, String baseURL) {
        this.fragment = fragment;
        this.protocol = protocol;
        this.tag = tag;
        this.baseURL = baseURL;
        this.protocolPhoto = protocolPhoto;
    }

    public TaggedPhotosTask() {
    }

    @Override
    protected ArrayList doInBackground(Object... voids) {
        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        if (!isCancelled()) {
            try {
                ArrayList<String> clustersId = getClustersIdArrayList(protocol);
                wholePhotosList = getPhotoArrayList(clustersId, tag);
            } catch (IOException ioEx) {
                Log.d("TaggedPhotosTask ER ", ioEx.toString());
            } catch (JSONException jsonEx) {
                Log.d("TaggedPhotosTask ER ", jsonEx.toString());
            }
        } else Thread.currentThread().interrupt();
        return wholePhotosList;
    }



    public void setFragment(GettingResults fragment) {
        this.fragment = fragment;
    }


    public ArrayList<String> getClustersIdArrayList(Map protocol) throws IOException {
        ArrayList<String> clusters_id = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);
        try {
            Call<ClustersTags> call = flickrAPI.getClustersTags(protocol, tag);

            Cluster[] cluster = call.execute().body().getClusters().getCluster();
            for(int i = 0; i < cluster.length; i++){
                clusters_id.add(cluster[i].getTag()[0].get_content());
            }
        } catch (IOException ioEx) {
            Log.d("TaggedPhotosTask", "getClustersIdArrayList " + ioEx.toString());
            onCancelled();
        }
        return clusters_id;
    }

    public ArrayList getPhotoArrayList(ArrayList<String> clusters_id, String tag) throws IOException, JSONException {

        int curCluster_id = fragment.getCurCluster_id();
        startPageNum = curCluster_id;

        ArrayList<PhotoObjectInfo> wholePhotosList = new ArrayList<>();
        ArrayList<PhotoObjectInfo> onePagePhotosList;

        int countLoadingPhotos = 0;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);

        while (countLoadingPhotos < loadingPhotosPerOnce) {
            if (curCluster_id < clusters_id.size()) {

                Call<CategoriesPhoto> call = flickrAPI.getPhotosTags(protocolPhoto, tag, clusters_id.get(curCluster_id++));
                Log.d("TaggedPhotosTask", call.request().url() + "");
                CategoriesPhoto categoriesPhoto = call.execute().body();
                Photo[] photo = categoriesPhoto.getPhotoss().getPhoto();
                onePagePhotosList = ParserJSONTo.PhotoArrayList(photo);
                addWHoleArrayToAnotherArray(onePagePhotosList, wholePhotosList);
                countLoadingPhotos += onePagePhotosList.size();

            } else {
                photoEnds = true;
                break;
            }
        }
        fragment.setCurCluster_id(curCluster_id);

        return wholePhotosList;

    }

    @Override
    protected void onPostExecute(ArrayList aVoid) {
        if (fragment != null && aVoid.size() != 0) {
            fragment.onGettingResult(aVoid, photoEnds);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("TaggedPhotosTask", "onCancelled");
        if (fragment != null) {
            fragment.setCurCluster_id(startPageNum);
        }
        super.onCancelled();
    }

    public void addWHoleArrayToAnotherArray(ArrayList putArr, ArrayList addToArr){
        for(int i = 0; i < putArr.size(); i++){
            addToArr.add(putArr.get(i));
        }
    }
}