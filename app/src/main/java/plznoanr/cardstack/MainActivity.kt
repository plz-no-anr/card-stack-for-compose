package plznoanr.cardstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plznoanr.cardstack.ui.theme.CardstackforcomposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardstackforcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TestCardStack("Android")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestCardStack(name: String, modifier: Modifier = Modifier) {
    CardStack(
        cardCount = 3,
        cardShape = RoundedCornerShape(12.dp),
        cardElevation = 8.dp,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(getBackGround(it)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hello $name! \n$it")
        }
    }
}

private fun getBackGround(index: Int) = when (index) {
    0 -> Color.Red
    1 -> Color.Yellow
    2 -> Color.Green
    else -> Color.Black
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CardstackforcomposeTheme {
        TestCardStack("Android")
    }
}