package com.example.visitchittorgarh.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// ─── Data Models ─────────────────────────────────────────────────────────────

data class CurrentWeather(
    val tempC: Int,
    val feelsLikeC: Int,
    val humidity: Int,
    val windKmph: Int,
    val uvIndex: Int,
    val visibility: Int,
    val pressure: Int,
    val description: String,
    val emoji: String,
    val sunrise: String,
    val sunset: String
)

data class DayForecast(
    val date: String,
    val dayLabel: String,
    val maxC: Int,
    val minC: Int,
    val description: String,
    val emoji: String,
    val avgHumidity: Int
)

// ─── Helper: map weather description to emoji ─────────────────────────────────

fun weatherEmoji(desc: String): String {
    val d = desc.lowercase()
    return when {
        "thunder" in d || "storm" in d  -> "⛈️"
        "rain" in d || "drizzle" in d   -> "🌧️"
        "snow" in d || "sleet" in d     -> "❄️"
        "fog" in d || "mist" in d       -> "🌫️"
        "overcast" in d                  -> "☁️"
        "cloudy" in d                    -> "⛅"
        "partly" in d                    -> "🌤️"
        "sunny" in d || "clear" in d    -> "☀️"
        "haze" in d || "smoke" in d     -> "🌁"
        "blizzard" in d                  -> "🌨️"
        else                             -> "🌡️"
    }
}

// ─── API Fetch ────────────────────────────────────────────────────────────────

suspend fun fetchChittorgarhWeather(): Pair<CurrentWeather, List<DayForecast>>? {
    return withContext(Dispatchers.IO) {
        try {
            val json = URL("https://wttr.in/Chittorgarh?format=j1").readText(Charsets.UTF_8)
            val root = JSONObject(json)

            val current = root.getJSONArray("current_condition").getJSONObject(0)
            val tempC = current.getString("temp_C").toIntOrNull() ?: 0
            val feelsLikeC = current.getString("FeelsLikeC").toIntOrNull() ?: 0
            val humidity = current.getString("humidity").toIntOrNull() ?: 0
            val windKmph = current.getString("windspeedKmph").toIntOrNull() ?: 0
            val uvIndex = current.getString("uvIndex").toIntOrNull() ?: 0
            val visibility = current.getString("visibility").toIntOrNull() ?: 0
            val pressure = current.getString("pressure").toIntOrNull() ?: 0
            val description = current.getJSONArray("weatherDesc")
                .getJSONObject(0).getString("value")

            val weatherArr = root.getJSONArray("weather")
            val todayAstro = weatherArr.getJSONObject(0)
                .getJSONArray("astronomy").getJSONObject(0)
            val sunrise = todayAstro.getString("sunrise")
            val sunset = todayAstro.getString("sunset")

            val cw = CurrentWeather(
                tempC = tempC,
                feelsLikeC = feelsLikeC,
                humidity = humidity,
                windKmph = windKmph,
                uvIndex = uvIndex,
                visibility = visibility,
                pressure = pressure,
                description = description,
                emoji = weatherEmoji(description),
                sunrise = sunrise,
                sunset = sunset
            )

            // 3-day forecast
            val dayNames = listOf("Today", "Tomorrow", "Day 3")
            val forecasts = (0 until minOf(3, weatherArr.length())).map { i ->
                val day = weatherArr.getJSONObject(i)
                val dateStr = day.getString("date")
                val maxC = day.getString("maxtempC").toIntOrNull() ?: 0
                val minC = day.getString("mintempC").toIntOrNull() ?: 0

                // Average humidity from hourly
                val hourly = day.getJSONArray("hourly")
                var humSum = 0
                var humCnt = 0
                var dayDesc = description
                for (h in 0 until hourly.length()) {
                    val hr = hourly.getJSONObject(h)
                    humSum += hr.getString("humidity").toIntOrNull() ?: 0
                    humCnt++
                    if (h == hourly.length() / 2) {
                        dayDesc = hr.getJSONArray("weatherDesc")
                            .getJSONObject(0).getString("value")
                    }
                }
                val avgHum = if (humCnt > 0) humSum / humCnt else 0

                // Format date label
                val cal = Calendar.getInstance()
                val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val parsedDate = try { fmt.parse(dateStr) } catch (e: Exception) { null }
                val dayLabel = when (i) {
                    0 -> "Today"
                    1 -> "Tomorrow"
                    else -> {
                        parsedDate?.let {
                            SimpleDateFormat("EEE, dd MMM", Locale.US).format(it)
                        } ?: dayNames[i]
                    }
                }

                DayForecast(
                    date = dateStr,
                    dayLabel = dayLabel,
                    maxC = maxC,
                    minC = minC,
                    description = dayDesc,
                    emoji = weatherEmoji(dayDesc),
                    avgHumidity = avgHum
                )
            }

            Pair(cw, forecasts)
        } catch (e: Exception) {
            null
        }
    }
}

// ─── Best time to visit data ──────────────────────────────────────────────────

data class SeasonInfo(
    val monthRange: String,
    val titleEn: String,
    val titleHi: String,
    val descEn: String,
    val descHi: String,
    val emoji: String,
    val color: Color,
    val isIdeal: Boolean
)

val chittorgarhSeasons = listOf(
    SeasonInfo(
        "Oct – Mar",
        "Best Season 🌟",
        "सर्वश्रेष्ठ मौसम 🌟",
        "Ideal weather 15–28°C. Cool days, clear skies — perfect for fort exploration and photography.",
        "आदर्श मौसम 15–28°C। ठंडे दिन, साफ आसमान — किले की यात्रा और फोटोग्राफी के लिए एकदम सही।",
        "🌿",
        Color(0xFF2E7D32),
        true
    ),
    SeasonInfo(
        "Apr – Jun",
        "Hot Summer",
        "गर्म गर्मी",
        "Very hot 38–45°C. Start visits early morning (6–9 AM) or late evening. Stay hydrated.",
        "बहुत गर्म 38–45°C। सुबह जल्दी (6–9 बजे) या देर शाम यात्रा करें। पानी पीते रहें।",
        "☀️",
        Color(0xFFF57F17),
        false
    ),
    SeasonInfo(
        "Jul – Sep",
        "Monsoon Season",
        "मानसून का मौसम",
        "Heavy rains, lush greenery 25–35°C. Fort looks magical but paths can be slippery.",
        "भारी बारिश, हरी-भरी हरियाली 25–35°C। किला जादुई लगता है पर रास्ते फिसलन भरे हो सकते हैं।",
        "🌧️",
        Color(0xFF1565C0),
        false
    )
)

// ─── Main Composable ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    var weather by remember { mutableStateOf<CurrentWeather?>(null) }
    var forecasts by remember { mutableStateOf<List<DayForecast>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Spin animation for refresh button
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing)),
        label = "refresh_spin"
    )

    // Determine gradient based on weather
    val bgGradient = remember(weather) {
        val d = weather?.description?.lowercase() ?: ""
        when {
            "thunder" in d || "storm" in d -> listOf(Color(0xFF1A0533), Color(0xFF2C1654))
            "rain" in d || "drizzle" in d  -> listOf(Color(0xFF0D2137), Color(0xFF1B3A5C))
            "overcast" in d || "cloudy" in d -> listOf(Color(0xFF1C1C2E), Color(0xFF2A2A3E))
            "sunny" in d || "clear" in d   -> listOf(Color(0xFF0D1B4A), Color(0xFF1A2F6B))
            else                            -> listOf(Color(0xFF0B1628), Color(0xFF162440))
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        hasError = false
        val result = fetchChittorgarhWeather()
        if (result != null) {
            weather = result.first
            forecasts = result.second
            hasError = false
        } else {
            hasError = true
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isEnglish) "Weather" else "मौसम",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Chittorgarh, Rajasthan",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Re-trigger LaunchedEffect not possible directly; user can swipe back and reopen
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = if (isLoading) GoldAccent else Color.White.copy(alpha = 0.7f),
                            modifier = if (isLoading) Modifier.rotate(spinAngle) else Modifier
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(bgGradient))
        ) {
            when {
                isLoading -> {
                    // Loading State
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = SaffronPrimary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isEnglish) "Fetching live weather..." else "लाइव मौसम ला रहे हैं...",
                                color = Color.White.copy(alpha = 0.8f),
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                hasError -> {
                    // Error / No internet State
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text("📡", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isEnglish) "No Internet Connection" else "इंटरनेट कनेक्शन नहीं है",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isEnglish) "Please check your connection and try again." else "कृपया अपना कनेक्शन जांचें और पुनः प्रयास करें।",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                weather != null -> {
                    val w = weather!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(bottom = 40.dp)
                    ) {
                        // ── CURRENT WEATHER HERO ──────────────────────────
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Location pin
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Chittorgarh Fort Area",
                                        color = GoldAccent,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                // Big emoji
                                Text(text = w.emoji, fontSize = 80.sp)

                                Spacer(modifier = Modifier.height(4.dp))

                                // Temperature
                                Text(
                                    text = "${w.tempC}°C",
                                    color = Color.White,
                                    fontSize = 72.sp,
                                    fontWeight = FontWeight.Thin,
                                    letterSpacing = (-2).sp
                                )

                                // Description
                                Text(
                                    text = w.description,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Feels like
                                Text(
                                    text = if (isEnglish) "Feels like ${w.feelsLikeC}°C" else "महसूस होता है ${w.feelsLikeC}°C",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // ── STATS GRID ────────────────────────────────────
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                WeatherStatCard(
                                    emoji = "💧",
                                    value = "${w.humidity}%",
                                    label = if (isEnglish) "Humidity" else "आर्द्रता",
                                    modifier = Modifier.weight(1f)
                                )
                                WeatherStatCard(
                                    emoji = "💨",
                                    value = "${w.windKmph} km/h",
                                    label = if (isEnglish) "Wind" else "हवा",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                WeatherStatCard(
                                    emoji = "☀️",
                                    value = "${w.uvIndex}",
                                    label = if (isEnglish) "UV Index" else "UV सूचकांक",
                                    modifier = Modifier.weight(1f)
                                )
                                WeatherStatCard(
                                    emoji = "👁️",
                                    value = "${w.visibility} km",
                                    label = if (isEnglish) "Visibility" else "दृश्यता",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                WeatherStatCard(
                                    emoji = "🌅",
                                    value = w.sunrise,
                                    label = if (isEnglish) "Sunrise" else "सूर्योदय",
                                    modifier = Modifier.weight(1f)
                                )
                                WeatherStatCard(
                                    emoji = "🌇",
                                    value = w.sunset,
                                    label = if (isEnglish) "Sunset" else "सूर्यास्त",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // ── 3-DAY FORECAST ─────────────────────────────────
                        item {
                            Text(
                                text = if (isEnglish) "3-DAY FORECAST" else "3 दिन का पूर्वानुमान",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp)
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(forecasts.size) { i ->
                                    ForecastCard(forecasts[i], isEnglish)
                                }
                            }
                        }

                        // ── TOURIST ADVISORY ────────────────────────────────
                        item {
                            val uvTip = when {
                                w.uvIndex >= 8 -> if (isEnglish)
                                    "🛡️ Very High UV today! Wear sunscreen SPF 50+ and a hat when visiting the fort."
                                else
                                    "🛡️ आज बहुत अधिक UV! किला देखते समय SPF 50+ सनस्क्रीन और टोपी पहनें।"
                                w.uvIndex >= 5 -> if (isEnglish)
                                    "😎 Moderate UV. Sunglasses recommended for the open fort area."
                                else
                                    "😎 मध्यम UV। खुले किले क्षेत्र में धूप के चश्मे की सलाह है।"
                                else -> if (isEnglish)
                                    "✅ Low UV. Great conditions for sightseeing!"
                                else
                                    "✅ कम UV। भ्रमण के लिए बेहतरीन स्थितियां!"
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.1f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = if (isEnglish) "TOURIST ADVISORY" else "पर्यटक सलाह",
                                        color = GoldAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = uvTip,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        // ── BEST TIME TO VISIT ─────────────────────────────
                        item {
                            Text(
                                text = if (isEnglish) "BEST TIME TO VISIT" else "आने का सही समय",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 10.dp)
                            )
                            chittorgarhSeasons.forEach { season ->
                                SeasonCard(season = season, isEnglish = isEnglish)
                            }
                        }

                        // Live data credit
                        item {
                            Text(
                                text = "🌐 Live data from wttr.in",
                                color = Color.White.copy(alpha = 0.35f),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Stat Card ────────────────────────────────────────────────────────────────

@Composable
private fun WeatherStatCard(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.10f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── Forecast Card ───────────────────────────────────────────────────────────

@Composable
private fun ForecastCard(forecast: DayForecast, isEnglish: Boolean) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.10f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = forecast.dayLabel,
                color = GoldAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = forecast.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${forecast.maxC}° / ${forecast.minC}°",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = forecast.description,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "💧 ${forecast.avgHumidity}%",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 10.sp
            )
        }
    }
}

// ─── Season Card ─────────────────────────────────────────────────────────────

@Composable
private fun SeasonCard(season: SeasonInfo, isEnglish: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (season.isIdeal)
                season.color.copy(alpha = 0.25f)
            else
                Color.White.copy(alpha = 0.08f)
        ),
        border = if (season.isIdeal)
            androidx.compose.foundation.BorderStroke(1.5.dp, season.color.copy(alpha = 0.7f))
        else
            androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(season.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = season.emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isEnglish) season.titleEn else season.titleHi,
                        color = if (season.isIdeal) season.color else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = season.monthRange,
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (isEnglish) season.descEn else season.descHi,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
