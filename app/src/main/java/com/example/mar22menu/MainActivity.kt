package com.example.mar22menu

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.mar22menu.databinding.ActivityMainBinding
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val SELECT_PICTURE = 2
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var thread: Thread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangeText.setOnClickListener {
            binding.tvHours.setText("Hello")
        }
        thread = Thread(countDownRunner)
        thread.start()

        binding.llDial.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            startActivity(intent)
        }
        binding.llMessaging.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
//            intent.setType("vnd.android-dir/mms-sms")
            intent.setData(Uri.parse("sms:"))
            startActivity(intent)
        }

        binding.llContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivity(intent)
        }
        binding.llCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, MainActivity.REQUEST_IMAGE_CAPTURE)
        }
        binding.llGallery.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
        }
//        binding.llMusic.setOnClickListener {
////            val intent = Intent()
////            intent.action = Intent.ACTION_VIEW
////            intent.setDataAndType(Uri.parse(YOUR_SONG_PATH), "audio/*")
////            startActivity(intent)
//            val sdcard = Environment.getExternalStorageDirectory()
//            val audioFile = File(sdcard.getPath() + "/Music/TruongVu.mp3")
//
//            val intent = Intent()
//            intent.action = Intent.ACTION_VIEW
//            intent.setDataAndType(Uri.fromFile(audioFile), "audio/*")
//            startActivity(intent)
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
//            val drawable=BitmapDrawable(resources,imageBitmap)
            binding.ivCamera.setImageBitmap(imageBitmap)
        }
        if (requestCode == MainActivity.SELECT_PICTURE && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                binding.ivGallery.setImageURI(selectedImageUri)
            }
        }
    }

    private fun changeHour2() {
        while (true) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val current = LocalDateTime.now().format(formatter)
            binding.tvHours.setText(current)
            Thread.sleep(1000)
            binding.tvHours.invalidate()
        }
    }

    private fun changeHour() {
        thread = Thread(runnable)
        thread.start()
//        runOnUiThread()
    }


    private var runnable = object : Runnable {
        override fun run() {
            while (true) {
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val current = LocalDateTime.now().format(formatter)
                binding.tvHours.setText(current)
//                Thread.sleep(1000)
                binding.tvHours.postInvalidate()
            }


        }
    }

    private var countDownRunner = object : Runnable {

        override fun run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    val dt: Date = Date()
                    val hours: Int = dt.getHours()
                    val minutes: Int = dt.getMinutes()
                    val seconds: Int = dt.getSeconds()
                    val curTime = "$hours:$minutes:$seconds"
                    binding.tvHours.setText(curTime)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}