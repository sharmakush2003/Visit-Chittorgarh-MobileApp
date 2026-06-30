package com.example.visitchittorgarh.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.visitchittorgarh.R
import com.example.visitchittorgarh.data.DefaultDataRepository
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import com.example.visitchittorgarh.ui.screens.*
import kotlinx.coroutines.launch

import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DefaultDataRepository()) },
    onBookingPassClick: (String, String, Double, String, Double, String, Double) -> Unit,
    onPartnerPortalClick: () -> Unit,
    onAboutDeveloperClick: () -> Unit,
    onAboutChittorgarhClick: () -> Unit,
    onHowToReachClick: () -> Unit,
    onAuthClick: () -> Unit,
    onEmergencyContactsClick: () -> Unit,
    onWeatherClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("chittorgarh_prefs", Context.MODE_PRIVATE) }

    var isEnglish by remember { mutableStateOf(sharedPrefs.getBoolean("is_english", true)) }
    var isIndia by remember { mutableStateOf(sharedPrefs.getBoolean("is_india", true)) }
    var showWelcomeDialog by remember { mutableStateOf(!sharedPrefs.contains("welcome_seen")) }

    var selectedTab by remember { mutableStateOf(0) }

    val attractions by viewModel.attractions.collectAsStateWithLifecycle()
    val packages by viewModel.packages.collectAsStateWithLifecycle()
    val cabs by viewModel.cabs.collectAsStateWithLifecycle()
    val guides by viewModel.guides.collectAsStateWithLifecycle()
    val stays by viewModel.stays.collectAsStateWithLifecycle()
    val locals by viewModel.locals.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var currentUserState by remember { mutableStateOf(com.google.firebase.auth.FirebaseAuth.getInstance().currentUser) }

    LaunchedEffect(key1 = true) {
        com.google.firebase.auth.FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            currentUserState = firebaseAuth.currentUser
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Transparent,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(320.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF0F2038), Color(0xFF050A1A))
                            )
                        )
                        .border(
                            BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                            RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header Banner - Royal Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(CrimsonSecondary, CrimsonDark)
                                    )
                                )
                                .border(
                                    BorderStroke(0.5.dp, GoldAccent.copy(alpha = 0.5f)),
                                    RoundedCornerShape(topEnd = 24.dp)
                                )
                                .padding(vertical = 32.dp, horizontal = 24.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_maharana),
                                    contentDescription = "Official Chittorgarh Logo",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(1.5.dp, GoldAccent, CircleShape)
                                        .padding(2.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = if (isEnglish) "Royal Chittorgarh" else "शाही चित्तौड़गढ़",
                                        color = GoldAccent,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Text(
                                        text = if (isEnglish) "Mewar Legacy" else "मेवाड़ की विरासत",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = FontFamily.SansSerif,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Navigation Drawer Items with Serif font style
                        val menuItems = listOf(
                            Triple(0, Icons.Default.Home, if (isEnglish) "Home Dashboard" else "होम डैशबोर्ड"),
                            Triple(1, Icons.Default.Star, if (isEnglish) "Royal Tour Packages" else "शाही यात्रा पैकेज"),
                            Triple(2, Icons.Default.Call, if (isEnglish) "Concierge Services" else "द्वारपाल सेवाएं"),
                            Triple(3, Icons.Default.Info, if (isEnglish) "Vocal for Local" else "लोकल के लिए वोकल")
                        )

                        menuItems.forEach { (index, icon, label) ->
                            NavigationDrawerItem(
                                label = { 
                                    Text(
                                        text = label, 
                                        fontWeight = FontWeight.SemiBold, 
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 14.sp
                                    ) 
                                },
                                selected = selectedTab == index,
                                onClick = {
                                    selectedTab = index
                                    scope.launch { drawerState.close() }
                                },
                                icon = { Icon(icon, contentDescription = null, tint = if (selectedTab == index) GoldAccent else Color.White.copy(alpha = 0.7f)) },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = SaffronPrimary.copy(alpha = 0.25f),
                                    selectedTextColor = GoldAccent,
                                    unselectedTextColor = Color.White.copy(alpha = 0.85f)
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SaffronPrimary.copy(alpha = 0.2f))
                        

                        // Auth / Profile Item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (currentUserState == null) {
                                        if (isEnglish) "Sign In / Register" else "साइन इन / रजिस्टर"
                                    } else {
                                        val nameStr = currentUserState?.displayName ?: currentUserState?.email ?: ""
                                        if (isEnglish) "Sign Out ($nameStr)" else "साइन आउट ($nameStr)"
                                    }, 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                if (currentUserState == null) {
                                    onAuthClick()
                                } else {
                                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                    Toast.makeText(context, if (isEnglish) "Signed out successfully" else "सफलतापूर्वक साइन आउट किया गया", Toast.LENGTH_SHORT).show()
                                }
                            },
                            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        // About Chittorgarh item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (isEnglish) "About Chittorgarh" else "चित्तौड़गढ़ के बारे में", 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onAboutChittorgarhClick()
                            },
                            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        // About Developers item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (isEnglish) "About the Developers" else "डेवलपर्स के बारे में", 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onAboutDeveloperClick()
                            },
                            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        // Emergency Contacts item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (isEnglish) "Emergency Contacts" else "आपातकालीन संपर्क", 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onEmergencyContactsClick()
                            },
                            icon = { Icon(Icons.Default.Call, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        // Weather item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (isEnglish) "Live Weather" else "लाइव मौसम", 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onWeatherClick()
                            },
                            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        // Partner Portal item
                        NavigationDrawerItem(
                            label = { 
                                Text(
                                    text = if (isEnglish) "Partner Verification Portal" else "साझेदार सत्यापन पोर्टल", 
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                ) 
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onPartnerPortalClick()
                            },
                            icon = { Icon(Icons.Default.Menu, contentDescription = null, tint = SaffronPrimary) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = Color.White.copy(alpha = 0.9f)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                Surface(
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (isEnglish) "ROYAL CHITTORGARH" else "शाही चित्तौड़गढ़",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 1.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = SaffronPrimary)
                            }
                        },
                        actions = {
                            TextButton(
                                onClick = {
                                    isEnglish = !isEnglish
                                    sharedPrefs.edit().putBoolean("is_english", isEnglish).apply()
                                }
                            ) {
                                Text(
                                    text = if (isEnglish) "English" else "हिंदी",
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary,
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val navTabs = listOf(
                        Triple(0, Icons.Default.Home, if (isEnglish) "Home" else "होम"),
                        Triple(1, Icons.Default.Star, if (isEnglish) "Packages" else "पैकेज"),
                        Triple(2, Icons.Default.Call, if (isEnglish) "Services" else "सेवाएं"),
                        Triple(3, Icons.Default.Info, if (isEnglish) "Local" else "लोकल"),
                        Triple(4, Icons.Default.LocationOn, if (isEnglish) "Transit" else "मार्ग")
                    )
                    navTabs.forEach { (index, icon, label) ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { 
                                Text(
                                    text = label, 
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Serif
                                ) 
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SaffronPrimary,
                                selectedTextColor = SaffronPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                indicatorColor = SaffronPrimary.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        attractions = attractions,
                        isEnglish = isEnglish,
                        onPlanJourneyClick = { selectedTab = 1 }
                    )
                    1 -> PackagesScreen(
                        packages = packages,
                        isEnglish = isEnglish,
                        onBookingPassClick = onBookingPassClick
                    )
                    2 -> ServicesScreen(
                        cabs = cabs,
                        guides = guides,
                        stays = stays,
                        isEnglish = isEnglish
                    )
                    3 -> LocalScreen(
                        locals = locals,
                        isEnglish = isEnglish
                    )
                    4 -> HowToReachScreen(
                        isEnglish = isEnglish,
                        onBackClick = { selectedTab = 0 }
                    )
                }

                if (showWelcomeDialog) {
                    WelcomeDialog(
                        onDismiss = { englishPref, indiaPref ->
                            isEnglish = englishPref
                            isIndia = indiaPref
                            sharedPrefs.edit()
                                .putBoolean("is_english", englishPref)
                                .putBoolean("is_india", indiaPref)
                                .putBoolean("welcome_seen", true)
                                .apply()
                            showWelcomeDialog = false
                        }
                    )
                }
            }
        }
    }
}

