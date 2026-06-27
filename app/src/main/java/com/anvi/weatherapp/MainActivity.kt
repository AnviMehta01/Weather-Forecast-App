package com.anvi.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.anvi.weatherapp.repository.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val apiKey = "09d731bbf4fc63d6df764a52cf7622b4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var city by remember { mutableStateOf("") }
            var result by remember { mutableStateOf("Enter a city") }

            MaterialTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Weather App",
                        fontSize = 28.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Enter City") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            if (city.isBlank()) {
                                result = "Please enter a city"
                                return@Button
                            }

                            lifecycleScope.launch {

                                try {

                                    val response = RetrofitInstance.api.getWeather(
                                        city = city.trim(),
                                        apiKey = apiKey
                                    )

                                    if (response.isSuccessful && response.body() != null) {

                                        val data = response.body()!!

                                        result =
                                            "📍 City: ${data.name}\n\n" +
                                                    "🌡 Temperature: ${data.main.temp}°C\n\n" +
                                                    "💧 Humidity: ${data.main.humidity}%\n\n" +
                                                    "☁ Weather: ${data.weather[0].description}"

                                    } else {

                                        result = response.errorBody()?.string()
                                            ?: "Unknown Error"

                                    }

                                } catch (e: Exception) {

                                    result = e.toString()

                                }

                            }

                        }
                    ) {
                        Text("Get Weather")
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = result,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}