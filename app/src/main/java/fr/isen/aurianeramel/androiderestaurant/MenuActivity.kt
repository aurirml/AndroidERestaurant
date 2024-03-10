package fr.isen.aurianeramel.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.aurianeramel.androiderestaurant.network.Category
import fr.isen.aurianeramel.androiderestaurant.network.Dish
import fr.isen.aurianeramel.androiderestaurant.network.MenuResult
import fr.isen.aurianeramel.androiderestaurant.network.NetworkConstants
import com.google.gson.GsonBuilder
import org.json.JSONObject

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = (intent.getSerializableExtra(CATEGROY_EXTRA_KEY) as? DishType) ?: DishType.STARTER

        setContent {

            MenuView(type)
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    override fun onPause() {
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Menu Activity - onDestroy")
        super.onDestroy()
    }

    companion object {
        val CATEGROY_EXTRA_KEY = "CATEGROY_EXTRA_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(type: DishType) {
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )
    val category = remember {
        mutableStateOf<Category?>(null)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Background()
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text(
                    text=type.title(),
                    style = TextStyle(fontFamily = zeldafont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp
                ),
                )
                        },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.6f),
                ),
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.wrapContentSize(Alignment.CenterEnd)
                    ) {
                        RetourButton()
                    }
                }
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                category.value?.let {
                    items(it.items) {
                        dishRow(it,type)
                    }
                }
            }
        }
        postData(type, category)
    }
}

@Composable fun dishRow(dish: Dish, type : DishType) {
        val context = LocalContext.current
        val zeldafont = FontFamily(
            Font(R.font.zelda, FontWeight(1))
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.6f)
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                    intent.putExtra(DetailActivity.DISH_INFO,type)
                    context.startActivity(intent)
                }
        ) {
            Column(            Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(240.dp)
                        .height(240.dp)
                        .clip(RoundedCornerShape(10))
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
                Text(
                    text=dish.name,
                    style = TextStyle(fontFamily = zeldafont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp
                    ),
                    modifier =  Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "${dish.prices.first().price} €",
                    style = TextStyle(fontFamily = zeldafont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp
                    ),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
}

@Composable
fun postData(type: DishType, category: MutableState<Category?>) {
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Request.Method.POST,
        NetworkConstants.URL,
        params,
        { response ->
            Log.d("request", response.toString(2))
            val result = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
            val filteredResult = result.data.first { categroy -> categroy.name == currentCategory }
            category.value = filteredResult
        },
        {
            Log.e("request", it.toString())
        }
    )

    queue.add(request)

}

@Composable
fun RetourButton() {
    val context = LocalContext.current
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )
    TextButton(
        onClick = { val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
                  },
        modifier = Modifier
            .padding(16.dp)
            .size(width = 80.dp, height = 32.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = Color(0x80, 0x80, 0x80, 0x80)
        )
        ,shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "<-",
            //style = MaterialTheme.typography.displaySmall,
            style = TextStyle(
                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp

            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}