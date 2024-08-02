package com.example.stuheads

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var imageButton: ImageButton
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         imageButton = findViewById(R.id.imageButton)
        textView = findViewById(R.id.textResult)
        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,233)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==233 && resultCode== RESULT_OK)
        {
            val imageUri : Uri? = data?.data

            if (imageUri != null)
            {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
                imageButton.setImageBitmap(bitmap)
                detectFace(bitmap)
            }
             else
            {
                Toast.makeText(this, "Error retrieving images",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detectFace(bitmap: Bitmap)
    {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap,0)


        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...
                var resultText :String = " "
                var i = 1;

                for(face in faces)
                {
                    resultText= i.toString()
                    i++
                }
                if(faces.isEmpty())
                {
                    Toast.makeText(this,"NO FACES DETECTED",Toast.LENGTH_SHORT).show()
                }
                else{
                  //Toast.makeText(this, resultText,Toast.LENGTH_SHORT).show()
                    textView.text="CLASS STRENGTH\n$resultText"
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

    }

}