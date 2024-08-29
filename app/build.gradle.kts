plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.korett.zulip_client"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.korett.zulip_client"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        val baseUrlName = "BASE_URL"
        val realBaseUrl = "\"https://some_url\""

        debug {
            versionNameSuffix = "-DEBUG"
            buildConfigField("String", baseUrlName, realBaseUrl)
        }

        release {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", baseUrlName, realBaseUrl)
        }

        create("debug-test") {
            initWith(getByName("debug"))
            isDebuggable = true

            val fakeBaseUrl = "\"http://localhost:8080/\""
            buildConfigField("String", baseUrlName, fakeBaseUrl)
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests {
            all { it.useJUnitPlatform() }
        }
        animationsDisabled = true
    }

    testBuildType = "debug-test"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle)

    implementation(libs.androidx.emoji2)
    implementation(libs.androidx.emoji2.views)
    implementation(libs.androidx.emoji2.views.helper)
    implementation(libs.androidx.emoji2.emojipicker)

    implementation(libs.facebook.shimmer)

    implementation(libs.bundles.kotlinx.coroutines)

    implementation(libs.bundles.bindingDelegate)

    implementation(libs.glide)

    implementation(libs.material)

    implementation(libs.terrakok.cicerone)

    implementation(libs.bundles.retrofit)

    implementation(libs.bundles.elmslie)

    implementation(libs.dagger)
    ksp(libs.daggerCompiler)

    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.roomCompiler)

    implementation(libs.androidx.test.core)

    implementation(libs.androidx.rules)
    implementation(libs.androidx.fragment.testing)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.ktx)

    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    androidTestImplementation(libs.hamcrest)

    androidTestImplementation(libs.kaspresso)

    androidTestImplementation(libs.androidx.espresso.intents)

    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
}