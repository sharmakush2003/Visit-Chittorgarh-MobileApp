package com.example.visitchittorgarh.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.example.visitchittorgarh.theme.SlateSurfaceDark
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.example.visitchittorgarh.data.MailHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    isEnglish: Boolean,
    onAuthSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Infinite transitions for glowing royal effects
    val infiniteTransition = rememberInfiniteTransition(label = "royal_glow")
    
    // Animate glowing border opacity
    val glowOpacity by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_opacity"
    )

    // Animate pulse scale of the background glow
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    isLoading = false
                    if (authTask.isSuccessful) {
                        val user = auth.currentUser
                        val isNewUser = authTask.result?.additionalUserInfo?.isNewUser == true
                        val userEmail = user?.email ?: ""
                        val displayName = user?.displayName ?: ""

                        if (isNewUser) {
                            // Send welcome email directly via SMTP MailHelper
                            if (userEmail.isNotEmpty()) {
                                coroutineScope.launch {
                                    MailHelper.sendWelcomeEmail(userEmail, displayName)
                                }
                            }

                            dialogTitle = if (isEnglish) "Registration Successful!" else "पंजीकरण सफल रहा!"
                            dialogMessage = if (isEnglish) {
                                "Welcome to Visit Chittorgarh app, $displayName! A welcome email has been sent to $userEmail."
                            } else {
                                "Visit Chittorgarh ऐप में आपका स्वागत है, $displayName! $userEmail पर एक स्वागत ईमेल भेजा गया है।"
                            }
                        } else {
                            dialogTitle = if (isEnglish) "Login Successful!" else "लॉगिन सफल रहा!"
                            dialogMessage = if (isEnglish) {
                                "Welcome back to Visit Chittorgarh, $displayName!"
                            } else {
                                "Visit Chittorgarh में आपका स्वागत है, $displayName!"
                            }
                        }
                        showDialog = true
                    } else {
                        Toast.makeText(context, authTask.exception?.localizedMessage ?: "Google Auth Failed", Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: com.google.android.gms.common.api.ApiException) {
            isLoading = false
            Toast.makeText(context, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Royal Background Image
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = "Chittorgarh background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Triple Layer Rich Dark Gradient Overlay (Crimson/Gold theme)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.75f),
                            CrimsonDark.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        // Soft background glow pulse behind the card
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(340.dp)
                .scale(pulseScale)
                .alpha(glowOpacity * 0.15f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(SaffronPrimary, Color.Transparent)
                    )
                )
        )

        // Scrollable Screen Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Royal Logo Crest
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(
                            BorderStroke(
                                2.dp,
                                Brush.sweepGradient(listOf(GoldAccent, SaffronPrimary, CrimsonSecondary, GoldAccent))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    GoldFortLogo(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Title
                Text(
                    text = if (isEnglish) "Chittorgarh Tourism" else "चित्तौड़गढ़ पर्यटन",
                    color = SaffronPrimary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isEnglish) "Proud Symbol of Mewar Sovereignty" else "मेवाड़ संप्रभुता का गौरवशाली प्रतीक",
                    color = GoldAccent.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // The World's Best Glassmorphic Card (Translucent, glowing double border)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                1.5.dp,
                                Brush.verticalGradient(
                                    listOf(
                                        SaffronPrimary.copy(alpha = glowOpacity),
                                        CrimsonSecondary.copy(alpha = 0.2f),
                                        GoldAccent.copy(alpha = glowOpacity)
                                    )
                                )
                            ),
                            shape = RoundedCornerShape(32.dp)
                        ),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.78f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Title Inside Card
                        Text(
                            text = if (isEnglish) "Welcome to Chittorgarh" else "चित्तौड़गढ़ में आपका स्वागत है",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = SaffronPrimary,
                            textAlign = TextAlign.Center
                        )

                        // Traditional Website Ornament Divider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 1.dp,
                                color = SaffronPrimary.copy(alpha = 0.25f)
                            )
                            Text(
                                text = "  ◆  ",
                                color = GoldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 1.dp,
                                color = SaffronPrimary.copy(alpha = 0.25f)
                            )
                        }

                        Text(
                            text = if (isEnglish) {
                                "Explore the land of historic forts, legendary stories of bravery, and rich heritage. Sign in to start your royal journey."
                            } else {
                                "ऐतिहासिक किलों, वीरता की गौरवशाली कहानियों और समृद्ध विरासत की भूमि का अन्वेषण करें। अपनी शाही यात्रा शुरू करने के लिए लॉगिन करें।"
                            },
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (isLoading) {
                            CircularProgressIndicator(
                                color = SaffronPrimary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            // High-End Luxury Google Sign-In Card Button (With gold gradient glow background)
                            Card(
                                onClick = {
                                    isLoading = true
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(context.getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build()
                                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                    googleSignInClient.signOut().addOnCompleteListener {
                                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .border(
                                        BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFFFFFFF),
                                                    Color(0xFFFFF6E6)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_google),
                                            contentDescription = "Google Logo",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = if (isEnglish) "Continue with Google" else "गूगल के साथ जारी रखें",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // High Security Credentials Footer Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(BorderStroke(0.5.dp, SaffronPrimary.copy(alpha = 0.2f)), RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure authentication",
                                tint = GoldAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Firebase Core Secure Authentication",
                                color = Color.White.copy(alpha = 0.55f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Premium Custom Styled Close Button
                TextButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Brush.linearGradient(listOf(GoldAccent.copy(alpha = glowOpacity), SaffronPrimary.copy(alpha = glowOpacity)))),
                    colors = ButtonDefaults.textButtonColors(containerColor = Color.Black.copy(alpha = 0.3f)),
                    modifier = Modifier.height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isEnglish) "Back to Home" else "मुख्य पृष्ठ पर वापस जाएँ",
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }
        }
    }

    // Success Alert Dialog (for Registration and Login)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onAuthSuccess()
            },
            title = {
                Text(
                    text = dialogTitle,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = CrimsonSecondary
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onAuthSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                ) {
                    Text(if (isEnglish) "OK" else "ठीक है")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
