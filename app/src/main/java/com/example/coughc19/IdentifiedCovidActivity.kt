package com.example.coughc19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import com.example.coughc19.databinding.ActivityHowToBinding
import com.example.coughc19.databinding.ActivityIdentifiedCovidBinding

class IdentifiedCovidActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityIdentifiedCovidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifiedCovidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                val intent = Intent(this@IdentifiedCovidActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}