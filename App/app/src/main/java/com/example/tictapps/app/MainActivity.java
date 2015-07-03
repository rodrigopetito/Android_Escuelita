package com.example.tictapps.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tictapps.app.BlurProject.StackBlurManager;
import com.example.tictapps.app.Utils.BlurImage;
import com.example.tictapps.app.Utils.ObservableScrollView;
import com.example.tictapps.app.Utils.ObservableScrollViewCallbacks;
import com.example.tictapps.app.Utils.ScrollState;


public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {


    private ImageView image;
    private TextView textView;
    private ObservableScrollView observableScrollView;
    private static final int WIDTH = 100;
    private static final int HEIGTH = 100;
    private Bitmap imageToBlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.text);
        observableScrollView = (ObservableScrollView) findViewById(R.id.observableScroll);
        observableScrollView.setScrollViewCallbacks(this);

        imageToBlur = decodeSampledBitmapFromResource(getResources(), R.drawable.pokeball, WIDTH, HEIGTH); //seteo imagen original al bitmap

//        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
//
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
//        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        image.setImageBitmap(imageToBlur);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        new Task().execute(scrollY);
        image.setTranslationY(scrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }



    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    private class Task extends AsyncTask<Integer, Void, StackBlurManager>{


        @Override
        protected StackBlurManager doInBackground(Integer... params) {
            image.setTranslationY(params[0]);
            StackBlurManager _stackBlurManager = new StackBlurManager(decodeSampledBitmapFromResource(getResources(), R.drawable.pokeball, WIDTH, HEIGTH));
            _stackBlurManager.process(params[0]/4);

            return _stackBlurManager;
        }

        @Override
        protected void onPostExecute(StackBlurManager param) {
            super.onPostExecute(param);
            image.setImageBitmap(param.returnBlurredImage());
        }

    }





}
