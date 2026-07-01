package com.example.visitchittorgarh.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
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
import com.example.visitchittorgarh.theme.SlateBackgroundLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

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
                            text = if (isEnglish) "ABOUT THE DEVELOPERS" else "विकासकर्ताओं की जानकारी",
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
            // ChittorTech Logo & Brand Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CrimsonSecondary, CrimsonDark)
                        )
                    )
                    .border(BorderStroke(0.5.dp, GoldAccent.copy(alpha = 0.3f)))
                    .padding(vertical = 36.dp, horizontal = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                            .border(2.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://chittortech.online/logo.png",
                            contentDescription = "ChittorTech Logo",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "ChittorTech",
                        color = GoldAccent,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) "🏆 Best IT Startup in Chittorgarh" else "🏆 चित्तौड़गढ़ का सर्वश्रेष्ठ आईटी स्टार्टअप",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Intro Text Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isEnglish) {
                                "ChittorTech is Rajasthan's recognized, premier travel-tech developer and software engineering team based in Chittorgarh. We develop high-speed cloud infrastructures, robust SaaS suites, professional e-commerce operations, and native mobile apps built on Jetpack Compose and Kotlin."
                            } else {
                                "चित्तौड़टेक राजस्थान की मान्यता प्राप्त, प्रमुख ट्रैवल-टेक डेवलपर और सॉफ्टवेयर इंजीनियरिंग टीम है जो चित्तौड़गढ़ में स्थित है। हम हाई-स्पीड क्लाउड इंफ्रास्ट्रक्चर, SaaS सुइट्स, ई-कॉमर्स सिस्टम और जेटपैक कंपोज़ और कोटलिन में बने ऐप्स विकसित करते हैं।"
                            },
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color.Black.copy(alpha = 0.8f),
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                // Startup Credentials Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "iStart Approved",
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Check, null, tint = SaffronPrimary, modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "iStart Rajasthan\nBronze Rating\nID: 05F896CE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.6f),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CrimsonSecondary.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Startup India",
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonSecondary,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Check, null, tint = CrimsonSecondary, modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Registered Startup\nBhaskar ID:\nIN-0426-9449SG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.6f),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                // Section Title: Founders
                Text(
                    text = if (isEnglish) "MEET THE FOUNDERS" else "संस्थापक टीम से मिलें",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    color = CrimsonSecondary,
                    letterSpacing = 1.sp
                )

                // Founder: Kush Sharma
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("KS", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CrimsonSecondary, fontFamily = FontFamily.Serif)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Kush Sharma",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Serif,
                                    color = CrimsonSecondary
                                )
                                Text(
                                    text = if (isEnglish) "Founder & Principal Software Engineer" else "संस्थापक और सॉफ्टवेयर इंजीनियर",
                                    fontSize = 11.sp,
                                    color = Color.Black.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isEnglish) {
                                "Kush is a Principal Software Engineer & GenAI specialist, holding a B.Tech in IT from JECRC Foundation (8.90 CGPA). Expert in Generative AI, RAG architecture, Node.js backend systems, and native mobile apps."
                            } else {
                                "कुश एक सॉफ्टवेयर इंजीनियर और GenAI विशेषज्ञ हैं, जिन्होंने JECRC फाउंडेशन (8.90 CGPA) से IT में बी.टेक किया है। जेनेरेटिव एआई, मोंगोडीबी और मोबाइल डेवलपमेंट के विशेषज्ञ।"
                            },
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                // Founder: Lav Sharma
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CrimsonSecondary.copy(alpha = 0.15f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonSecondary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("LS", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SaffronPrimary, fontFamily = FontFamily.Serif)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Lav Sharma",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Serif,
                                    color = CrimsonSecondary
                                )
                                Text(
                                    text = if (isEnglish) "Co-Founder & Lead Technical Architect" else "सह-संस्थापक और मुख्य तकनीकी आर्किटेक्ट",
                                    fontSize = 11.sp,
                                    color = Color.Black.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isEnglish) {
                                "Lav is a Co-founder and lead technical architect, holding a B.Tech in IT from JECRC Foundation (8.52 CGPA). Specializes in complete web/SaaS architecture, Express.js backend scaling, and high-performance cloud databases."
                            } else {
                                "लव सह-संस्थापक और मुख्य तकनीकी आर्किटेक्ट हैं, जिन्होंने JECRC फाउंडेशन (8.52 CGPA) से IT में बी.टेक किया है। क्लाउड आर्किटेक्चर, नोड.जेएस और बड़ी डेटाबेस प्रणालियों के विशेषज्ञ।"
                            },
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                // Section Title: Projects
                Text(
                    text = if (isEnglish) "DIGITAL PORTFOLIO" else "हमारे मुख्य प्रोजेक्ट्स",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    color = CrimsonSecondary,
                    letterSpacing = 1.sp
                )

                // Projects List (with Emojis and premium design)
                val projects = listOf(
                    Triple(
                        "Visit Chittorgarh Mobile App",
                        "A premium travel-tech mobile application and digital concierge portal built with Jetpack Compose, Kotlin, and Firebase. It offers seamless native pass generation, direct tour package bookings, and real-time vendor management without middleman commissions, empowering local tourism.",
                        "📱"
                    ),
                    Triple(
                        "Mewari Achaar E-Commerce",
                        "A fluid Android e-commerce application built in native Kotlin, designed to digitize traditional food delivery. It features high-performance 60FPS Lottie vector animation modules, highly responsive Firestore databases, background data sync routines, and secure Google Sign-in protocols.",
                        "🍯"
                    ),
                    Triple(
                        "Digify Gift Shop Portal",
                        "A robust corporate multi-vendor e-commerce platform and customer CRM suite built using Next.js, Node.js, Express, and MongoDB. It integrates automatic inventory sync, multi-currency payment nodes, order processing pipelines, and secure analytics dashboards.",
                        "🎁"
                    ),
                    Triple(
                        "SaaS Enterprise & AI Solutions",
                        "Tailor-made cloud architectures, enterprise customer dashboards, custom AI Chatbots (RAG), and digital automation workflows. Engineered on Next.js, Node.js, and MongoDB, these systems streamline operations, enhance client interaction, and process big data securely.",
                        "☁️"
                    ),
                    Triple(
                        "Business & Corporate Web Portals",
                        "High-performance, pixel-perfect, and mobile-responsive website systems engineered for corporate brands, educational bodies, and local startups. Optimised for speed, SEO rankings, Google schema markup, and multi-channel customer lead generation.",
                        "🌐"
                    )
                )

                projects.forEach { proj ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.12f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SaffronPrimary.copy(alpha = 0.08f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(proj.third, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = proj.first,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = CrimsonSecondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFE8F5E9))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isEnglish) "ACTIVE" else "सक्रिय",
                                                color = Color(0xFF2E7D32),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(SaffronPrimary.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "iStart Approved",
                                                color = SaffronPrimary,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = proj.second,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = Color.Black.copy(alpha = 0.65f),
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                // Helpdesk Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEnglish) "CHITTORTECH HELPDESK" else "चित्तौड़टेक हेल्पडेस्क",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonSecondary,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isEnglish) "Direct lines for support and project queries" else "सपोर्ट और नए प्रोजेक्ट के लिए सीधी हेल्पडेस्क लाइन्स",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black.copy(alpha = 0.5f),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

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
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Text(
                                text = if (isEnglish) "Contact WhatsApp Support" else "व्हाट्सएप सपोर्ट पर संपर्क करें",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Call & Email Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917597451057"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, CrimsonSecondary),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonSecondary)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isEnglish) "Call Us" else "कॉल करें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, SaffronPrimary),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary)
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isEnglish) "Email Us" else "ईमेल करें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (isEnglish) "Return to Dashboard" else "डैशबोर्ड पर लौटें",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
