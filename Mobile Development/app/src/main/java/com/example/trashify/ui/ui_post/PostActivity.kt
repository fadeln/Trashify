package com.example.trashify.ui.ui_post

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trashify.ViewModelFactory
import com.example.trashify.data.response.ListPostItem
import com.example.trashify.databinding.ActivityPostBinding
import com.example.trashify.helper.AuthData
import com.example.trashify.model.AuthViewModel
import com.example.trashify.preferences.AuthPreferences
import com.example.trashify.preferences.dataStore
import com.example.trashify.ui.adapter.PostAdapter
import com.example.trashify.ui.ui_post.add_post.AddPostActivity
import com.example.trashify.ui.ui_post.view_model.PostViewModel
import com.example.trashify.ui.ui_post.view_model.ViewModelFactoryPost

class PostActivity : AppCompatActivity() {

    private var token: AuthData? = null
    private lateinit var binding: ActivityPostBinding
    private lateinit var adapter: PostAdapter
    private lateinit var storyViewModel : PostViewModel
    private lateinit var tokenViewModel: AuthViewModel

    companion object{
        const val TOKEN_INTENT_KEY = "TOKEN_KEY"
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK){
                adapter.refresh()
                adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
                    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                        if (positionStart == 0){
                            binding.rvPost.scrollToPosition(0)
                        }
                    }
                })
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PostAdapter()
        binding.rvPost.adapter = adapter

        val pref = AuthPreferences.getInstance(application.dataStore)
        tokenViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        token = if (Build.VERSION.SDK_INT >= 33){
            intent.getSerializableExtra(TOKEN_INTENT_KEY, AuthData::class.java)
        }else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra((TOKEN_INTENT_KEY)) as AuthData
        }

        storyViewModel = ViewModelProvider(this, ViewModelFactoryPost(this, token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRJLTgyVENTb0tpbk1uUTYiLCJpYXQiOjE3MDMwOTA5NDR9.MMFJx06LQ_ndzETprqTx-kOMB7cXKojY5WhZmuY8xNM"))[PostViewModel::class.java]

        getData()

        val layoutManager = LinearLayoutManager(this)
        binding.rvPost.layoutManager = layoutManager

        binding.fabAddPost.setOnClickListener{
            val intentDetail = Intent(this, AddPostActivity::class.java)
            intentDetail.putExtra(AddPostActivity.ADD_STORY_KEY, AuthData(nama = token?.nama, userId = token?.userId, token = token?.token))
            getResult.launch(intentDetail)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getData()
            Toast.makeText(this, "List stories refreshed", Toast.LENGTH_SHORT).show()
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun setStory(story: PagingData<ListPostItem>){
        adapter.submitData(lifecycle, story)
        adapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListPostItem) {

            }
        })
    }

    private fun getData(){
        storyViewModel.getPaging.observe(this){
            binding.swipeRefresh.isRefreshing = false
            if (it != null){
                Log.d("ISERROR", it.toString())
                setStory(it)

            } else {

            }
        }
    }
}