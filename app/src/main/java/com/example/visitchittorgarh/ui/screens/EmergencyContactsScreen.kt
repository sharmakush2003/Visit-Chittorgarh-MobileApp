package com.example.visitchittorgarh.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // Pulse animation for SOS button
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val sosPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sos_scale"
    )

    val contacts = listOf(
        EmergencyContact(
            nameEn = "Police Control Room",
            nameHi = "पुलिस कंट्रोल रूम",
            descriptionEn = "24×7 emergency police assistance for tourists and locals",
            descriptionHi = "पर्यटकों और स्थानीय लोगों के लिए 24×7 आपातकालीन पुलिस सहायता",
            phone = "100",
            icon = Icons.Default.Warning,
            categoryColor = Color(0xFFE53935),
            isSOS = true
        ),
        EmergencyContact(
            nameEn = "Ambulance / Medical",
            nameHi = "एम्बुलेंस / चिकित्सा",
            descriptionEn = "Emergency medical services, ambulance dispatch",
            descriptionHi = "आपातकालीन चिकित्सा सेवाएं, एम्बुलेंस",
            phone = "108",
            icon = Icons.Default.Call,
            categoryColor = Color(0xFFE53935)
        ),
        EmergencyContact(
            nameEn = "Fire Brigade",
            nameHi = "अग्निशमन सेवा",
            descriptionEn = "Fire emergency and rescue services",
            descriptionHi = "अग्नि आपात और बचाव सेवाएं",
            phone = "101",
            icon = Icons.Default.Warning,
            categoryColor = Color(0xFFFF6F00)
        ),
        EmergencyContact(
            nameEn = "Tourist Helpline (Rajasthan)",
            nameHi = "पर्यटक हेल्पलाइन (राजस्थान)",
            descriptionEn = "Official Rajasthan Tourism helpline for visitors, complaints and assistance",
            descriptionHi = "पर्यटकों की सहायता, शिकायत और मार्गदर्शन के लिए आधिकारिक राजस्थान पर्यटन हेल्पलाइन",
            phone = "1364",
            icon = Icons.Default.Info,
            categoryColor = Color(0xFF00897B)
        ),
        EmergencyContact(
            nameEn = "Chittorgarh District Hospital",
            nameHi = "चित्तौड़गढ़ जिला अस्पताल",
            descriptionEn = "Main government district hospital, Chittorgarh city",
            descriptionHi = "मुख्य सरकारी जिला अस्पताल, चित्तौड़गढ़ शहर",
            phone = "01472-240773",
            icon = Icons.Default.Call,
            categoryColor = Color(0xFF1E88E5)
        ),
        EmergencyContact(
            nameEn = "Chittorgarh Police Station",
            nameHi = "चित्तौड़गढ़ थाना",
            descriptionEn = "Kotwali Police Station, near Fort area",
            descriptionHi = "कोतवाली थाना, किला क्षेत्र के पास",
            phone = "01472-240225",
            icon = Icons.Default.LocationOn,
            categoryColor = Color(0xFF5E35B1)
        ),
        EmergencyContact(
            nameEn = "Archaeological Survey of India",
            nameHi = "भारतीय पुरातत्व सर्वेक्षण",
            descriptionEn = "Fort information, monument complaints — ASI Chittorgarh office",
            descriptionHi = "किला जानकारी, स्मारक शिकायतें — ASI चित्तौड़गढ़ कार्यालय",
            phone = "01472-241089",
            icon = Icons.Default.Info,
            categoryColor = GoldAccent
        ),
        EmergencyContact(
            nameEn = "Railway Station Helpdesk",
            nameHi = "रेलवे स्टेशन हेल्पडेस्क",
            descriptionEn = "Chittorgarh Junction (COR) — train info, lost & found",
            descriptionHi = "चित्तौड़गढ़ जंक्शन (COR) — ट्रेन जानकारी, खोया सामान",
            phone = "139",
            icon = Icons.Default.Call,
            categoryColor = Color(0xFF43A047)
        ),
        EmergencyContact(
            nameEn = "Women Helpline",
            nameHi = "महिला हेल्पलाइन",
            descriptionEn = "24×7 women safety and support helpline — Rajasthan",
            descriptionHi = "24×7 महिला सुरक्षा और सहायता हेल्पलाइन — राजस्थान",
            phone = "1090",
            icon = Icons.Default.Call,
            categoryColor = Color(0xFFD81B60)
        ),
        EmergencyContact(
            nameEn = "National Emergency Number",
            nameHi = "राष्ट्रीय आपातकालीन नंबर",
            descriptionEn = "Single integrated emergency number for all services across India",
            descriptionHi = "सभी सेवाओं के लिए एकीकृत राष्ट्रीय आपातकालीन नंबर",
            phone = "112",
            icon = Icons.Default.Warning,
            categoryColor = CrimsonSecondary
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // SOS Banner Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF8B0000), CrimsonDark)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Pulsing SOS call button
                        Box(
                            modifier = Modifier
                                .scale(sosPulse)
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE53935))
                                .border(3.dp, Color.White.copy(alpha = 0.6f), CircleShape)
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
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isEnglish) "Emergency? Tap SOS to call 112" else "आपातकाल? SOS दबाएं — 112",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = if (isEnglish) "National Emergency Number — All services" else "राष्ट्रीय आपातकालीन नंबर — सभी सेवाएं",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Section title
            item {
                Text(
                    text = if (isEnglish) "Important Numbers" else "महत्वपूर्ण नंबर",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                )
            }

            // Contact Cards
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color icon circle
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(contact.categoryColor.copy(alpha = 0.15f))
                    .border(1.5.dp, contact.categoryColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = contact.icon,
                    contentDescription = null,
                    tint = contact.categoryColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEnglish) contact.nameEn else contact.nameHi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isEnglish) contact.descriptionEn else contact.descriptionHi,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Call Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = contact.phone,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
