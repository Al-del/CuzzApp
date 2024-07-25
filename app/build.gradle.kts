plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.relay") version "0.3.00"
    id("com.google.secrets_gradle_plugin")  version "0.4"


}
android {

    namespace = "com.example.cuzzapp"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.example.cuzzapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            abiFilters  += listOf("arm64-v8a", "x86_64")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        mlModelBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.baselibrary)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.room.common)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.androidx.room.ktx)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.code.gson:gson:2.8.8")
    implementation("org.osmdroid:osmdroid-android:6.1.10")
    implementation("com.google.accompanist:accompanist-permissions:0.20.3")

    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.compose.material:material:1.0.5")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")

    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation ("androidx.core:core:1.9.0")

    implementation("io.sanghun:compose-video:1.2.0")
    implementation("androidx.media3:media3-exoplayer:1.1.0") // [Required] androidx.media3 ExoPlayer dependency
    implementation("androidx.media3:media3-session:1.1.0") // [Required] MediaSession Extension dependency
    implementation("androidx.media3:media3-ui:1.1.0") // [Required] Base Player UI

    implementation("androidx.media3:media3-exoplayer-dash:1.1.0") // [Optional] If your media item is DASH
    implementation("androidx.media3:media3-exoplayer-hls:1.1.0") // [Optional] If your media item is HLS (m3u8..)

 //   implementation("com.chaquo.python:gradle:15.0.1")

    implementation( "com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.28")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation ("androidx.compose.material:material-icons-extended")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //retrofit-converter-moshi
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    //okhttp
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    //moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.15.1")

    implementation ("com.itextpdf:itext7-core:7.1.9")
    implementation("com.itextpdf:itextpdf:5.5.13.1")
    implementation("androidx.camera:camera-camera2:1.0.1")
    implementation("androidx.camera:camera-lifecycle:1.0.1")
    implementation("androidx.camera:camera-view:1.0.0-alpha27")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.8")
}

