package com.example.visitchittorgarh.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.visitchittorgarh.R

interface DataRepository {
    val attractions: Flow<List<Attraction>>
    val packages: Flow<List<TourPackage>>
    val cabs: Flow<List<CabOption>>
    val guides: Flow<List<GuideOption>>
    val stays: Flow<List<StayOption>>
    val locals: Flow<List<LocalItem>>
}

class DefaultDataRepository : DataRepository {
    override val attractions: Flow<List<Attraction>> = flow {
        emit(
            listOf(
                Attraction(
                    name = Translation(
                        en = "Chittorgarh Fort",
                        hi = "चित्तौड़गढ़ किला"
                    ),
                    category = Translation(
                        en = "Heritage",
                        hi = "धरोहर"
                    ),
                    description = Translation(
                        en = "The largest fort in India and the grand symbol of Rajput valour, sacrifice, and history. Home to Padmini Palace, Mira Temple, and Gaumukh Reservoir.",
                        hi = "भारत का सबसे बड़ा किला और राजपूत वीरता, त्याग और इतिहास का भव्य प्रतीक। पद्मिनी पैलेस, मीरा मंदिर और गौमुख जलाशय का घर।"
                    ),
                    entryFee = Translation(
                        en = "₹50 (Indian), ₹600 (Foreign)",
                        hi = "₹50 (भारतीय), ₹600 (विदेशी)"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 6:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.9,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/Chittorgarh%20Fort.webp"
                ),
                Attraction(
                    name = Translation(
                        en = "Vijay Stambh (Tower of Victory)",
                        hi = "विजय स्तम्भ"
                    ),
                    category = Translation(
                        en = "Heritage",
                        hi = "धरोहर"
                    ),
                    description = Translation(
                        en = "A massive 9-story tower constructed by Maharana Kumbha in 1448 to commemorate his victory over the armies of Malwa and Gujarat.",
                        hi = "मालवा और गुजरात की सेनाओं पर अपनी जीत की स्मृति में 1448 में महाराणा कुंभा द्वारा निर्मित एक विशाल 9 मंजिला स्तंभ।"
                    ),
                    entryFee = Translation(
                        en = "Included in Fort Ticket",
                        hi = "किले के टिकट में शामिल"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 6:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.8,
                    imageUrl = R.drawable.vijay_stambh
                ),
                Attraction(
                    name = Translation(
                        en = "Sanwariaji Temple",
                        hi = "सांवलिया जी मंदिर"
                    ),
                    category = Translation(
                        en = "Spiritual",
                        hi = "आध्यात्मिक"
                    ),
                    description = Translation(
                        en = "Located 40 km from Chittorgarh, this magnificent temple dedicated to Lord Krishna (Sanwaria Seth) is one of the most prominent pilgrimage centers in Rajasthan.",
                        hi = "चित्तौड़गढ़ से 40 किमी दूर स्थित, भगवान कृष्ण (सांवलिया सेठ) को समर्पित यह भव्य मंदिर राजस्थान के सबसे प्रमुख तीर्थ स्थलों में से एक है।"
                    ),
                    entryFee = Translation(
                        en = "Free Entry",
                        hi = "निःशुल्क प्रवेश"
                    ),
                    timing = Translation(
                        en = "5:00 AM - 12:00 PM, 2:30 PM - 11:00 PM",
                        hi = "सुबह 5:00 - दोपहर 12:00, दोपहर 2:30 - रात 11:00"
                    ),
                    rating = 4.9,
                    imageUrl = R.drawable.sanwaria_temple
                ),
                Attraction(
                    name = Translation(
                        en = "Matrikundia Temple",
                        hi = "मातृकुण्डिया मंदिर"
                    ),
                    category = Translation(
                        en = "Spiritual",
                        hi = "आध्यात्मिक"
                    ),
                    description = Translation(
                        en = "Known as the 'Haridwar of Rajasthan', this holy site dedicated to Lord Shiva features sacred water pools where pilgrims perform rituals. Situated on the banks of Banas river.",
                        hi = "राजस्थान का 'हरिद्वार' कहा जाने वाला, भगवान शिव को समर्पित यह पवित्र स्थल पवित्र कुंडों से सुसज्जित है जहाँ श्रद्धालु अनुष्ठान करते हैं। बनास नदी के तट पर स्थित।"
                    ),
                    entryFee = Translation(
                        en = "Free Entry",
                        hi = "निःशुल्क प्रवेश"
                    ),
                    timing = Translation(
                        en = "6:00 AM - 8:00 PM",
                        hi = "सुबह 6:00 बजे - रात 8:00 बजे"
                    ),
                    rating = 4.7,
                    imageUrl = R.drawable.matrikundia_temple
                ),
                Attraction(
                    name = Translation(
                        en = "Bassi Wildlife Sanctuary",
                        hi = "बस्सी वन्यजीव अभयारण्य"
                    ),
                    category = Translation(
                        en = "Nature",
                        hi = "प्रकृति"
                    ),
                    description = Translation(
                        en = "Lush green sanctuary home to leopards, wild boars, antelopes, and a paradise for birdwatchers. Bordered by the scenic Bassi Dam.",
                        hi = "तेंदुओं, जंगली सूअरों, एंटीलोप्स और पक्षी प्रेमियों के लिए एक स्वर्ग। सुंदर बस्सी बांध की सीमा से लगा हरा-भरा अभयारण्य।"
                    ),
                    entryFee = Translation(
                        en = "₹100 (Indian), ₹500 (Foreign)",
                        hi = "₹100 (भारतीय), ₹500 (विदेशी)"
                    ),
                    timing = Translation(
                        en = "7:00 AM - 5:00 PM",
                        hi = "सुबह 7:00 बजे - शाम 5:00 बजे"
                    ),
                    rating = 4.5,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/Bassi-Wildlife-Sanctuary-Chittorgarh.jpg"
                ),
                Attraction(
                    name = Translation(
                        en = "Sita Mata Wildlife Sanctuary",
                        hi = "सीता माता वन्यजीव अभयारण्य"
                    ),
                    category = Translation(
                        en = "Nature",
                        hi = "प्रकृति"
                    ),
                    description = Translation(
                        en = "Famous for its rare Flying Squirrels, dense teak forests, and rich biodiversity. A gorgeous sanctuary steeped in local folklore and natural springs.",
                        hi = "अपनी दुर्लभ उड़ने वाली गिलहरियों, घने सागौन के जंगलों और समृद्ध जैव विविधता के लिए प्रसिद्ध। स्थानीय लोककथाओं और प्राकृतिक झरनों से समृद्ध एक भव्य अभयारण्य।"
                    ),
                    entryFee = Translation(
                        en = "₹100 (Indian), ₹500 (Foreign)",
                        hi = "₹100 (भारतीय), ₹500 (विदेशी)"
                    ),
                    timing = Translation(
                        en = "6:00 AM - 6:00 PM",
                        hi = "सुबह 6:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.6,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/nature_generated.png"
                ),
                Attraction(
                    name = Translation(
                        en = "Kirti Stambh (Tower of Fame)",
                        hi = "कीर्ति स्तम्भ"
                    ),
                    category = Translation(
                        en = "Heritage",
                        hi = "धरोहर"
                    ),
                    description = Translation(
                        en = "A gorgeous 12th-century tower built by a Jain merchant, dedicated to Adinath, the first Jain Tirthankara. Adorned with beautiful figures of the Digambara sect.",
                        hi = "एक जैन व्यापारी द्वारा निर्मित एक भव्य 12वीं सदी का स्तंभ, जो पहले जैन तीर्थंकर आदिनाथ को समर्पित है। दिगंबर संप्रदाय की सुंदर आकृतियों से सुसज्जित।"
                    ),
                    entryFee = Translation(
                        en = "Included in Fort Ticket",
                        hi = "किले के टिकट में शामिल"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 6:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.7,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/Kirti%20Stambh.jpg"
                ),
                Attraction(
                    name = Translation(
                        en = "Menal Waterfall & Temples",
                        hi = "मेनाल जलप्रपात और मंदिर"
                    ),
                    category = Translation(
                        en = "Scenic",
                        hi = "मनोरम"
                    ),
                    description = Translation(
                        en = "A scenic 150-feet waterfall dropping into a deep gorge, surrounded by lush green forests and ancient 11th-century temples resembling the Khajuraho art style.",
                        hi = "एक गहरी घाटी में गिरने वाला 150 फीट का मनोरम जलप्रपात, जो हरे-भरे जंगलों और खजुराहो कला शैली से मिलते-जुलते प्राचीन 11वीं सदी के मंदिरों से घिरा हुआ है।"
                    ),
                    entryFee = Translation(
                        en = "₹40 (Indian), ₹300 (Foreign)",
                        hi = "₹40 (भारतीय), ₹300 (विदेशी)"
                    ),
                    timing = Translation(
                        en = "8:00 AM - 5:00 PM",
                        hi = "सुबह 8:00 बजे - शाम 5:00 बजे"
                    ),
                    rating = 4.8,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/waterfall_generated.png"
                ),
                Attraction(
                    name = Translation(
                        en = "Nagari Ancient Ruins",
                        hi = "नागरी के प्राचीन अवशेष"
                    ),
                    category = Translation(
                        en = "Scenic",
                        hi = "मनोरम"
                    ),
                    description = Translation(
                        en = "One of the oldest towns in Rajasthan, nagari displays ancient stupas, temples, Maurya/Sunga brick structures, and historical stone inscriptions.",
                        hi = "राजस्थान के सबसे पुराने कस्बों में से एक, नागरी प्राचीन स्तूपों, मंदिरों, मौर्य/शुंग कालीन ईंटों की संरचनाओं और ऐतिहासिक शिलालेखों को प्रदर्शित करता है।"
                    ),
                    entryFee = Translation(
                        en = "Free Entry",
                        hi = "निःशुल्क प्रवेश"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 5:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 5:00 बजे"
                    ),
                    rating = 4.4,
                    imageUrl = "https://www.visitchittorgarh.in/Fort.png"
                ),
                Attraction(
                    name = Translation(
                        en = "Kumbha Shyam Temple",
                        hi = "कुंभा श्याम मंदिर"
                    ),
                    category = Translation(
                        en = "Spiritual",
                        hi = "आध्यात्मिक"
                    ),
                    description = Translation(
                        en = "Dedicated to Lord Vishnu, this temple built by Rana Kumbha is a masterpiece of Indo-Aryan architecture, adorned with rich carvings.",
                        hi = "भगवान विष्णु को समर्पित, राणा कुंभा द्वारा निर्मित यह मंदिर भारत-आर्य वास्तुकला का एक उत्कृष्ट नमूना है, जो समृद्ध नक्काशी से सजाया गया है।"
                    ),
                    entryFee = Translation(
                        en = "Included in Fort Ticket",
                        hi = "किले के टिकट में शामिल"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 6:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.7,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/Kumbha%20SHyam.jpg"
                ),
                Attraction(
                    name = Translation(
                        en = "Kalika Mata Temple",
                        hi = "कालिका माता मंदिर"
                    ),
                    category = Translation(
                        en = "Spiritual",
                        hi = "आध्यात्मिक"
                    ),
                    description = Translation(
                        en = "Originally built as a Sun Temple in the 8th century, this structure was later converted into a temple dedicated to Goddess Kali, the mother goddess.",
                        hi = "मूल रूप से 8वीं शताब्दी में सूर्य मंदिर के रूप में निर्मित, इस संरचना को बाद में मातृ देवी काली माता को समर्पित मंदिर में बदल दिया गया था।"
                    ),
                    entryFee = Translation(
                        en = "Included in Fort Ticket",
                        hi = "किले के टिकट में शामिल"
                    ),
                    timing = Translation(
                        en = "6:00 AM - 8:00 PM",
                        hi = "सुबह 6:00 बजे - रात 8:00 बजे"
                    ),
                    rating = 4.8,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/fateh-prakash-new.jpg"
                ),
                Attraction(
                    name = Translation(
                        en = "Padmavati Temple & Memorial",
                        hi = "पद्मावती मंदिर और स्मारक"
                    ),
                    category = Translation(
                        en = "Heritage",
                        hi = "धरोहर"
                    ),
                    description = Translation(
                        en = "A beautiful memorial dedicated to the legendary Queen Padmini, reflecting the timeless tales of Rajputana pride, beauty, and Jauhar sacrifice.",
                        hi = "प्रसिद्ध रानी पद्मिनी को समर्पित एक सुंदर स्मारक, जो राजपूत गौरव, सौंदर्य और जौहर बलिदान की कालजयी कहानियों को दर्शाता है।"
                    ),
                    entryFee = Translation(
                        en = "Free Entry",
                        hi = "निःशुल्क प्रवेश"
                    ),
                    timing = Translation(
                        en = "9:00 AM - 6:00 PM",
                        hi = "सुबह 9:00 बजे - शाम 6:00 बजे"
                    ),
                    rating = 4.6,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/Padmini%20Palace.jpg"
                ),
                Attraction(
                    name = Translation(
                        en = "Light & Sound Show",
                        hi = "लाइट एंड साउंड शो"
                    ),
                    category = Translation(
                        en = "Heritage",
                        hi = "धरोहर"
                    ),
                    description = Translation(
                        en = "A mesmerizing evening show at the fort narrating the glorious battles, sacrifices, and history of the Mewar kingdom.",
                        hi = "किले में एक शानदार शाम का शो जो मेवाड़ साम्राज्य के गौरवशाली युद्धों, बलिदानों और इतिहास का वर्णन करता है।"
                    ),
                    entryFee = Translation(
                        en = "₹100 (Indian), ₹300 (Foreign)",
                        hi = "₹100 (भारतीय), ₹300 (विदेशी)"
                    ),
                    timing = Translation(
                        en = "7:00 PM - 8:00 PM (Daily)",
                        hi = "शाम 7:00 बजे - रात 8:00 बजे (दैनिक)"
                    ),
                    rating = 4.8,
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/fateh-prakash-new.jpg"
                )
            )
        )
    }

    override val packages: Flow<List<TourPackage>> = flow {
        emit(
            listOf(
                TourPackage(
                    id = "pkg_1",
                    title = Translation(
                        en = "1-Day Fort Explorer",
                        hi = "1-दिवसीय किला खोजी"
                    ),
                    duration = Translation(
                        en = "1 Day (8 Hours)",
                        hi = "1 दिन (8 घंटे)"
                    ),
                    price = Translation(
                        en = "₹999 / Person",
                        hi = "₹999 / प्रति व्यक्ति"
                    ),
                    summary = Translation(
                        en = "Perfect for travelers on a short visit. Covers Chittorgarh Fort, Vijay Stambh, Padmini Palace, and a traditional Mewari lunch.",
                        hi = "कम समय की यात्रा वाले यात्रियों के लिए आदर्श। इसमें चित्तौड़गढ़ किला, विजय स्तम्भ, पद्मिनी पैलेस और एक पारंपरिक मेवाड़ी भोजन शामिल है।"
                    ),
                    itinerary = listOf(
                        Translation(
                            en = "09:00 AM - Pick up from station/hotel and brief by local guide.",
                            hi = "सुबह 09:00 - स्टेशन/होटल से पिक-अप और स्थानीय गाइड द्वारा संक्षिप्त जानकारी।"
                        ),
                        Translation(
                            en = "09:30 AM - Guided tour of Chittorgarh Fort entrance & Kirti Stambh.",
                            hi = "सुबह 09:30 - चित्तौड़गढ़ किला प्रवेश और कीर्ति स्तम्भ का गाइडेड दौरा।"
                        ),
                        Translation(
                            en = "11:30 AM - Visit Kumbha Palace and historical Vijay Stambh.",
                            hi = "सुबह 11:30 - कुंभा पैलेस और ऐतिहासिक विजय स्तम्भ का भ्रमण।"
                        ),
                        Translation(
                            en = "01:30 PM - Authentic Mewari Lunch (Dal Baati Churma).",
                            hi = "दोपहर 01:30 - प्रामाणिक मेवाड़ी लंच (दाल बाटी चूरमा)।"
                        ),
                        Translation(
                            en = "03:00 PM - Visit Padmini Palace & Gaumukh Kund.",
                            hi = "दोपहर 03:00 - पद्मिनी पैलेस और गौमुख कुंड का भ्रमण।"
                        ),
                        Translation(
                            en = "05:00 PM - Evening sound & light show or drop-off.",
                            hi = "शाम 05:00 - शाम का लाइट एंड साउंड शो या ड्रॉप-ऑफ।"
                        )
                    ),
                    imageUrl = R.drawable.vijay_stambh
                ),
                TourPackage(
                    id = "pkg_2",
                    title = Translation(
                        en = "2-Day Royal Heritage Tour",
                        hi = "2-दिवसीय रॉयल हेरिटेज यात्रा"
                    ),
                    duration = Translation(
                        en = "2 Days / 1 Night",
                        hi = "2 दिन / 1 रात"
                    ),
                    price = Translation(
                        en = "₹2,499 / Person",
                        hi = "₹2,499 / प्रति व्यक्ति"
                    ),
                    summary = Translation(
                        en = "Deep dive into local history and spirituality. Covers Chittorgarh Fort, Sanwariaji Temple, and local block print artisans.",
                        hi = "स्थानीय इतिहास और आध्यात्मिकता में गहराई से उतरें। इसमें चित्तौड़गढ़ किला, सांवलिया जी मंदिर और स्थानीय ब्लॉक प्रिंट कारीगर शामिल हैं।"
                    ),
                    itinerary = listOf(
                        Translation(
                            en = "Day 1: Full Fort expedition, royal heritage walk, sound & light show, and overnight stay at a heritage hotel.",
                            hi = "दिन 1: पूरा किला भ्रमण, शाही विरासत वॉक, लाइट एंड साउंड शो, और हेरिटेज होटल में रात बिताना।"
                        ),
                        Translation(
                            en = "Day 2: Morning visit to Sanwariaji Seth temple, local handicraft shopping, and block printing workshop in Akola.",
                            hi = "दिन 2: सुबह सांवलिया जी सेठ मंदिर के दर्शन, स्थानीय हस्तशिल्प की खरीदारी, और अकोला में ब्लॉक प्रिंटिंग कार्यशाला।"
                        )
                    ),
                    imageUrl = R.drawable.sanwaria_temple
                ),
                TourPackage(
                    id = "pkg_3",
                    title = Translation(
                        en = "3-Day Heritage & Nature Expedition",
                        hi = "3-दिवसीय हेरिटेज और प्रकृति अभियान"
                    ),
                    duration = Translation(
                        en = "3 Days / 2 Nights",
                        hi = "3 दिन / 2 रातें"
                    ),
                    price = Translation(
                        en = "₹4,499 / Person",
                        hi = "₹4,499 / प्रति व्यक्ति"
                    ),
                    summary = Translation(
                        en = "The complete Chittorgarh experience. Covers Forts, Spiritual temples, Bassi Wildlife Sanctuary safari, and local Mewari food experiences.",
                        hi = "संपूर्ण चित्तौड़गढ़ अनुभव। किले, आध्यात्मिक मंदिर, बस्सी वन्यजीव अभयारण्य सफारी और स्थानीय मेवाड़ी भोजन का अनुभव।"
                    ),
                    itinerary = listOf(
                        Translation(
                            en = "Day 1: Fort sightseeing, victory tower, weapon collection museum, and traditional Rajasthani dinner.",
                            hi = "दिन 1: किला दर्शन, विजय स्तम्भ, हथियार संग्रह संग्रहालय, और पारंपरिक राजस्थानी डिनर।"
                        ),
                        Translation(
                            en = "Day 2: Wilderness safari at Bassi Wildlife Sanctuary, scenic boating in Bassi Dam, and campfire under the stars.",
                            hi = "दिन 2: बस्सी वन्यजीव अभयारण्य में सफारी, बस्सी बांध में नौका विहार, और तारों के नीचे कैंपफ़ायर।"
                        ),
                        Translation(
                            en = "Day 3: Sanwariaji Temple visit, Mewar Achaar tasting tour, and artisan village crafts exhibition.",
                            hi = "दिन 3: सांवलिया जी मंदिर के दर्शन, मेवाड़ अचार चखने का दौरा, और कारीगर गाँव शिल्प प्रदर्शनी।"
                        )
                    ),
                    imageUrl = R.drawable.palace_stay
                )
            )
        )
    }

    override val cabs: Flow<List<CabOption>> = flow {
        emit(
            listOf(
                CabOption(
                    vehicleName = "Innova / Ertiga (SUV)",
                    capacity = Translation(
                        en = "6 + 1 Seats",
                        hi = "6 + 1 सीटें"
                    ),
                    ratePerKm = Translation(
                        en = "₹14/km",
                        hi = "₹14/किमी"
                    ),
                    description = Translation(
                        en = "Ideal for families. Includes dynamic AC, luggage carrier, and an experienced local driver.",
                        hi = "परिवारों के लिए आदर्श। इसमें एसी, लगेज कैरियर और एक अनुभवी स्थानीय ड्राइवर शामिल है।"
                    ),
                    imageUrl = R.drawable.innova
                ),
                CabOption(
                    vehicleName = "Swift Dzire (Sedan)",
                    capacity = Translation(
                        en = "4 + 1 Seats",
                        hi = "4 + 1 सीटें"
                    ),
                    ratePerKm = Translation(
                        en = "₹11/km",
                        hi = "₹11/किमी"
                    ),
                    description = Translation(
                        en = "Comfortable, economical, and swift. Best for small groups or couples.",
                        hi = "आरामदायक, किफायती और तेज़। छोटे समूहों या जोड़ों के लिए सर्वश्रेष्ठ।"
                    ),
                    imageUrl = R.drawable.swift_dzire
                ),
                CabOption(
                    vehicleName = "Swift (Hatchback)",
                    capacity = Translation(
                        en = "4 + 1 Seats",
                        hi = "4 + 1 सीटें"
                    ),
                    ratePerKm = Translation(
                        en = "₹9/km",
                        hi = "₹9/किमी"
                    ),
                    description = Translation(
                        en = "Highly affordable transport for standard local running and daily commute.",
                        hi = "मानक स्थानीय दौड़ और दैनिक आवागमन के लिए अत्यधिक किफायती परिवहन।"
                    ),
                    imageUrl = R.drawable.swift_hatchback
                )
            )
        )
    }

    override val guides: Flow<List<GuideOption>> = flow {
        emit(
            listOf(
                GuideOption(
                    name = Translation(
                        en = "Chandra Shekhar Rathore",
                        hi = "चंद्रशेखर राठौड़"
                    ),
                    experience = Translation(
                        en = "12 Years",
                        hi = "12 वर्ष"
                    ),
                    languages = Translation(
                        en = "Hindi, English, Mewari",
                        hi = "हिंदी, अंग्रेजी, मेवाड़ी"
                    ),
                    specialty = Translation(
                        en = "Chittorgarh Fort History & Architecture",
                        hi = "चित्तौड़गढ़ किले का इतिहास और वास्तुकला"
                    ),
                    rating = 4.9,
                    imageUrl = R.drawable.logo_maharana
                ),
                GuideOption(
                    name = Translation(
                        en = "Vikram Singh Mewari",
                        hi = "विक्रम सिंह मेवाड़ी"
                    ),
                    experience = Translation(
                        en = "8 Years",
                        hi = "8 वर्ष"
                    ),
                    languages = Translation(
                        en = "Hindi, Mewari",
                        hi = "हिंदी, मेवाड़ी"
                    ),
                    specialty = Translation(
                        en = "Local Folk Legends, Battles & Fort Trails",
                        hi = "स्थानीय लोक कथाएं, युद्ध और किले के रास्ते"
                    ),
                    rating = 4.8,
                    imageUrl = R.drawable.logo_maharana
                ),
                GuideOption(
                    name = Translation(
                        en = "Pushpendra Sharma",
                        hi = "पुष्पेन्द्र शर्मा"
                    ),
                    experience = Translation(
                        en = "5 Years",
                        hi = "5 वर्ष"
                    ),
                    languages = Translation(
                        en = "Hindi, English, Sanskrit",
                        hi = "हिंदी, अंग्रेजी, संस्कृत"
                    ),
                    specialty = Translation(
                        en = "Spiritual temples, Archeology & Inscriptions",
                        hi = "आशिष्ट मंदिर, पुरातत्व और शिलालेख"
                    ),
                    rating = 4.7,
                    imageUrl = R.drawable.logo_maharana
                )
            )
        )
    }

    override val stays: Flow<List<StayOption>> = flow {
        emit(
            listOf(
                StayOption(
                    name = "X_Y_Z Palace",
                    type = Translation(
                        en = "Heritage Stay",
                        hi = "विरासत निवास"
                    ),
                    rating = 4.8,
                    priceRange = Translation(
                        en = "₹4,500 - ₹8,000 / Night",
                        hi = "₹4,500 - ₹8,000 / रात"
                    ),
                    description = Translation(
                        en = "A 16th-century palace restored into a luxury heritage hotel, offering royal hospitality, ethnic rooms, and beautiful gardens.",
                        hi = "एक 16वीं सदी का महल जिसे एक लक्जरी हेरिटेज होटल में तब्दील किया गया है, जो शाही आतिथ्य, जातीय कमरे और सुंदर उद्यान प्रदान करता है।"
                    ),
                    imageUrl = R.drawable.palace_stay
                ),
                StayOption(
                    name = "Y_Z Hotel",
                    type = Translation(
                        en = "Premium Hotel",
                        hi = "प्रीमियम होटल"
                    ),
                    rating = 4.4,
                    priceRange = Translation(
                        en = "₹2,500 - ₹4,500 / Night",
                        hi = "₹2,500 - ₹4,500 / रात"
                    ),
                    description = Translation(
                        en = "Modern comforts in the heart of Chittorgarh. Highly rated for its cleanliness, rooftop dining, and fortress views.",
                        hi = "चित्तौड़गढ़ के केंद्र में आधुनिक सुविधाएं। इसकी स्वच्छता, छत पर भोजन और किले के दृश्यों के लिए अत्यधिक प्रशंसित।"
                    ),
                    imageUrl = R.drawable.palace_stay
                ),
                StayOption(
                    name = "A_B_C Resort",
                    type = Translation(
                        en = "Heritage Lodge & Camps",
                        hi = "विरासत लॉज और शिविर"
                    ),
                    rating = 4.6,
                    priceRange = Translation(
                        en = "₹3,500 - ₹6,500 / Night",
                        hi = "₹3,500 - ₹6,500 / रात"
                    ),
                    description = Translation(
                        en = "Nestled in the Vindhyachal hills, offering horse safaris, heritage suites, wildlife hikes, and organic dining.",
                        hi = "विंध्याचल पहाड़ियों में बसा, जो हॉर्स सफारी, हेरिटेज सुइट्स, वन्यजीव ट्रेक और जैविक भोजन प्रदान करता है।"
                    ),
                    imageUrl = R.drawable.palace_stay
                )
            )
        )
    }

    override val locals: Flow<List<LocalItem>> = flow {
        emit(
            listOf(
                LocalItem(
                    name = Translation(
                        en = "Mewari Achaar",
                        hi = "मेवाड़ी अचार"
                    ),
                    category = Translation(
                        en = "Cuisine / Food",
                        hi = "व्यंजन / भोजन"
                    ),
                    description = Translation(
                        en = "Experience authentic, spicy & tangy pickles made from traditional recipes with local Mewari spices and love.",
                        hi = "स्थानीय सरसों के तेल और प्रामाणिक मेवाड़ मसालों के साथ हाथ से बने तीखे अचार। लाल मिर्च, आम और लहसुन के अचार के लिए प्रसिद्ध।"
                    ),
                    origin = Translation(
                        en = "Chittorgarh Villages",
                        hi = "चित्तौड़गढ़ के गाँव"
                    ),
                    imageUrl = R.drawable.mewari_achaar,
                    minPriceInr = 1000.0,
                    maxPriceInr = 2000.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Akola Block Printing",
                        hi = "अकोला ब्लॉक प्रिंटिंग"
                    ),
                    category = Translation(
                        en = "Fabrics / Arts",
                        hi = "कपड़े / कला"
                    ),
                    description = Translation(
                        en = "Discover the heritage of hand-block printed fabrics from Akola village, known for natural dyes & intricate Dabu patterns.",
                        hi = "प्राकृतिक रंगों और मिट्टी-प्रतिरोध तकनीकों का उपयोग करके मुद्रित शानदार जैविक हाथ से ब्लॉक किए गए वस्त्र, जो अकोला गांव के लिए अद्वितीय हैं।"
                    ),
                    origin = Translation(
                        en = "Akola Village, Chittorgarh",
                        hi = "अकोला गाँव, चित्तौड़गढ़"
                    ),
                    imageUrl = R.drawable.akola_printing,
                    minPriceInr = 3700.0,
                    maxPriceInr = 10000.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Bassi Wooden Toys & Temples (Kavad)",
                        hi = "बस्सी लकड़ी के खिलौने और मंदिर (कावड़)"
                    ),
                    category = Translation(
                        en = "Handicrafts",
                        hi = "हस्तशिल्प"
                    ),
                    description = Translation(
                        en = "Explore colorful, traditional toys intricately carved & painted with centuries-old craftsmanship from Mewar.",
                        hi = "स्थानीय सुथार कारीगरों द्वारा जीवंत, गैर-विषाक्त रंगों से सजे जटिल लकड़ी के मंदिर (कावड़) और हाथ से नक्काशीदार खिलौने।"
                    ),
                    origin = Translation(
                        en = "Bassi Village, Chittorgarh",
                        hi = "बस्सी गाँव, चित्तौड़गढ़"
                    ),
                    imageUrl = R.drawable.wooden_toys,
                    minPriceInr = 1500.0,
                    maxPriceInr = 5400.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Thewa Art Jewelry",
                        hi = "थेवा कला आभूषण"
                    ),
                    category = Translation(
                        en = "Jewelry / Art",
                        hi = "आभूषण / कला"
                    ),
                    description = Translation(
                        en = "Exquisite fusion of 23K gold sheets on colored glass bases, producing detailed mythological scenes of Rajasthan.",
                        hi = "रंगीन कांच के आधारों पर 23K सोने की परतों का उत्कृष्ट संलयन, जो राजस्थान के विस्तृत पौराणिक दृश्यों का निर्माण करता है।"
                    ),
                    origin = Translation(
                        en = "Mewar Region",
                        hi = "मेवाड़ क्षेत्र"
                    ),
                    imageUrl = R.drawable.thewa_jewelry,
                    minPriceInr = 12500.0,
                    maxPriceInr = 37500.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Leather Jutis",
                        hi = "चमड़े की जूतियाँ"
                    ),
                    category = Translation(
                        en = "Handicraft",
                        hi = "हस्तशिल्प"
                    ),
                    description = Translation(
                        en = "Traditional hand-stitched leather footwear featuring unique Rajasthani designs, durability, and embroidery.",
                        hi = "पारंपरिक हाथ से सिली हुई चमड़े की जूतियाँ जिनमें अद्वितीय राजस्थानी डिज़ाइन, स्थायित्व और कढ़ाई है।"
                    ),
                    origin = Translation(
                        en = "Local Bazaar, Chittorgarh",
                        hi = "स्थानीय बाजार, चित्तौड़गढ़"
                    ),
                    imageUrl = R.drawable.leather_jutis,
                    minPriceInr = 1600.0,
                    maxPriceInr = 3700.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Marble Crafts",
                        hi = "संगमरमर शिल्प"
                    ),
                    category = Translation(
                        en = "Handicraft",
                        hi = "हस्तशिल्प"
                    ),
                    description = Translation(
                        en = "Beautiful figurines, coasters, and items hand-carved from premium quality local white marble by village artisans.",
                        hi = "ग्रामीण कारीगरों द्वारा प्रीमियम गुणवत्ता वाले स्थानीय सफेद संगमरमर से हाथ से नक्काशीदार सुंदर मूर्तियाँ, कोस्टर और सामान।"
                    ),
                    origin = Translation(
                        en = "Chittorgarh Artisans",
                        hi = "चित्तौड़गढ़ के कारीगर"
                    ),
                    imageUrl = R.drawable.marble_crafts,
                    minPriceInr = 1250.0,
                    maxPriceInr = 6200.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Dal Batti Churma",
                        hi = "दाल बाटी चूरमा"
                    ),
                    category = Translation(
                        en = "Dish",
                        hi = "व्यंजन"
                    ),
                    description = Translation(
                        en = "A signature Rajasthani dish consisting of hard wheat rolls (Baati), spiced lentil curry (Dal), and sweet crumbled wheat (Churma) with pure ghee.",
                        hi = "एक सिग्नेचर राजस्थानी व्यंजन जिसमें कठोर गेहूं के रोल्स (बाटी), मसालेदार दाल की कढ़ी (दाल) और शुद्ध घी के साथ मीठा पिसा हुआ गेहूं (चूरमा) शामिल है।"
                    ),
                    origin = Translation(
                        en = "Traditional Mewari Kitchens",
                        hi = "पारंपरिक मेवाड़ी रसोई"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/dal-batti-churma-new.jpg",
                    minPriceInr = 150.0,
                    maxPriceInr = 300.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Gulab Jamun",
                        hi = "गुलाब जामुन"
                    ),
                    category = Translation(
                        en = "Dish",
                        hi = "व्यंजन"
                    ),
                    description = Translation(
                        en = "Deliciously soft milk-solid dumplings fried and soaked in rose-flavored sugar syrup, served warm.",
                        hi = "स्वादिष्ट रूप से नरम मावा के गोले जिन्हें तला जाता है और गुलाब के स्वाद वाले चीनी के सिरप में भिगोया जाता है, गर्म परोसा जाता है।"
                    ),
                    origin = Translation(
                        en = "Local Sweet Shops",
                        hi = "स्थानीय मिठाई की दुकानें"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/gulab-jamun-new.jpg",
                    minPriceInr = 50.0,
                    maxPriceInr = 150.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Gatte Ki Sabzi",
                        hi = "गट्टे की सब्जी"
                    ),
                    category = Translation(
                        en = "Dish",
                        hi = "व्यंजन"
                    ),
                    description = Translation(
                        en = "Gram flour dumplings cooked in a rich, tangy yogurt-based gravy with local Mewari spices.",
                        hi = "स्थानीय मेवाड़ी मसालों के साथ एक समृद्ध, खट्टी दही-आधारित ग्रेवी में पकाया गया बेसन का गट्टा।"
                    ),
                    origin = Translation(
                        en = "Home Kitchens",
                        hi = "घरेलू रसोई"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/gatte-ki-sabzi.jpg",
                    minPriceInr = 120.0,
                    maxPriceInr = 250.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Mirchi Bada",
                        hi = "मिर्ची बड़ा"
                    ),
                    category = Translation(
                        en = "Dish",
                        hi = "व्यंजन"
                    ),
                    description = Translation(
                        en = "Spicy potato-stuffed green chilies dipped in gram flour batter and deep fried to golden perfection. Best enjoyed with green chutney.",
                        hi = "मसालेदार आलू से भरी हरी मिर्च को बेसन के घोल में डुबोकर सुनहरा होने तक डीप फ्राई किया जाता है। हरी चटनी के साथ सबसे अच्छा आनंद लिया जाता है।"
                    ),
                    origin = Translation(
                        en = "Street Vendors",
                        hi = "सड़क किनारे विक्रेता"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/mirchi-bada.jpg",
                    minPriceInr = 20.0,
                    maxPriceInr = 50.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Somani Vatika",
                        hi = "सोमानी वाटिका"
                    ),
                    category = Translation(
                        en = "Restaurant",
                        hi = "भोजनालय"
                    ),
                    description = Translation(
                        en = "Highly famous for serving authentic, rustic Dal Baati Churma and Mewari thalis in a green garden setting.",
                        hi = "हरे-भरे बगीचे के वातावरण में प्रामाणिक, देहाती दाल बाटी चूरमा और मेवाड़ी थाली परोसने के लिए अत्यधिक प्रसिद्ध।"
                    ),
                    origin = Translation(
                        en = "Near Collectorate (2 km from station)",
                        hi = "कलेक्ट्रेट के पास (स्टेशन से 2 किमी)"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/dal-batti-churma-new.jpg",
                    minPriceInr = 250.0,
                    maxPriceInr = 600.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Agarsen Sweets",
                        hi = "अग्रसेन स्वीट्स"
                    ),
                    category = Translation(
                        en = "Restaurant",
                        hi = "भोजनालय"
                    ),
                    description = Translation(
                        en = "Famous for its melt-in-the-mouth hot Gulab Jamuns, and rich Rajasthani sweets and snacks.",
                        hi = "मुंह में पिघलने वाले गर्म गुलाब जामुन और समृद्ध राजस्थानी मिठाइयों और स्नैक्स के लिए प्रसिद्ध।"
                    ),
                    origin = Translation(
                        en = "Fort Road (3 km from station)",
                        hi = "किला रोड (स्टेशन से 3 किमी)"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/gulab-jamun-new.jpg",
                    minPriceInr = 100.0,
                    maxPriceInr = 400.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Manuhar Dining Hall",
                        hi = "मनुहार डाइनिंग हॉल"
                    ),
                    category = Translation(
                        en = "Restaurant",
                        hi = "भोजनालय"
                    ),
                    description = Translation(
                        en = "Serving unlimited traditional Rajasthani and Mewari Thali with pure, authentic local flavors.",
                        hi = "शुद्ध, प्रामाणिक स्थानीय स्वादों के साथ असीमित पारंपरिक राजस्थानी और मेवाड़ी थाली परोसना।"
                    ),
                    origin = Translation(
                        en = "Near Railway Station (0.5 km from station)",
                        hi = "रेलवे स्टेशन के पास (स्टेशन से 0.5 किमी)"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/dal-batti-churma-new.jpg",
                    minPriceInr = 300.0,
                    maxPriceInr = 500.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Akbari Restaurant",
                        hi = "अकबरी रेस्टोरेंट"
                    ),
                    category = Translation(
                        en = "Restaurant",
                        hi = "भोजनालय"
                    ),
                    description = Translation(
                        en = "Famous for authentic Mewari non-vegetarian dishes, especially spicy Laal Maas, and local snacks.",
                        hi = "प्रामाणिक मेवाड़ी गैर-शाकाहारी व्यंजनों, विशेष रूप से तीखे लाल मास और स्थानीय स्नैक्स के लिए प्रसिद्ध।"
                    ),
                    origin = Translation(
                        en = "City Center (5 km from station)",
                        hi = "सिटी सेंटर (स्टेशन से 5 किमी)"
                    ),
                    imageUrl = "https://www.visitchittorgarh.in/assets/images/mirchi-bada.jpg",
                    minPriceInr = 350.0,
                    maxPriceInr = 800.0
                ),
                LocalItem(
                    name = Translation(
                        en = "The Muds For Buds",
                        hi = "द मड्स फॉर बड्स"
                    ),
                    category = Translation(
                        en = "Cafe",
                        hi = "कैफे"
                    ),
                    description = Translation(
                        en = "Cozy rooftop hangout with spectacular views of the fort, serving excellent coffees, shakes, and quick bites.",
                        hi = "किले के शानदार दृश्यों के साथ आरामदायक छत पर हैंगआउट, उत्कृष्ट कॉफी, शेक और त्वरित भोजन परोसता है।"
                    ),
                    origin = Translation(
                        en = "Fort Road (3.5 km from station)",
                        hi = "किला रोड (स्टेशन से 3.5 किमी)"
                    ),
                    imageUrl = R.drawable.muds_for_buds_cafe,
                    minPriceInr = 150.0,
                    maxPriceInr = 500.0
                ),
                LocalItem(
                    name = Translation(
                        en = "The Rawal Kothi",
                        hi = "द रावल कोठी"
                    ),
                    category = Translation(
                        en = "Cafe",
                        hi = "कैफे"
                    ),
                    description = Translation(
                        en = "Savor premium beverages and mocktails in a beautifully restored heritage environment overlooking the city.",
                        hi = "शहर के दृश्य वाले खूबसूरती से पुनर्निर्मित विरासत वातावरण में प्रीमियम पेय पदार्थों और मॉकटेल का स्वाद लें।"
                    ),
                    origin = Translation(
                        en = "Near City View Point (6 km from station)",
                        hi = "सिटी व्यू पॉइंट के पास (स्टेशन से 6 किमी)"
                    ),
                    imageUrl = R.drawable.rawal_kothi_cafe,
                    minPriceInr = 200.0,
                    maxPriceInr = 600.0
                ),
                LocalItem(
                    name = Translation(
                        en = "Zero Degree",
                        hi = "जीरो डिग्री"
                    ),
                    category = Translation(
                        en = "Cafe",
                        hi = "कैफे"
                    ),
                    description = Translation(
                        en = "A lively modern cafe popular among the youth, serving thick shakes, mocktails, pizzas, and pastas.",
                        hi = "युवाओं के बीच लोकप्रिय एक जीवंत आधुनिक कैफे, जिसमें थिक शेक, मॉकटेल, पिज्जा और पास्ता परोसा जाता है।"
                    ),
                    origin = Translation(
                        en = "City Center (4 km from station)",
                        hi = "सिटी सेंटर (स्टेशन से 4 किमी)"
                    ),
                    imageUrl = R.drawable.zero_degree_cafe,
                    minPriceInr = 120.0,
                    maxPriceInr = 450.0
                )
            )
        )
    }
}
