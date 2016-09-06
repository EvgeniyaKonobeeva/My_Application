package com.example.appwithfragment.RetrofitPack.otherCategories.Clusters;

/**
 * Created by e.konobeeva on 06.09.2016.
 */
public class Cluster {
    private int total;
    private Tag[] tag = new Tag[total];

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Tag[] getTag() {
        return tag;
    }

    public void setTag(Tag[] tag) {
        this.tag = tag;
    }
}
