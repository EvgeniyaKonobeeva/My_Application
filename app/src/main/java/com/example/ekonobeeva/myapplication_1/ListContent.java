package com.example.ekonobeeva.myapplication_1;

import java.util.List;

/**
 * Created by e.konobeeva on 28.07.2016.
 */
public class ListContent {
    private String string;
    private int imRes;
    public ListContent(String str, int N){
        string = str;
        imRes = N;
    }
    public String getString(){
        return string;
    }
    public int getImRes(){
        return imRes;
    }
}
