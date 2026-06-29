package com.example.visitchittorgarh.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.visitchittorgarh.data.CabOption
import com.example.visitchittorgarh.data.GuideOption
import com.example.visitchittorgarh.data.StayOption
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    cabs: List<CabOption>,
    guides: List<GuideOption>,
    stays: List<StayOption>,
    isEnglish: Boolean
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        if (isEnglish) "Royal Chauffeur" else "शाही वाहन",
        if (isEnglish) "Historian Guides" else "इतिहासकार गाइड",
        if (isEnglish) "Palace Stays" else "राजशाही आवास"
    )
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Royal Crimson Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CrimsonSecondary, CrimsonDark)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = if (isEnglish) "On-Demand Services" else "ऑन-डिमांड सेवाएं",
                    color = GoldAccent,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEnglish) 
                        "Hire professional historians, reserve luxury transport, or secure palace hotel reservations." 
                        else "पेशेवर इतिहासकारों को नियुक्त करें, लक्जरी परिवहन आरक्षित करें, या महल होटलों में बुकिंग सुरक्षित करें।",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // Tab Selector Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SaffronPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = with(TabRowDefaults) { Modifier.tabIndicatorOffset(tabPositions[selectedTab]) },
                    color = SaffronPrimary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { 
                        Text(
                            text = title, 
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp
                        ) 
                    }
                )
            }
        }

        // Tab Content lists
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> CabTabContent(cabs = cabs, isEnglish = isEnglish, context = context)
                1 -> GuideTabContent(guides = guides, isEnglish = isEnglish, context = context)
                2 -> StayTabContent(stays = stays, isEnglish = isEnglish, context = context)
            }
        }
    }
}

// Cabs Tab Content (Flat list items)
@Composable
fun CabTabContent(cabs: List<CabOption>, isEnglish: Boolean, context: Context) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(cabs) { cab ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    AsyncImage(
                        model = cab.imageUrl,
                        contentDescription = cab.vehicleName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cab.vehicleName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Box(
                                modifier = Modifier
                                    .background(SaffronPrimary.copy(alpha = 0.12f), shape = RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = cab.ratePerKm.get(isEnglish),
                                    color = SaffronPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Capacity",
                                tint = CrimsonSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = (if (isEnglish) "Capacity: " else "क्षमता: ") + cab.capacity.get(isEnglish),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = cab.description.get(isEnglish),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val msg = "Hello Visit Chittorgarh! I want to book a royal taxi/cab service. Vehicle preference: *${cab.vehicleName}*."
                                sendWhatsApp(context, "+917597451057", msg)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonSecondary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Book", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isEnglish) "Book Cab" else "वाहन बुक करें",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// Guides Tab Content (Flat list items)
@Composable
fun GuideTabContent(guides: List<GuideOption>, isEnglish: Boolean, context: Context) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
    ) {
        items(guides) { guide ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = guide.imageUrl,
                            contentDescription = guide.name.get(isEnglish),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = guide.name.get(isEnglish),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = (if (isEnglish) "Experience: " else "अनुभव: ") + guide.experience.get(isEnglish),
                                fontSize = 12.sp,
                                color = SaffronPrimary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(SaffronPrimary.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = GoldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = guide.rating.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = SaffronPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = (if (isEnglish) "Specialty: " else "विशेषता: ") + guide.specialty.get(isEnglish),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = (if (isEnglish) "Languages: " else "भाषाएँ: ") + guide.languages.get(isEnglish),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                             val msg = "Hello Visit Chittorgarh! I want to hire a local historian guide. Preferred guide: *${guide.name.get(isEnglish)}*."
                             sendWhatsApp(context, "+917597451057", msg)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonSecondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (isEnglish) "Hire Guide" else "गाइड नियुक्त करें",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// Stays Tab Content (Flat list items)
@Composable
fun StayTabContent(stays: List<StayOption>, isEnglish: Boolean, context: Context) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(stays) { stay ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    AsyncImage(
                        model = stay.imageUrl,
                        contentDescription = stay.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stay.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stay.type.get(isEnglish).uppercase(),
                                    fontSize = 11.sp,
                                    color = CrimsonSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(SaffronPrimary.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stay.rating.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SaffronPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stay.priceRange.get(isEnglish),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stay.description.get(isEnglish),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val msg = "Hello Visit Chittorgarh! I want to inquire about hotel reservations at *${stay.name}*."
                                sendWhatsApp(context, "+917597451057", msg)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonSecondary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = if (isEnglish) "Book Stay" else "आवास बुक करें",
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun sendWhatsApp(context: Context, phoneNumber: String, message: String) {
    try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed on this device.", Toast.LENGTH_LONG).show()
    }
}
