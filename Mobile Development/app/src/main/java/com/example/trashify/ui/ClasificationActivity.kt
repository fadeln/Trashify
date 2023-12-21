package com.example.trashify.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.trashify.ml.ConvertedModel
import com.example.trashify.ui.ui_util.theme.GreenBtn1
import com.example.trashify.ui.ui_util.theme.Primary1
import com.example.trashify.ui.ui_util.theme.TrashifyTheme
import com.example.trashify.ui.ui_util.theme.WhiteCust
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Serializable
data class Tutorial(
    @SerialName("_id") val id: String,
    val Judul: String,
    val Bahan: List<String>,
    val Steps: Map<String, List<String>>,  // This line uses the Step class indirectly
    @SerialName("Link_Yt") val linkYt: String
)

class ClasificationActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrashifyTheme {
                var highestPercentage by remember {
                    mutableStateOf<String?>(null)
                }
                var predictionResult by remember {
                    mutableStateOf<String?>(null)
                }
                var selectedImageHD by remember {
                    mutableStateOf<Bitmap?>(null)
                }
                var tutorialData by remember { mutableStateOf<Tutorial?>(null) }
                var selectedImage by remember {
                    mutableStateOf<Bitmap?>(null)
                }
                val imageSize = 224
                val classes = arrayOf("Cardboard", "Glass", "Metal", "Organic", "Paper", "Plastic")
                var percentages by remember {
                    mutableStateOf<List<Float>>(emptyList())
                }
                var isTutorialDialogVisible by remember { mutableStateOf(false) }

                suspend fun getCraftingTutorial(material: String) {
                    val client = HttpClient(CIO) {
                        install(ContentNegotiation) {
                            json()
                        }
                    }
                    try {
                        tutorialData =
                            client.get("https://backend-dot-trashifycapstone.et.r.appspot.com/tutorial/${material.lowercase()}")
                                .body()

                    } catch (e: Exception) {
                        println(e)
                        e.printStackTrace()
                    } finally {
                        client.close()
                    }
                }

                val contentResolver: ContentResolver = LocalContext.current.contentResolver

                fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
                    return try {
                        val inputStream = contentResolver.openInputStream(uri)
                        BitmapFactory.decodeStream(inputStream)
                    } catch (e: IOException) {
                        // Handle specific exceptions related to decoding
                        e.printStackTrace()
                        null
                    } catch (e: Exception) {
                        // Handle other exceptions
                        e.printStackTrace()
                        null
                    }
                }

                fun classifyImage(image: Bitmap?) {
                    val model = ConvertedModel.newInstance(applicationContext)
                    // Creates inputs for reference.
                    val inputFeature0 =
                        TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                    val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                    byteBuffer.order(ByteOrder.nativeOrder())
                    val intValues = IntArray(imageSize * imageSize)
                    image?.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
                    var pixel = 0
                    // Iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                    for (i in 0 until imageSize) {
                        for (j in 0 until imageSize) {
                            val value = intValues[pixel++]
                            byteBuffer.putFloat(((value ushr 16 and 0xFF) * (1f / 255f)))
                            byteBuffer.putFloat(((value ushr 8 and 0xFF) * (1f / 255f)))
                            byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
                        }
                    }
                    inputFeature0.loadBuffer(byteBuffer)
                    // Runs model inference and gets result.
                    val outputs = model.process(inputFeature0)
                    val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                    // Update the confidence percentages
                    val confidences = outputFeature0.floatArray
                    percentages = confidences.map { (it * 100) }
                    highestPercentage = (percentages.maxOrNull() ?: 0.0).toString()
                    // Find the index of the class with the biggest confidence.
                    var maxPos = 0
                    var maxConfidence = 0f
                    for (i in confidences.indices) {
                        if (confidences[i] > maxConfidence) {
                            maxConfidence = confidences[i]
                            maxPos = i
                        }
                    }
                    predictionResult = classes[maxPos]
                    model.close()
                }

                fun startPredict(bitmapImage: Bitmap?) {
                    if (bitmapImage == null) {
                        return
                    }
                    val scaledBitmap =
                        Bitmap.createScaledBitmap(bitmapImage, imageSize, imageSize, false)
                    selectedImage = scaledBitmap
                    classifyImage(scaledBitmap)
                }

                val cameraLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
                        onResult = { bitmapImage ->
                            selectedImageHD = bitmapImage
                            startPredict(bitmapImage)
                        })
                val photoPickerLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri ->
                            val bitmapImage = uri?.let { uriToBitmap(contentResolver, it) }
                            selectedImageHD = bitmapImage
                            startPredict(bitmapImage)
                        })
                val cameraPermissionResultLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            if (isGranted) {
                                cameraLauncher.launch(null)
                            } else {
                                Toast.makeText(
                                    this@ClasificationActivity,
                                    "Camera permission required",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                // UI Part
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "screen1"
                ) {
                    composable("screen1") { entry ->

                        if (isTutorialDialogVisible) {
                            tutorialData?.let {
                                TutorialDialog(
                                    tutorialData = it,
                                    onClose = {
                                        isTutorialDialogVisible = false
                                    }
                                )
                            }
                        }

                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                                        containerColor = Primary1,
                                        titleContentColor = WhiteCust,
                                    ), title = {
                                        Text("Scan")
                                    })
                            },
                        ) { innerPadding ->
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(innerPadding),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box {
                                        if (selectedImage != null) {
                                            AsyncImage(
                                                model = selectedImageHD,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth(0.9f) // 90% of screen width
                                                    .fillMaxHeight(0.5f) // 50% of screen height
                                                    .clip(MaterialTheme.shapes.medium)


                                            )
                                        }
                                    }

                                    if (predictionResult != null) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "Classified as : ", style = TextStyle(
                                                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                                                ), modifier = Modifier.padding(top = 6.dp)
                                            )
                                            predictionResult?.let {
                                                Text(
                                                    text = it,
                                                    color = Color(0xFFC30000),
                                                    style = TextStyle(
                                                        fontSize = 27.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                )
                                            }
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                contentPadding = PaddingValues(horizontal = 32.dp)
                                            ) {
                                                items(classes.size) { index ->
                                                    Text(
                                                        text = "${classes[index]}: ${
                                                            String.format(
                                                                "%.2f", percentages[index]
                                                            )
                                                        }%"
                                                    )
                                                }
                                            }
                                        }
                                    }


                                    if (predictionResult == null) {
                                        Text(text = "No image selected")
                                    }

                                    Column {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (predictionResult != null) {
                                            Button(
                                                onClick = {
                                                    GlobalScope.launch(Dispatchers.Main) {
                                                        try {
                                                            navController.navigate("loadingScreen") {
                                                            }
                                                            getCraftingTutorial(predictionResult!!)
                                                            navController.navigate("screen tutorial") {
                                                            }
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                    }

                                                }, modifier = Modifier.width(350.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    GreenBtn1
                                                )

                                            ) {
                                                Text(
                                                    text = "Get Tutorial", style = TextStyle(
                                                        fontSize = 21.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = {
                                                cameraPermissionResultLauncher.launch(
                                                    Manifest.permission.CAMERA
                                                )
                                            },
                                            modifier = Modifier
                                                .width(350.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                GreenBtn1
                                            )
                                        ) {
                                            Text(
                                                text = "Take Picture", style = TextStyle(
                                                    fontSize = 21.sp, fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Button(
                                            onClick = {
                                                photoPickerLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            }, modifier = Modifier.width(350.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                GreenBtn1
                                            )
                                        ) {
                                            Text(
                                                text = "Launch Gallery", style = TextStyle(
                                                    fontSize = 21.sp, fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))

                                    }

                                }
                            }
                        }
                    }

                    //screen tutorial
                    composable("screen tutorial") {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    navigationIcon = {
                                        IconButton(onClick = {navController.navigate("screen1")
                                        },
                                            modifier = Modifier.clickable{}
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                                        containerColor = Primary1,
                                        titleContentColor = WhiteCust,
                                    ), title = {
                                        Text("Crafting Tutorial")
                                    }

                                )
                            },
                        ) { innerPadding ->

                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                LazyColumn{
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                                .padding(innerPadding),
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(1f) //90%
                                                    .fillMaxHeight(1f)
                                                    .height(LocalConfiguration.current.screenWidthDp.dp)//60%
                                            ){
                                                AsyncImage(
                                                    model = selectedImageHD,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                )
                                            }

                                            Text(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                text = predictionResult.let { result ->
                                                    highestPercentage.let { percentage ->
                                                        "$result : $percentage"
                                                    }
                                                } ?: "Default Text",
                                                style = TextStyle(
                                                    fontSize = 18.sp, // Set text size
                                                    fontWeight = FontWeight.Bold, // Set font weight
                                                    color = Color.Black // Set text color
                                                )
                                            )
                                            // isi data dari tutorial api
                                            tutorialData?.let { data ->
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    text = data.Judul,
                                                    style = TextStyle(
                                                        fontSize = 18.sp, // Set text size
                                                        fontWeight = FontWeight.Bold, // Set font weight
                                                        color = Color.Black // Set text color
                                                    )
                                                )
                                                TextList(texts = data.Bahan)
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    text = "Steps :",
                                                    style = TextStyle(
                                                        fontSize = 18.sp, // Set text size
                                                        fontWeight = FontWeight.Bold, // Set font weight
                                                        color = Color.Black // Set text color
                                                    )
                                                )
                                                // Modify the list by adding a prefix to each item
                                                val modifiedList =
                                                    data.Steps["Step 1"]?.mapIndexed { index, text -> "Step ${index + 1}: $text" }
                                                // Pass the modified list to the original TextList composable
                                                if (modifiedList != null) {
                                                    TextList(modifiedList)
                                                }
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    text = data.linkYt,
                                                    style = TextStyle(
                                                        fontSize = 18.sp, // Set text size
                                                        color = Color.Blue // Set text color
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    composable("loadingScreen") {
                        LaunchedEffect(true){
                            delay(200)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp),)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TextList(texts: List<String>) {
    Box(
        modifier = Modifier.padding(16.dp)
    ){
        Column {
            texts.forEach { text ->
                ListItem(text = text)
            }
        }
    }
}

@Composable
fun ListItem(
    text: String
) {
    Text(
        text = text,
        style = TextStyle(
            color = Color.Black, // Set text color
            fontSize = 16.sp, // Set text size
            fontStyle = FontStyle.Normal // Set font style
        ),
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun TutorialDialog(
    tutorialData: Tutorial,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(tutorialData.Judul)
        },
        text = {
            Text(tutorialData.Bahan.toString())
            Text("Steps : ")
            Text(tutorialData.Bahan.toString())
        },
        confirmButton = {
            Button(
                onClick = onClose,
            ) {
                Text("Close")
            }
        }
    )
}