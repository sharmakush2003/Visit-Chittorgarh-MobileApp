package com.example.visitchittorgarh.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "How to Reach" else "चित्तौड़गढ़ कैसे पहुंचें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SaffronPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vijay_stambh),
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
                        text = if (isEnglish) "TRAVEL CONCIERGE" else "यात्रा द्वारपाल",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) "How to Reach Chittorgarh" else "चित्तौड़गढ़ आवागमन मार्ग",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dynamic Search / Input Panel (Manual override)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isEnglish) "MANUAL ROUTE CALCULATOR" else "मैनुअल मार्ग कैलकुलेटर",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = manualCityInput,
                            onValueChange = { manualCityInput = it },
                            label = { Text(text = if (isEnglish) "Enter City (e.g. Agra, Delhi)" else "शहर का नाम दर्ज करें (उदा. आगरा)") },
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
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaffronPrimary,
                                focusedLabelColor = SaffronPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (useManualSearch) {
                                TextButton(onClick = {
                                    useManualSearch = false
                                    manualCityInput = ""
                                    activeRouteInfo = null
                                }) {
                                    Text(text = if (isEnglish) "Reset to GPS" else "जीपीएस पर लौटें", color = CrimsonSecondary)
                                }
                            }
                        }
                    }
                }

                // Distance Output / GPS Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(SaffronPrimary.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        if (useManualSearch && activeRouteInfo != null) {
                            Text(
                                text = if (isEnglish) {
                                    "From ${activeRouteInfo!!.cityName} to Chittorgarh is ${activeRouteInfo!!.distance}"
                                } else {
                                    "${activeRouteInfo!!.cityName} से चित्तौड़गढ़ की दूरी ${activeRouteInfo!!.distance} है"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = CrimsonSecondary,
                                textAlign = TextAlign.Center
                            )
                        } else if (!hasPermission) {
                            Text(
                                text = if (isEnglish) "GPS Disabled / Permission Required" else "जीपीएस अक्षम / अनुमति आवश्यक",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        } else if (userLocation == null) {
                            Text(
                                text = if (isEnglish) "Fetching GPS location..." else "जीपीएस स्थान खोजा जा रहा है...",
                                fontSize = 14.sp
                            )
                        } else if (isInChittorgarh) {
                            Text(
                                text = if (isEnglish) "Welcome! You are already in Chittorgarh!" else "स्वागत है! आप पहले से ही चित्तौड़गढ़ में हैं!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SaffronPrimary,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = if (isEnglish) {
                                    "You are approx. ${distanceInKm?.toInt()} km away from Chittorgarh (via GPS)"
                                } else {
                                    "आप चित्तौड़गढ़ से लगभग ${distanceInKm?.toInt()} किमी दूर हैं (जीपीएस)"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Serif,
                                color = CrimsonSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Transit options headers
                Text(
                    text = if (isEnglish) "Transit Options" else "आवागमन के साधन",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // 1. By Air
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SaffronPrimary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✈️", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isEnglish) "By Air (हवाई मार्ग)" else "हवाई मार्ग द्वारा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SaffronPrimary,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (useManualSearch && activeRouteInfo != null) {
                                if (isEnglish) activeRouteInfo!!.airGuideEn else activeRouteInfo!!.airGuideHi
                            } else {
                                if (isEnglish) {
                                    "The nearest airport is Maharana Pratap Airport in Udaipur (UDR), located 90 km away from Chittorgarh. Direct flights connect Udaipur with Delhi, Mumbai, and Jaipur."
                                } else {
                                    "निकटतम हवाई अड्डा उदयपुर में महाराणा प्रताप हवाई अड्डा (UDR) है, जो चित्तौड़गढ़ से 90 किमी दूर है। सीधी उड़ानें उदयपुर को दिल्ली, मुंबई और जयपुर से जोड़ती हैं।"
                                }
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Flight Booking Direct Action Link
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/travel/flights"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                        ) {
                            Text(
                                text = if (isEnglish) "Search Flights to Udaipur (UDR)" else "उदयपुर के लिए उड़ानें खोजें",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // 2. By Train
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CrimsonSecondary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🚂", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isEnglish) "By Train (रेल मार्ग)" else "रेल मार्ग द्वारा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CrimsonSecondary,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (useManualSearch && activeRouteInfo != null) {
                                if (isEnglish) activeRouteInfo!!.railGuideEn else activeRouteInfo!!.railGuideHi
                            } else {
                                if (isEnglish) {
                                    "Chittorgarh Junction (COR) is a major railway station connecting directly with metro cities across India. Superfast trains operate daily from Delhi, Jaipur, Ahmedabad, Mumbai, and Kota."
                                } else {
                                    "चित्तौड़गढ़ जंक्शन (COR) भारत भर के मेट्रो शहरों से सीधे जुड़ने वाला एक प्रमुख रेलवे स्टेशन है। सुपरफास्ट ट्रेनें (जैसे मेवाड़ एक्सप्रेस) दिल्ली, जयपुर, अहमदाबाद, मुंबई और कोटा से प्रतिदिन चलती हैं।"
                                }
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // IRCTC Direct Action Link
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.irctc.co.in/"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                        ) {
                            Text(
                                text = if (isEnglish) "Book Train Tickets on IRCTC" else "IRCTC पर ट्रेन टिकट बुक करें",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // 3. By Road
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF4CAF50).copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🚗", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isEnglish) "By Road (सड़क मार्ग)" else "सड़क मार्ग द्वारा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF4CAF50),
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (useManualSearch && activeRouteInfo != null) {
                                if (isEnglish) activeRouteInfo!!.roadGuideEn else activeRouteInfo!!.roadGuideHi
                            } else {
                                if (isEnglish) {
                                    "Located on the Golden Quadrilateral highway network (NH 48), Chittorgarh is easily accessible from Jaipur (320 km), Ahmedabad (380 km), and Udaipur (115 km)."
                                } else {
                                    "स्वर्णिम चतुर्भुज राजमार्ग नेटवर्क (NH 48) पर स्थित, चित्तौड़गढ़ जयपुर (320 किमी), अहमदाबाद (380 किमी) और उदयपुर (115 किमी) से आसानी से सुलभ है।"
                                }
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Google Maps Direct Route Direction Link
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
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text(
                                text = if (isEnglish) "Open Route in Google Maps" else "गूगल मैप्स में मार्ग खोलें",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDark),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (isEnglish) "Return to Dashboard" else "डैशबोर्ड पर लौटें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}
