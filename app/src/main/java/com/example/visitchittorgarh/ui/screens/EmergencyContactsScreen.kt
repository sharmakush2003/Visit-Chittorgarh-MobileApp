package com.example.visitchittorgarh.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.google.android.gms.location.*

// ─── Data Model ───────────────────────────────────────────────────────────────

data class EmergencyContact(
    val nameEn: String,
    val nameHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val phone: String,
    val icon: ImageVector,
    val categoryColor: Color,
    val isSOS: Boolean = false
)

// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // ── Location state ────────────────────────────────────────────────────────
    var locationStatus by remember { mutableStateOf<LocationStatus>(LocationStatus.Idle) }
    var currentLat by remember { mutableStateOf<Double?>(null) }
    var currentLon by remember { mutableStateOf<Double?>(null) }

    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Permission launcher
    val locationPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocationAndSendSMS(context, fusedClient, isEnglish) { lat, lon, status ->
                currentLat = lat
                currentLon = lon
                locationStatus = status
            }
        } else {
            locationStatus = LocationStatus.PermissionDenied
        }
    }

    // ── SOS Pulse animation ───────────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val sosPulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "sos_scale"
    )
    val ringPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ), label = "ring_scale"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseOut),
            repeatMode = RepeatMode.Restart
        ), label = "ring_alpha"
    )

    // ── Location fetch pulse (loading indicator) ──────────────────────────────
    val locLoadingAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "loc_load"
    )

    val contacts = listOf(
        EmergencyContact(
            nameEn = "Police Control Room",
            nameHi = "पुलिस कंट्रोल रूम",
            descriptionEn = "24×7 emergency police assistance for tourists and locals",
            descriptionHi = "पर्यटकों और स्थानीय लोगों के लिए 24×7 आपातकालीन पुलिस सहायता",
            phone = "100", icon = Icons.Default.Warning, categoryColor = Color(0xFFE53935)
        ),
        EmergencyContact(
            nameEn = "Ambulance / Medical",
            nameHi = "एम्बुलेंस / चिकित्सा",
            descriptionEn = "Emergency medical services, ambulance dispatch",
            descriptionHi = "आपातकालीन चिकित्सा सेवाएं, एम्बुलेंस",
            phone = "108", icon = Icons.Default.Call, categoryColor = Color(0xFFE53935)
        ),
        EmergencyContact(
            nameEn = "Fire Brigade",
            nameHi = "अग्निशमन सेवा",
            descriptionEn = "Fire emergency and rescue services",
            descriptionHi = "अग्नि आपात और बचाव सेवाएं",
            phone = "101", icon = Icons.Default.Warning, categoryColor = Color(0xFFFF6F00)
        ),
        EmergencyContact(
            nameEn = "Tourist Helpline (Rajasthan)",
            nameHi = "पर्यटक हेल्पलाइन (राजस्थान)",
            descriptionEn = "Official Rajasthan Tourism helpline — complaints & assistance",
            descriptionHi = "आधिकारिक राजस्थान पर्यटन हेल्पलाइन — शिकायत और मार्गदर्शन",
            phone = "1364", icon = Icons.Default.Info, categoryColor = Color(0xFF00897B)
        ),
        EmergencyContact(
            nameEn = "Chittorgarh District Hospital",
            nameHi = "चित्तौड़गढ़ जिला अस्पताल",
            descriptionEn = "Main government district hospital, Chittorgarh city",
            descriptionHi = "मुख्य सरकारी जिला अस्पताल, चित्तौड़गढ़ शहर",
            phone = "01472-240773", icon = Icons.Default.Call, categoryColor = Color(0xFF1E88E5)
        ),
        EmergencyContact(
            nameEn = "Chittorgarh Police Station",
            nameHi = "चित्तौड़गढ़ थाना",
            descriptionEn = "Kotwali Police Station, near Fort area",
            descriptionHi = "कोतवाली थाना, किला क्षेत्र के पास",
            phone = "01472-240225", icon = Icons.Default.LocationOn, categoryColor = Color(0xFF5E35B1)
        ),
        EmergencyContact(
            nameEn = "Archaeological Survey of India",
            nameHi = "भारतीय पुरातत्व सर्वेक्षण",
            descriptionEn = "Fort information, monument complaints — ASI Chittorgarh",
            descriptionHi = "किला जानकारी, स्मारक शिकायतें — ASI चित्तौड़गढ़",
            phone = "01472-241089", icon = Icons.Default.Info, categoryColor = GoldAccent
        ),
        EmergencyContact(
            nameEn = "Railway Helpdesk",
            nameHi = "रेलवे हेल्पडेस्क",
            descriptionEn = "Chittorgarh Junction (COR) — train info, lost & found",
            descriptionHi = "चित्तौड़गढ़ जंक्शन — ट्रेन जानकारी, खोया सामान",
            phone = "139", icon = Icons.Default.Call, categoryColor = Color(0xFF43A047)
        ),
        EmergencyContact(
            nameEn = "Women Helpline",
            nameHi = "महिला हेल्पलाइन",
            descriptionEn = "24×7 women safety and support helpline — Rajasthan",
            descriptionHi = "24×7 महिला सुरक्षा और सहायता हेल्पलाइन — राजस्थान",
            phone = "1090", icon = Icons.Default.Call, categoryColor = Color(0xFFD81B60)
        ),
        EmergencyContact(
            nameEn = "National Emergency",
            nameHi = "राष्ट्रीय आपातकालीन",
            descriptionEn = "Single integrated emergency number for all services across India",
            descriptionHi = "सभी सेवाओं के लिए एकीकृत राष्ट्रीय आपातकालीन नंबर",
            phone = "112", icon = Icons.Default.Warning, categoryColor = CrimsonSecondary
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Emergency Contacts" else "आपातकालीन संपर्क",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── SOS HERO BANNER ───────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF8B0000), CrimsonDark)
                            )
                        )
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        // Pulsing ring + SOS button
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(110.dp)) {
                            // Outer animated ring
                            Box(
                                modifier = Modifier
                                    .scale(ringPulse)
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935).copy(alpha = ringAlpha))
                            )
                            // Inner SOS button
                            Box(
                                modifier = Modifier
                                    .scale(sosPulse)
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935))
                                    .border(3.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                                        context.startActivity(intent)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "SOS",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        text = "SOS",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isEnglish) "Emergency? Tap SOS to call 112" else "आपातकाल? SOS दबाएं — 112",
                            color = Color.White, fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = if (isEnglish) "National Emergency — All services" else "राष्ट्रीय आपातकालीन — सभी सेवाएं",
                            color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // ── SHARE MY LOCATION CARD ────────────────────────────────────────
            item {
                ShareLocationCard(
                    isEnglish = isEnglish,
                    locationStatus = locationStatus,
                    currentLat = currentLat,
                    currentLon = currentLon,
                    locLoadingAnim = locLoadingAnim,
                    onShareClick = {
                        locationStatus = LocationStatus.Loading
                        val fineGranted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        val coarseGranted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (fineGranted || coarseGranted) {
                            fetchLocationAndSendSMS(context, fusedClient, isEnglish) { lat, lon, status ->
                                currentLat = lat
                                currentLon = lon
                                locationStatus = status
                            }
                        } else {
                            locationPermLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    onSendSMS = { lat, lon ->
                        openSMSWithLocation(context, lat, lon, isEnglish)
                    }
                )
            }

            // ── CONTACTS SECTION TITLE ────────────────────────────────────────
            item {
                Text(
                    text = if (isEnglish) "Important Numbers" else "महत्वपूर्ण नंबर",
                    fontSize = 13.sp, fontWeight = FontWeight.Bold,
                    color = SaffronPrimary, letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                )
            }

            // ── CONTACT CARDS ─────────────────────────────────────────────────
            items(contacts.size) { i ->
                val contact = contacts[i]
                EmergencyContactCard(
                    contact = contact,
                    isEnglish = isEnglish,
                    onCallClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

// ─── Share Location Card ──────────────────────────────────────────────────────

@Composable
private fun ShareLocationCard(
    isEnglish: Boolean,
    locationStatus: LocationStatus,
    currentLat: Double?,
    currentLon: Double?,
    locLoadingAnim: Float,
    onShareClick: () -> Unit,
    onSendSMS: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0A2E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1A0A2E), Color(0xFF2D1B4E))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SaffronPrimary.copy(alpha = 0.2f))
                        .border(1.5.dp, SaffronPrimary.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📍", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = if (isEnglish) "Share My Location" else "मेरी लोकेशन भेजें",
                        color = Color.White, fontWeight = FontWeight.Bold,
                        fontSize = 17.sp, fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = if (isEnglish) "Send GPS location via SMS to emergency services"
                        else "आपातकालीन सेवाओं को SMS से GPS लोकेशन भेजें",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 12.sp, lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status display
            when (locationStatus) {
                LocationStatus.Idle -> {
                    Button(
                        onClick = onShareClick,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronPrimary,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "Get My Location & Send SMS" else "लोकेशन लो और SMS भेजो",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp
                        )
                    }
                }

                LocationStatus.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SaffronPrimary.copy(alpha = locLoadingAnim * 0.3f + 0.1f))
                            .border(1.dp, SaffronPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                color = SaffronPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isEnglish) "Getting GPS location..." else "GPS लोकेशन मिल रही है...",
                                color = SaffronPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp
                            )
                        }
                    }
                }

                LocationStatus.Success -> {
                    if (currentLat != null && currentLon != null) {
                        // Show location coords
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1B4332).copy(alpha = 0.6f))
                                .border(1.dp, Color(0xFF2D6A4F), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✅", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isEnglish) "Location Found!" else "लोकेशन मिल गई!",
                                        color = Color(0xFF52B788),
                                        fontWeight = FontWeight.Bold, fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Lat: ${"%.6f".format(currentLat)}, Lon: ${"%.6f".format(currentLon)}",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp, fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        // SMS Send buttons
                        Text(
                            text = if (isEnglish) "Send location to:" else "लोकेशन भेजें:",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp)
                        )

                        SMSButton(
                            emoji = "👮",
                            label = if (isEnglish) "Police — 100" else "पुलिस — 100",
                            color = Color(0xFFE53935),
                            onClick = { openSMSWithLocationTo(context, currentLat, currentLon, "100", isEnglish) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SMSButton(
                            emoji = "🚑",
                            label = if (isEnglish) "Ambulance — 108" else "एम्बुलेंस — 108",
                            color = Color(0xFF1E88E5),
                            onClick = { openSMSWithLocationTo(context, currentLat, currentLon, "108", isEnglish) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SMSButton(
                            emoji = "🆘",
                            label = if (isEnglish) "National Emergency — 112" else "राष्ट्रीय आपातकाल — 112",
                            color = Color(0xFF8B0000),
                            onClick = { openSMSWithLocationTo(context, currentLat, currentLon, "112", isEnglish) }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(
                            onClick = { onShareClick() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isEnglish) "🔄 Refresh Location" else "🔄 लोकेशन अपडेट करें",
                                color = SaffronPrimary.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                LocationStatus.PermissionDenied -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7B1F1F).copy(alpha = 0.3f))
                            .border(1.dp, Color(0xFFE53935).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isEnglish) "⚠️ Location Permission Denied" else "⚠️ लोकेशन अनुमति अस्वीकृत",
                                color = Color(0xFFEF5350), fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            Text(
                                text = if (isEnglish) "Please allow location access in App Settings to use this feature."
                                else "इस सुविधा का उपयोग करने के लिए ऐप सेटिंग में लोकेशन एक्सेस की अनुमति दें।",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp, lineHeight = 17.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                LocationStatus.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF7B1F1F).copy(alpha = 0.3f))
                            .border(1.dp, Color(0xFFE53935).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "❌ Could not get location. Make sure GPS is ON and try again."
                            else "❌ लोकेशन नहीं मिली। GPS चालू करें और पुनः प्रयास करें।",
                            color = Color(0xFFEF5350), fontSize = 13.sp, lineHeight = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onShareClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary)
                    ) {
                        Text(if (isEnglish) "Try Again" else "पुनः प्रयास करें", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ─── SMS Button Helper ────────────────────────────────────────────────────────

@Composable
private fun SMSButton(
    emoji: String,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(

        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(46.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.15f),
            contentColor = color
        )
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text("📩", fontSize = 14.sp)
    }
}

// ─── Contact Card ─────────────────────────────────────────────────────────────

@Composable
private fun EmergencyContactCard(
    contact: EmergencyContact,
    isEnglish: Boolean,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(contact.categoryColor.copy(alpha = 0.15f))
                    .border(1.5.dp, contact.categoryColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = contact.icon, contentDescription = null,
                    tint = contact.categoryColor, modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEnglish) contact.nameEn else contact.nameHi,
                    fontWeight = FontWeight.Bold, fontSize = 15.sp,
                    fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isEnglish) contact.descriptionEn else contact.descriptionHi,
                    fontSize = 12.sp, lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            FilledTonalButton(
                onClick = onCallClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = contact.categoryColor.copy(alpha = 0.15f),
                    contentColor = contact.categoryColor
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(42.dp)
            ) {
                Icon(Icons.Default.Call, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(contact.phone, fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
    }
}

// ─── Location Status Sealed Class ────────────────────────────────────────────

sealed class LocationStatus {
    object Idle : LocationStatus()
    object Loading : LocationStatus()
    object Success : LocationStatus()
    object PermissionDenied : LocationStatus()
    object Error : LocationStatus()
}

// ─── Location Fetch Function ──────────────────────────────────────────────────

fun fetchLocationAndSendSMS(
    context: Context,
    fusedClient: FusedLocationProviderClient,
    isEnglish: Boolean,
    onResult: (Double?, Double?, LocationStatus) -> Unit
) {
    try {
        // Try last known location first (fast)
        fusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onResult(location.latitude, location.longitude, LocationStatus.Success)
            } else {
                // Request fresh location
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 5000L
                ).setMaxUpdates(1).build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        fusedClient.removeLocationUpdates(this)
                        val loc = result.lastLocation
                        if (loc != null) {
                            onResult(loc.latitude, loc.longitude, LocationStatus.Success)
                        } else {
                            onResult(null, null, LocationStatus.Error)
                        }
                    }
                }
                fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
            }
        }.addOnFailureListener {
            onResult(null, null, LocationStatus.Error)
        }
    } catch (e: SecurityException) {
        onResult(null, null, LocationStatus.PermissionDenied)
    } catch (e: Exception) {
        onResult(null, null, LocationStatus.Error)
    }
}

// ─── SMS with Location Helper ─────────────────────────────────────────────────

fun openSMSWithLocation(context: Context, lat: Double, lon: Double, isEnglish: Boolean) {
    openSMSWithLocationTo(context, lat, lon, "112", isEnglish)
}

fun openSMSWithLocationTo(
    context: Context?,
    lat: Double?,
    lon: Double?,
    phone: String,
    isEnglish: Boolean
) {
    if (context == null || lat == null || lon == null) return
    val mapsLink = "https://maps.google.com/?q=$lat,$lon"
    val message = if (isEnglish) {
        "🆘 EMERGENCY! I need help. My current GPS location:\n$mapsLink\n(Lat: $lat, Lon: $lon)\nSent from Visit Chittorgarh App."
    } else {
        "🆘 आपातकाल! मुझे मदद चाहिए। मेरी वर्तमान GPS लोकेशन:\n$mapsLink\n(अक्षांश: $lat, देशांतर: $lon)\nVisit Chittorgarh App से भेजा गया।"
    }
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:$phone")
        putExtra("sms_body", message)
    }
    context.startActivity(intent)
}
