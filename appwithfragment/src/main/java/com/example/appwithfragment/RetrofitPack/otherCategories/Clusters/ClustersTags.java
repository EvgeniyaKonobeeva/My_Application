package com.example.appwithfragment.RetrofitPack.otherCategories.Clusters;

/**
 * Created by e.konobeeva on 06.09.2016.
 *
 */

/*final String baseURL = "https://api.flickr.com";
            countClicks++;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                FlickrAPI flickrAPI = retrofit.create(FlickrAPI.class);
                Call<PhotosApi> call = flickrAPI.getPhotos("flickr.interestingness.getList", "b14e644ffd373999f625f4d2ba244522", "json", "1", "3", Integer.toString(countClicks));
                call.enqueue(new Callback<PhotosApi>() {
                    @Override
                    public void onResponse(Call<PhotosApi> call, Response<PhotosApi> response) {
                        response.body();
                        textView.append(response.body().toString() + "\n");
                        Photo[] photo = response.body().getPhotos().getPhoto();
                        textView.append("page " + countClicks + "\n");
                        for(int i = 0; i < photo.length; i++){
                            textView.append(photo[i].getTitle() + "\n");
                        }
                    }

                    @Override
                    public void onFailure(Call<PhotosApi> call, Throwable t) {
                        Log.d("Main", t.getMessage());
                    }
                });
        }
        });*/


public class ClustersTags {

    private Clusters clusters;


    
    public Clusters getClusters() {
        return clusters;
    }

    public void setClusters(Clusters clusters) {
        this.clusters = clusters;
    }




}
