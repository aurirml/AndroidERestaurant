package fr.isen.aurianeramel.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.emeric.androiderestaurant.ui.theme.AndroidERestaurantTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.aurianeramel.androiderestaurant.basket.BasketActivity


enum class DishType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.entree)
            MAIN -> stringResource(id = R.string.plat)
            DESSERT -> stringResource(id = R.string.dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}

class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //getString(R.string.menu_starter)
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    SetupView(this)
                }
            }
        }
        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun dishPressed(dishType: DishType) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.CATEGROY_EXTRA_KEY, dishType)
        startActivity(intent)
    }

    override fun onPause() {
        Log.d("lifeCycle", "Home Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Home Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Home Activity - onDestroy")
        super.onDestroy()
    }
}

@Composable
fun SetupView(menu: MenuInterface) {
    Box(modifier = Modifier.fillMaxSize()) {
        Background()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(painterResource(R.drawable.logo), null)
            CustomButton(type = DishType.STARTER, menu)
            CustomButton(type = DishType.MAIN, menu)
            CustomButton(type = DishType.DESSERT, menu)
            PanierButton()
        }
    }
}

@Composable
fun CustomButton(type: DishType, menu: MenuInterface) {
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )
    TextButton(
        onClick = { menu.dishPressed(type) },
        modifier = Modifier
            .padding(16.dp)
            .size(width = 160.dp, height = 64.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            //containerColor = Color(0x80, 0x80, 0x80, 0x80)
            containerColor =Color.White.copy(alpha = 0.6f)
        )
        ,shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = type.title(),
            style = TextStyle(
                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp

            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(HomeActivity())
    }
}

@Composable
fun Background(){
    Box(modifier = Modifier
    ){
        Image(
            painter = painterResource(R.drawable.proto3),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PanierButton() {
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )

        val context = LocalContext.current
    TextButton(
        onClick = {
            val intent = Intent(context, BasketActivity::class.java)
            context.startActivity(intent) },
        modifier = Modifier
            .padding(16.dp)
            .size(width = 160.dp, height = 64.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            //containerColor = Color(0x80, 0x80, 0x80, 0x80)
            containerColor =Color.White.copy(alpha = 0.6f)
        )
        ,shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Panier",
            style = TextStyle(
                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp

            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}