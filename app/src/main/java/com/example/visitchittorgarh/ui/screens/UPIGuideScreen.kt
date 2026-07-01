package com.example.visitchittorgarh.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.GoldAccent
import com.example.visitchittorgarh.theme.SaffronPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// ─── Data Models ─────────────────────────────────────────────────────────────

data class CurrencyRate(val code: String, val nameEn: String, val nameHi: String, val flag: String, val rateToInr: Double)

data class WizardStep(
    val stepNo: Int,
    val titleEn: String,
    val titleHi: String,
    val descEn: String,
    val descHi: String,
    val detailEn: String,
    val detailHi: String,
    val emoji: String
)

// ─── Constants & Fallback Rates ───────────────────────────────────────────────

val currencies = listOf(
    CurrencyRate("USD", "US Dollar", "अमेरिकी डॉलर", "🇺🇸", 83.45),
    CurrencyRate("EUR", "Euro", "यूरो", "🇪🇺", 89.62),
    CurrencyRate("GBP", "British Pound", "ब्रिटिश पाउंड", "🇬🇧", 105.80),
    CurrencyRate("AUD", "Australian Dollar", "ऑस्ट्रेलियाई डॉलर", "🇦🇺", 55.42),
    CurrencyRate("CAD", "Canadian Dollar", "कनाडाई डॉलर", "🇨🇦", 61.20),
    CurrencyRate("SGD", "Singapore Dollar", "सिंगापुर डॉलर", "🇸🇬", 61.75),
    CurrencyRate("JPY", "Japanese Yen", "जापानी येन", "🇯🇵", 0.52),
    CurrencyRate("AED", "UAE Dirham", "संयुक्त अरब अमीरात दिरहाम", "🇦🇪", 22.72)
)

val wizardSteps = listOf(
    WizardStep(
        stepNo = 1,
        titleEn = "Document Readiness",
        titleHi = "दस्तावेज तैयार रखें",
        descEn = "Passport, Visa & Active Number",
        descHi = "पासपोर्ट, वीजा और सक्रिय मोबाइल नंबर",
        detailEn = "Ensure you have your physical passport, active tourist Visa (or e-Visa), and an active international phone number capable of receiving SMS.",
        detailHi = "सुनिश्चित करें कि आपके पास आपका भौतिक पासपोर्ट, सक्रिय पर्यटक वीजा (या ई-वीजा), और एसएमएस प्राप्त करने में सक्षम एक सक्रिय अंतर्राष्ट्रीय फोन नंबर है।",
        emoji = "🛂"
    ),
    WizardStep(
        stepNo = 2,
        titleEn = "Visit Exchange Kiosk",
        titleHi = "अधिकृत काउंटर पर जाएं",
        descEn = "Verify at Airport or Forex Shop",
        descHi = "हवाई अड्डे या विदेशी मुद्रा दुकान पर सत्यापन",
        detailEn = "Upon landing at Delhi, Mumbai, or Jaipur airport, visit an authorized Forex provider (like EbixCash or Thomas Cook) or certified kiosk inside the city.",
        detailHi = "दिल्ली, मुंबई या जयपुर हवाई अड्डे पर उतरने के बाद, अधिकृत फॉरेक्स प्रदाता (जैसे EbixCash या Thomas Cook) या शहर के अंदर प्रमाणित काउंटर पर जाएं।",
        emoji = "🏢"
    ),
    WizardStep(
        stepNo = 3,
        titleEn = "Install Wallet App",
        titleHi = "वॉलेट ऐप इंस्टॉल करें",
        descEn = "Download Certified UPI Apps",
        descHi = "प्रमाणित यूपीआई ऐप डाउनलोड करें",
        detailEn = "Install certified merchant apps like 'Cheq UPI' or bank-sponsored wallets. The kiosk agent will scan your passport, verify documents, and activate the prepaid wallet.",
        detailHi = "प्रमाणित मर्चेंट ऐप जैसे 'Cheq UPI' या बैंक-प्रायोजित वॉलेट इंस्टॉल करें। काउंटर एजेंट आपका पासपोर्ट स्कैन करेगा, दस्तावेजों को सत्यापित करेगा और प्रीपेड वॉलेट सक्रिय करेगा।",
        emoji = "📱"
    ),
    WizardStep(
        stepNo = 4,
        titleEn = "Load Wallet Cash/Card",
        titleHi = "वॉलेट में पैसे लोड करें",
        descEn = "Exchange Money into Wallet Balance",
        descHi = "विदेशी मुद्रा को वॉलेट बैलेंस में बदलें",
        detailEn = "Give cash (USD, EUR, etc.) to the counter agent, or load balance digitally using an international credit card. The money is instantly converted and credited to your wallet in Indian Rupees (INR).",
        detailHi = "काउंटर एजेंट को नकद (यूएसडी, यूरो, आदि) दें, या अंतर्राष्ट्रीय क्रेडिट कार्ड का उपयोग करके डिजिटल रूप से बैलेंस लोड करें। पैसा तुरंत भारतीय रुपये (INR) में बदलकर आपके वॉलेट में जमा हो जाता है।",
        emoji = "💵"
    ),
    WizardStep(
        stepNo = 5,
        titleEn = "Scan any Indian QR code",
        titleHi = "किसी भी भारतीय क्यूआर कोड को स्कैन करें",
        descEn = "Seamless Scan & Pay Anywhere",
        descHi = "कहीं भी निर्बाध स्कैन और भुगतान करें",
        detailEn = "Open your activated wallet app, click Scan, and scan any local payment QR code (GPay, PhonePe, Paytm) at any tea stall, restaurant, cab, or guide in Rajasthan!",
        detailHi = "अपने सक्रिय वॉलेट ऐप को खोलें, स्कैन पर क्लिक करें, और राजस्थान में किसी भी चाय की दुकान, रेस्तरां, टैक्सी या गाइड के पास मर्चेंट क्यूआर कोड को स्कैन करके भुगतान करें!",
        emoji = "🛍️"
    )
)

// ─── Network Fetch: Live INR Rates ───────────────────────────────────────────

suspend fun fetchLiveRates(): Map<String, Double>? {
    return withContext(Dispatchers.IO) {
        try {
            val jsonStr = URL("https://open.er-api.com/v6/latest/INR").readText(Charsets.UTF_8)
            val json = JSONObject(jsonStr)
            val rates = json.getJSONObject("rates")
            val ratesMap = mutableMapOf<String, Double>()
            currencies.forEach { cur ->
                if (rates.has(cur.code)) {
                    val ratePerInr = rates.getDouble(cur.code)
                    if (ratePerInr > 0) {
                        ratesMap[cur.code] = 1.0 / ratePerInr
                    }
                }
            }
            ratesMap
        } catch (e: Exception) {
            null
        }
    }
}

// ─── Composable Screen ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UPIGuideScreen(
    isEnglish: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var activeTab by remember { mutableStateOf(0) } // 0 = UPI Guide, 1 = Converter

    // --- Currency rates caching logic ---
    var dynamicRates by remember {
        mutableStateOf(
            currencies.associate { it.code to it.rateToInr }.toMutableMap()
        )
    }
    var rateUpdateTime by remember { mutableStateOf("Default Rates") }

    LaunchedEffect(Unit) {
        val result = fetchLiveRates()
        if (result != null) {
            result.forEach { (code, rate) ->
                dynamicRates[code] = rate
            }
            rateUpdateTime = SimpleDateFormat("h:mm a", Locale.US).format(Date())
        }
    }

    // --- Calculator State ---
    var selectedCurrency by remember { mutableStateOf(currencies[0]) }
    var foreignAmountInput by remember { mutableStateOf("100") }
    var inrAmountInput by remember { mutableStateOf("") }
    var isForeignToInr by remember { mutableStateOf(true) } // direction
    var showDropdown by remember { mutableStateOf(false) }

    // State for keeping track of expanded wizard step (StepNo)
    var expandedStepNo by remember { mutableStateOf(1) }

    // Compute converted value
    val calculatedResult: String = remember(foreignAmountInput, inrAmountInput, selectedCurrency, isForeignToInr, dynamicRates) {
        val currentRate = dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr
        if (isForeignToInr) {
            val amt = foreignAmountInput.toDoubleOrNull() ?: 0.0
            "₹ " + "%,.2f".format(amt * currentRate)
        } else {
            val amt = inrAmountInput.toDoubleOrNull() ?: 0.0
            selectedCurrency.flag + " " + "%,.2f".format(if (currentRate > 0) amt / currentRate else 0.0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEnglish) "Foreigner Payment Guide" else "विदेशी भुगतान गाइड",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
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
        ) {
            // ─── CUSTOM SEGMENTED PILL SWITCHER ──────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SaffronPrimary.copy(alpha = 0.08f))
                    .border(1.dp, SaffronPrimary.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                val isGuideSelected = activeTab == 0
                
                val guideBgColor by animateColorAsState(
                    targetValue = if (isGuideSelected) SaffronPrimary else Color.Transparent,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                    label = "GuideBg"
                )
                val guideTextColor by animateColorAsState(
                    targetValue = if (isGuideSelected) Color(0xFF0A090D) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    animationSpec = tween(durationMillis = 250),
                    label = "GuideText"
                )
                val converterBgColor by animateColorAsState(
                    targetValue = if (!isGuideSelected) SaffronPrimary else Color.Transparent,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                    label = "ConverterBg"
                )
                val converterTextColor by animateColorAsState(
                    targetValue = if (!isGuideSelected) Color(0xFF0A090D) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    animationSpec = tween(durationMillis = 250),
                    label = "ConverterText"
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(guideBgColor)
                        .clickable { activeTab = 0 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEnglish) "UPI Guide" else "यूपीआई गाइड",
                        color = guideTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(converterBgColor)
                        .clickable { activeTab = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEnglish) "Currency Converter" else "मुद्रा परिवर्तक",
                        color = converterTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            if (activeTab == 0) {
                // ─── TAB 1: UPI WALKTHROUGH GUIDE (Timeline Style) ────────────────
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        // Redesigned Info Card
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = SaffronPrimary.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = SaffronPrimary.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    SaffronPrimary.copy(alpha = 0.25f),
                                                    SaffronPrimary.copy(alpha = 0.05f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Info, null, tint = SaffronPrimary)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isEnglish) "What is UPI One World?" else "यूपीआई वन वर्ल्ड क्या है?",
                                        color = SaffronPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isEnglish)
                                            "An official RBI program that lets foreign tourists pay seamlessly by scanning local QR codes using pre-paid wallets."
                                        else
                                            "एक आधिकारिक आरबीआई कार्यक्रम जो विदेशी पर्यटकों को प्रीपेड वॉलेट का उपयोग करके स्थानीय क्यूआर कोड को स्कैन करके भुगतान करने की अनुमति देता है।",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    // Render steps with connection timeline lines
                    itemsIndexed(wizardSteps) { index, step ->
                        TimelineWizardStep(
                            step = step,
                            isEnglish = isEnglish,
                            isExpanded = expandedStepNo == step.stepNo,
                            isLastStep = index == wizardSteps.size - 1,
                            onExpandToggle = {
                                expandedStepNo = if (expandedStepNo == step.stepNo) -1 else step.stepNo
                            }
                        )
                    }
                }
            } else {
                // ─── TAB 2: CURRENCY CONVERTER & CHARTS ───────────────────────────
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        // Live exchange rates update timestamp indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (rateUpdateTime == "Default Rates") Color.Red else Color.Green)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEnglish) "Live Exchange Rates" else "लाइव विनिमय दरें",
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = if (rateUpdateTime == "Default Rates") {
                                    if (isEnglish) "Using Fallback" else "डिफ़ॉल्ट दरें"
                                } else {
                                    if (isEnglish) "Updated $rateUpdateTime" else "अपडेट किया गया $rateUpdateTime"
                                },
                                color = SaffronPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        // --- Redesigned Premium Converter Card ---
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                
                                // Source Currency Input Box
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SaffronPrimary.copy(alpha = 0.05f))
                                        .border(0.5.dp, SaffronPrimary.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isForeignToInr) {
                                                if (isEnglish) "You Send" else "आप भेजते हैं"
                                            } else {
                                                if (isEnglish) "You Send (INR)" else "आप भेजते हैं (INR)"
                                            },
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (isForeignToInr) {
                                            Box {
                                                CurrencyDropdownTrigger(
                                                    selectedCurrency = selectedCurrency,
                                                    isEnglish = isEnglish,
                                                    onClick = { showDropdown = true }
                                                )
                                                DropdownMenu(
                                                    expanded = showDropdown,
                                                    onDismissRequest = { showDropdown = false },
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.surface)
                                                        .border(0.5.dp, SaffronPrimary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                                ) {
                                                    currencies.forEach { cur ->
                                                        DropdownMenuItem(
                                                            text = {
                                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                                    Text(cur.flag, fontSize = 20.sp)
                                                                    Spacer(modifier = Modifier.width(8.dp))
                                                                    Text(
                                                                        text = "${cur.code} (${if (isEnglish) cur.nameEn else cur.nameHi})",
                                                                        color = MaterialTheme.colorScheme.onSurface,
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 14.sp
                                                                    )
                                                                }
                                                            },
                                                            onClick = {
                                                                selectedCurrency = cur
                                                                showDropdown = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("🇮🇳", fontSize = 22.sp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "INR",
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    BasicTextField(
                                        value = if (isForeignToInr) foreignAmountInput else inrAmountInput,
                                        onValueChange = {
                                            if (isForeignToInr) foreignAmountInput = it else inrAmountInput = it
                                        },
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        cursorBrush = SolidColor(SaffronPrimary),
                                        decorationBox = { innerTextField ->
                                            if ((isForeignToInr && foreignAmountInput.isEmpty()) || (!isForeignToInr && inrAmountInput.isEmpty())) {
                                                Text(
                                                    text = "0.00",
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                                    fontSize = 28.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Serif
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }

                                // Interactive Swap Button Divider
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Divider(
                                        color = SaffronPrimary.copy(alpha = 0.2f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    )
                                    
                                    val swapRotation by animateFloatAsState(
                                        targetValue = if (isForeignToInr) 0f else 180f,
                                        animationSpec = tween(durationMillis = 300),
                                        label = "SwapRotation"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(SaffronPrimary)
                                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                            .clickable {
                                                isForeignToInr = !isForeignToInr
                                                if (isForeignToInr) {
                                                    val inrAmt = inrAmountInput.toDoubleOrNull() ?: 0.0
                                                    val currentRate = dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr
                                                    foreignAmountInput = if (currentRate > 0) "%.2f".format(inrAmt / currentRate) else "0.00"
                                                } else {
                                                    val forAmt = foreignAmountInput.toDoubleOrNull() ?: 0.0
                                                    val currentRate = dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr
                                                    inrAmountInput = "%.2f".format(forAmt * currentRate)
                                                }
                                            }
                                            .rotate(swapRotation),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.SwapVert,
                                            contentDescription = "Swap Direction",
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                // Target Currency Calculated Box
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SaffronPrimary.copy(alpha = 0.05f))
                                        .border(0.5.dp, SaffronPrimary.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isForeignToInr) {
                                                if (isEnglish) "You Get (Estimated)" else "आपको मिलते हैं (अनुमानित)"
                                            } else {
                                                if (isEnglish) "You Get (${selectedCurrency.code})" else "आपको मिलते हैं (${selectedCurrency.code})"
                                            },
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (isForeignToInr) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("🇮🇳", fontSize = 22.sp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "INR",
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        } else {
                                            Box {
                                                CurrencyDropdownTrigger(
                                                    selectedCurrency = selectedCurrency,
                                                    isEnglish = isEnglish,
                                                    onClick = { showDropdown = true }
                                                )
                                                DropdownMenu(
                                                    expanded = showDropdown,
                                                    onDismissRequest = { showDropdown = false },
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.surface)
                                                        .border(0.5.dp, SaffronPrimary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                                ) {
                                                    currencies.forEach { cur ->
                                                        DropdownMenuItem(
                                                            text = {
                                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                                    Text(cur.flag, fontSize = 20.sp)
                                                                    Spacer(modifier = Modifier.width(8.dp))
                                                                    Text(
                                                                        text = "${cur.code} (${if (isEnglish) cur.nameEn else cur.nameHi})",
                                                                        color = MaterialTheme.colorScheme.onSurface,
                                                                        fontWeight = FontWeight.Bold,
                                                                        fontSize = 14.sp
                                                                    )
                                                                }
                                                            },
                                                            onClick = {
                                                                selectedCurrency = cur
                                                                showDropdown = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = calculatedResult,
                                        color = SaffronPrimary,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Guaranteed Exchange Rate Bottom Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isEnglish) "Mid-Market Exchange Rate" else "मध्य-बाजार विनिमय दर",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "1 ${selectedCurrency.code} = ₹${"%.2f".format(dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr)}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // --- Quick Conversion Reference Table ---
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish) "Quick Conversion Table" else "त्वरित रूपांतरण तालिका",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (isEnglish) "Foreign (${selectedCurrency.code})" else "विदेशी (${selectedCurrency.code})",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = if (isEnglish) "Indian Rupees (INR)" else "भारतीय रुपये (INR)",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = SaffronPrimary.copy(alpha = 0.15f))
                                Spacer(modifier = Modifier.height(8.dp))

                                val quickAmounts = listOf(10.0, 50.0, 100.0, 500.0, 1000.0)
                                val currentRate = dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr

                                quickAmounts.forEachIndexed { index, amount ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${selectedCurrency.flag} ${"%,.0f".format(amount)} ${selectedCurrency.code}",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "₹ ${"%,.2f".format(amount * currentRate)}",
                                            color = SaffronPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    if (index < quickAmounts.size - 1) {
                                        Divider(color = SaffronPrimary.copy(alpha = 0.08f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Sub-Composables ─────────────────────────────────────────────────────────

@Composable
fun CurrencyDropdownTrigger(
    selectedCurrency: CurrencyRate,
    isEnglish: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SaffronPrimary.copy(alpha = 0.08f))
            .border(0.5.dp, SaffronPrimary.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(selectedCurrency.flag, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = selectedCurrency.code,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = SaffronPrimary,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun TimelineWizardStep(
    step: WizardStep,
    isEnglish: Boolean,
    isExpanded: Boolean,
    isLastStep: Boolean,
    onExpandToggle: () -> Unit
) {
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Aligns vertical timeline line height to match card height dynamically
    ) {
        // Left Column: Timeline vertical line & bullet
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isExpanded) SaffronPrimary else SaffronPrimary.copy(alpha = 0.12f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isExpanded) SaffronPrimary else SaffronPrimary.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.stepNo.toString(),
                    color = if (isExpanded) Color.Black else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            if (!isLastStep) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(
                            color = SaffronPrimary.copy(alpha = 0.25f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Right Column: Expandable Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 3.dp else 1.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (isExpanded) SaffronPrimary else SaffronPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable(onClick = onExpandToggle)
                .animateContentSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                SaffronPrimary.copy(alpha = 0.08f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(step.emoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isEnglish) step.titleEn else step.titleHi,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Text(
                            text = if (isEnglish) step.descEn else step.descHi,
                            color = if (isExpanded) SaffronPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = SaffronPrimary,
                        modifier = Modifier.rotate(rotationState)
                    )
                }
                
                // Show detail block when expanded
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = SaffronPrimary.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEnglish) step.detailEn else step.detailHi,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
