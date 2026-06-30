package com.example.visitchittorgarh.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gold Logo
            GoldFortLogo(
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = if (isEnglish) "Chittorgarh Tourism" else "चित्तौड़गढ़ पर्यटन",
                color = SaffronPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Auth Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isEnglish) "Welcome" else "स्वागत है",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = CrimsonSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isEnglish) {
                            "Sign in with Google to explore historical monuments, book royal travel passes, and guide services."
                        } else {
                            "ऐतिहासिक स्मारकों को देखने, शाही यात्रा पास और गाइड सेवाएं बुक करने के लिए गूगल से लॉगिन करें।"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    if (isLoading) {
                        CircularProgressIndicator(color = SaffronPrimary)
                    } else {
                        // Google Sign-In Button
                        Button(
                            onClick = {
                                isLoading = true
                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(context.getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build()
                                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_google),
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isEnglish) "Continue with Google" else "गूगल के साथ जारी रखें",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Back/Skip Text Button
            TextButton(onClick = onBackClick) {
                Text(
                    text = if (isEnglish) "Back to Home" else "मुख्य पृष्ठ पर वापस जाएँ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
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
