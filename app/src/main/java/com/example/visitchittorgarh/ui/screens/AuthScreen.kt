package com.example.visitchittorgarh.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    isEnglish: Boolean,
    onAuthSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                        Toast.makeText(context, if (isEnglish) "Welcome back!" else "स्वागत है!", Toast.LENGTH_SHORT).show()
                        onAuthSuccess()
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
        // Royal Background Image (matching Splash Onboarding)
        Image(
            painter = painterResource(id = R.drawable.onboarding_bg),
            contentDescription = "Chittorgarh background",
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
            // Gold Logo matching Splash
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
                        text = if (isLoginMode) {
                            if (isEnglish) "Sign In" else "लॉग इन करें"
                        } else {
                            if (isEnglish) "Create Account" else "खाता बनाएं"
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = CrimsonSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Name Field (Sign up only)
                    if (!isLoginMode) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(if (isEnglish) "Full Name" else "पूरा नाम") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(if (isEnglish) "Email Address" else "ईमेल पता") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SaffronPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(if (isEnglish) "Password" else "पासवर्ड") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronPrimary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isLoading) {
                        CircularProgressIndicator(color = SaffronPrimary)
                    } else {
                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, if (isEnglish) "Please fill in all fields" else "कृपया सभी फ़ील्ड भरें", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (password.length < 6) {
                                    Toast.makeText(context, if (isEnglish) "Password must be at least 6 characters" else "पासवर्ड कम से कम 6 अक्षरों का होना चाहिए", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!isLoginMode && name.isBlank()) {
                                    Toast.makeText(context, if (isEnglish) "Please enter your name" else "कृपया अपना नाम दर्ज करें", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                isLoading = true
                                if (isLoginMode) {
                                    auth.signInWithEmailAndPassword(email.trim(), password)
                                        .addOnCompleteListener { task ->
                                            isLoading = false
                                            if (task.isSuccessful) {
                                                Toast.makeText(context, if (isEnglish) "Welcome back!" else "स्वागत है!", Toast.LENGTH_SHORT).show()
                                                onAuthSuccess()
                                            } else {
                                                Toast.makeText(context, task.exception?.localizedMessage ?: "Sign In Failed", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                } else {
                                    auth.createUserWithEmailAndPassword(email.trim(), password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                val profileUpdates = userProfileChangeRequest {
                                                    displayName = name.trim()
                                                }
                                                user?.updateProfile(profileUpdates)
                                                    ?.addOnCompleteListener {
                                                        isLoading = false
                                                        Toast.makeText(context, if (isEnglish) "Registration Successful!" else "पंजीकरण सफल रहा!", Toast.LENGTH_SHORT).show()
                                                        onAuthSuccess()
                                                    }
                                            } else {
                                                isLoading = false
                                                Toast.makeText(context, task.exception?.localizedMessage ?: "Registration Failed", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonSecondary)
                        ) {
                            Text(
                                text = if (isLoginMode) {
                                    if (isEnglish) "Sign In" else "साइन इन करें"
                                } else {
                                    if (isEnglish) "Register" else "रजिस्टर करें"
                                },
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Google Sign-In Button
                        OutlinedButton(
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
                            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
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
                                    text = if (isLoginMode) {
                                        if (isEnglish) "Continue with Google" else "गूगल के साथ जारी रखें"
                                    } else {
                                        if (isEnglish) "Sign Up with Google" else "गूगल के साथ साइन अप करें"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch Mode Text Button
                    TextButton(onClick = { isLoginMode = !isLoginMode }) {
                        Text(
                            text = if (isLoginMode) {
                                if (isEnglish) "Don't have an account? Register" else "खाता नहीं है? रजिस्टर करें"
                            } else {
                                if (isEnglish) "Already have an account? Sign In" else "पहले से खाता है? साइन इन करें"
                            },
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
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
}
