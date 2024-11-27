plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.crm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.crm"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation("com.balysv:material-ripple:1.0.2")
    implementation("androidx.security:security-crypto:1.1.0-alpha03") // or the latest stable version
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
//    implementation("com.google.android.libraries.places:places:2.8.0")
    implementation("com.google.android.libraries.places:places:3.2.0")



//    implementation("com.google.firebase:firebase-crashlytics")
}