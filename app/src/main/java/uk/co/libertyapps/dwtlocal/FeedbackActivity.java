package uk.co.libertyapps.dwtlocal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_REQUEST_CODE = 132;
    private ImageView imageView;
    public String imageFileName;
    public Uri imageToUploadUri, photoURI;
    private String product, prod_id, bakery, email, filter, mCurrentPhotoPath, score, param_name, comment, groupfilter;
    File photoFile;
    public boolean groupshot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_feedback);
        setTitle("Image feedback");

        SharedPreferences settings = getSharedPreferences("USER", Context.MODE_PRIVATE);
        prod_id = settings.getString("PRODUCT", "");
        product = settings.getString("PRODUCT_NAME", "");
        email = settings.getString("EMAIL", "");
        filter = settings.getString("FILTER", "");



        if (getIntent() != null) {
            Intent intent = getIntent();

            //todo need to pass score / param name / comment
            bakery = intent.getStringExtra("Bakery");
            score = intent.getStringExtra("Score");
            param_name = intent.getStringExtra("ParameterName");
            comment = intent.getStringExtra("Comment");
            groupshot = intent.getBooleanExtra("Groupshot", false);
            groupfilter = intent.getStringExtra("GroupFilter");

        }
//        TextView selected = (TextView) findViewById(R.id.result);
//        selected.setText(product + " " + prod_id);
        imageView = (ImageView) findViewById(R.id.outcome);

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), TestingActivity.class);
                //startActivity(intent);

                finish();
            }
        });


        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            takeImageNew();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("created file", mCurrentPhotoPath);
        return image;
    }



    public void takeImageNew() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            takeImage();
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            photoFile = null;
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    // Continue only if the File was successfully created
                    photoURI = FileProvider.getUriForFile(this,
                            "uk.co.libertyapps.dwtlocal.provider",
                            photoFile);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this,"Problem creating file. Try again",Toast.LENGTH_LONG).show();
                Log.d("ERROR", "file error" + ex.toString());
            }
        }
    }

    public void takeImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        photoFile = null;
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                // Continue only if the File was successfully created
                photoURI = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d("ERROR", "file error" + ex.toString());
            Toast.makeText(this,"Problem creating file. Try again",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeImageNew();
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }


    private class S3Example extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("doInBackground", "S3Example");

            /*// Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(), // Application Context
                    "eu-west-2:ced55db8-8049-4f6c-96f5-7095f69fbfb0", // Identity Pool ID
                    Regions.EU_WEST_2 // Region enum
            );

            AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
            File fileToUpload = new File(imageToUploadUri.getPath());

            //(Replace "MY-BUCKET" with your S3 bucket name, and "MY-OBJECT-KEY" with whatever you would like to name the file in S3)
            PutObjectRequest putRequest = new PutObjectRequest("warbies", imageFileName + ".jpg",
                    fileToUpload);
            PutObjectResult putResponse = s3Client.putObject(putRequest);*/

            return null;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult", "request = " + requestCode + " /  Result  = " + resultCode);
        Log.d("photoURI", String.valueOf(photoURI));

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("onActivityResult", "good cam & result");

            File f = new File(mCurrentPhotoPath);
            imageToUploadUri = Uri.fromFile(f);


            if(imageToUploadUri != null){
                Log.d("imageToUploadUri", String.valueOf(imageToUploadUri));

                Bitmap reducedSizeBitmap = getBitmap(f.getPath());
                if(reducedSizeBitmap != null){
                    imageView.setImageBitmap(reducedSizeBitmap);

                    S3Example bob = new S3Example();
                    bob.execute();

                } else {
                    Toast.makeText(this,"Problem creating file to upload",Toast.LENGTH_LONG).show();
                    Log.d("error", "Problem creating file to upload");
                }

            } else{
                Toast.makeText(this,"Could not find file to upload", Toast.LENGTH_LONG).show();
                Log.d("error", "Could not find file to upload");

            }
        }
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

         //   Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
         //           b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }


}
