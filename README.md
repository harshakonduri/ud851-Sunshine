# Vedic Vidya - Hindu Knowledge & Wisdom App

**Production-Ready Android Bootstrap for Vedic Scripture Knowledge Sharing**

A scalable mobile application that enables authentic question-answering from Vedic scriptures using advanced RAG (Retrieval-Augmented Generation) architecture with AI agents.

## 🎯 Project Overview

Vedic Vidya is a mobile app designed to make Hindu philosophical and spiritual knowledge accessible through:
- **Intelligent Q&A System**: Ask questions and get answers sourced from authentic Vedic texts with citations
- **Sacred Text Library**: Browse and read verses from Bhagavad Gita, Ramayana, Mahabharata, Upanishads, and more
- **AI Agent**: Hybrid architecture combining on-device vector search with cloud LLM synthesis
- **Scalable Design**: Built to handle 100,000+ verses efficiently on mobile devices

## 🏗️ Architecture

### **Hierarchical Multi-Tier RAG System**

The app uses a sophisticated three-tier architecture to handle large scriptural corpora efficiently:

```
┌─────────────────────────────────────────────────┐
│  TIER 1: Summary Layer (~1MB, always on-device) │
│  - Book/chapter summaries with embeddings       │
│  - Fast coarse-grained search                   │
│  - Identifies relevant texts and sections       │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│  TIER 2: Verse Index Layer (Hybrid storage)    │
│  - Popular verses on device (~200MB)            │
│  - Full corpus in cloud (~2GB)                  │
│  - Smart caching with LRU eviction              │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│  TIER 3: LLM Synthesis Layer (Cloud API)       │
│  - Claude / Gemini / GPT API integration        │
│  - Synthesizes answers from retrieved verses   │
│  - Maintains authenticity through citations     │
└─────────────────────────────────────────────────┘
```

### **Key Features**

✅ **Vector Semantic Search**: Find relevant verses using cosine similarity on embeddings
✅ **Query Intelligence**: Analyzes questions to determine search scope and strategy
✅ **Smart Caching**: LRU cache with priority-based eviction for offline support
✅ **Quality Tiers**: Adapts responses based on translation availability
✅ **Multiple Philosophical Schools**: Shows different interpretations (Advaita, Vishishtadvaita, etc.)
✅ **Offline-First**: Cached responses and on-device search
✅ **Scalable**: Designed for complete Vedic corpus (600,000+ verses)

## 📱 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material Design 3
- **Architecture**: MVVM + Repository Pattern
- **Database**: Room (SQLite) with vector storage
- **DI**: Hilt (Dagger)
- **Async**: Kotlin Coroutines + Flow
- **Networking**: Retrofit + OkHttp (for cloud sync)
- **AI Integration**: Ready for Claude/Gemini/GPT APIs

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- Gradle 8.2+

### Build & Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ud851-Sunshine
   ```

2. **Open in Android Studio**
   - File → Open → Select project folder
   - Wait for Gradle sync

3. **Build and run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

   Or use Android Studio's Run button (▶️)

### App Size

- **Initial APK**: ~15MB
- **On-device data**: ~60MB (Bhagavad Gita + summaries)
- **Full corpus** (optional download): 300-500MB

## 🧠 Agent System

### Current Implementation: Mock Agent

The bootstrap includes a fully functional **MockVedicAgent** that:
- Demonstrates the complete agent workflow
- Returns realistic demo responses
- Shows source citations
- Works 100% offline

### Enabling Real AI Agent

To activate the cloud-based AI agent:

1. **Get an API key** from:
   - [Anthropic Claude](https://console.anthropic.com/) (Recommended)
   - [Google Gemini](https://ai.google.dev/)
   - [OpenAI GPT](https://platform.openai.com/)

2. **Update `AppModule.kt`**:
   ```kotlin
   @Provides
   @Singleton
   fun provideVedicAgent(
       database: VedicDatabase,
       searchEngine: VectorSearchEngine,
       embeddingGenerator: EmbeddingGenerator,
       queryAnalyzer: QueryAnalyzer
   ): VedicAgent {
       // Replace with:
       return CloudVedicAgent(
           database,
           searchEngine,
           embeddingGenerator,
           queryAnalyzer,
           apiKey = "your-api-key-here"
       )
   }
   ```

3. **Implement API call in `CloudVedicAgent.callLLMAPI()`**:
   ```kotlin
   private suspend fun callLLMAPI(
       question: String,
       context: String,
       queryType: String
   ): String {
       val client = OkHttpClient()
       val json = buildJsonPayload(question, context)

       val request = Request.Builder()
           .url("https://api.anthropic.com/v1/messages")
           .addHeader("x-api-key", apiKey!!)
           .addHeader("anthropic-version", "2023-06-01")
           .addHeader("content-type", "application/json")
           .post(json.toRequestBody())
           .build()

       val response = client.newCall(request).execute()
       return parseClaudeResponse(response.body?.string())
   }
   ```

## 📊 Data Structure

### Verse Model

```kotlin
data class VedicVerse(
    val id: String,                    // "BG_2_47"
    val textName: String,              // "Bhagavad Gita"
    val chapter: Int,                  // 2
    val verse: Int,                    // 47
    val sanskrit: String,              // Devanagari
    val transliteration: String,       // Roman script
    val translation: String,           // English
    val commentary: String?,           // Optional commentary
    val embedding: String,             // Vector embedding (CSV)
    val topics: List<String>,          // ["dharma", "karma"]
    val concepts: List<String>,        // ["Nishkama Karma"]
    val qualityTier: QualityTier,     // HIGH, MEDIUM, LOW
    val storageTier: StorageTier       // HOT, WARM, COLD
)
```

### Adding New Texts

1. **Prepare data** in JSON format:
   ```json
   {
     "verses": [
       {
         "id": "BG_2_47",
         "textName": "Bhagavad Gita",
         "chapter": 2,
         "verse": 47,
         "sanskrit": "कर्मण्येवाधिकारस्ते...",
         "transliteration": "karmany evadhikaras te...",
         "translation": "You have a right to perform...",
         "topics": ["karma", "dharma"],
         "concepts": ["Nishkama Karma"]
       }
     ]
   }
   ```

2. **Generate embeddings** using OpenAI/Cohere/Voyage API

3. **Import to database** using Room migrations

## 🎨 UI Screens

- **Home**: Daily wisdom, quick access, categories
- **Library**: Browse sacred texts, search verses
- **Q&A Chat**: Ask questions, get cited answers
- **Profile**: Settings, downloads, about

## 🔧 Configuration

### Storage Limits

Edit `AppModule.kt`:

```kotlin
@Provides
@Singleton
fun provideSmartCacheManager(): SmartCacheManager {
    return SmartCacheManager(maxSizeMB = 200) // Adjust as needed
}
```

### Vector Dimensions

For production, use higher-quality embeddings:

```kotlin
// In EmbeddingGenerator
CloudEmbeddingGenerator(
    apiKey = apiKey,
    dimension = 384  // or 768, 1024, 1536
)
```

## 📈 Scaling to Large Corpora

### Corpus Size Estimates

| Text | Verses | On-Device | Cloud |
|------|--------|-----------|-------|
| Bhagavad Gita | 700 | ✅ 5MB | N/A |
| Ramayana | 24,000 | 📦 Summaries | ☁️ 70MB |
| Mahabharata | 100,000 | 📦 Summaries | ☁️ 300MB |
| All 18 Puranas | 400,000 | 📦 Summaries | ☁️ 1.2GB |
| **Complete Corpus** | **600,000+** | **60MB** | **2-3GB** |

### Performance Targets

- **Summary search**: 10-20ms
- **On-device verse search**: 50-100ms
- **Cloud verse retrieval**: 300-500ms
- **Full LLM response**: 1-3 seconds

## 🛣️ Roadmap

### Phase 1: Bootstrap ✅ (Current)
- [x] Hierarchical database architecture
- [x] Vector search engine
- [x] Mock agent with demo responses
- [x] Beautiful Jetpack Compose UI
- [x] Cloud agent infrastructure (ready for API)

### Phase 2: Production Data
- [ ] Load complete Bhagavad Gita (700 verses)
- [ ] Add Ramayana summaries + key episodes
- [ ] Add Mahabharata summaries + key episodes
- [ ] Add 10 major Upanishads
- [ ] Generate embeddings for all content

### Phase 3: Cloud Integration
- [ ] Claude API integration
- [ ] Embedding generation service
- [ ] Cloud database sync
- [ ] Batch processing pipeline

### Phase 4: Advanced Features
- [ ] On-device LLM option (Gemini Nano)
- [ ] Audio pronunciations
- [ ] Multi-language support (Hindi, Telugu, Tamil)
- [ ] Offline PDF export
- [ ] Community Q&A verification

## 🤝 Contributing

We welcome contributions! Areas of focus:
- **Content**: Adding more texts with authentic translations
- **Translations**: Multi-language support
- **Commentaries**: Traditional school interpretations
- **Audio**: Sanskrit pronunciation recordings
- **Testing**: Unit and integration tests

## 📚 Data Sources

Authenticated sources used:
- **Gita Supersite** (gitasupersite.iitk.ac.in)
- **Vedabase** (vedabase.io)
- **Internet Sacred Text Archive**
- **Monier-Williams Sanskrit Dictionary**

## 📄 License

This project is provided as an educational and devotional tool. Please respect copyright of translated texts and commentaries.

## 🙏 Acknowledgments

Built with reverence for the ancient wisdom of:
- Veda Vyasa
- Valmiki
- Adi Shankaracharya
- Ramanuja
- Madhvacharya
- And all the great teachers of Sanatana Dharma

---

**Om Tat Sat** 🕉️

For questions or support, please open an issue on GitHub.
