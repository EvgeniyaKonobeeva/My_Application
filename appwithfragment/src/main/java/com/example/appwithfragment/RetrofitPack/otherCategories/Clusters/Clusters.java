package com.example.appwithfragment.RetrofitPack.otherCategories.Clusters;

/**
 * Created by e.konobeeva on 06.09.2016.
 */
public class Clusters {
    private int total;
    private Cluster[] cluster = new Cluster[total];

    public Cluster[] getCluster() {
        return cluster;
    }

    public void setCluster(Cluster[] cluster) {
        this.cluster = cluster;
    }



    public void setTotal(int total) {
        this.total = total;
    }


    public int getTotal() {
        return total;
    }


}
