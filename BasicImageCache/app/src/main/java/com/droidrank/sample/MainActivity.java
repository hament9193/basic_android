package com.droidrank.sample;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.droidrank.sample.models.FetchImageTask;
import com.droidrank.sample.models.ImageModel;
import com.droidrank.sample.models.Response;
import com.droidrank.sample.utils.ImageUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchImageTask.OnLoadListener{

    private Button previous, next;
    private ImageView imageView;
    private ArrayList<ImageModel> images = new ArrayList<>();
    int offset = 1;
    int currentPosition = 0;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageview);
        previous = (Button) findViewById(R.id.previous);
        //onclick of previous button should navigate the user to previous image
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                currentPosition -= 1;
                currentPosition = (currentPosition<0)?0:currentPosition;
                updateImageView();
            }
        });
        next = (Button) findViewById(R.id.next);
        //onclick of next button should navigate the user to next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition += 1;
                imageView.setImageBitmap(null);
                if(currentPosition==images.size()-5){
                    FetchImageTask fetchImageTask = new FetchImageTask(MainActivity.this, MainActivity.this);
                    fetchImageTask.execute(offset);
                }
                if(currentPosition==images.size()-1){
                    return;
                }
                updateImageView();
            }
        });

        FetchImageTask fetchImageTask = new FetchImageTask(this, this);
        fetchImageTask.execute(offset);
    }

    @Override
    public void onLoadComplete(Response response) {
        offset += 20;
        images.addAll(response.getImages());
        updateImageView();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "something went wrong",Toast.LENGTH_SHORT).show();
    }

    private void updateImageView(){
        if(images.size()==0){
            return;
        }
        final String imageUrl = images.get(currentPosition).getImageUrl();
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask();
        bitmapWorkerTask.execute(imageUrl);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap!=null) {
                imageView.setImageBitmap(bitmap);
            }else{
                BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask();
                bitmapWorkerTask.execute("https://s.yimg.com/pw/images/en-us/photo_unavailable_m.png");
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            bitmap = ImageUtils.getChche(MainActivity.this, params[0]);
            return bitmap;
        }
    }
}
