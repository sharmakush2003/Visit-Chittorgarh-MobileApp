package com.example.visitchittorgarh.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.visitchittorgarh.data.LocalItem
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.example.visitchittorgarh.theme.GoldAccent

// Currency configuration enum with symbols, codes, and conversion rates from INR
enum class AppCurrency(val symbol: String, val code: String, val rateFromInr: Double) {
    INR("₹", "INR", 1.0),
    USD("$", "USD", 0.012),
    EUR("€", "EUR", 0.011),
    GBP("£", "GBP", 0.0094)
}

@Composable
fun LocalScreen(
    locals: List<LocalItem>,
    isEnglish: Boolean
) {
    var selectedLocalItem by remember { mutableStateOf<LocalItem?>(null) }
    var selectedCurrency by remember { mutableStateOf(AppCurrency.INR) } // Default currency is INR (Rupees)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Unified Palace Cream Background
    ) {
        // Scrollable Specialties List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Main Title
            item {
                Text(
                    text = "Discover Authentic\nRajasthani Crafts",
                    color = MaterialTheme.colorScheme.primary, // Crimson Maroon Title
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 36.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            // Currency Switcher Control
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = if (isEnglish) "DISPLAY CURRENCY:" else "मुद्रा चुनें:",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppCurrency.values().forEach { currency ->
                            val isSelected = selectedCurrency == currency
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedCurrency = currency }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${currency.symbol} ${currency.code}",
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                }
            }

            // Specialty Cards List (Unified Compact Horizontal Cards)
            items(locals) { item ->
                SpecialtyCard(
                    item = item,
                    isEnglish = isEnglish,
                    currency = selectedCurrency,
                    onClick = { selectedLocalItem = item }
                )
            }
        }
    }

    selectedLocalItem?.let { item ->
        // Format precise converted price range for the details dialog
        val minConv = (item.minPriceInr * selectedCurrency.rateFromInr).toInt()
        val maxConv = (item.maxPriceInr * selectedCurrency.rateFromInr).toInt()
        val formattedPrice = "${selectedCurrency.symbol}$minConv - ${selectedCurrency.symbol}$maxConv"

        LocalItemDetailsDialog(
            item = item,
            isEnglish = isEnglish,
            formattedPrice = formattedPrice,
            onDismiss = { selectedLocalItem = null }
        )
    }
}

// Compact Horizontal Specialty Card
@Composable
fun SpecialtyCard(
    item: LocalItem,
    isEnglish: Boolean,
    currency: AppCurrency,
    onClick: () -> Unit
) {
    // Perform precise conversion from INR
    val minConv = (item.minPriceInr * currency.rateFromInr).toInt()
    val maxConv = (item.maxPriceInr * currency.rateFromInr).toInt()
    val formattedPrice = "${currency.symbol}$minConv - ${currency.symbol}$maxConv"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)), // Gold border
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // White Surface
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Image (Square cropped)
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name.get(isEnglish),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            
            Spacer(modifier = Modifier.width(14.dp))
            
            // Right Content Area
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title and Category Tag Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name.get(isEnglish).uppercase(),
                        color = MaterialTheme.colorScheme.primary, // Crimson color
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Subtle Category Tag
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.category.get(isEnglish),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Description (Max 2 lines for compact look)
                Text(
                    text = item.description.get(isEnglish),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price, Origin, and Details Button Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Converted Price Range
                        Text(
                            text = formattedPrice,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Origin location
                        Text(
                            text = item.origin.get(isEnglish),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2 // Wrap text to multiple lines if too long
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Small premium Gold Button
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "Details" else "विवरण",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// Redesigned Local specialties dialog (light theme consistent)
@Composable
fun LocalItemDetailsDialog(
    item: LocalItem,
    isEnglish: Boolean,
    formattedPrice: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Header Banner (Unified Royal Crimson Gradient)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(CrimsonSecondary, CrimsonDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .background(GoldAccent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item.category.get(isEnglish).uppercase(),
                                color = GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.name.get(isEnglish),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                // Image
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name.get(isEnglish),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                // Details Content
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isEnglish) "ESTIMATED PRICE RANGE" else "अनुमानित मूल्य सीमा",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isEnglish) "DESCRIPTION" else "विवरण",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.description.get(isEnglish),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isEnglish) "ORIGIN / LOCATION" else "उत्पत्ति / स्थान",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.origin.get(isEnglish),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Button(
                        onClick = {
                            val msg = "Hello Visit Chittorgarh! I want to inquire about *${item.name.get(isEnglish)}* (${item.category.get(isEnglish)})."
                            sendWhatsAppHelper(context, "+917597451057", msg)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Text(
                            text = if (isEnglish) "Inquire on WhatsApp" else "व्हाट्सएप पर पूछताछ करें",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            val dial = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+917597451057")
                            }
                            context.startActivity(dial)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (isEnglish) "Call Helpline" else "हेल्पलाइन पर कॉल करें",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isEnglish) "Close details" else "विवरण बंद करें",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }
        }
    }
}

private fun sendWhatsAppHelper(context: Context, phoneNumber: String, message: String) {
    try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + java.net.URLEncoder.encode(message, "UTF-8")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
    }
}
