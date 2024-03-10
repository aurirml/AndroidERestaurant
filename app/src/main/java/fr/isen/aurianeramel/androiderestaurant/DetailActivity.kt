package fr.isen.aurianeramel.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import fr.isen.aurianeramel.androiderestaurant.basket.Basket
import fr.isen.aurianeramel.androiderestaurant.basket.BasketActivity
import fr.isen.aurianeramel.androiderestaurant.network.Dish
import kotlin.math.max

class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        val dishty=(intent.getSerializableExtra(DISH_INFO) as? DishType)?: DishType.STARTER
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Background()
                val context = LocalContext.current
                val count = remember {
                    mutableIntStateOf(1)
                }
                val ingredient = dish?.ingredients?.map { it.name }?.joinToString(", ") ?: ""
                val prix = dish?.prices?.map { it.price }?.joinToString(", ") ?: ""
                val pagerState = rememberPagerState(pageCount = {
                    dish?.images?.count() ?: 0
                })
                val zeldafont = FontFamily(
                    Font(R.font.zelda, FontWeight(1))
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = { Text(
                            text=dish?.name ?: "",
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
                                RetourButtonHome(dishty)
                            }
                        }
                    )
                    HorizontalPager(state = pagerState) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(dish?.images?.get(it))
                                .build(),
                            null,
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            error = painterResource(R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                    Text(
                        ingredient,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            fontFamily = zeldafont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp

                        ),
                    )
                    Text(
                        prix+"€",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            fontFamily = zeldafont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp

                        ),
                    )

                    Row(

                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))




                        OutlinedButton(onClick = {
                            count.value = max(1, count.value - 1)},
                            colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor =Color.White.copy(alpha = 0.6f))
                            ) {
                            Text("-",
                                style = TextStyle(
                                    fontFamily = zeldafont, // Utiliser la police de caractères zelda
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 32.sp),
                                )
                        }
                        Text(
                            text=count.value.toString(),
                            style = TextStyle(
                                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp

                        ),

                        )
                        OutlinedButton(onClick = {
                            count.value = count.value + 1 },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor =Color.White.copy(alpha = 0.6f)
                            )




                        ) {
                            Text("+",
                                style = TextStyle(
                                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                                fontWeight = FontWeight.Normal,
                                fontSize = 32.sp),
                                )
                        }
                        Spacer(Modifier.weight(1f))
                    }

                    TextButton(
                        onClick = {                         if (dish != null) {
                            Basket.current(context).add(dish, count.value, context)
                            Toast.makeText(context, "Ajouter a votre panier !", Toast.LENGTH_SHORT).show()
                        } },
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
                            text = "Commander",
                            style = TextStyle(
                                fontFamily = zeldafont, // Utiliser la police de caractères zelda
                                fontWeight = FontWeight.Normal,
                                fontSize = 32.sp

                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    TextButton(
                        onClick = { val intent = Intent(context, BasketActivity::class.java)
                            context.startActivity(intent)},
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
            }
        }
    }



    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
        val DISH_INFO ="DISH_INFO"
    }
}

@Composable
fun RetourButtonHome(type: DishType) {
    val context = LocalContext.current
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )
    TextButton(
        onClick = { val intent = Intent(context, MenuActivity::class.java)
            intent.putExtra(MenuActivity.CATEGROY_EXTRA_KEY, type)
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