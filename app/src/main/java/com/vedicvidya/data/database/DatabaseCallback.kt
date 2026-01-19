package com.vedicvidya.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vedicvidya.data.model.QualityTier
import com.vedicvidya.data.model.StorageTier
import com.vedicvidya.data.model.VedicVerse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Callback to populate database with sample data on first creation
 */
class DatabaseCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Populate database on first creation
        // This runs in a background thread
    }

    /**
     * Populate database with sample Bhagavad Gita verses
     */
    fun populateDatabase(database: VedicDatabase) {
        scope.launch(Dispatchers.IO) {
            val verseDao = database.vedicVerseDao()
            val textSummaryDao = database.textSummaryDao()

            // Insert sample Bhagavad Gita verses
            verseDao.insertVerses(getSampleBhagavadGitaVerses())

            // Insert text summaries
            textSummaryDao.insertSummaries(getSampleTextSummaries())
        }
    }

    private fun getSampleBhagavadGitaVerses(): List<VedicVerse> {
        // Sample popular verses from Bhagavad Gita
        // In production, these would come from a JSON file or API

        val sampleEmbedding = FloatArray(128) { 0.1f } // Mock embedding
        val embeddingString = sampleEmbedding.joinToString(",")

        return listOf(
            // BG 2.47 - Most famous verse
            VedicVerse(
                id = "BG_2_47",
                textName = "Bhagavad Gita",
                textId = "BG",
                chapter = 2,
                verse = 47,
                section = "Sankhya Yoga",
                sanskrit = "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन। मा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि॥",
                transliteration = "karmaṇy-evādhikāras te mā phaleṣhu kadāchana\nmā karma-phala-hetur bhūr mā te saṅgo 'stvakarmaṇi",
                translation = "You have a right to perform your prescribed duty, but you are not entitled to the fruits of action. Never consider yourself to be the cause of the results of your activities, nor be attached to inaction.",
                wordByWordMeaning = "karmaṇi—in prescribed duties; eva—certainly; adhikāraḥ—right; te—your; mā—never; phaleṣhu—in the fruits; kadāchana—at any time",
                commentary = "This is the essence of Karma Yoga. Lord Krishna teaches that we should focus on performing our duty without attachment to results. This verse is the foundation of selfless action.",
                additionalTranslations = null,
                embedding = embeddingString,
                embeddingDimension = 128,
                topics = listOf("karma", "duty", "action", "detachment"),
                concepts = listOf("Nishkama Karma", "Karma Yoga"),
                relatedVerses = listOf("BG_3_19", "BG_3_30", "BG_18_66"),
                qualityTier = QualityTier.HIGH,
                accessCount = 100,
                lastAccessedAt = System.currentTimeMillis(),
                isFavorite = false,
                storageTier = StorageTier.HOT
            ),

            // BG 2.20 - Nature of the soul
            VedicVerse(
                id = "BG_2_20",
                textName = "Bhagavad Gita",
                textId = "BG",
                chapter = 2,
                verse = 20,
                section = "Sankhya Yoga",
                sanskrit = "न जायते म्रियते वा कदाचित् नायं भूत्वा भविता वा न भूयः। अजो नित्यः शाश्वतोऽयं पुराणो न हन्यते हन्यमाने शरीरे॥",
                transliteration = "na jāyate mriyate vā kadācit\nnāyaṁ bhūtvā bhavitā vā na bhūyaḥ\najo nityaḥ śāśvato 'yaṁ purāṇo\nna hanyate hanyamāne śarīre",
                translation = "The soul is never born and never dies. Having come into being once, it never ceases to be. It is unborn, eternal, ever-existing, undying and primeval. It is not slain when the body is slain.",
                wordByWordMeaning = "na—never; jāyate—is born; mriyate—dies; vā—or; kadācit—at any time; na—never; ayam—this; bhūtvā—having been; bhavitā—will be",
                commentary = "This verse explains the eternal nature of the soul. Understanding this fundamental truth is essential for spiritual wisdom and overcoming grief.",
                additionalTranslations = null,
                embedding = embeddingString,
                embeddingDimension = 128,
                topics = listOf("soul", "atman", "immortality", "death"),
                concepts = listOf("Atman", "Immortality of Soul"),
                relatedVerses = listOf("BG_2_22", "BG_2_23", "BG_2_24"),
                qualityTier = QualityTier.HIGH,
                accessCount = 80,
                lastAccessedAt = System.currentTimeMillis(),
                isFavorite = false,
                storageTier = StorageTier.HOT
            ),

            // BG 18.66 - Surrender to God
            VedicVerse(
                id = "BG_18_66",
                textName = "Bhagavad Gita",
                textId = "BG",
                chapter = 18,
                verse = 66,
                section = "Moksha Yoga",
                sanskrit = "सर्वधर्मान्परित्यज्य मामेकं शरणं व्रज। अहं त्वा सर्वपापेभ्यो मोक्षयिष्यामि मा शुचः॥",
                transliteration = "sarva-dharmān parityajya\nmām ekaṁ śaraṇaṁ vraja\nahaṁ tvāṁ sarva-pāpebhyo\nmokṣhayiṣhyāmi mā śhuchaḥ",
                translation = "Abandon all varieties of dharmas and simply surrender unto Me alone. I shall liberate you from all sinful reactions; do not fear.",
                wordByWordMeaning = "sarva-dharmān—all varieties of dharmas; parityajya—abandoning; mām—unto Me; ekam—only; śaraṇam—for refuge; vraja—go",
                commentary = "This is the final and most important teaching of the Bhagavad Gita. Complete surrender to the Supreme Lord is the ultimate path to liberation.",
                additionalTranslations = null,
                embedding = embeddingString,
                embeddingDimension = 128,
                topics = listOf("surrender", "bhakti", "liberation", "moksha"),
                concepts = listOf("Sharanagati", "Bhakti Yoga", "Moksha"),
                relatedVerses = listOf("BG_9_34", "BG_12_6", "BG_12_8"),
                qualityTier = QualityTier.HIGH,
                accessCount = 90,
                lastAccessedAt = System.currentTimeMillis(),
                isFavorite = false,
                storageTier = StorageTier.HOT
            ),

            // BG 2.62-63 - Progression of desire to destruction
            VedicVerse(
                id = "BG_2_62",
                textName = "Bhagavad Gita",
                textId = "BG",
                chapter = 2,
                verse = 62,
                section = "Sankhya Yoga",
                sanskrit = "ध्यायतो विषयान्पुंसः सङ्गस्तेषूपजायते। सङ्गात्सञ्जायते कामः कामात्क्रोधोऽभिजायते॥",
                transliteration = "dhyāyato viṣhayān puṁsaḥ\nsaṅgas teṣhūpajāyate\nsaṅgāt sañjāyate kāmaḥ\nkāmāt krodho 'bhijāyate",
                translation = "While contemplating the objects of the senses, a person develops attachment for them. From attachment, desire is born. From desire, anger arises.",
                wordByWordMeaning = "dhyāyataḥ—while contemplating; viṣhayān—sense objects; puṁsaḥ—of a person; saṅgaḥ—attachment; teṣhu—to them; upajāyate—arises",
                commentary = "This verse describes the chain reaction that leads from attachment to anger and ultimately to delusion. It warns against dwelling on sense objects.",
                additionalTranslations = null,
                embedding = embeddingString,
                embeddingDimension = 128,
                topics = listOf("desire", "anger", "attachment", "mind control"),
                concepts = listOf("Kama", "Krodha", "Sense Control"),
                relatedVerses = listOf("BG_2_63", "BG_3_37"),
                qualityTier = QualityTier.HIGH,
                accessCount = 50,
                lastAccessedAt = System.currentTimeMillis(),
                isFavorite = false,
                storageTier = StorageTier.WARM
            ),

            // BG 4.7 - Avatar
            VedicVerse(
                id = "BG_4_7",
                textName = "Bhagavad Gita",
                textId = "BG",
                chapter = 4,
                verse = 7,
                section = "Jnana Karma Sanyasa Yoga",
                sanskrit = "यदा यदा हि धर्मस्य ग्लानिर्भवति भारत। अभ्युत्थानमधर्मस्य तदात्मानं सृजाम्यहम्॥",
                transliteration = "yadā yadā hi dharmasya\nglānir bhavati bhārata\nabhyutthānam adharmasya\ntadātmānaṁ sṛijāmyaham",
                translation = "Whenever there is a decline of righteousness and a rise of unrighteousness, O descendant of Bharata, at that time I manifest Myself.",
                wordByWordMeaning = "yadā yadā—whenever; hi—certainly; dharmasya—of righteousness; glāniḥ—decline; bhavati—occurs; bhārata—O Bharata",
                commentary = "This famous verse explains the doctrine of Avatar - that God descends to Earth whenever dharma declines to restore righteousness.",
                additionalTranslations = null,
                embedding = embeddingString,
                embeddingDimension = 128,
                topics = listOf("avatar", "dharma", "incarnation", "divine"),
                concepts = listOf("Avatar", "Divine Incarnation", "Dharma Restoration"),
                relatedVerses = listOf("BG_4_8"),
                qualityTier = QualityTier.HIGH,
                accessCount = 70,
                lastAccessedAt = System.currentTimeMillis(),
                isFavorite = false,
                storageTier = StorageTier.HOT
            )
        )
    }

    private fun getSampleTextSummaries(): List<com.vedicvidya.data.model.TextSummary> {
        return listOf(
            com.vedicvidya.data.model.TextSummary(
                id = "BG_SUMMARY",
                textId = "BG",
                textName = "Bhagavad Gita",
                category = "Smriti",
                language = "Sanskrit",
                chapterCount = 18,
                verseCount = 700,
                summary = "The Bhagavad Gita is a 700-verse Hindu scripture that is part of the epic Mahabharata. It is a conversation between Prince Arjuna and Lord Krishna on the battlefield of Kurukshetra. The Gita addresses the moral and philosophical dilemmas faced by Arjuna and provides profound spiritual wisdom on dharma, karma, bhakti, and jnana.",
                topics = listOf("dharma", "karma", "bhakti", "jnana", "yoga"),
                embedding = FloatArray(128) { 0.1f }.joinToString(","),
                embeddingDimension = 128
            ),
            com.vedicvidya.data.model.TextSummary(
                id = "RAMAYANA_SUMMARY",
                textId = "RAMAYANA",
                textName = "Valmiki Ramayana",
                category = "Itihasa",
                language = "Sanskrit",
                chapterCount = 7,
                verseCount = 24000,
                summary = "The Ramayana is an ancient Indian epic that narrates the life of Prince Rama, his wife Sita, and his devoted companion Hanuman. It teaches about dharma, devotion, loyalty, and the triumph of good over evil through the story of Rama's exile and battle against the demon king Ravana.",
                topics = listOf("dharma", "devotion", "loyalty", "righteousness", "bhakti"),
                embedding = FloatArray(128) { 0.1f }.joinToString(","),
                embeddingDimension = 128
            ),
            com.vedicvidya.data.model.TextSummary(
                id = "MAHABHARATA_SUMMARY",
                textId = "MAHABHARATA",
                textName = "Mahabharata",
                category = "Itihasa",
                language = "Sanskrit",
                chapterCount = 18,
                verseCount = 100000,
                summary = "The Mahabharata is the longest epic poem in the world, containing over 100,000 verses. It narrates the story of the Kurukshetra War between the Pandavas and Kauravas, along with numerous philosophical and devotional teachings. The Bhagavad Gita is a part of this epic.",
                topics = listOf("dharma", "war", "family", "duty", "righteousness"),
                embedding = FloatArray(128) { 0.1f }.joinToString(","),
                embeddingDimension = 128
            )
        )
    }
}
