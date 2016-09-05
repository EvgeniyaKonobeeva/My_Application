package com.example.appwithfragment;

import com.example.appwithfragment.RecyclerViewFragment.ParserJSONTo;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void removeIsCorrect(){
        /*expected*/
        ArrayList<PhotoObjectInfo> expectedArr = new ArrayList<>();
        expectedArr.add(new PhotoObjectInfo("aaa1", "aaa2"));
        expectedArr.add(new PhotoObjectInfo("eee1", "eee2"));
        expectedArr.add(new PhotoObjectInfo("bbb1", "bbb2"));
        expectedArr.add(new PhotoObjectInfo("ccc1", "ccc2"));
        expectedArr.add(new PhotoObjectInfo("ddd1", "ddd2"));
        ArrayList<PhotoObjectInfo> arrayList = new ArrayList<>();
        arrayList.add(new PhotoObjectInfo("aaa1", "aaa2"));
        arrayList.add(new PhotoObjectInfo("eee1", "eee2"));
        arrayList.add(new PhotoObjectInfo("bbb1", "bbb2"));
        arrayList.add(new PhotoObjectInfo("ccc1", "ccc2"));
        arrayList.add(new PhotoObjectInfo("aaa1", "aaa2"));
        arrayList.add(new PhotoObjectInfo("aaa1", "aaa2"));
        arrayList.add(new PhotoObjectInfo("ddd1", "ddd2"));
        arrayList.add(new PhotoObjectInfo("eee1", "eee2"));
        arrayList.add(new PhotoObjectInfo("eee1", "eee2"));
        //ParserJSONTo.removeRepeatingElements(arrayList);


        assertEquals("TEST RESULT : ", expectedArr, arrayList);
    }
}