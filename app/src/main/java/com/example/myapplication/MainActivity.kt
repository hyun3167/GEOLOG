package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.lang.Exception
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager //위치관리자 정보


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListner())

    }

    internal inner class locationListner : LocationListener {
        override fun onLocationChanged(p0: Location) {


            val dir = filesDir.absolutePath
            val filename = "geo.txt"


            val geocoder: Geocoder = Geocoder(this@MainActivity) //위치 -> 주소
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val curTime = dateFormat.format(Date(time))
            var list = geocoder.getFromLocation(p0.latitude, p0.longitude, 1) as List<Address> // 주소배열 -> list 최대
//            text1.text = "위도 : " + p0.latitude + "\n경도 : " + p0.longitude + "\n 주소 : " + list[0].getAddressLine(0) + "\n시간 :" + curTime + "\n"
            val contents = "위도 : " + p0.latitude + "\n경도 : " + p0.longitude + "\n 주소 : " + list[0].getAddressLine(0) + "\n시간 :" + curTime + "\n"
            writeTextFile(dir, filename, contents)
            val result = readTextFile(dir + "/" + filename)
            text1.text = result
            Log.d("파일내용", result)
        }


        fun readTextFile(fullpath: String): String {
            val file = File(fullpath)
            if (!file.exists()) return ""

            val reader = FileReader(file)
            val buffer = BufferedReader(reader)


            var temp:String? = ""
            var result = StringBuffer()

            while (true) {
                temp = buffer.readLine() // 줄단위로 읽어서 임시저장
                if (temp == null) break
                else result.append(temp + "\n")
            }
            buffer.close()
            return result.toString()
        }

        fun writeTextFile(directory: String, filename: String, content: String) {
            val dir = File(directory)


            if (!dir.exists()) {
                dir.mkdirs()
            }
            val fullpath = directory + "/" + filename
            val writer = FileWriter(fullpath, true)
            val buffer = BufferedWriter(writer)

            buffer.append(content)
            buffer.flush()
            buffer.close()

        }
    }
}









        //            try {
//                var output = openFileOutput("myFile.txt",Context.MODE_PRIVATE)
//                var dos = DataOutputStream(output)
//                dos.writeUTF("위도 : "+p0.latitude+"\n경도 : "+ p0.longitude+"\n 주소 : "+ list[0].getAddressLine(0)+"\n시간 :" + curTime+"\n")
//                dos.flush()
//                dos.close()
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//
//
//            button2.setOnClickListener{ view ->
//                try {
//                    var input = openFileInput("myFile.txt")
//                    var dis = DataInputStream(input)
//
//                    var value1 = dis.readUTF()
//                    dis.close()
//
//                    text1.append("${value1}\n")
//
//
//                }catch (e : Exception){
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//}