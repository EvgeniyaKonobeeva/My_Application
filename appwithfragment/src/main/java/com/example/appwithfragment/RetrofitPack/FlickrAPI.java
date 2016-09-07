package com.example.appwithfragment.RetrofitPack;


import com.example.appwithfragment.RetrofitPack.otherCategories.CategoriesPhoto;
import com.example.appwithfragment.RetrofitPack.otherCategories.Clusters.ClustersTags;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by e.konobeeva on 06.09.2016.
 */
public interface FlickrAPI {
    /*take photos interestingness*/
    @GET("/services/rest/")
    Call<PhotosApi> getInterestingPhotos(@QueryMap Map<String, String> params, @Query("per_page") String perPage, @Query("page") String page);

    /*take clusters tags*/
    @GET("/services/rest/")
    Call<ClustersTags> getClustersTags(@QueryMap Map<String, String> params, @Query("tag") String tag);

    @GET("/services/rest/")
    Call<CategoriesPhoto> getPhotosTags(@QueryMap Map<String, String> params, @Query("tag") String tag, @Query("cluster_id") String cluster_id);


}
