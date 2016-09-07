package com.example.appwithfragment.FullScreenPicture;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appwithfragment.ImageLoader.MyImageLoader;
import com.example.appwithfragment.MyActivity;
import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.R;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    public static final String TAG = "FrgFullScreenPicture";
    private static final String keyListContent = "PhotoObjectInfo";
    private static final String keyIsLiked = "liked";
    private IOnLikePhotoListener onLikePhotoListener;
    private boolean isLiked = false;
    private ImageButton imgButton;
    private String url;


    public static FragmentFullScreenPicture newInstance(PhotoObjectInfo lc, MyActivity ctx, IOnLikePhotoListener onLikePhotoListener){
        Log.d("FragmentFullPicture", "Create single pic");
        FragmentFullScreenPicture f = new FragmentFullScreenPicture();

        ctx.setDrawerIndicatorEnabled(false);
        Bundle b = new Bundle();
        b.putSerializable(keyListContent, lc);
        b.putSerializable(MyActivity.keyContext, ctx);
        b.putSerializable(MyActivity.keyLikeListener, onLikePhotoListener);
        b.putBoolean(keyIsLiked, onLikePhotoListener.isLikedPhoto(lc));

        f.setArguments(b);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FragmentFullPicture", "onCreateView");
        MyActivity mainActivity=(MyActivity)getActivity();
        mainActivity.setDrawerIndicatorEnabled(false);
        View view = inflater.inflate(R.layout.single_pic_frag, null);

        setViews(view, R.id.fullImage, R.id.title);




        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FragmentFullPicture", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        //Log.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    public void setViews(View view, int imageViewId, int textViewId){

        ImageView imgView = (ImageView)view.findViewById(imageViewId);
        //imgView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final TextView txt =(TextView)view.findViewById(textViewId);

        MyImageLoader iml = new MyImageLoader((Context) getArguments().get(MyActivity.keyContext));

        final PhotoObjectInfo lc = (PhotoObjectInfo) getArguments().get(keyListContent);
        url = lc.getImgUrl();

        iml.setResourceUrl(url.replace("_m", ""));
        iml.setImgInto(imgView);

        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText(lc.getFullTitle());
            }
        });


        isLiked = (boolean)getArguments().get(keyIsLiked);
        onLikePhotoListener = (IOnLikePhotoListener)getArguments().get(MyActivity.keyLikeListener);

        imgButton = (ImageButton)view.findViewById(R.id.imageButton);
        if (isLiked) {
            imgButton.setImageResource(R.mipmap.favorite_heart_button);
        }

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLiked) {
                    imgButton.setImageResource(R.mipmap.heart);
                    onLikePhotoListener.removePhoto(lc);
                    isLiked = false;
                }
                else{
                    imgButton.setImageResource(R.mipmap.favorite_heart_button);
                    onLikePhotoListener.onLikePhotoListener(lc);
                    isLiked = true;
                }

            }
        });


    }




    @Override
    public void onPause() {
        super.onPause();
    }


}
