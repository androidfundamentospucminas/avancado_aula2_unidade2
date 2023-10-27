package com.walker.aula2modulo2androidavancado

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.walker.aula2modulo2androidavancado.ui.theme.Aula2Modulo2AndroidAvancadoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aula2Modulo2AndroidAvancadoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SideEffectExample()

                        Divider(thickness = 2.dp, color = Color.Black)

                        LaunchedEffectCoroutineCountdownTimer()
                    }
                }
            }
        }
    }
}

@Composable
fun SideEffectExample() {
    var counter by remember { mutableStateOf(0) }
    var counter2 by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(onClick = {
            counter++
            counter2+=2
        }) {
            Text(text = "Increment Counter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Counter: $counter")

        SideEffect {
            println("Counter was created: $counter")
            println("Counter2 was created: $counter2")
        }
    }
}

@Composable
fun LaunchedEffectCoroutineCountdownTimer() {
    var remainingTime by remember { mutableStateOf(10) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val notificationManager = NotificationManagerCompat.from(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Remaining Time: $remainingTime")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    while (remainingTime > 0) {
                        delay(1000)
                        remainingTime--
                    }
                }
            }
        ) {
            Text(text = "Start Countdown")
        }

        LaunchedEffect(key1 = remainingTime, block = {
            if (remainingTime == 0) {
                createNotificationChannel(context, "timer_channel", "Timer Channel")

                val notification = NotificationCompat.Builder(context, "timer_channel")
                    .setContentTitle("Countdown Timer")
                    .setContentText("Timer has reached zero!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build()

                notificationManager.notify(1, notification)

                // Comes back to 10
                remainingTime = 10
            }
        })
    }
}

fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
