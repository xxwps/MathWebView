plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mathwebview"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.mathwebview"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

configurations.all {
    resolutionStrategy {
        force(libs.guava)
    }
    exclude(group = "com.google.guava", module = "listenablefuture")
}

dependencies {
    // AndroidX Core
    implementation(libs.bundles.androidx.core)
    
    // Lifecycle
    implementation(libs.bundles.lifecycle)
    
    // Material Design
    implementation(libs.google.material)

    // AndroidMath - 纯 Kotlin LaTeX 渲染库 (无需 WebView)
    implementation(libs.androidmath)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.ui.tooling)

    // latex-renderer - Compose 跨平台 LaTeX 渲染库
    implementation(libs.latex.renderer)
}
