package fr.isen.aurianeramel.androiderestaurant.basket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import fr.isen.aurianeramel.androiderestaurant.Background
import fr.isen.aurianeramel.androiderestaurant.R
import fr.isen.aurianeramel.androiderestaurant.RetourButton
import android.widget.Toast
import androidx.compose.material3.CardDefaults


class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }
    val zeldafont = FontFamily(
        Font(R.font.zelda, FontWeight(1))
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Background()
        Column(Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            TopAppBar(
                title = {
                    Text(
                        text = "Panier",
                        style = TextStyle(
                            fontFamily = zeldafont,
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


            LazyColumn {


                items(basketItems) {
                    BasketItemView(it, basketItems)

                }
            }
            basketItems.addAll(Basket.current(context).items)

            TextButton(
                onClick = {basketItems.clear()
                    Toast.makeText(context, "Merci pour votre commande !", Toast.LENGTH_SHORT).show()},
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


        }
    }
}

@Composable fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
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
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    Text(item.dish.name,
                        style = TextStyle(
                            fontFamily = zeldafont, // Utiliser la police de caractères zelda
                            fontWeight = FontWeight.Normal,
                            fontSize = 25.sp),)
                    Text("${item.dish.prices.first().price} €",
                        style = TextStyle(
                            fontFamily = zeldafont, // Utiliser la police de caractères zelda
                            fontWeight = FontWeight.Normal,
                            fontSize = 25.sp),)
                }

                Spacer(Modifier.weight(1f))
                Text(text=item.count.toString(),
                    style = TextStyle(
                        fontFamily = zeldafont, // Utiliser la police de caractères zelda
                        fontWeight = FontWeight.Normal,
                        fontSize = 25.sp),
                    modifier= Modifier.align(alignment = Alignment.CenterVertically)
                )
                Button(colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor =Color.White.copy(alpha = 0.6f)) ,onClick = {
                    val currentBasket = Basket.current(context)
                    // delete item and redraw view
                   /* Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)*/
                    if (item.count > 1) {
                        currentBasket.add(item.dish,-1,context)
                        basketItems.clear()
                        basketItems.addAll(currentBasket.items)
                    } else if (item.count == 1) {
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                    }
                    }



                ) {
                    Text("-",
                        style = TextStyle(
                        fontFamily = zeldafont, // Utiliser la police de caractères zelda
                        fontWeight = FontWeight.Normal,
                        fontSize = 32.sp),
                    )
        }
}
}
}