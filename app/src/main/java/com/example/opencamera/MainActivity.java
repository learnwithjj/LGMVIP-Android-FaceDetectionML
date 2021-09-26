package com.example.opencamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static int result_code=155;
    Button button;
    ImageView imageView;
    TextView resultView;
    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == result_code) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            detectFace(photo);
        } else {
            Toast.makeText(MainActivity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
        public void detectFace(Bitmap photo)
        {
            FirebaseVisionFaceDetectorOptions options=new FirebaseVisionFaceDetectorOptions
                    .Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                    .build();

            try {
                image= FirebaseVisionImage.fromBitmap(photo);
                detector= FirebaseVision.getInstance().getVisionFaceDetector(options);
            } catch (Exception e) {
                e.printStackTrace();
            }

            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                            String result="";
                            for (FirebaseVisionFace face:firebaseVisionFaces)
                            {
                                result=result.concat("Smile : "+face.getSmilingProbability()*100+"%")
                                        .concat("Left eye open : "+face.getLeftEyeOpenProbability()*100 +"%")
                                        .concat("Right eye open"+face.getRightEyeOpenProbability()*100+"%");
                            }
                            if(firebaseVisionFaces.size()==0)
                            {
                                Toast.makeText(MainActivity.this, "No Face deteced!!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Bundle bundle=new Bundle();
                                bundle.putString(LOCFaceDetection.result_text,result);
                                DialogFragment resultDialog=new ResultDialog();
                                resultDialog.setArguments(bundle);
                                resultDialog.setCancelable(true);
                                resultDialog.show(getSupportFragmentManager(),LOCFaceDetection.result_dialog);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this ,"OOps something is wrong!!1", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        imageView=findViewById(R.id.imageView);
        resultView=findViewById(R.id.reusltview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                if(checkPermission())
                {
                    startActivityForResult(intent,result_code);
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},result_code);
                    startActivityForResult(intent,result_code);

                }
            }
        });




    }
    private boolean checkPermission()
    {
        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
            return false;
    }
}