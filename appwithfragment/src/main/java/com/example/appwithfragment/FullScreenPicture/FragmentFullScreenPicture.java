package com.example.appwithfragment.FullScreenPicture;

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

import com.example.appwithfragment.PhotoObjectInfo;
import com.example.appwithfragment.R;
import com.example.appwithfragment.TabsFragments.IOnLikePhotoListener;
import com.squareup.picasso.Picasso;

/**
 * Created by e.konobeeva on 02.08.2016.
 */
public class FragmentFullScreenPicture extends Fragment {
    public static final String TAG = "FrgFullScreenPicture";
    private static final String keyListContent = "PhotoObjectInfo";
    private static final String keyIsLiked = "liked";
    public static final String keyLikeListener = "likeListener";
    private IOnLikePhotoListener onLikePhotoListener;
    private boolean isLiked = false;
    private ImageButton imgButton;
    private String url;


    public static FragmentFullScreenPicture newInstance(PhotoObjectInfo lc, IOnLikePhotoListener onLikePhotoListener){
        Log.d("FragmentFullPicture", "Create single pic");
        FragmentFullScreenPicture f = new FragmentFullScreenPicture();

        Bundle b = new Bundle();
        b.putSerializable(keyListContent, lc);
        b.putSerializable(keyLikeListener, onLikePhotoListener);
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
        View view = inflater.inflate(R.layout.single_pic_frag, null);
        setViews(view, R.id.fullImage, R.id.title);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    public void setViews(View view, int imageViewId, int textViewId){

        ImageView imgView = (ImageView)view.findViewById(imageViewId);
        final TextView txt =(TextView)view.findViewById(textViewId);

        final PhotoObjectInfo lc = (PhotoObjectInfo) getArguments().get(keyListContent);
        url = lc.getImgUrl().replace("_m", "");

        Picasso.with(getActivity())
                .load(url)
                .error(R.mipmap.ic_launcher)
                .into(imgView);

        txt.post(new Runnable() {
            @Override
            public void run() {
                txt.setText(lc.getFullTitle());
            }
        });

        isLiked = (boolean)getArguments().get(keyIsLiked);
        onLikePhotoListener = (IOnLikePhotoListener)getArguments().get(keyLikeListener);

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
        Log.d(TAG, "onPause");
        super.onPause();
    }


}
