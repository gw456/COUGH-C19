package com.example.coughc19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.coughc19.databinding.ActivityHowToBinding

class HowToActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHowToBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                val intent = Intent(this@HowToActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}