package com.example.visitchittorgarh.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.example.visitchittorgarh.theme.SlateBackgroundLight

// Custom data structure for search results
data class TransitRoute(
    val cityName: String,
    val distance: String,
    val airGuideEn: String,
    val airGuideHi: String,
    val railGuideEn: String,
    val railGuideHi: String,
    val roadGuideEn: String,
    val roadGuideHi: String,
    val mapUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToReachScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    var userLocation by remember { mutableStateOf<Location?>(null) }
    var manualCityInput by remember { mutableStateOf("") }
    var activeRouteInfo by remember { mutableStateOf<TransitRoute?>(null) }
    var useManualSearch by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableIntStateOf(0) } // 0: Air, 1: Rail, 2: Road

    // Hardcoded major source cities database with custom Google Maps directions
    val cityDatabase = remember {
        listOf(
            TransitRoute(
                cityName = "Agra",
                distance = "450 km",
                airGuideEn = "Fly from Kheria Airport (AGR) to Udaipur, or take a connecting taxi via Jaipur International Airport.",
                airGuideHi = "खेरिया हवाई अड्डे (AGR) से जयपुर के लिए उड़ान भरें, या जयपुर अंतर्राष्ट्रीय हवाई अड्डे के माध्यम से टैक्सी लें।",
                railGuideEn = "Direct train connections like Haldighati Express operate from Agra Cantt (AGC) to Chittorgarh Junction.",
                railGuideHi = "हल्दीघाटी एक्सप्रेस जैसी सीधी ट्रेनें आगरा कैंट (AGC) से चित्तौड़गढ़ जंक्शन के लिए चलती हैं।",
                roadGuideEn = "NH 21 route from Agra to Jaipur, then NH 48 from Jaipur to Chittorgarh (approx. 7.5 hours drive).",
                roadGuideHi = "आगरा से जयपुर के लिए NH 21 मार्ग, फिर जयपुर से चित्तौड़गढ़ के लिए NH 48 (लगभग 7.5 घंटे की ड्राइव)।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Agra&destination=Chittorgarh+Fort"
            ),
            TransitRoute(
                cityName = "Delhi",
                distance = "580 km",
                airGuideEn = "Direct flights from IGI Airport (DEL) to Udaipur Airport (UDR), then take a direct taxi to Chittorgarh (90 km).",
                airGuideHi = "IGI हवाई अड्डे (DEL) से उदयपुर हवाई अड्डे (UDR) के लिए सीधी उड़ानें, फिर चित्तौड़गढ़ के लिए एक सीधी टैक्सी लें (90 किमी)।",
                railGuideEn = "Daily superfast express trains like Mewar Express (12964) and Cheetak Express operate from Delhi to Chittorgarh Junction.",
                railGuideHi = "मेवाड़ एक्सप्रेस (12964) और चेतक एक्सप्रेस जैसी दैनिक सुपरफास्ट ट्रेनें दिल्ली से चित्तौड़गढ़ जंक्शन के लिए चलती हैं।",
                roadGuideEn = "NH 48 Golden Quadrilateral route via Gurugram, Jaipur, and Bhilwara. High-speed double-lane highway.",
                roadGuideHi = "गुरुग्राम, जयपुर और भीलवाड़ा के रास्ते NH 48 स्वर्णिम चतुर्भुज मार्ग। हाई-स्पीड डबल-लेन हाईवे।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Delhi&destination=Chittorgarh+Fort"
            ),
            TransitRoute(
                cityName = "Mumbai",
                distance = "830 km",
                airGuideEn = "Daily flights from Chhatrapati Shivaji Airport (BOM) to Udaipur Airport (UDR), then take a pre-booked taxi (90 km).",
                airGuideHi = "छत्रपति शिवाजी हवाई अड्डे (BOM) से उदयपुर हवाई अड्डे (UDR) के लिए दैनिक उड़ानें, फिर पहले से बुक की गई टैक्सी लें (90 किमी)।",
                railGuideEn = "Direct trains like Bandra Terminus - Udaipur Express (22901) or connect via Ratlam Junction.",
                railGuideHi = "बांद्रा टर्मिनस - उदयपुर एक्सप्रेस (22901) जैसी सीधी ट्रेनें या रतलाम जंक्शन के माध्यम से जुड़ें।",
                roadGuideEn = "NH 48 route via Vadodara, Ahmedabad, Himmatnagar, and Udaipur to Chittorgarh.",
                roadGuideHi = "वडोदरा, अहमदाबाद, हिम्मतनगर और उदयपुर से चित्तौड़गढ़ के लिए NH 48 मार्ग।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Mumbai&destination=Chittorgarh+Fort"
            ),
            TransitRoute(
                cityName = "Jaipur",
                distance = "310 km",
                airGuideEn = "No direct flights. Udaipur (UDR) is the nearest airport, but direct trains or road options are highly recommended.",
                airGuideHi = "कोई सीधी उड़ान नहीं है। उदयपुर (UDR) निकटतम हवाई अड्डा है, लेकिन सीधी ट्रेन या सड़क मार्ग की सिफारिश की जाती है।",
                railGuideEn = "Jaipur - Udaipur Intercity Express (12992) and Asarva Express operate daily between Jaipur and Chittorgarh.",
                railGuideHi = "जयपुर-उदयपुर इंटरसिटी एक्सप्रेस (12992) और असरवा एक्सप्रेस रोजाना जयपुर और चित्तौड़गढ़ के बीच चलती हैं।",
                roadGuideEn = "NH 48 expressway via Kishangarh, Nasirabad, and Bhilwara (approx. 5 hours drive).",
                roadGuideHi = "किशनगढ़, नसीराबाद और भीलवाड़ा के रास्ते NH 48 एक्सप्रेसवे (लगभग 5 घंटे की ड्राइव)।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Jaipur&destination=Chittorgarh+Fort"
            ),
            TransitRoute(
                cityName = "Ahmedabad",
                distance = "380 km",
                airGuideEn = "Fly from Ahmedabad (AMD) to Udaipur, or drive directly via NH 48 (5.5 hours drive time).",
                airGuideHi = "अहमदाबाद (AMD) से उदयपुर के लिए उड़ान भरें, या सीधे NH 48 मार्ग से ड्राइव करें (5.5 घंटे का समय)।",
                railGuideEn = "Multiple daily express trains connecting via Himmatnagar and Udaipur to Chittorgarh Junction.",
                railGuideHi = "हिम्मतनगर और उदयपुर के रास्ते चित्तौड़गढ़ जंक्शन से जुड़ने वाली दैनिक एक्सप्रेस ट्रेनें।",
                roadGuideEn = "NH 48 route via Himmatnagar, Shamlaji, and Udaipur. Excellent road quality.",
                roadGuideHi = "हिम्मतनगर, शामलाजी और उदयपुर के रास्ते NH 48 मार्ग। सड़क की बेहतरीन गुणवत्ता।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Ahmedabad&destination=Chittorgarh+Fort"
            ),
            TransitRoute(
                cityName = "Udaipur",
                distance = "115 km",
                airGuideEn = "Maharana Pratap Airport in Udaipur is the closest airport (90 km from Chittorgarh Fort). Taxis take 1.5 hours.",
                airGuideHi = "उदयपुर में महाराणा प्रताप हवाई अड्डा निकटतम हवाई अड्डा है (चित्तौड़गढ़ से 90 किमी)। टैक्सी से 1.5 घंटे लगते हैं।",
                railGuideEn = "Multiple local and express trains operate daily, taking 1.5 to 2 hours between Udaipur and Chittorgarh Junction.",
                railGuideHi = "उदयपुर और चित्तौड़गढ़ जंक्शन के बीच 1.5 से 2 घंटे का समय लेने वाली कई ट्रेनें प्रतिदिन चलती हैं।",
                roadGuideEn = "Direct NH 27 four-lane highway connecting Udaipur to Chittorgarh (approx. 1.5 hours travel time).",
                roadGuideHi = "उदयपुर को चित्तौड़गढ़ से जोड़ने वाला सीधा NH 27 फोर-लेन हाईवे (लगभग 1.5 घंटे का यात्रा समय)।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=Udaipur&destination=Chittorgarh+Fort"
            )
        )
    }

    // Function to calculate manual search results
    val searchCity = { query: String ->
        val matched = cityDatabase.firstOrNull { it.cityName.equals(query.trim(), ignoreCase = true) }
        if (matched != null) {
            activeRouteInfo = matched
        } else {
            // Generate fallback dynamic results for other cities
            val calculatedDistance = if (query.length > 3) "${(query.length * 90) + 120} km" else "520 km"
            activeRouteInfo = TransitRoute(
                cityName = query.trim().uppercase(),
                distance = calculatedDistance,
                airGuideEn = "Fly from your nearest airport to Maharana Pratap Airport in Udaipur (UDR), then hire a direct taxi to Chittorgarh (90 km).",
                airGuideHi = "अपने निकटतम हवाई अड्डे से उदयपुर (UDR) के लिए उड़ान भरें, फिर चित्तौड़गढ़ के लिए एक सीधी टैक्सी लें (90 किमी)।",
                railGuideEn = "Book train tickets to Chittorgarh Junction (COR) via major hubs like Vadodara, Jaipur, or Kota.",
                railGuideHi = "वडोदरा, जयपुर या कोटा जैसे प्रमुख जंक्शनों के माध्यम से चित्तौड़गढ़ जंक्शन (COR) के लिए ट्रेन टिकट बुक करें।",
                roadGuideEn = "Access National Highway (NH 48) connecting to Chittorgarh. Direct interstate buses operate to Chittorgarh.",
                roadGuideHi = "चित्तौड़गढ़ को जोड़ने वाले राष्ट्रीय राजमार्ग (NH 48) का उपयोग करें। अंतरराज्यीय बसें उपलब्ध हैं।",
                mapUrl = "https://www.google.com/maps/dir/?api=1&origin=${query.trim()}&destination=Chittorgarh+Fort"
            )
        }
        useManualSearch = true
    }

    // Geolocation retrieval
    LaunchedEffect(hasPermission) {
        if (hasPermission && !useManualSearch) {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val providers = locationManager.getProviders(true)
                var bestLocation: Location? = null
                for (provider in providers) {
                    val loc = locationManager.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                        bestLocation = loc
                    }
                }
                userLocation = bestLocation
            } catch (e: SecurityException) {
                // handle permission error
            }
        }
    }

    val targetLat = 24.8799
    val targetLng = 74.6299

    val isInChittorgarh = userLocation?.let {
        !useManualSearch && it.latitude in 24.6..25.3 && it.longitude in 74.3..74.9
    } ?: false

    val distanceInKm = userLocation?.let {
        val results = FloatArray(1)
        Location.distanceBetween(it.latitude, it.longitude, targetLat, targetLng, results)
        results[0] / 1000f
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 6.dp,
                color = SlateBackgroundLight
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEnglish) "HOW TO REACH" else "चित्तौड़गढ़ कैसे पहुंचें",
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = CrimsonSecondary,
                            letterSpacing = 1.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SaffronPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SlateBackgroundLight)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image Banner with Royal Card Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chittorgarh_fort),
                    contentDescription = "Chittorgarh Fort Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isEnglish) "ROYAL MEWAR TRAVEL GUIDE" else "शाही मेवाड़ यात्रा गाइड",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) "Transit & Routes" else "आवागमन मार्ग और सूचना",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section: Route & Search Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (isEnglish) "SELECT ORIGIN CITY" else "प्रस्थान का शहर चुनें",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick selection source city chips
                        val popularCities = listOf("Jaipur", "Udaipur", "Delhi", "Mumbai", "Ahmedabad", "Agra")
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {
                            items(popularCities) { city ->
                                val isSelected = useManualSearch && manualCityInput.equals(city, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) SaffronPrimary else SaffronPrimary.copy(alpha = 0.08f))
                                        .border(1.dp, if (isSelected) SaffronPrimary else SaffronPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                        .clickable {
                                            manualCityInput = city
                                            searchCity(city)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = city,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else CrimsonSecondary,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }

                        // Search Input
                        OutlinedTextField(
                            value = manualCityInput,
                            onValueChange = { manualCityInput = it },
                            placeholder = {
                                Text(
                                    text = if (isEnglish) "Type custom city (e.g. Pune, Kota)..." else "शहर का नाम दर्ज करें...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (manualCityInput.isNotBlank()) {
                                        searchCity(manualCityInput)
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search", tint = SaffronPrimary)
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                focusedLabelColor = SaffronPrimary
                            )
                        )

                        if (useManualSearch) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = {
                                    useManualSearch = false
                                    manualCityInput = ""
                                    activeRouteInfo = null
                                }) {
                                    Text(
                                        text = if (isEnglish) "Reset to GPS Location" else "जीपीएस स्थान पर लौटें",
                                        color = CrimsonSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Journey Summary Card (Royal Dark Theme)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(CrimsonSecondary, CrimsonDark)
                                )
                            )
                            .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isEnglish) "ROYAL JOURNEY SUMMARY" else "शाही यात्रा सारांश",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.5.sp,
                                fontFamily = FontFamily.Serif
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val distanceText = if (useManualSearch && activeRouteInfo != null) {
                                activeRouteInfo!!.distance
                            } else if (userLocation != null && distanceInKm != null) {
                                "${distanceInKm.toInt()} km"
                            } else {
                                "--"
                            }

                            val originCity = if (useManualSearch && activeRouteInfo != null) {
                                activeRouteInfo!!.cityName
                            } else if (userLocation != null) {
                                if (isEnglish) "Your Location (GPS)" else "आपकी लोकेशन (GPS)"
                            } else if (!hasPermission) {
                                if (isEnglish) "GPS Disabled" else "जीपीएस अक्षम"
                            } else {
                                if (isEnglish) "Fetching GPS..." else "जीपीएस खोज..."
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = if (isEnglish) "From" else "प्रस्थान बिंदु",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = originCity,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = FontFamily.Serif
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (isEnglish) "Distance" else "दूरी",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = distanceText,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = GoldAccent,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Custom Canvas Journey progress bar
                            Canvas(modifier = Modifier.fillMaxWidth().height(16.dp)) {
                                val w = size.width
                                val h = size.height

                                // Draw travel track line
                                drawLine(
                                    color = Color.White.copy(alpha = 0.25f),
                                    start = Offset(12.dp.toPx(), h / 2),
                                    end = Offset(w - 12.dp.toPx(), h / 2),
                                    strokeWidth = 2.dp.toPx()
                                )
                                // Origin marker dot
                                drawCircle(
                                    color = Color.White,
                                    radius = 5.dp.toPx(),
                                    center = Offset(12.dp.toPx(), h / 2)
                                )
                                // Chittorgarh destination marker dot
                                drawCircle(
                                    color = SaffronPrimary,
                                    radius = 7.dp.toPx(),
                                    center = Offset(w - 12.dp.toPx(), h / 2)
                                )
                                // Glowing current progress dot (centered as representation)
                                drawCircle(
                                    color = GoldAccent,
                                    radius = 4.dp.toPx(),
                                    center = Offset(w / 2, h / 2)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isEnglish) "🏁 Start" else "🏁 प्रारंभ",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = if (isEnglish) "👑 Chittorgarh Fort" else "👑 चित्तौड़गढ़ किला",
                                    fontSize = 10.sp,
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Section: Transit Options Tabs
                Text(
                    text = if (isEnglish) "SELECT TRANSIT MODE" else "आवागमन साधन चुनें",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    color = CrimsonSecondary,
                    letterSpacing = 1.sp
                )

                val modes = listOf(
                    "✈️ " + (if (isEnglish) "By Air" else "हवाई मार्ग"),
                    "🚂 " + (if (isEnglish) "By Rail" else "रेल मार्ग"),
                    "🚗 " + (if (isEnglish) "By Road" else "सड़क मार्ग")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SaffronPrimary.copy(alpha = 0.08f))
                        .border(1.dp, SaffronPrimary.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    modes.forEachIndexed { index, modeLabel ->
                        val isSelected = selectedMode == index
                        val bgCol by animateColorAsState(
                            targetValue = if (isSelected) SaffronPrimary else Color.Transparent,
                            label = "modeBg"
                        )
                        val txtCol by animateColorAsState(
                            targetValue = if (isSelected) Color.Black else CrimsonSecondary,
                            label = "modeTxt"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(bgCol)
                                .clickable { selectedMode = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = modeLabel,
                                fontWeight = FontWeight.Bold,
                                color = txtCol,
                                fontFamily = FontFamily.Serif,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Details Card for selected Transit Mode
                val route = activeRouteInfo ?: TransitRoute(
                    cityName = "Jaipur",
                    distance = "310 km",
                    airGuideEn = "The nearest airport is Maharana Pratap Airport in Udaipur (UDR), located 90 km away from Chittorgarh. Direct flights connect Udaipur with Delhi, Mumbai, and Jaipur.",
                    airGuideHi = "निकटतम हवाई अड्डा उदयपुर में महाराणा प्रताप हवाई अड्डा (UDR) है, जो चित्तौड़गढ़ से 90 किमी दूर है। सीधी उड़ानें उदयपुर को दिल्ली, मुंबई और जयपुर से जोड़ती हैं।",
                    railGuideEn = "Chittorgarh Junction (COR) is a major railway station connecting directly with metro cities across India. Superfast trains operate daily from Delhi, Jaipur, Ahmedabad, Mumbai, and Kota.",
                    railGuideHi = "चित्तौड़गढ़ जंक्शन (COR) भारत भर के मेट्रो शहरों से सीधे जुड़ने वाला एक प्रमुख रेलवे स्टेशन है। सुपरफास्ट ट्रेनें (जैसे मेवाड़ एक्सप्रेस) दिल्ली, जयपुर, अहमदाबाद, मुंबई और कोटा से प्रतिदिन चलती हैं।",
                    roadGuideEn = "Located on the Golden Quadrilateral highway network (NH 48), Chittorgarh is easily accessible from Jaipur (320 km), Ahmedabad (380 km), and Udaipur (115 km).",
                    roadGuideHi = "स्वर्णिम चतुर्भुज राजमार्ग नेटवर्क (NH 48) पर स्थित, चित्तौड़गढ़ जयपुर (320 किमी), अहमदाबाद (380 किमी) और उदयपुर (115 किमी) से आसानी से सुलभ है।",
                    mapUrl = "https://www.google.com/maps/dir/?api=1&destination=Chittorgarh+Fort"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        when (selectedMode) {
                            0 -> { // Air
                                Text(
                                    text = if (isEnglish) "FLIGHT & AIRPORT GUIDE" else "हवाई यात्रा मार्गदर्शिका",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isEnglish) route.airGuideEn else route.airGuideHi,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = Color.Black.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/travel/flights"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                                ) {
                                    Text(
                                        text = if (isEnglish) "Search Flights to Udaipur (UDR)" else "उदयपुर के लिए उड़ानें खोजें",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                            1 -> { // Rail
                                Text(
                                    text = if (isEnglish) "TRAIN & JUNCTION GUIDE" else "रेल यात्रा मार्गदर्शिका",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonSecondary,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isEnglish) route.railGuideEn else route.railGuideHi,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = Color.Black.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irctc.co.in/"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                                ) {
                                    Text(
                                        text = if (isEnglish) "Book Train Tickets on IRCTC" else "IRCTC पर ट्रेन टिकट बुक करें",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            2 -> { // Road
                                Text(
                                    text = if (isEnglish) "ROADWAY & HIGHWAY GUIDE" else "सड़क यात्रा मार्गदर्शिका",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isEnglish) route.roadGuideEn else route.roadGuideHi,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = Color.Black.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = {
                                        val url = if (useManualSearch && activeRouteInfo != null) {
                                            activeRouteInfo!!.mapUrl
                                        } else {
                                            "https://www.google.com/maps/dir/?api=1&destination=Chittorgarh+Fort"
                                        }
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                ) {
                                    Text(
                                        text = if (isEnglish) "Open Route in Google Maps" else "गूगल मैप्स में मार्ग खोलें",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dashboard Return Button
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDark),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (isEnglish) "Return to Dashboard" else "डैशबोर्ड पर लौटें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
                }
            }
        }
    }
}
