plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.beautystore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.beautystore"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")

    //Firebase Messaging
    implementation("com.google.firebase:firebase-messaging:23.3.1")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //momo
    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("org.greenrobot:eventbus:3.3.1")
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")
    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
}