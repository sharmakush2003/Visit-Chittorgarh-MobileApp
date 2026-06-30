package com.example.visitchittorgarh.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "About the Developers" else "विकासकर्ताओं के बारे में",
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
            // ChittorTech Logo & Subtitle Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CrimsonSecondary, CrimsonDark)
                        )
                    )
                    .border(BorderStroke(0.5.dp, GoldAccent.copy(alpha = 0.3f)))
                    .padding(vertical = 32.dp, horizontal = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            .border(1.5.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🚀",
                            fontSize = 36.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "ChittorTech Solutions",
                        color = GoldAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) "Best IT Startup in Chittorgarh" else "चित्तौड़गढ़ का सर्वश्रेष्ठ आईटी स्टार्टअप",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Intro Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isEnglish) {
                                "ChittorTech is a leading IT company and web development agency based in Chittorgarh, Rajasthan. We engineer premium websites, SaaS enterprise applications, custom AI Chatbots (RAG), and high-performance native mobile apps built on Expo and Kotlin."
                            } else {
                                "चित्तौड़टेक चित्तौड़गढ़, राजस्थान में स्थित एक प्रमुख आईटी कंपनी और वेब डेवलपमेंट एजेंसी है। हम प्रीमियम वेबसाइट्स, एंटरप्राइज सॉफ्टवेयर (SaaS), कस्टम AI चैटबॉट्स और फ्लूइड मोबाइल ऐप्स बनाते हैं।"
                            },
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                // iStart & Startup India Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "iStart Rajasthan",
                                fontWeight = FontWeight.Bold,
                                color = SaffronPrimary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bronze Card Rating\nStartup ID: 05F896CE",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CrimsonSecondary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Startup India",
                                fontWeight = FontWeight.Bold,
                                color = CrimsonSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "DPIIT Recognized\nBhaskar ID: IN-0426-9449SG",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Section: Team Leadership
                Text(
                    text = if (isEnglish) "Meet the Founders" else "संस्थापक टीम",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Kush Profile
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Kush Sharma", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(if (isEnglish) "Founder & Software Engineer" else "संस्थापक और सॉफ्टवेयर इंजीनियर", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish) {
                                "A software engineer and GenAI specialist, B.Tech IT graduate from JECRC Foundation (8.90 CGPA). Expert in Generative AI, LangChain, Node.js, and MongoDB. Built complex RAG systems and digital tourism structures."
                            } else {
                                "एक सॉफ्टवेयर इंजीनियर और GenAI विशेषज्ञ, JECRC फाउंडेशन (8.90 CGPA) से बी.टेक आईटी स्नातक। जेनेरेटिव एआई, LangChain, नोड.जेएस और मोंगोडीबी के विशेषज्ञ।"
                            },
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Lav Profile
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CrimsonSecondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Lav Sharma", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(if (isEnglish) "Co-Founder & Lead AI Developer" else "सह-संस्थापक और मुख्य एआई डेवलपर", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish) {
                                "Co-founder and lead AI Full-Stack Developer, B.Tech IT graduate from JECRC Foundation (8.52 CGPA). Specializes in complete web/SaaS architecture, Express.js, Node.js, and high-performance databases."
                            } else {
                                "सह-संस्थापक और मुख्य एआई फुल-स्टैक डेवलपर, JECRC फाउंडेशन (8.52 CGPA) से बी.टेक आईटी स्नातक। वेब/SaaS आर्किटेक्चर, नोड.जेएस और मोंगोडीबी के विशेषज्ञ।"
                            },
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Section: Projects
                Text(
                    text = if (isEnglish) "Our Projects" else "हमारे प्रोजेक्ट्स",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Project List
                val projects = listOf(
                    Triple("Visit Chittorgarh App", "Tourism Travel-Tech platform with native pass bookings & direct local vendor connection.", "Active"),
                    Triple("Mewari Achaar App", "Mobile E-Commerce system facilitating native ordering of local Mewari pickles.", "Active"),
                    Triple("Digify Gift Shop Portal", "Enterprise multi-vendor e-commerce platform and CRM architecture.", "Active")
                )

                projects.forEach { proj ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(proj.first, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(proj.second, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge(containerColor = SaffronPrimary.copy(alpha = 0.2f), contentColor = SaffronPrimary) {
                                Text(proj.third, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                }

                // helplines
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEnglish) "CHITTORTECH HELPDESK" else "चित्तौड़टेक हेल्पडेस्क",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonSecondary,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isEnglish) "Direct lines for support and project queries" else "सपोर्ट और नए प्रोजेक्ट के प्रश्नों के लिए सीधी लाइनें",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // WhatsApp Chat Button
                        Button(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=+917597451057&text=Hello ChittorTech Support!"
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Text(
                                text = if (isEnglish) "Contact WhatsApp Support" else "व्हाट्सएप सपोर्ट पर संपर्क करें",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Call & Email Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917597451057"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, CrimsonSecondary),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonSecondary)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isEnglish) "Call" else "कॉल", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:visitchittorgarh@gmail.com")
                                        putExtra(Intent.EXTRA_SUBJECT, "Visit Chittorgarh Query")
                                    }
                                    context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SaffronPrimary),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary)
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isEnglish) "Email" else "ईमेल", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Close Button
                Button(
                    onClick = onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
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
