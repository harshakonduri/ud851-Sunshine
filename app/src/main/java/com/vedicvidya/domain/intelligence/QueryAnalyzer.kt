package com.vedicvidya.domain.intelligence

/**
 * Analyzes user queries to determine intent, entities, and search scope
 * This enables smart routing and efficient search across large corpus
 */
class QueryAnalyzer {

    /**
     * Analyze a user query
     */
    fun analyze(query: String): QueryAnalysis {
        val lowercaseQuery = query.lowercase()

        val type = detectQueryType(lowercaseQuery)
        val entities = extractEntities(lowercaseQuery)
        val concepts = extractConcepts(lowercaseQuery)
        val likelyTexts = identifyTexts(entities, concepts, lowercaseQuery)
        val scope = determineScope(type, entities, likelyTexts)

        return QueryAnalysis(
            originalQuery = query,
            type = type,
            entities = entities,
            concepts = concepts,
            likelyTexts = likelyTexts,
            scope = scope
        )
    }

    private fun detectQueryType(query: String): QueryType {
        return when {
            query.contains(Regex("what happened|tell me about|story of|episode")) ->
                QueryType.NARRATIVE

            query.contains(Regex("what is|meaning of|explain|define")) ->
                QueryType.PHILOSOPHICAL

            query.contains(Regex("how to|steps|perform|ritual|practice")) ->
                QueryType.PRACTICAL

            query.contains(Regex("difference|compare|versus|vs")) ->
                QueryType.COMPARATIVE

            query.contains(Regex("find|verse|shloka|quote|reference")) ->
                QueryType.REFERENCE

            else -> QueryType.GENERAL
        }
    }

    private fun extractEntities(query: String): List<Entity> {
        val entities = mutableListOf<Entity>()

        // Character/Deity entities
        ENTITY_DICTIONARY.forEach { (name, info) ->
            if (query.contains(name.lowercase())) {
                entities.add(Entity(name, EntityType.DEITY, info.texts))
            }
        }

        // Place entities
        PLACE_DICTIONARY.forEach { (name, info) ->
            if (query.contains(name.lowercase())) {
                entities.add(Entity(name, EntityType.PLACE, info.texts))
            }
        }

        return entities
    }

    private fun extractConcepts(query: String): List<String> {
        val concepts = mutableListOf<String>()

        CONCEPT_DICTIONARY.forEach { (concept, keywords) ->
            if (keywords.any { query.contains(it.lowercase()) }) {
                concepts.add(concept)
            }
        }

        return concepts
    }

    private fun identifyTexts(
        entities: List<Entity>,
        concepts: List<String>,
        query: String
    ): List<TextIdentification> {
        val textScores = mutableMapOf<String, Float>()

        // Score based on entities
        entities.forEach { entity ->
            entity.relatedTexts.forEach { textId ->
                textScores[textId] = textScores.getOrDefault(textId, 0f) + 0.5f
            }
        }

        // Direct text mentions
        TEXT_PATTERNS.forEach { (textId, patterns) ->
            patterns.forEach { pattern ->
                if (query.contains(pattern.lowercase())) {
                    textScores[textId] = textScores.getOrDefault(textId, 0f) + 1.0f
                }
            }
        }

        // Concept-based text hints
        concepts.forEach { concept ->
            CONCEPT_TEXT_MAPPING[concept]?.forEach { textId ->
                textScores[textId] = textScores.getOrDefault(textId, 0f) + 0.2f
            }
        }

        return textScores
            .map { (textId, score) ->
                TextIdentification(
                    textId = textId,
                    textName = TEXT_ID_TO_NAME[textId] ?: textId,
                    confidence = score.coerceIn(0f, 1f)
                )
            }
            .sortedByDescending { it.confidence }
    }

    private fun determineScope(
        type: QueryType,
        entities: List<Entity>,
        likelyTexts: List<TextIdentification>
    ): SearchScope {
        return when {
            // Very specific query with clear entities and text
            entities.isNotEmpty() && likelyTexts.firstOrNull()?.confidence ?: 0f > 0.8f ->
                SearchScope.NARROW

            // Some specificity
            likelyTexts.isNotEmpty() || entities.isNotEmpty() ->
                SearchScope.MEDIUM

            // Broad philosophical concepts
            type == QueryType.PHILOSOPHICAL ->
                SearchScope.BROAD

            else ->
                SearchScope.MEDIUM
        }
    }

    companion object {
        // Entity dictionary: Name -> Related texts
        private val ENTITY_DICTIONARY = mapOf(
            "Krishna" to EntityInfo(listOf("BG", "MBH", "BHAGAVATA")),
            "Arjuna" to EntityInfo(listOf("BG", "MBH")),
            "Rama" to EntityInfo(listOf("RAM", "MBH")),
            "Sita" to EntityInfo(listOf("RAM")),
            "Hanuman" to EntityInfo(listOf("RAM", "MBH")),
            "Ravana" to EntityInfo(listOf("RAM")),
            "Shiva" to EntityInfo(listOf("SHIVA_PURANA", "MBH")),
            "Vishnu" to EntityInfo(listOf("VISHNU_PURANA", "BHAGAVATA")),
            "Ganesha" to EntityInfo(listOf("GANESHA_PURANA")),
            "Lakshmi" to EntityInfo(listOf("LAKSHMI_PURANA")),
            "Durga" to EntityInfo(listOf("DEVI_BHAGAVATA"))
        )

        private val PLACE_DICTIONARY = mapOf(
            "Kurukshetra" to EntityInfo(listOf("BG", "MBH")),
            "Ayodhya" to EntityInfo(listOf("RAM")),
            "Lanka" to EntityInfo(listOf("RAM")),
            "Vrindavan" to EntityInfo(listOf("BHAGAVATA"))
        )

        // Concept dictionary: Concept -> Keywords
        private val CONCEPT_DICTIONARY = mapOf(
            "Dharma" to listOf("dharma", "duty", "righteousness"),
            "Karma" to listOf("karma", "action", "deed"),
            "Bhakti" to listOf("bhakti", "devotion", "love"),
            "Moksha" to listOf("moksha", "liberation", "freedom"),
            "Yoga" to listOf("yoga", "union", "discipline"),
            "Atman" to listOf("atman", "soul", "self"),
            "Brahman" to listOf("brahman", "absolute", "ultimate reality")
        )

        // Concept to text mapping
        private val CONCEPT_TEXT_MAPPING = mapOf(
            "Dharma" to listOf("BG", "MBH", "RAM"),
            "Karma" to listOf("BG"),
            "Bhakti" to listOf("BG", "BHAGAVATA"),
            "Moksha" to listOf("BG", "UPANISHADS"),
            "Yoga" to listOf("BG", "YOGA_SUTRAS")
        )

        // Text patterns for direct identification
        private val TEXT_PATTERNS = mapOf(
            "BG" to listOf("bhagavad gita", "gita", "geeta"),
            "RAM" to listOf("ramayana", "valmiki"),
            "MBH" to listOf("mahabharata", "mahabharat"),
            "UPANISHADS" to listOf("upanishad", "upanishads"),
            "VEDAS" to listOf("veda", "vedas", "rigveda", "yajurveda"),
            "BHAGAVATA" to listOf("bhagavata", "bhagavatam", "srimad bhagavatam")
        )

        private val TEXT_ID_TO_NAME = mapOf(
            "BG" to "Bhagavad Gita",
            "RAM" to "Ramayana",
            "MBH" to "Mahabharata",
            "UPANISHADS" to "Upanishads",
            "VEDAS" to "Vedas",
            "BHAGAVATA" to "Srimad Bhagavatam"
        )
    }
}

data class QueryAnalysis(
    val originalQuery: String,
    val type: QueryType,
    val entities: List<Entity>,
    val concepts: List<String>,
    val likelyTexts: List<TextIdentification>,
    val scope: SearchScope
)

data class Entity(
    val name: String,
    val type: EntityType,
    val relatedTexts: List<String>
)

data class EntityInfo(
    val texts: List<String>
)

data class TextIdentification(
    val textId: String,
    val textName: String,
    val confidence: Float
)

enum class QueryType {
    NARRATIVE,       // Story/event questions
    PHILOSOPHICAL,   // Conceptual questions
    PRACTICAL,       // How-to questions
    COMPARATIVE,     // Comparison questions
    REFERENCE,       // Find specific verse
    GENERAL          // Other
}

enum class EntityType {
    DEITY,
    CHARACTER,
    PLACE,
    CONCEPT
}

enum class SearchScope {
    NARROW,    // Search 100-1000 verses in specific section
    MEDIUM,    // Search 1000-5000 verses in specific text
    BROAD      // Search across multiple texts
}
