package com.example.visitchittorgarh.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.example.visitchittorgarh.theme.SlateSurfaceDark
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    isEnglish: Boolean,
    onAuthSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    var isLoading by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

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
                            // Send welcome email via Firestore
                            if (userEmail.isNotEmpty()) {
                                val db = FirebaseFirestore.getInstance()
                                val mailData = hashMapOf(
                                    "to" to listOf(userEmail),
                                    "message" to hashMapOf(
                                        "subject" to "Welcome to Visit Chittorgarh!",
                                        "text" to """
                                            Hello ${if (displayName.isNotEmpty()) displayName else "Traveler"},

                                            Welcome to the Visit Chittorgarh app! We are excited to have you on board. Discover the rich history of Chittorgarh, book royal travel passes, hotel stays, and professional guides all in one place.

                                            Best Regards,
                                            Visit Chittorgarh Team
                                        """.trimIndent()
                                    )
                                )
                                db.collection("mail").add(mailData)
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

        // Dark gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            CrimsonDark.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Responsive Scrollable Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gold Logo
                GoldFortLogo(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 12.dp)
                )

                Text(
                    text = if (isEnglish) "Chittorgarh Tourism" else "चित्तौड़गढ़ पर्यटन",
                    color = SaffronPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Main Auth Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceDark.copy(alpha = 0.95f)),
                    border = BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.8f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isEnglish) "Welcome to Chittorgarh" else "चित्तौड़गढ़ में आपका स्वागत है",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = SaffronPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isEnglish) {
                                "Explore the land of historic forts, legendary stories of bravery, and rich heritage. Sign in to start your royal journey."
                            } else {
                                "ऐतिहासिक किलों, वीरता की गौरवशाली कहानियों और समृद्ध विरासत की भूमि का अन्वेषण करें। अपनी शाही यात्रा शुरू करने के लिए लॉगिन करें।"
                            },
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (isLoading) {
                            CircularProgressIndicator(color = SaffronPrimary)
                        } else {
                            // Premium White Google Sign-In Button
                            Button(
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
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.6f)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
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
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Back/Skip Text Button
                TextButton(onClick = onBackClick) {
                    Text(
                        text = if (isEnglish) "Back to Home" else "मुख्य पृष्ठ पर वापस जाएँ",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
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
