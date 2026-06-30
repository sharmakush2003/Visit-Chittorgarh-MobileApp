package com.example.visitchittorgarh.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.visitchittorgarh.R
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val alphaAnim = remember { Animatable(0f) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // High quality stylized illustration background
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = "Chittorgarh Fort sunset illustration",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay for readability and premium feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Content container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Logo and Brand Name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 30.dp)
            ) {
                // ── Lottie Fort Glow Animation ────────────────────────────
                val lottieComposition by rememberLottieComposition(
                    LottieCompositionSpec.Asset("fort_glow.json")
                )
                val lottieProgress by animateLottieCompositionAsState(
                    composition = lottieComposition,
                    iterations = LottieConstants.IterateForever,
                    speed = 0.85f
                )
                LottieAnimation(
                    composition = lottieComposition,
                    progress = { lottieProgress },
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Visit",
                    color = Color(0xFFF3C15D), // Gold color
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Chittorgarh",
                    color = Color(0xFFF3C15D), // Gold color
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp
                )
            }

            // Bottom Section: Swipeable Pager, Indicator, Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                // Swipeable Text Area (HorizontalPager)
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) { page ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Dynamic Title
                        Text(
                            text = when (page) {
                                0 -> "Explore\nChittorgarh"
                                1 -> "Mewar\nTreasures"
                                else -> "Royal\nConcierge"
                            },
                            color = Color(0xFFF3C15D), // Gold/yellow color
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 44.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic Subtitle Text
                        Text(
                            text = when (page) {
                                0 -> "Discover the glorious history, heritage, and timeless beauty of India's largest fort."
                                1 -> "Discover authentic Mewari cuisine, hand-blocked prints, wooden crafts, and regional souvenirs."
                                else -> "Get instant access to verified packages, local guides, stays, and travel passes for an effortless journey."
                            },
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Page indicator dots (orange, gray, gray) - dynamically updating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isActive = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isActive) 24.dp else 6.dp, 6.dp)
                                .background(
                                    if (isActive) Color(0xFFF5B041) else Color.White.copy(alpha = 0.4f),
                                    RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Get Started Button (slides pager on click, enters dashboard on last page)
                Button(
                    onClick = {
                        if (pagerState.currentPage < 2) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onSplashFinished()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5B041) // Orange/gold background
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (pagerState.currentPage == 2) "Get Started" else "Continue",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Arrow Forward",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GoldFortLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val size = this.size.width
        val goldColor = Color(0xFFF3C15D) // Gold color matching the screenshot
        
        // Draw the circular outline
        drawCircle(
            color = goldColor,
            radius = size / 2f * 0.9f,
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Draw the fort silhouette
        val path = Path().apply {
            val base = size * 0.68f
            // Base line
            moveTo(size * 0.25f, base)
            
            // Left wall & dome
            lineTo(size * 0.32f, base)
            lineTo(size * 0.32f, size * 0.58f)
            // Left dome
            quadraticTo(size * 0.38f, size * 0.50f, size * 0.44f, size * 0.58f)
            lineTo(size * 0.44f, base)
            
            // Middle section and dome
            lineTo(size * 0.47f, base)
            lineTo(size * 0.47f, size * 0.55f)
            // Middle dome/arch
            cubicTo(size * 0.50f, size * 0.45f, size * 0.56f, size * 0.45f, size * 0.59f, size * 0.55f)
            lineTo(size * 0.59f, base)
            
            // Tower on the right
            lineTo(size * 0.63f, base)
            lineTo(size * 0.63f, size * 0.38f) // Tower body left side
            lineTo(size * 0.61f, size * 0.38f) // Projection left
            lineTo(size * 0.61f, size * 0.36f)
            lineTo(size * 0.63f, size * 0.36f)
            lineTo(size * 0.63f, size * 0.32f) // Tower top left
            // Tower dome
            quadraticTo(size * 0.66f, size * 0.26f, size * 0.69f, size * 0.32f)
            lineTo(size * 0.69f, size * 0.36f)
            lineTo(size * 0.71f, size * 0.36f)
            lineTo(size * 0.71f, size * 0.38f)
            lineTo(size * 0.69f, size * 0.38f) // Projection right
            lineTo(size * 0.69f, base) // Tower body right side
            
            lineTo(size * 0.75f, base)
        }
        
        drawPath(
            path = path,
            color = goldColor,
            style = Fill
        )
    }
}
