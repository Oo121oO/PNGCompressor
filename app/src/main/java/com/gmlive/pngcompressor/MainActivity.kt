package com.gmlive.pngcompressor

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gmlive.pngquant.LibPngQuant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //请求权限
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 0)
        } else {
            startCompress()
        }
        //加载原图
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeFile("/sdcard/tmp/goldfish.png")
            launch(Dispatchers.Main) {
                this@MainActivity.original.setImageBitmap(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startCompress()
    }

    private fun startCompress() {
        val start = System.currentTimeMillis()
        GlobalScope.launch(Dispatchers.IO) {
            val test = File("/sdcard/tmp/goldfish.png")
            val result = PngUtils.getTempFile(this@MainActivity, "result", ".png")
            val ret = LibPngQuant.pngQuantFile(test, result)
            val time = System.currentTimeMillis() - start
            val msg = "pngQuantFile:$ret, before=${test.length()}, after=${result.length()}, time=$time"
            Log.d("MainActivity", msg)
            val bitmap = BitmapFactory.decodeFile(result.absolutePath)
            launch(Dispatchers.Main) {
                this@MainActivity.result.text = msg
                this@MainActivity.compress.setImageBitmap(bitmap)
            }
        }
    }
}