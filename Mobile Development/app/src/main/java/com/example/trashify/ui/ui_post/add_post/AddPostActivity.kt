package com.example.trashify.ui.ui_post.add_post

import android.Manifest
import android.app.Activity
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.components.ButtonCustom
import com.example.trashify.databinding.ActivityAddPostBinding
import com.example.trashify.helper.AuthData
import com.example.trashify.helper.getImageUri
import com.example.trashify.helper.requestPermissionLauncher
import com.example.trashify.helper.uriToFile

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var thisButton: ButtonCustom
    private  lateinit var addStoryViewModel: AddPostViewModel
    private var currentImageUri: Uri? = null
    private var locationToReq: Location? = null
    private var token: AuthData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher(this, REQUIRED_PERMISSION)

        thisButton = binding.btnSubmit

        binding.iconBack.setOnClickListener {
            finish()
        }

        addStoryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AddPostViewModel::class.java]

        val token = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra(ADD_STORY_KEY, AuthData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(ADD_STORY_KEY) as AuthData
        }

        addStoryViewModel.story.observe(this){ result ->
            setButtonEnable(true)
//            Log.d("APA", result.toString())
            if (result.error == true){
                Toast.makeText(this@AddPostActivity, result.message, Toast.LENGTH_SHORT).show()
            } else {
//                storyViewModel.getAllStory(token?.token ?: "")
                Toast.makeText(this@AddPostActivity, result.message, Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        addStoryViewModel.isLoading.observe(this){
            setButtonLoading(it)
        }

        binding.btnCamera.setOnClickListener {
            openCamera()
        }

        binding.btnGallery.setOnClickListener {
            openGalerry()
        }

        binding.btnSubmit.setOnClickListener {
            uploadImage(token?.token ?: "")
        }
    }

    private fun setButtonEnable(value: Boolean) {
        thisButton.isEnabled = value
    }

    private fun setButtonLoading(value: Boolean){
        setButtonEnable(false)
        thisButton.setLoading(value)
    }

    private fun openGalerry() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadImage(token: String) {

        if (currentImageUri != null && binding.editDesc.text.isNotEmpty()){
            setButtonLoading(true)
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this)
                Log.d("ImageFile", "showImage: ${imageFile.path}")
                val description =  binding.editDesc.text.toString()
                addStoryViewModel.uploadImage(imageFile, description,  lon = locationToReq?.longitude, lat = locationToReq?.latitude, token = token)
            }
        } else {
            Log.d("HASIL", "${currentImageUri}  ${binding.editDesc.text.isNotEmpty()}")
            Toast.makeText(this@AddPostActivity, "Please select image and fill description", Toast.LENGTH_SHORT).show()
        }

    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("ImageURI", "showImage: $it")
            binding.imgPreview.setImageURI(it)
        }
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    companion object{
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val ADD_STORY_KEY = "ADD_STORY_KEY"
    }
}