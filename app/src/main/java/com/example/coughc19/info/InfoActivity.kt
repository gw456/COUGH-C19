package com.example.coughc19.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coughc19.MainActivity
import com.example.coughc19.R
import com.example.coughc19.databinding.ActivityInfoBinding

class InfoActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityInfoBinding
    private val list: ArrayList<InfoEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInfo

        list.addAll(InfoData.listData)
        showRecyclerList()

        binding.btnBack.setOnClickListener(this)

    }

    private fun showRecyclerList() {
        binding.btnInfo.layoutManager = LinearLayoutManager(this)
        val infoAdapter = InfoAdapter(list)
        binding.btnInfo.adapter = infoAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                val intent = Intent(this@InfoActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}