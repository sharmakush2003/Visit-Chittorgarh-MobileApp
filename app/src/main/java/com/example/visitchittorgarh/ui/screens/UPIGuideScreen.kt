package com.example.visitchittorgarh.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.visitchittorgarh.theme.CrimsonDark
import com.example.visitchittorgarh.theme.CrimsonSecondary
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
                // API returns units of target currency per 1 INR (e.g. 0.012 USD per 1 INR)
                // So 1 unit of target currency = 1 / rateToInr INR
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
    val sharedPrefs = remember { context.getSharedPreferences("chittorgarh_prefs", Context.MODE_PRIVATE) }
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

    // Compute converted value
    val calculatedResult: String = remember(foreignAmountInput, inrAmountInput, selectedCurrency, isForeignToInr, dynamicRates) {
        val currentRate = dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr
        if (isForeignToInr) {
            val amt = foreignAmountInput.toDoubleOrNull() ?: 0.0
            "₹ " + "%.2f".format(amt * currentRate)
        } else {
            val amt = inrAmountInput.toDoubleOrNull() ?: 0.0
            selectedCurrency.flag + " " + "%.2f".format(if (currentRate > 0) amt / currentRate else 0.0)
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
                        fontSize = 18.sp
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
            // --- Custom Tabs Selector ---
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = SaffronPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = SaffronPrimary
                    )
                }
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = {
                        Text(
                            text = if (isEnglish) "UPI Guide" else "यूपीआई गाइड",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp
                        )
                    }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = {
                        Text(
                            text = if (isEnglish) "Currency Converter" else "मुद्रा परिवर्तक",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp
                        )
                    }
                )
            }

            if (activeTab == 0) {
                // ─── TAB 1: UPI WALKTHROUGH GUIDE ─────────────────────────────────
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E140A)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SaffronPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Info, null, tint = SaffronPrimary)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isEnglish) "What is UPI One World?" else "यूपीआई वन वर्ल्ड क्या है?",
                                        color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Text(
                                        text = if (isEnglish)
                                            "An official RBI program that lets foreign tourists pay seamlessly by scanning local QR codes using pre-paid wallets."
                                        else
                                            "एक आधिकारिक आरबीआई कार्यक्रम जो विदेशी पर्यटकों को प्रीपेड वॉलेट का उपयोग करके स्थानीय क्यूआर कोड को स्कैन करके भुगतान करने की अनुमति देता है।",
                                        color = Color.White.copy(alpha = 0.8f), fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    items(wizardSteps) { step ->
                        WizardStepCard(step = step, isEnglish = isEnglish)
                    }
                }
            } else {
                // ─── TAB 2: OFFLINE CURRENCY CONVERTER ─────────────────────────────
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        // Rates timestamp card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isEnglish) "Live Exchange Rates Cache" else "लाइव विनिमय दर कैश",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = rateUpdateTime,
                                color = SaffronPrimary,
                                fontSize = 12.sp, fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        // Direction Selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                .padding(4.dp)
                        ) {
                            Button(
                                onClick = { isForeignToInr = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isForeignToInr) SaffronPrimary else Color.Transparent,
                                    contentColor = if (isForeignToInr) Color.Black else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (isEnglish) "Foreign ➔ INR" else "विदेशी ➔ रुपये",
                                    fontWeight = FontWeight.Bold, fontSize = 12.sp
                                )
                            }
                            Button(
                                onClick = { isForeignToInr = false },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isForeignToInr) SaffronPrimary else Color.Transparent,
                                    contentColor = if (!isForeignToInr) Color.Black else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (isEnglish) "INR ➔ Foreign" else "रुपये ➔ विदेशी",
                                    fontWeight = FontWeight.Bold, fontSize = 12.sp
                                )
                            }
                        }
                    }

                    item {
                        // Calculator Box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Select Currency Row
                                Text(
                                    text = if (isEnglish) "Select Foreign Currency" else "विदेशी मुद्रा चुनें",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                var showDropdown by remember { mutableStateOf(false) }
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colorScheme.background)
                                            .clickable { showDropdown = true }
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(selectedCurrency.flag, fontSize = 24.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(selectedCurrency.code, fontWeight = FontWeight.Bold, color = Color.White)
                                                Text(
                                                    if (isEnglish) selectedCurrency.nameEn else selectedCurrency.nameHi,
                                                    fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                )
                                            }
                                        }
                                        Icon(Icons.Default.KeyboardArrowDown, null, tint = SaffronPrimary)
                                    }

                                    DropdownMenu(
                                        expanded = showDropdown,
                                        onDismissRequest = { showDropdown = false }
                                    ) {
                                        currencies.forEach { cur ->
                                            DropdownMenuItem(
                                                text = {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(cur.flag, fontSize = 20.sp)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("${cur.code} (${if (isEnglish) cur.nameEn else cur.nameHi})", color = Color.White)
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

                                Spacer(modifier = Modifier.height(16.dp))

                                // Input Row
                                Text(
                                    text = if (isForeignToInr)
                                        (if (isEnglish) "Amount to Convert (${selectedCurrency.code})" else "परिवर्तित करने की राशि (${selectedCurrency.code})")
                                    else
                                        (if (isEnglish) "Amount in Rupees (INR)" else "रुपये में राशि (INR)"),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 11.sp, fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = if (isForeignToInr) foreignAmountInput else inrAmountInput,
                                    onValueChange = {
                                        if (isForeignToInr) foreignAmountInput = it else inrAmountInput = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SaffronPrimary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Result area
                                Text(
                                    text = if (isEnglish) "CONVERTED VALUE" else "परिवर्तित मूल्य",
                                    color = SaffronPrimary,
                                    fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = calculatedResult,
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isEnglish)
                                        "1 ${selectedCurrency.code} = ₹${"%.2f".format(dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr)}"
                                    else
                                        "1 ${selectedCurrency.code} = ₹${"%.2f".format(dynamicRates[selectedCurrency.code] ?: selectedCurrency.rateToInr)}",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 12.sp, fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Wizard Step Card Composable ──────────────────────────────────────────────

@Composable
private fun WizardStepCard(step: WizardStep, isEnglish: Boolean) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, SaffronPrimary.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(step.emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isEnglish) step.titleEn else step.titleHi,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SaffronPrimary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "STEP ${step.stepNo}" else "कदम ${step.stepNo}",
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = if (isEnglish) step.descEn else step.descHi,
                    color = GoldAccent,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isEnglish) step.detailEn else step.detailHi,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
