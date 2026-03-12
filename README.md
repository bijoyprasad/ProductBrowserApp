# ProductBrowser

A Kotlin Multiplatform (KMP) app that lets users browse, search, and inspect products fetched from the [DummyJSON Products API](https://dummyjson.com/products). The UI is built entirely with Compose Multiplatform and runs natively on both **Android** and **iOS** from a single shared codebase.

---

## Business Requirements

| # | Requirement |
|---|-------------|
| 1 | Display a paginated / scrollable list of products fetched from the remote API. |
| 2 | Allow users to search for products by keyword (delegated to the API's search endpoint). |
| 3 | Allow users to filter products by category using chip selectors. |
| 4 | Tapping a product opens a detail screen showing full information: images, price (with discount), rating, stock status, tags, product specs, and user reviews. |
| 5 | Show meaningful loading, empty, and error states throughout the app. |
| 6 | Support both Android and iOS from a single shared codebase. |

---

## Architecture Overview

The project follows **Clean Architecture** with three distinct layers:

```
composeApp/
└── src/commonMain/kotlin/com/bijoy/productbrowser/
    ├── data/               # Data layer
    │   ├── api/            # Ktor HTTP client & endpoint definitions
    │   ├── di/             # Koin dependency injection modules
    │   ├── mapper/         # DTO → Domain model mappers
    │   ├── model/          # Serializable DTO data classes
    │   └── repository/     # ProductRepositoryImpl
    ├── domain/             # Domain layer (pure Kotlin, no framework deps)
    │   ├── model/          # Product, Review, Dimensions domain models
    │   ├── repository/     # ProductRepository interface
    │   └── usecase/        # GetProductsUseCase, SearchProductsUseCase, GetSingleProductUseCase
    └── presentation/       # Presentation layer
        ├── productlist/    # ProductListScreen + ProductListViewModel
        ├── productdetails/ # ProductDetailScreen + ProductDetailViewModel
        ├── ui/             # Shared UI components (LoadingView, ErrorView, EmptyView)
        └── util/           # Extension functions (e.g. Double.toPrice())
```

### Layer responsibilities

**Data layer** — owns all network I/O. `ProductRepositoryImpl` uses a Ktor `HttpClient` to call the DummyJSON API and maps `ProductDto` responses to domain `Product` models via `ProductMapper`.

**Domain layer** — framework-free business logic. The `ProductRepository` interface decouples the domain from any specific data source. Use cases (`GetProductsUseCase`, `SearchProductsUseCase`, `GetSingleProductUseCase`) act as single-responsibility entry points for the presentation layer.

**Presentation layer** — Compose Multiplatform screens backed by `ViewModel`s that expose `StateFlow<UiState>`. The ViewModels call use cases and translate results (via the `Resource<T>` sealed class) into immutable UI state objects.

### Key technology choices

| Concern | Library |
|---------|---------|
| Shared UI | Compose Multiplatform (JetBrains) |
| Networking | Ktor Client (`ktor-client-core`, platform engines) |
| JSON parsing | `kotlinx.serialization` |
| DI | Koin (`koin-compose-viewmodel`) |
| Image loading | Coil 3 (`coil-compose`) |
| State management | `StateFlow` + `ViewModel` (KMP-compatible) |

---

## Building & Running

### Prerequisites

- **JDK 17+**
- **Android Studio Hedgehog (2023.1.1) or newer** with the *Kotlin Multiplatform* and *Compose Multiplatform* plugins installed
- **Xcode 15+** (macOS only, required for iOS)
- **CocoaPods** installed (`sudo gem install cocoapods`)

### Clone the repository

```bash
git clone https://github.com/your-username/ProductBrowser.git
cd ProductBrowser
```

### Android

1. Open the project in Android Studio.
2. Wait for Gradle sync to finish.
3. Select the `composeApp` run configuration and choose an Android emulator or physical device.
4. Click **Run ▶**.

Alternatively, from the terminal:

```bash
./gradlew :composeApp:assembleDebug
# Install on a connected device / running emulator
./gradlew :composeApp:installDebug
```

### iOS

> macOS with Xcode is required for iOS builds.

1. Install CocoaPods dependencies (first time only):

```bash
cd iosApp
pod install
cd ..
```

2. Open `iosApp/iosApp.xcworkspace` in Xcode (**not** the `.xcodeproj`).
3. Select a simulator or a provisioned physical device from the scheme selector.
4. Press **⌘R** to build and run.

Alternatively, using the Gradle wrapper to build the shared framework and then launch via `xcodebuild`:

```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64   # or IosX64 / IosArm64
```

---

## API Reference

The app consumes the free [DummyJSON](https://dummyjson.com) Products API — no API key required.

| Use case | Endpoint |
|----------|----------|
| List products | `GET /products?limit={n}&skip={n}` |
| Product detail | `GET /products/{id}` |
| Search | `GET /products/search?q={query}` |

---

## Trade-offs & Assumptions

**Pagination not fully implemented** — The `ProductRepository` interface supports `limit` and `skip` parameters and the API comment notes `limit=0` returns all items. The current `ProductListViewModel` fetches a single page of 30 items on launch. Infinite-scroll / paging was deliberately deferred to keep the scope focused.

**Search is API-delegated** — Keyword search is forwarded directly to the `/products/search` endpoint rather than filtering a locally cached list. This keeps the implementation simple but means an active network connection is required for every search query.

**Category filtering is client-side** — Available categories are derived from the currently loaded product list. Filtering by category is done in memory inside the ViewModel. This avoids an extra API call but means categories reflect only the loaded page, not the entire catalogue.

**No offline / caching layer** — There is no local database (e.g. Room / SQLDelight). Data is re-fetched on every cold start. Adding a caching layer would be the natural next step for production readiness.

**Koin used over manual DI** — The brief allows manual DI, but Koin was chosen because it integrates cleanly with `koinViewModel()` in Compose Multiplatform and requires minimal boilerplate for a project of this size.

**iOS navigation** — Navigation is handled by the shared `AppNavigation` composable using `androidx.navigation` for Compose Multiplatform, which provides a consistent experience across both platforms without platform-specific nav code.