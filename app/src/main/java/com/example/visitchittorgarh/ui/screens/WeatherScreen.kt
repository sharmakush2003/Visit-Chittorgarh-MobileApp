package com.example.visitchittorgarh.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// ═══════════════════════════════════════════════════════════════════════
// DATA MODELS & LOCATIONS
// ═══════════════════════════════════════════════════════════════════════

data class WeatherLocation(
    val nameEn: String,
    val nameHi: String,
    val lat: Double,
    val lon: Double,
    val stateEn: String
)

// The 41 districts of Rajasthan as requested
val weatherLocations = listOf(
    WeatherLocation("Chittorgarh", "चित्तौड़गढ़", 24.8887, 74.6269, "Rajasthan"),
    WeatherLocation("Ajmer", "अजमेर", 26.4499, 74.6399, "Rajasthan"),
    WeatherLocation("Alwar", "अलवर", 27.5530, 76.6346, "Rajasthan"),
    WeatherLocation("Balotra", "बालोतरा", 25.8322, 72.2422, "Rajasthan"),
    WeatherLocation("Banswara", "बांसवाड़ा", 23.5461, 74.4348, "Rajasthan"),
    WeatherLocation("Baran", "बारां", 25.1011, 76.5111, "Rajasthan"),
    WeatherLocation("Barmer", "बाड़मेर", 25.7532, 71.4181, "Rajasthan"),
    WeatherLocation("Beawar", "ब्यावर", 26.1014, 74.3184, "Rajasthan"),
    WeatherLocation("Bharatpur", "भरतपुर", 27.2152, 77.4930, "Rajasthan"),
    WeatherLocation("Bhilwara", "भीलवाड़ा", 25.3484, 74.6384, "Rajasthan"),
    WeatherLocation("Bikaner", "बीकानेर", 28.0229, 73.3119, "Rajasthan"),
    WeatherLocation("Bundi", "बूंदी", 25.4414, 75.6414, "Rajasthan"),
    WeatherLocation("Churu", "चूरू", 28.2917, 74.9667, "Rajasthan"),
    WeatherLocation("Dausa", "दौसा", 26.8920, 76.3310, "Rajasthan"),
    WeatherLocation("Deeg", "डीग", 27.4722, 77.3278, "Rajasthan"),
    WeatherLocation("Didwana-Kuchaman", "डीडवाना-कुचामन", 27.3997, 74.5667, "Rajasthan"),
    WeatherLocation("Dholpur", "धौलपुर", 26.6989, 77.8931, "Rajasthan"),
    WeatherLocation("Dungarpur", "डूंगरपुर", 23.8411, 73.7142, "Rajasthan"),
    WeatherLocation("Hanumangarh", "हनुमानगढ़", 29.5800, 74.3200, "Rajasthan"),
    WeatherLocation("Jaipur", "जयपुर", 26.9124, 75.7873, "Rajasthan"),
    WeatherLocation("Jaisalmer", "जैसलमेर", 26.9157, 70.9083, "Rajasthan"),
    WeatherLocation("Jalore", "जालौर", 25.3422, 72.6169, "Rajasthan"),
    WeatherLocation("Jhalawar", "झालावाड़", 24.5973, 76.1610, "Rajasthan"),
    WeatherLocation("Jhunjhunu", "झुंझुनू", 28.1289, 75.3995, "Rajasthan"),
    WeatherLocation("Jodhpur", "जोधपुर", 26.2389, 73.0243, "Rajasthan"),
    WeatherLocation("Karauli", "करौली", 26.4950, 77.0250, "Rajasthan"),
    WeatherLocation("Khairthal-Tijara", "खैरथल-तिजारा", 27.8011, 76.6341, "Rajasthan"),
    WeatherLocation("Kota", "कोटा", 25.1825, 75.8369, "Rajasthan"),
    WeatherLocation("Kotputli-Behror", "कोटपुतली-बहरोड़", 27.7022, 76.1922, "Rajasthan"),
    WeatherLocation("Nagaur", "नागौर", 27.2000, 73.7333, "Rajasthan"),
    WeatherLocation("Pali", "पाली", 25.7725, 73.3234, "Rajasthan"),
    WeatherLocation("Phalodi", "फलोदी", 27.1333, 72.3667, "Rajasthan"),
    WeatherLocation("Pratapgarh", "प्रतापगढ़", 24.0320, 74.7811, "Rajasthan"),
    WeatherLocation("Rajsamand", "राजसमंद", 25.0717, 73.8814, "Rajasthan"),
    WeatherLocation("Salumbar", "सलूम्बर", 24.1333, 74.0500, "Rajasthan"),
    WeatherLocation("Sawai Madhopur", "सवाई माधोपुर", 25.9928, 76.3525, "Rajasthan"),
    WeatherLocation("Sikar", "सीकर", 27.6018, 75.1396, "Rajasthan"),
    WeatherLocation("Sirohi", "सिरोही", 24.8826, 72.8684, "Rajasthan"),
    WeatherLocation("Sri Ganganagar", "श्रीगंगानगर", 29.9167, 73.8833, "Rajasthan"),
    WeatherLocation("Tonk", "टोंक", 26.1620, 75.7895, "Rajasthan"),
    WeatherLocation("Udaipur", "उदयपुर", 24.5854, 73.7125, "Rajasthan")
)

data class WeatherData(
    val currentTemp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val uvIndex: Double,
    val visibility: Double,
    val pressure: Double,
    val precipitation: Double,
    val cloudCover: Int,
    val weatherCode: Int,
    val sunrise: String,
    val sunset: String,
    val hourly: List<HourlyItem>,
    val daily: List<DailyItem>,
    val fetchTime: String
)

data class HourlyItem(
    val time: String,
    val hour: String,
    val temp: Double,
    val weatherCode: Int,
    val precipProb: Int
)

data class DailyItem(
    val dayLabel: String,
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val weatherCode: Int,
    val precipSum: Double,
    val uvMax: Double,
    val windMax: Double,
    val sunrise: String,
    val sunset: String
)

// ═══════════════════════════════════════════════════════════════════════
// WMO WEATHER CODE INTERPRETATION
// ═══════════════════════════════════════════════════════════════════════

fun wmoToEmoji(code: Int): String = when (code) {
    0            -> "☀️"
    1            -> "🌤️"
    2            -> "⛅"
    3            -> "☁️"
    45, 48       -> "🌫️"
    51, 53, 55   -> "🌦️"
    56, 57       -> "🌧️"
    61, 63       -> "🌧️"
    65           -> "🌊"
    71, 73       -> "🌨️"
    75, 77       -> "❄️"
    80, 81, 82   -> "🌦️"
    85, 86       -> "🌨️"
    95           -> "⛈️"
    96, 99       -> "⛈️"
    else         -> "🌡️"
}

fun wmoToDescEn(code: Int): String = when (code) {
    0            -> "Clear Sky"
    1            -> "Mainly Clear"
    2            -> "Partly Cloudy"
    3            -> "Overcast"
    45           -> "Foggy"
    48           -> "Icy Fog"
    51           -> "Light Drizzle"
    53           -> "Moderate Drizzle"
    55           -> "Dense Drizzle"
    61           -> "Slight Rain"
    63           -> "Moderate Rain"
    65           -> "Heavy Rain"
    71           -> "Slight Snowfall"
    73           -> "Moderate Snowfall"
    75           -> "Heavy Snowfall"
    80           -> "Slight Showers"
    81           -> "Moderate Showers"
    82           -> "Violent Showers"
    95           -> "Thunderstorm"
    96, 99       -> "Thunderstorm with Hail"
    else         -> "Unknown"
}

fun wmoToDescHi(code: Int): String = when (code) {
    0            -> "साफ आसमान"
    1            -> "मुख्यतः साफ"
    2            -> "आंशिक बादल"
    3            -> "पूरी तरह बादल"
    45           -> "कोहरा"
    48           -> "बर्फीला कोहरा"
    51           -> "हल्की बूंदाबांदी"
    53           -> "मध्यम बूंदाबांदी"
    55           -> "घनी बूंदाबांदी"
    61           -> "हल्की बारिश"
    63           -> "मध्यम बारिश"
    65           -> "भारी बारिश"
    71           -> "हल्की बर्फ"
    73           -> "मध्यम बर्फ"
    75           -> "भारी बर्फ"
    80           -> "हल्की फुहार"
    81           -> "मध्यम फुहार"
    82           -> "तेज फुहार"
    95           -> "आंधी तूफान"
    96, 99       -> "ओलों के साथ तूफान"
    else         -> "अज्ञात"
}

fun skyGradient(code: Int, hourOfDay: Int): List<Color> {
    val isNight = hourOfDay < 6 || hourOfDay >= 19
    val isDusk = hourOfDay in 17..18
    val isDawn = hourOfDay in 5..7
    return when {
        code >= 95 -> listOf(Color(0xFF07070F), Color(0xFF140D26), Color(0xFF22113A))
        code in 45..82 && isNight -> listOf(Color(0xFF0A0F14), Color(0xFF131E2A))
        code in 45..82 -> listOf(Color(0xFF1F2C39), Color(0xFF2E3E4F))
        isNight -> listOf(Color(0xFF030712), Color(0xFF091124), Color(0xFF0F1E3D))
        isDusk -> listOf(Color(0xFF120720), Color(0xFF401633), Color(0xFF6E2833), Color(0xFF904F2F))
        isDawn -> listOf(Color(0xFF091124), Color(0xFF3B2352), Color(0xFF753B3C), Color(0xFF9E7036))
        else -> listOf(Color(0xFF091524), Color(0xFF0F253F), Color(0xFF1A3B60), Color(0xFF2A5380))
    }
}

fun uvLabel(uv: Double, en: Boolean): String = when {
    uv <= 2  -> if (en) "Low" else "कम"
    uv <= 5  -> if (en) "Moderate" else "मध्यम"
    uv <= 7  -> if (en) "High" else "अधिक"
    uv <= 10 -> if (en) "Very High" else "बहुत अधिक"
    else     -> if (en) "Extreme" else "अत्यधिक"
}

fun uvColor(uv: Double): Color = when {
    uv <= 2  -> Color(0xFF4CAF50)
    uv <= 5  -> Color(0xFFFFEB3B)
    uv <= 7  -> Color(0xFFFF9800)
    uv <= 10 -> Color(0xFFE53935)
    else     -> Color(0xFF9C27B0)
}

fun windDirLabel(deg: Int): String {
    val dirs = listOf("N","NE","E","SE","S","SW","W","NW")
    return dirs[((deg + 22.5) / 45).toInt() % 8]
}

// ─── Network Fetch: Live Weather ─────────────────────────────────────────────

suspend fun fetchOpenMeteoWeather(lat: Double, lon: Double): WeatherData? = withContext(Dispatchers.IO) {
    try {
        val url = "https://api.open-meteo.com/v1/forecast?" +
            "latitude=$lat&longitude=$lon" +
            "&current=temperature_2m,relative_humidity_2m,apparent_temperature," +
            "precipitation,weather_code,cloud_cover,wind_speed_10m," +
            "wind_direction_10m,uv_index,visibility,surface_pressure" +
            "&hourly=temperature_2m,relative_humidity_2m,precipitation_probability,weather_code" +
            "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum," +
            "sunrise,sunset,uv_index_max,wind_speed_10m_max" +
            "&timezone=Asia%2FKolkata&forecast_days=7"

        val raw = URL(url).readText(Charsets.UTF_8)
        val json = JSONObject(raw)

        val cur = json.getJSONObject("current")
        val currentCode = cur.getInt("weather_code")

        // Parse hourly
        val hourlyJson = json.getJSONObject("hourly")
        val hourlyTimes = hourlyJson.getJSONArray("time")
        val hourlyTemps = hourlyJson.getJSONArray("temperature_2m")
        val hourlyPrecip = hourlyJson.getJSONArray("precipitation_probability")
        val hourlyCodes = hourlyJson.getJSONArray("weather_code")

        val nowCal = Calendar.getInstance()
        val nowHour = nowCal.get(Calendar.HOUR_OF_DAY)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        val hourlyItems = mutableListOf<HourlyItem>()
        for (i in 0 until minOf(48, hourlyTimes.length())) {
            val timeStr = hourlyTimes.getString(i)
            val parsed = try { sdf.parse(timeStr) } catch (e: Exception) { null }
            if (parsed != null) {
                val cal = Calendar.getInstance().apply { time = parsed }
                val h = cal.get(Calendar.HOUR_OF_DAY)
                val label = when {
                    h == nowHour && i < 24 -> if (nowHour == 0) "12 AM" else if (nowHour < 12) "${nowHour} AM" else if (nowHour == 12) "12 PM" else "${nowHour - 12} PM"
                    else -> {
                        when (h) {
                            0 -> "12 AM"; 12 -> "12 PM"
                            in 1..11 -> "$h AM"
                            else -> "${h - 12} PM"
                        }
                    }
                }
                hourlyItems.add(
                    HourlyItem(
                        time = timeStr,
                        hour = label,
                        temp = hourlyTemps.getDouble(i),
                        weatherCode = hourlyCodes.getInt(i),
                        precipProb = hourlyPrecip.optInt(i, 0)
                    )
                )
                if (hourlyItems.size >= 24) break
            }
        }

        // Parse daily
        val dailyJson = json.getJSONObject("daily")
        val dailyDates = dailyJson.getJSONArray("time")
        val dailyMaxT = dailyJson.getJSONArray("temperature_2m_max")
        val dailyMinT = dailyJson.getJSONArray("temperature_2m_min")
        val dailyCodes = dailyJson.getJSONArray("weather_code")
        val dailyPrecip = dailyJson.getJSONArray("precipitation_sum")
        val dailyUV = dailyJson.getJSONArray("uv_index_max")
        val dailyWind = dailyJson.getJSONArray("wind_speed_10m_max")
        val dailySunrise = dailyJson.getJSONArray("sunrise")
        val dailySunset = dailyJson.getJSONArray("sunset")

        val dailyFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val displayFmt = SimpleDateFormat("EEE, dd MMM", Locale.US)
        val timeFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)

        val dailyItems = (0 until minOf(7, dailyDates.length())).map { i ->
            val dateStr = dailyDates.getString(i)
            val parsed = try { dailyFmt.parse(dateStr) } catch (e: Exception) { null }
            val label = when (i) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> parsed?.let { displayFmt.format(it) } ?: dateStr
            }
            val srTime = dailySunrise.getString(i).let { s ->
                try { SimpleDateFormat("HH:mm", Locale.US).format(timeFmt.parse(s)!!) } catch (e: Exception) { "6:00" }
            }
            val ssTime = dailySunset.getString(i).let { s ->
                try { SimpleDateFormat("HH:mm", Locale.US).format(timeFmt.parse(s)!!) } catch (e: Exception) { "19:00" }
            }
            DailyItem(
                dayLabel = label, date = dateStr,
                maxTemp = dailyMaxT.getDouble(i), minTemp = dailyMinT.getDouble(i),
                weatherCode = dailyCodes.getInt(i), precipSum = dailyPrecip.getDouble(i),
                uvMax = dailyUV.getDouble(i), windMax = dailyWind.getDouble(i),
                sunrise = srTime, sunset = ssTime
            )
        }

        // Sunrise/Sunset for today
        val todaySrRaw = dailySunrise.getString(0)
        val todaySsRaw = dailySunset.getString(0)
        val displayTimeFmt = SimpleDateFormat("h:mm a", Locale.US)
        val srDisplay = try { displayTimeFmt.format(timeFmt.parse(todaySrRaw)!!) } catch (e: Exception) { "6:15 AM" }
        val ssDisplay = try { displayTimeFmt.format(timeFmt.parse(todaySsRaw)!!) } catch (e: Exception) { "7:20 PM" }

        val fetchTime = SimpleDateFormat("h:mm a", Locale.US).format(Date())

        WeatherData(
            currentTemp = cur.getDouble("temperature_2m"),
            feelsLike = cur.getDouble("apparent_temperature"),
            humidity = cur.getInt("relative_humidity_2m"),
            windSpeed = cur.optDouble("wind_speed_10m", 0.0),
            windDirection = cur.optInt("wind_direction_10m", 0),
            uvIndex = cur.optDouble("uv_index", 0.0),
            visibility = cur.optDouble("visibility", 10000.0) / 1000.0,
            pressure = cur.getDouble("surface_pressure"),
            precipitation = cur.getDouble("precipitation"),
            cloudCover = cur.getInt("cloud_cover"),
            weatherCode = currentCode,
            sunrise = srDisplay,
            sunset = ssDisplay,
            hourly = hourlyItems,
            daily = dailyItems,
            fetchTime = fetchTime
        )
    } catch (e: Exception) {
        null
    }
}

// ═══════════════════════════════════════════════════════════════════════
// MAIN WEATHER SCREEN
// ═══════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    var selectedLocation by remember { mutableStateOf(weatherLocations[0]) }
    var showSearchDialog by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var filteredLocations by remember { mutableStateOf(weatherLocations) }

    var weather by remember { mutableStateOf<WeatherData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    val hourOfDay = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }

    LaunchedEffect(selectedLocation, refreshTrigger) {
        isLoading = true; hasError = false
        val result = fetchOpenMeteoWeather(selectedLocation.lat, selectedLocation.lon)
        weather = result
        hasError = result == null
        isLoading = false
    }

    // Reset states when search dialog status is modified
    LaunchedEffect(showSearchDialog) {
        if (!showSearchDialog) {
            searchQuery = ""
            filteredLocations = weatherLocations
        }
    }

    // Local filter on the 41 Rajasthan districts (runs instantly, zero network call)
    LaunchedEffect(searchQuery) {
        val query = searchQuery.trim()
        filteredLocations = if (query.isEmpty()) {
            weatherLocations
        } else {
            weatherLocations.filter {
                it.nameEn.contains(query, ignoreCase = true) ||
                it.nameHi.contains(query, ignoreCase = true)
            }
        }
    }

    val gradient = remember(weather) {
        skyGradient(weather?.weatherCode ?: 0, hourOfDay)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "wx")
    val spinDeg by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "spin"
    )
    val loadPulse by infiniteTransition.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(900, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradient))
    ) {
        // Content list
        when {
            isLoading -> LoadingState(loadPulse, isEnglish)
            hasError  -> ErrorState(isEnglish) { refreshTrigger++ }
            weather != null -> WeatherContent(weather!!, isEnglish, hourOfDay)
        }

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showSearchDialog = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📍 ${if (isEnglish) selectedLocation.nameEn else selectedLocation.nameHi}",
                        color = Color.White, fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select City",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = if (isEnglish) "Tap to change district" else "जिला बदलने के लिए टैप करें",
                    color = SaffronPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = { refreshTrigger++ }) {
                Icon(
                    Icons.Default.Refresh, null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = if (isLoading) Modifier.rotate(spinDeg) else Modifier
                )
            }
        }

        // Local District Selection Dialog
        if (showSearchDialog) {
            AlertDialog(
                onDismissRequest = { showSearchDialog = false },
                title = {
                    Text(
                        text = if (isEnglish) "Select District in Rajasthan" else "राजस्थान का जिला चुनें",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    if (isEnglish) "Search district (e.g. Udaipur, Churu)..." else "जिला खोजें (जैसे Udaipur, Churu)...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                focusedLabelColor = SaffronPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier
                                .height(260.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(filteredLocations.size) { index ->
                                val loc = filteredLocations[index]
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            selectedLocation = loc
                                            showSearchDialog = false
                                            refreshTrigger++
                                        }
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("📍", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isEnglish) loc.nameEn else loc.nameHi,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (isEnglish) "Rajasthan, India" else "राजस्थान, भारत",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                            if (filteredLocations.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isEnglish) "No districts found" else "कोई जिला नहीं मिला",
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showSearchDialog = false }) {
                        Text(
                            text = if (isEnglish) "Close" else "बंद करें",
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// LOADING / ERROR STATES
// ═══════════════════════════════════════════════════════════════════════

@Composable
private fun LoadingState(pulse: Float, isEnglish: Boolean) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🌍", fontSize = (56 * pulse).sp)
            Spacer(Modifier.height(16.dp))
            Text(
                if (isEnglish) "Fetching live weather from\nOpen-Meteo (ECMWF powered)…"
                else "Open-Meteo से लाइव मौसम ला रहे हैं…",
                color = Color.White.copy(alpha = 0.9f), textAlign = TextAlign.Center,
                fontSize = 15.sp, lineHeight = 22.sp, fontFamily = FontFamily.Serif
            )
            Spacer(Modifier.height(20.dp))
            LinearProgressIndicator(
                color = SaffronPrimary,
                trackColor = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.width(200.dp).clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
private fun ErrorState(isEnglish: Boolean, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("📡", fontSize = 56.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                if (isEnglish) "Could not fetch weather\nCheck internet & try again"
                else "मौसम नहीं मिला\nइंटरनेट जांचें और दोबारा कोशिश करें",
                color = Color.White, textAlign = TextAlign.Center,
                fontSize = 18.sp, fontFamily = FontFamily.Serif, lineHeight = 26.sp
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)) {
                Text(if (isEnglish) "Retry" else "पुनः प्रयास", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// MAIN CONTENT
// ═══════════════════════════════════════════════════════════════════════

@Composable
private fun WeatherContent(w: WeatherData, isEnglish: Boolean, hourOfDay: Int) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 72.dp, bottom = 48.dp)
    ) {

        // ── HERO: Big temperature ──────────────────────────────────────────
        item {
            Column(
                Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(wmoToEmoji(w.weatherCode), fontSize = 88.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${w.currentTemp.toInt()}°",
                    color = Color.White, fontSize = 96.sp,
                    fontWeight = FontWeight.Thin, letterSpacing = (-4).sp
                )
                Text(
                    if (isEnglish) wmoToDescEn(w.weatherCode) else wmoToDescHi(w.weatherCode),
                    color = Color.White.copy(alpha = 0.9f), fontSize = 22.sp,
                    fontFamily = FontFamily.Serif, fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    if (isEnglish) "Feels like ${w.feelsLike.toInt()}°  •  H:${w.daily.firstOrNull()?.maxTemp?.toInt()}°  L:${w.daily.firstOrNull()?.minTemp?.toInt()}°"
                    else "महसूस ${w.feelsLike.toInt()}°  •  अधिक:${w.daily.firstOrNull()?.maxTemp?.toInt()}°  न्यून:${w.daily.firstOrNull()?.minTemp?.toInt()}°",
                    color = Color.White.copy(alpha = 0.75f), fontSize = 15.sp
                )
            }
            Spacer(Modifier.height(28.dp))
        }

        // ── HOURLY FORECAST ────────────────────────────────────────────────
        item {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column {
                    SectionLabel(if (isEnglish) "HOURLY FORECAST" else "प्रति घंटा पूर्वानुमान")
                    Spacer(Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(w.hourly.size) { i ->
                            HourlyCard(w.hourly[i])
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── 7-DAY FORECAST ─────────────────────────────────────────────────
        item {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column {
                    SectionLabel(if (isEnglish) "7-DAY FORECAST" else "7 दिन का पूर्वानुमान")
                    Spacer(Modifier.height(6.dp))
                    w.daily.forEachIndexed { i, day ->
                        DailyRow(day, isEnglish, w.daily.minOf { it.minTemp }, w.daily.maxOf { it.maxTemp })
                        if (i < w.daily.size - 1) HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── STATS 2×3 GRID ─────────────────────────────────────────────────
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        emoji = "💧", title = if (isEnglish) "HUMIDITY" else "आर्द्रता",
                        value = "${w.humidity}%",
                        sub = if (isEnglish) when { w.humidity > 70 -> "Feels humid" ; w.humidity > 40 -> "Comfortable" ; else -> "Very dry" }
                              else when { w.humidity > 70 -> "उमस भरा" ; w.humidity > 40 -> "आरामदायक" ; else -> "बहुत शुष्क" },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        emoji = "🌬️", title = if (isEnglish) "WIND" else "हवा",
                        value = "${w.windSpeed.toInt()} km/h",
                        sub = windDirLabel(w.windDirection),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    UVStatCard(uv = w.uvIndex, isEnglish = isEnglish, modifier = Modifier.weight(1f))
                    StatCard(
                        emoji = "👁️", title = if (isEnglish) "VISIBILITY" else "दृश्यता",
                        value = "${"%.1f".format(w.visibility)} km",
                        sub = if (isEnglish) when { w.visibility > 10 -> "Excellent" ; w.visibility > 5 -> "Good" ; else -> "Poor" }
                              else when { w.visibility > 10 -> "बेहतरीन" ; w.visibility > 5 -> "अच्छा" ; else -> "कम" },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(
                        emoji = "🔵", title = if (isEnglish) "PRESSURE" else "दबाव",
                        value = "${w.pressure.toInt()} hPa",
                        sub = if (isEnglish) when { w.pressure > 1013 -> "High pressure" ; else -> "Low pressure" }
                              else when { w.pressure > 1013 -> "उच्च दबाव" ; else -> "कम दबाव" },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        emoji = "☁️", title = if (isEnglish) "CLOUD COVER" else "बादल",
                        value = "${w.cloudCover}%",
                        sub = if (isEnglish) when { w.cloudCover > 75 -> "Overcast" ; w.cloudCover > 25 -> "Partly cloudy" ; else -> "Clear sky" }
                              else when { w.cloudCover > 75 -> "पूरे बादल" ; w.cloudCover > 25 -> "आंशिक बादल" ; else -> "साफ" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── SUNRISE / SUNSET ARC ───────────────────────────────────────────
        item {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column {
                    SectionLabel(if (isEnglish) "SUNRISE & SUNSET" else "सूर्योदय और सूर्यास्त")
                    Spacer(Modifier.height(8.dp))
                    SunArcView(w.sunrise, w.sunset, hourOfDay)
                    Row(
                        Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌅", fontSize = 24.sp)
                            Text(w.sunrise, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(if (isEnglish) "Sunrise" else "सूर्योदय", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌇", fontSize = 24.sp)
                            Text(w.sunset, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(if (isEnglish) "Sunset" else "सूर्यास्त", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── WIND COMPASS ──────────────────────────────────────────────────
        item {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        SectionLabel(if (isEnglish) "WIND" else "हवा")
                        Spacer(Modifier.height(6.dp))
                        Text("${w.windSpeed.toInt()} km/h", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Thin)
                        Text(
                            windDirLabel(w.windDirection) + " — " + if (isEnglish) "Direction" else "दिशा",
                            color = Color.White.copy(alpha = 0.65f), fontSize = 13.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (isEnglish) "Today's max: ${w.daily.firstOrNull()?.windMax?.toInt() ?: "--"} km/h"
                            else "आज अधिकतम: ${w.daily.firstOrNull()?.windMax?.toInt() ?: "--"} km/h",
                            color = GoldAccent, fontSize = 12.sp
                        )
                    }
                    WindCompassView(degrees = w.windDirection.toFloat(), modifier = Modifier.size(100.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── TOURIST ADVISORY ──────────────────────────────────────────────
        item {
            TouristAdvisoryCard(w, isEnglish)
            Spacer(Modifier.height(12.dp))
        }

        // ── BEST SEASON GUIDE ─────────────────────────────────────────────
        item {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column {
                    SectionLabel(if (isEnglish) "CHITTORGARH VISIT GUIDE" else "चित्तौड़गढ़ यात्रा गाइड")
                    Spacer(Modifier.height(8.dp))
                    listOf(
                        Triple("Oct – Mar", if (isEnglish) "🌟 Best Season — 15–28°C. Clear skies, ideal for fort walk & photography." else "🌟 सर्वश्रेष्ठ — 15–28°C। साफ आसमान, किला घूमने के लिए एकदम सही।", Color(0xFF2E7D32)),
                        Triple("Apr – Jun", if (isEnglish) "☀️ Hot Summer — 38–45°C. Visit early morning (6–9 AM). Stay hydrated!" else "☀️ गर्म गर्मी — 38–45°C। सुबह 6–9 बजे जाएं। पानी पीते रहें!", Color(0xFFE65100)),
                        Triple("Jul – Sep", if (isEnglish) "🌧️ Monsoon — 25–35°C. Lush green fort. Paths may be slippery." else "🌧️ मानसून — 25–35°C। हरा-भरा किला। रास्ते फिसलन भरे हो सकते हैं।", Color(0xFF1565C0))
                    ).forEach { (period, desc, color) ->
                        Row(Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
                            Text(
                                period, color = GoldAccent,
                                fontWeight = FontWeight.Bold, fontSize = 11.sp,
                                modifier = Modifier.width(60.dp).padding(top = 2.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(Modifier.width(3.dp).height(40.dp).background(color, RoundedCornerShape(2.dp)))
                            Spacer(Modifier.width(10.dp))
                            Text(desc, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── CREDIT ────────────────────────────────────────────────────────
        item {
            Text(
                "⚡ Powered by Open-Meteo (ECMWF) — Free & Open Source",
                color = Color.White.copy(alpha = 0.35f), fontSize = 10.sp,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─── Sub-Composables ─────────────────────────────────────────────────────────

@Composable
private fun HourlyCard(item: HourlyItem) {
    Column(
        modifier = Modifier
            .width(58.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(item.hour, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(wmoToEmoji(item.weatherCode), fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text("${item.temp.toInt()}°", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun DailyRow(day: DailyItem, isEnglish: Boolean, globalMin: Double, globalMax: Double) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day.dayLabel,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(75.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(42.dp)
        ) {
            Text(wmoToEmoji(day.weatherCode), fontSize = 18.sp)
            if (day.precipSum > 0.1) {
                Text(
                    text = "${"%.0f".format(day.precipSum)}mm",
                    color = Color(0xFF64B5F6),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = "${day.minTemp.toInt()}°",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 13.sp,
            modifier = Modifier.width(26.dp),
            textAlign = TextAlign.End
        )

        Spacer(Modifier.width(6.dp))

        val range = (globalMax - globalMin).let { if (it == 0.0) 1.0 else it }
        val barStart = ((day.minTemp - globalMin) / range).toFloat()
        val barEnd   = ((day.maxTemp - globalMin) / range).toFloat()
        Canvas(modifier = Modifier.weight(1f).height(4.dp)) {
            drawLine(
                Color.White.copy(alpha = 0.15f),
                Offset(0f, size.height / 2),
                Offset(size.width, size.height / 2),
                4.dp.toPx(),
                StrokeCap.Round
            )
            drawLine(
                Brush.linearGradient(listOf(Color(0xFF64B5F6), SaffronPrimary, Color(0xFFE53935))),
                Offset(barStart * size.width, size.height / 2),
                Offset(barEnd * size.width, size.height / 2),
                4.dp.toPx(),
                StrokeCap.Round
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = "${day.maxTemp.toInt()}°",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(26.dp)
        )
    }
}

@Composable
private fun StatCard(emoji: String, title: String, value: String, sub: String, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 14.sp)
                Spacer(Modifier.width(4.dp))
                Text(title, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Thin, letterSpacing = (-0.5).sp)
            Spacer(Modifier.height(2.dp))
            Text(sub, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        }
    }
}

@Composable
private fun UVStatCard(uv: Double, isEnglish: Boolean, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("☀️", fontSize = 14.sp)
                Spacer(Modifier.width(4.dp))
                Text("UV INDEX", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("${uv.toInt()}", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Thin)
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier.fillMaxWidth().height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF4CAF50), Color(0xFFFFEB3B), Color(0xFFFF9800), Color(0xFFE53935), Color(0xFF9C27B0))))
            )
            Spacer(Modifier.height(2.dp))
            Text(uvLabel(uv, isEnglish), color = uvColor(uv), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SunArcView(sunrise: String, sunset: String, hourOfDay: Int) {
    val progress = remember(hourOfDay) {
        val sr = 6f; val ss = 19f
        val now = hourOfDay.toFloat()
        ((now - sr) / (ss - sr)).coerceIn(0f, 1f)
    }
    Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        val w = size.width; val h = size.height
        val padding = 40.dp.toPx()
        val cx = w / 2f; val cy = h + 10.dp.toPx()
        val rx = (w / 2f) - padding; val ry = h - 16.dp.toPx()

        drawArc(
            color = Color.White.copy(alpha = 0.25f), startAngle = 180f, sweepAngle = 180f, useCenter = false,
            topLeft = Offset(cx - rx, cy - ry), size = Size(rx * 2, ry * 2),
            style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f)))
        )

        val sweep = 180f * progress
        drawArc(
            brush = Brush.linearGradient(listOf(Color(0xFFF5C843), Color(0xFFE8824A), Color(0xFF9C27B0))),
            startAngle = 180f, sweepAngle = sweep, useCenter = false,
            topLeft = Offset(cx - rx, cy - ry), size = Size(rx * 2, ry * 2),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        val rad = Math.toRadians((180.0 + 180.0 * progress)).toFloat()
        val sunX = cx + rx * kotlin.math.cos(rad)
        val sunY = cy + ry * kotlin.math.sin(rad)

        drawCircle(SaffronPrimary.copy(alpha = 0.3f), radius = 16.dp.toPx(), center = Offset(sunX, sunY))
        drawCircle(SaffronPrimary, radius = 8.dp.toPx(), center = Offset(sunX, sunY))

        drawLine(Color.White.copy(alpha = 0.15f), Offset(padding, cy), Offset(w - padding, cy), 1.dp.toPx())
    }
}

@Composable
private fun WindCompassView(degrees: Float, modifier: Modifier = Modifier) {
    val rotation = remember(degrees) { degrees }
    Canvas(modifier = modifier) {
        val cx = size.width / 2; val cy = size.height / 2
        val r = minOf(cx, cy) - 4.dp.toPx()

        drawCircle(Color.White.copy(alpha = 0.15f), radius = r, center = Offset(cx, cy), style = Stroke(1.dp.toPx()))

        val labels = listOf("N" to 0f, "E" to 90f, "S" to 180f, "W" to 270f)
        labels.forEach { (_, angle) ->
            val rad = Math.toRadians(angle.toDouble() - 90)
            val tx = cx + (r - 10.dp.toPx()) * kotlin.math.cos(rad).toFloat()
            val ty = cy + (r - 10.dp.toPx()) * kotlin.math.sin(rad).toFloat()
            drawCircle(Color.White.copy(alpha = 0.4f), 2.dp.toPx(), Offset(tx, ty))
        }

        val arrowRad = Math.toRadians(rotation.toDouble() - 90)
        val tipX = cx + r * 0.6f * kotlin.math.cos(arrowRad).toFloat()
        val tipY = cy + r * 0.6f * kotlin.math.sin(arrowRad).toFloat()
        val tailX = cx - r * 0.5f * kotlin.math.cos(arrowRad).toFloat()
        val tailY = cy - r * 0.5f * kotlin.math.sin(arrowRad).toFloat()

        drawLine(
            brush = Brush.linearGradient(listOf(SaffronPrimary, Color(0xFFE53935))),
            start = Offset(tailX, tailY), end = Offset(tipX, tipY),
            strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round
        )
        drawCircle(SaffronPrimary, 5.dp.toPx(), Offset(tipX, tipY))
        drawCircle(Color.White.copy(alpha = 0.5f), 3.dp.toPx(), Offset(tailX, tailY))
    }
}

@Composable
private fun TouristAdvisoryCard(w: WeatherData, isEnglish: Boolean) {
    val tips = buildList {
        if (w.uvIndex >= 8) add(if (isEnglish) "🛡️ Very High UV! Use SPF 50+ sunscreen & hat at the fort." else "🛡️ बहुत अधिक UV! किले पर SPF 50+ और टोपी जरूरी।")
        if (w.currentTemp >= 38) add(if (isEnglish) "🥤 Extreme heat! Carry 2L+ water. Visit early morning or evening only." else "🥤 भीषण गर्मी! 2 लीटर पानी रखें। सुबह या शाम ही जाएं।")
        if (w.windSpeed >= 40) add(if (isEnglish) "💨 Strong winds — be cautious on fort towers and open areas." else "💨 तेज हवाएं — किले के मीनारों पर सावधान रहें।")
        if (w.weatherCode in 51..82) add(if (isEnglish) "☔ Rain expected! Carry umbrella. Fort paths may be slippery." else "☔ बारिश की संभावना! छाता रखें। किले के रास्ते फिसलन भरे हो सकते हैं।")
        if (w.visibility < 3) add(if (isEnglish) "🌫️ Low visibility. Photography may be difficult today." else "🌫️ कम दृश्यता। आज फोटोग्राफी मुश्किल हो सकती है।")
        val uvNow = w.uvIndex
        val isGoodTime = w.currentTemp < 35 && uvNow < 7
        if (isGoodTime) add(if (isEnglish) "✅ Great conditions for visiting the fort right now!" else "✅ अभी किला देखने के लिए बेहतरीन मौसम है!")
        if (isEmpty()) add(if (isEnglish) "ℹ️ Standard conditions. Stay hydrated and enjoy Chittorgarh!" else "ℹ️ सामान्य मौसम। पानी पीते रहें और चित्तौड़गढ़ का आनंद लें!")
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        tint = Color(0xFFB71C1C).copy(alpha = 0.15f)
    ) {
        Column {
            SectionLabel(if (isEnglish) "TOURIST ADVISORY — TODAY" else "पर्यटक सलाह — आज")
            Spacer(Modifier.height(8.dp))
            tips.forEach { tip ->
                Text(tip, color = Color.White, fontSize = 13.sp, lineHeight = 20.sp, modifier = Modifier.padding(bottom = 6.dp))
            }
        }
    }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    tint: Color = Color.Black.copy(alpha = 0.35f),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(tint)
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text, color = Color.White.copy(alpha = 0.55f),
        fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp
    )
}
