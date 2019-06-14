package com.example.gls_gallery;

import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.jsibbold.zoomage.ZoomageView;
        import com.squareup.picasso.Picasso;

        import java.io.ByteArrayOutputStream;
        import java.io.File;

public class Image_View extends AppCompatActivity {

    ImageView imageView;
    ImageView back;
    TextView ImageName;
    BitmapFactory.Options opts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__image);

        imageView = findViewById(R.id.image);
        back = findViewById(R.id.btn_back);
        ImageName = findViewById(R.id.tv_imageName);
        opts = new BitmapFactory.Options();

        String validateImageName = getIntent().getExtras().getString("imageName","");

        if(validateImageName.length() > 24){
            validateImageName = validateImageName.substring(0,24)+"..";
        }

        ImageName.setText(validateImageName);

        bitmapBackgroundTask task = new bitmapBackgroundTask();
        task.execute();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public int calculateSampleSize(int reqheight, int reqwidth, BitmapFactory.Options opts){
        int height = opts.outHeight;
        int width = opts.outWidth;
        int result = 0;
        if(height > reqheight || width > reqwidth){
            int rasioHeight = height/reqheight;
            int rasioWidth = width/reqwidth;
            result = (rasioHeight < rasioWidth)? rasioHeight:rasioWidth;
        }
        return result;
    }

    public class bitmapBackgroundTask extends AsyncTask<Void,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            String s = getIntent().getExtras().getString("imagePath");
            File image = new File(Uri.parse(s).getPath());
            Bitmap bitmap1;
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image.getAbsolutePath(),opts);
            opts.inSampleSize = calculateSampleSize(1000,1000,opts);
            opts.inJustDecodeBounds = false;
            bitmap1 = BitmapFactory.decodeFile(image.getAbsolutePath(),opts);

            return bitmap1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}




