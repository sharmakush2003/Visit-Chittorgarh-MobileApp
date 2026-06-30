package com.example.visitchittorgarh.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary

data class TouristPlace(
    val nameEn: String,
    val nameHi: String,
    val descEn: String,
    val descHi: String,
    val image: Any
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutChittorgarhScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val touristPlaces = listOf(
        TouristPlace(
            nameEn = "Chittorgarh Fort",
            nameHi = "चित्तौड़गढ़ किला",
            descEn = "The largest fort in India, standing on a 180m hill. It is the core center of Rajput pride, featuring seven majestic gates and royal palaces.",
            descHi = "180 मीटर ऊँची पहाड़ी पर स्थित भारत का सबसे बड़ा किला। यह राजपूत गौरव का मुख्य केंद्र है, जिसमें सात राजसी द्वार और शाही महल बने हैं।",
            image = R.drawable.chittorgarh_fort
        ),
        TouristPlace(
            nameEn = "Vijay Stambh (Tower of Victory)",
            nameHi = "विजय स्तम्भ",
            descEn = "A massive 9-story tower constructed by Maharana Kumbha in 1448 to commemorate his victory over the armies of Malwa and Gujarat.",
            descHi = "मालवा और गुजरात की सेनाओं पर अपनी जीत की स्मृति में 1448 में महाराणा कुंभा द्वारा निर्मित एक विशाल 9 मंजिला स्तंभ।",
            image = R.drawable.vijay_stambh
        ),
        TouristPlace(
            nameEn = "Kirti Stambh (Tower of Fame)",
            nameHi = "कीर्ति स्तम्भ",
            descEn = "A 12th-century tower built by a Jain merchant, dedicated to Adinath, the first Jain Tirthankara. Beautifully carved with Digambara figures.",
            descHi = "एक जैन व्यापारी द्वारा निर्मित 12वीं सदी का स्तंभ, जो पहले जैन तीर्थंकर आदिनाथ को समर्पित है। दिगंबर आकृतियों से सुंदर नक्काशी की गई है।",
            image = "https://images.unsplash.com/photo-1603258591326-e14541824a8e?w=800&auto=format&fit=crop"
        ),
        TouristPlace(
            nameEn = "Padmini Palace",
            nameHi = "पद्मिनी महल",
            descEn = "The royal palace of Queen Padmini, located near the water body where Delhi Sultan Alauddin Khilji was allowed a glimpse of the queen's reflection.",
            descHi = "रानी पद्मिनी का शाही महल, जो जलाशय के पास स्थित है जहाँ दिल्ली के सुल्तान अलाउद्दीन खिलजी को रानी के प्रतिबिंब की झलक देखने की अनुमति दी गई थी।",
            image = "https://images.unsplash.com/photo-1599661046289-e31897846e41?w=800&auto=format&fit=crop"
        ),
        TouristPlace(
            nameEn = "Rana Kumbha Palace",
            nameHi = "राणा कुम्भा महल",
            descEn = "The ruined palace of Maharana Kumbha, featuring beautiful Mewari architecture. It is also the historic site where Jauhar took place.",
            descHi = "महाराणा कुंभा का खंडहर महल, जिसमें सुंदर मेवाड़ी वास्तुकला है। यह वही ऐतिहासिक स्थल भी है जहाँ जौहर की रस्म हुई थी।",
            image = "https://images.unsplash.com/photo-1600703136783-bdd5ee2a252f?w=800&auto=format&fit=crop"
        ),
        TouristPlace(
            nameEn = "Gaumukh Reservoir",
            nameHi = "गौमुख कुंड",
            descEn = "A deep sacred pool fed by a natural spring flowing from a rock shaped like a cow's mouth. A tranquil spiritual site inside the fort.",
            descHi = "एक गहरा पवित्र कुंड जो गाय के मुंह के आकार की चट्टान से बहने वाले प्राकृतिक झरने से भरा रहता है। किले के अंदर एक शांत आध्यात्मिक स्थल।",
            image = "https://images.unsplash.com/photo-1545128485-c400e7702796?w=800&auto=format&fit=crop"
        ),
        TouristPlace(
            nameEn = "Sanwariaji Temple",
            nameHi = "सांवलिया जी मंदिर",
            descEn = "A grand temple complex dedicated to Lord Krishna (Sanwaria Seth), located 40 km from the city. It is one of the richest temples in Rajasthan.",
            descHi = "भगवान कृष्ण (सांवलिया सेठ) को समर्पित एक भव्य मंदिर परिसर, जो शहर से 40 किमी दूर स्थित है। यह राजस्थान के सबसे समृद्ध मंदिरों में से एक है।",
            image = R.drawable.sanwaria_temple
        ),
        TouristPlace(
            nameEn = "Meera Temple",
            nameHi = "मीरा मंदिर",
            descEn = "Built by Rana Kumbha, this temple is dedicated to the saint-poetess Meera Bai and her pure devotion to Lord Krishna. Features a small shrine of Saint Ravidas.",
            descHi = "महाराणा कुंभा द्वारा निर्मित, यह मंदिर संत-कवयित्री मीरा बाई और भगवान कृष्ण के प्रति उनकी शुद्ध भक्ति को समर्पित है। इसमें संत रविदास का एक छोटा मंदिर है।",
            image = "https://images.unsplash.com/photo-1602643163983-ed0babc39797?w=800&auto=format&fit=crop"
        ),
        TouristPlace(
            nameEn = "Matrikundia Temple",
            nameHi = "मातृकुण्डिया मंदिर",
            descEn = "Known as the 'Haridwar of Rajasthan', this holy site on the Banas river features sacred pools where pilgrims perform ancestral rituals.",
            descHi = "बनास नदी के तट पर स्थित इस पवित्र स्थल को 'राजस्थान का हरिद्वार' कहा जाता है, जहाँ श्रद्धालु पूर्वजों के लिए अनुष्ठान करते हैं।",
            image = R.drawable.matrikundia_temple
        ),
        TouristPlace(
            nameEn = "Menal Waterfall & Temples",
            nameHi = "मेनाल जलप्रपात और मंदिर",
            descEn = "A scenic 150-feet waterfall dropping into a deep gorge, surrounded by ancient 11th-century temples resembling the Khajuraho art style.",
            descHi = "एक गहरी घाटी में गिरने वाला 150 फीट का मनोरम जलप्रपात, जो खजुराहो कला शैली से मिलते-जुलते प्राचीन 11वीं सदी के मंदिरों से घिरा हुआ है।",
            image = "https://images.unsplash.com/photo-1508496404318-d419e9bc339c?w=800&auto=format&fit=crop"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "About Chittorgarh" else "चित्तौड़गढ़ विरासत",
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
            // Full Bleed Banner Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vijay_stambh),
                    contentDescription = "Vijay Stambh Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(GoldAccent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                            .border(0.5.dp, GoldAccent, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "UNESCO WORLD HERITAGE SITE" else "यूनेस्को विश्व धरोहर स्थल",
                            color = GoldAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isEnglish) "The Pride of Rajputana" else "राजपूताना का स्वाभिमान",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Royal Quote Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, GoldAccent.copy(alpha = 0.3f)),
                    colors = CardDefaults.cardColors(containerColor = CrimsonDark.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEnglish) {
                                "\"To see Chittorgarh is to see the soul of Rajputana. There is no other fort in the world that matches its stories of valor, loyalty, and supreme sacrifice.\""
                            } else {
                                "\"चित्तौड़गढ़ को देखना राजपूताना की आत्मा को देखना है। दुनिया में ऐसा कोई अन्य किला नहीं है जो इसकी वीरता, वफादारी और सर्वोच्च बलिदान की कहानियों का मुकाबला कर सके।\""
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            color = CrimsonSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Statistics Summary Card (Grid View)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "700+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                            Text(text = if (isEnglish) "Acres Hilltop" else "एकड़ पहाड़ी", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "180m", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                            Text(text = if (isEnglish) "Fort Elevation" else "किले की ऊँचाई", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "22", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                            Text(text = if (isEnglish) "Active Reservoirs" else "सक्रिय जलाशय", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }

                // Tourist Places Title
                Text(
                    text = if (isEnglish) "Top Tourist Destinations" else "प्रमुख पर्यटन स्थल",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // List of 10 Tourist Places with Image Loader
                touristPlaces.forEach { place ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            // Image loader supporting both local drawables and web URLs
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                if (place.image is Int) {
                                    Image(
                                        painter = painterResource(id = place.image),
                                        contentDescription = place.nameEn,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    AsyncImage(
                                        model = place.image,
                                        contentDescription = place.nameEn,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        placeholder = painterResource(id = R.drawable.vijay_stambh)
                                    )
                                }

                                // Dark overlay on bottom of the image for text contrast
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                            )
                                        )
                                )

                                Text(
                                    text = if (isEnglish) place.nameEn else place.nameHi,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                )
                            }

                             // Description panel
                             Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                 Text(
                                     text = if (isEnglish) place.descEn else place.descHi,
                                     fontSize = 13.sp,
                                     lineHeight = 18.sp,
                                     color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                 )
                                 Spacer(modifier = Modifier.height(12.dp))
                                 val context = LocalContext.current
                                 OutlinedButton(
                                     onClick = {
                                         val destinationName = "${place.nameEn}, Chittorgarh, Rajasthan"
                                         val intent = android.content.Intent(
                                             android.content.Intent.ACTION_VIEW,
                                             android.net.Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${java.net.URLEncoder.encode(destinationName, "UTF-8")}")
                                         )
                                         context.startActivity(intent)
                                     },
                                     modifier = Modifier
                                         .fillMaxWidth()
                                         .height(40.dp),
                                     shape = RoundedCornerShape(8.dp),
                                     border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.8f)),
                                     colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary)
                                 ) {
                                     Icon(
                                         imageVector = Icons.Default.LocationOn,
                                         contentDescription = "Get Directions",
                                         tint = SaffronPrimary,
                                         modifier = Modifier.size(16.dp)
                                     )
                                     Spacer(modifier = Modifier.width(6.dp))
                                     Text(
                                         text = if (isEnglish) "Get Directions" else "दिशा-निर्देश प्राप्त करें",
                                         fontWeight = FontWeight.Bold,
                                         fontSize = 13.sp,
                                         fontFamily = FontFamily.Serif
                                     )
                                 }
                             }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (isEnglish) "Return to Dashboard" else "डैशबोर्ड पर लौटें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
