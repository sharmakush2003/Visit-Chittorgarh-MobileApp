package com.example.visitchittorgarh.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary

data class Restaurant(
    val nameEn: String,
    val nameHi: String,
    val cuisineEn: String,
    val cuisineHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val specialityEn: String,
    val specialityHi: String,
    val priceRange: String,
    val rating: Double,
    val location: String,
    val mapsUrl: String,
    val imageUrl: String,
    val tags: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRestaurantsScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("All") }

    val tags = listOf("All", "Dal Baati", "Thali", "Sweet", "Snacks", "Veg")

    val restaurants = listOf(
        Restaurant(
            nameEn = "Chittorgarh Dhaba — Fort Road",
            nameHi = "चित्तौड़गढ़ ढाबा — फोर्ट रोड",
            cuisineEn = "Rajasthani Authentic",
            cuisineHi = "राजस्थानी प्रामाणिक",
            descriptionEn = "Legendary roadside dhaba serving the most authentic dal baati churma in Chittorgarh. Family-run since 1982.",
            descriptionHi = "चित्तौड़गढ़ में सबसे प्रामाणिक दाल बाटी चूरमा परोसने वाला प्रसिद्ध ढाबा। 1982 से पारिवारिक संचालन।",
            specialityEn = "Dal Baati Churma, Gatte ki Sabzi",
            specialityHi = "दाल बाटी चूरमा, गट्टे की सब्जी",
            priceRange = "₹150–₹300",
            rating = 4.7,
            location = "Fort Road, Near Vijay Stambh",
            mapsUrl = "https://maps.google.com/?q=Chittorgarh+Fort+Road+Dhaba",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Dal_Baati_Churma.jpg/1280px-Dal_Baati_Churma.jpg",
            tags = listOf("Dal Baati", "Thali", "Veg")
        ),
        Restaurant(
            nameEn = "Mewar Bhoj Griha",
            nameHi = "मेवाड़ भोज गृह",
            cuisineEn = "Traditional Mewari Thali",
            cuisineHi = "पारंपरिक मेवाड़ी थाली",
            descriptionEn = "Traditional Mewari thali restaurant with unlimited servings. Pure vegetarian. Famous for Rajasthani curries and baati.",
            descriptionHi = "असीमित परोसन के साथ पारंपरिक मेवाड़ी थाली रेस्तरां। शुद्ध शाकाहारी। राजस्थानी करी और बाटी के लिए प्रसिद्ध।",
            specialityEn = "Unlimited Mewari Thali, Ker Sangri",
            specialityHi = "असीमित मेवाड़ी थाली, केर सांगरी",
            priceRange = "₹200–₹350",
            rating = 4.5,
            location = "Collectorate Circle, Chittorgarh",
            mapsUrl = "https://maps.google.com/?q=Mewar+Bhoj+Griha+Chittorgarh",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Rajasthani-thali.jpg/1280px-Rajasthani-thali.jpg",
            tags = listOf("Thali", "Veg", "Dal Baati")
        ),
        Restaurant(
            nameEn = "Pratap Restaurant",
            nameHi = "प्रताप रेस्तरां",
            cuisineEn = "North Indian & Rajasthani",
            cuisineHi = "उत्तर भारतीय और राजस्थानी",
            descriptionEn = "A popular hotel restaurant near the main bus stand, known for quick authentic Rajasthani food and warm hospitality.",
            descriptionHi = "मुख्य बस स्टैंड के पास एक लोकप्रिय होटल रेस्तरां, त्वरित प्रामाणिक राजस्थानी भोजन और गर्म आतिथ्य के लिए जाना जाता है।",
            specialityEn = "Laal Maas, Bajre ki Roti, Churma Ladoo",
            specialityHi = "लाल मांस, बाजरे की रोटी, चूरमा लड्डू",
            priceRange = "₹250–₹500",
            rating = 4.3,
            location = "Near Bus Stand, Chittorgarh",
            mapsUrl = "https://maps.google.com/?q=Pratap+Restaurant+Chittorgarh",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Laal_maas.jpg/1280px-Laal_maas.jpg",
            tags = listOf("Thali", "Snacks")
        ),
        Restaurant(
            nameEn = "Shreeji Sweets & Snacks",
            nameHi = "श्रीजी मिठाई और स्नैक्स",
            cuisineEn = "Rajasthani Sweets & Street Food",
            cuisineHi = "राजस्थानी मिठाई और स्ट्रीट फूड",
            descriptionEn = "Famous local sweet shop for Mawa Kachori, Ghevar, and Churma Ladoo. A must-visit sweet shop in the old city.",
            descriptionHi = "मावा कचौरी, घेवर और चूरमा लड्डू के लिए प्रसिद्ध स्थानीय मिठाई की दुकान। पुराने शहर में अवश्य देखने वाली मिठाई की दुकान।",
            specialityEn = "Mawa Kachori, Churma Ladoo, Ghevar",
            specialityHi = "मावा कचौरी, चूरमा लड्डू, घेवर",
            priceRange = "₹30–₹200",
            rating = 4.8,
            location = "Sadar Bazar, Old City Chittorgarh",
            mapsUrl = "https://maps.google.com/?q=Shreeji+Sweets+Chittorgarh",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Mawa_kachori.jpg/1280px-Mawa_kachori.jpg",
            tags = listOf("Sweet", "Snacks", "Veg")
        ),
        Restaurant(
            nameEn = "Padmini Palace Restaurant",
            nameHi = "पद्मिनी पैलेस रेस्तरां",
            cuisineEn = "Royal Rajputana Cuisine",
            cuisineHi = "शाही राजपूताना व्यंजन",
            descriptionEn = "Upscale restaurant in a heritage hotel. Serves royal Rajputana cuisine with lake views. Fine-dining experience.",
            descriptionHi = "एक हेरिटेज होटल में हाई-एंड रेस्तरां। झील के दृश्य के साथ शाही राजपूताना व्यंजन परोसता है। फाइन डाइनिंग अनुभव।",
            specialityEn = "Royal Thali, Maas ki Kadhi, Saffron Kheer",
            specialityHi = "शाही थाली, मास की कढ़ी, केसर खीर",
            priceRange = "₹500–₹1200",
            rating = 4.6,
            location = "Near Padmini Palace, Fort Area",
            mapsUrl = "https://maps.google.com/?q=Padmini+Palace+Restaurant+Chittorgarh",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Rajasthani_food.jpg/1280px-Rajasthani_food.jpg",
            tags = listOf("Thali", "Veg")
        ),
        Restaurant(
            nameEn = "Ratan ki Chai — Tea Stall",
            nameHi = "रतन की चाय — टी स्टॉल",
            cuisineEn = "Rajasthani Chai & Snacks",
            cuisineHi = "राजस्थानी चाय और स्नैक्स",
            descriptionEn = "Iconic roadside chai stall near the fort. Famous for Masala Chai, Kachori, and Pyaaz ki Kachori. Budget traveler's paradise.",
            descriptionHi = "किले के पास प्रतिष्ठित रोडसाइड चाय स्टॉल। मसाला चाय, कचौरी और प्याज की कचौरी के लिए प्रसिद्ध। बजट यात्री का स्वर्ग।",
            specialityEn = "Masala Chai, Pyaaz Kachori, Mirchi Bada",
            specialityHi = "मसाला चाय, प्याज कचौरी, मिर्ची बड़ा",
            priceRange = "₹10–₹80",
            rating = 4.9,
            location = "Fort Gate Road, Chittorgarh",
            mapsUrl = "https://maps.google.com/?q=Tea+Stall+Chittorgarh+Fort",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Pyaz_kachori.jpg/1280px-Pyaz_kachori.jpg",
            tags = listOf("Snacks", "Veg")
        )
    )

    val filtered = restaurants.filter { r ->
        val matchSearch = r.nameEn.contains(searchQuery, ignoreCase = true) ||
                r.cuisineEn.contains(searchQuery, ignoreCase = true) ||
                r.specialityEn.contains(searchQuery, ignoreCase = true)
        val matchTag = selectedTag == "All" || r.tags.contains(selectedTag)
        matchSearch && matchTag
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Food & Restaurants" else "भोजन और रेस्तरां",
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
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Dal_Baati_Churma.jpg/1280px-Dal_Baati_Churma.jpg",
                        contentDescription = "Dal Baati Churma",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.88f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "MEWARI CUISINE GUIDE" else "मेवाड़ी व्यंजन गाइड",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (isEnglish) "Food & Restaurants" else "भोजन और रेस्तरां",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }

            // Dal Baati Churma Highlight Box
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SaffronPrimary.copy(alpha = 0.12f)
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🍽️", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (isEnglish) "Must Try: Dal Baati Churma" else "जरूर खाएं: दाल बाटी चूरमा",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Serif,
                                color = SaffronPrimary
                            )
                            Text(
                                text = if (isEnglish) "Rajasthan's iconic dish — baked wheat balls (baati) served with lentil curry (dal) and sweet crushed wheat (churma). Best enjoyed in a traditional thali." else "राजस्थान का प्रतिष्ठित व्यंजन — गेहूं की बाटी, दाल और मीठे चूरमे के साथ। पारंपरिक थाली में सबसे अच्छा स्वाद।",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text(if (isEnglish) "Search dal baati, sweets..." else "दाल बाटी, मिठाई खोजें...")
                    },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = SaffronPrimary) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaffronPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
            }

            // Tag Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    items(tags) { tag ->
                        val isSelected = selectedTag == tag
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTag = tag },
                            label = { Text(tag, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaffronPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = SaffronPrimary,
                                containerColor = Color.Transparent,
                                labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true, selected = isSelected,
                                borderColor = SaffronPrimary, selectedBorderColor = SaffronPrimary, borderWidth = 1.dp
                            )
                        )
                    }
                }
            }

            // Restaurant cards
            items(filtered.size) { i ->
                RestaurantCard(
                    restaurant = filtered[i],
                    isEnglish = isEnglish,
                    onMapsClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(filtered[i].mapsUrl))
                        context.startActivity(intent)
                    }
                )
            }

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEnglish) "No restaurants found." else "कोई रेस्तरां नहीं मिला।",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RestaurantCard(
    restaurant: Restaurant,
    isEnglish: Boolean,
    onMapsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Food image
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = restaurant.nameEn,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
                // Price range badge top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = restaurant.priceRange,
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Name + rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEnglish) restaurant.nameEn else restaurant.nameHi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = restaurant.rating.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Text(
                    text = if (isEnglish) restaurant.cuisineEn else restaurant.cuisineHi,
                    color = SaffronPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = if (isEnglish) restaurant.descriptionEn else restaurant.descriptionHi,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 6.dp),
                    maxLines = 2
                )

                // Speciality
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = "⭐", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isEnglish) restaurant.specialityEn else restaurant.specialityHi,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = CrimsonSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.location,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                            modifier = Modifier.widthIn(max = 170.dp)
                        )
                    }
                    FilledTonalButton(
                        onClick = onMapsClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = CrimsonSecondary.copy(alpha = 0.12f),
                            contentColor = CrimsonSecondary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEnglish) "Directions" else "दिशा", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
