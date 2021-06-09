package com.example.coughc19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coughc19.databinding.ActivityDetectCoughBinding
import com.example.coughc19.databinding.ActivityMainBinding
import com.example.coughc19.info.InfoActivity
import com.example.coughc19.info.InfoAdapter
import com.example.coughc19.info.InfoEntity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val list: ArrayList<InfoEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDetect.setOnClickListener(this)
        binding.btnHow.setOnClickListener(this)
        binding.btnInfo.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_detect -> {
                val intent = Intent(this@MainActivity, DetectCoughActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@MainActivity, "Detect Cough", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_how -> {
                val intent = Intent(this@MainActivity, HowToActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@MainActivity, "How to Use", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_info -> {
                val intent = Intent(this@MainActivity, InfoActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@MainActivity, "Information", Toast.LENGTH_SHORT).show()
            }

        }
    }
}