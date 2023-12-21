package com.example.trashify.ui.ui_post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.trashify.databinding.ActivityDetailPostBinding
import com.example.trashify.helper.DetailData

class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }

        val detailData = if (Build.VERSION.SDK_INT >= 33){
            intent.getSerializableExtra(DETAIL_INTENT_KEY, DetailData::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(DETAIL_INTENT_KEY) as DetailData
        }

        binding.tvName.text = detailData?.nama
        binding.tvDescription.text = detailData?.description

        binding.imgPhotos.setOnClickListener{
            val intentDetail = Intent(this, ImageViewActivity::class.java)
            intentDetail.putExtra(ImageViewActivity.TAG, detailData?.image)
            startActivity(intentDetail)
        }

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(15))

        Glide.with(binding.root)
            .load(detailData?.image)
            .apply(requestOptions)
            .into((binding.imgPhotos))
    }

    companion object{
        const val DETAIL_INTENT_KEY ="DETAIL FOR POST"
    }
}