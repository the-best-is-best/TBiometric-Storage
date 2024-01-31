<h1 align="center">T Biometric Storage</h1><br>

<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=21" rel="nofollow"><img alt="API" src="https://camo.githubusercontent.com/0eda703da08220e08354f624a3fc0023f10416a302565c69c3759bf6e0800d40/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4150492d32312532422d627269676874677265656e2e7376673f7374796c653d666c6174" data-canonical-src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" style="max-width: 100%;"></a>
<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a>
<a href="https://central.sonatype.com/search?q=io.github.the-best-is-best&smo=true"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/io.github.the-best-is-best/TBiometricStorage"/></a>
</div>

TBiometric Storage is a library for Android that provides a secure storage with biometric authentication.

## Download

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/TBiometricStorage)](https://central.sonatype.com/artifact/io.github.the-best-is-best/TBiometricStorage)

T Biometric Storage is available on `mavenCentral()`.

### Note

- You can use crypt and decrypt functions with your own key without biometric authentication for secure data if you don't need to ask for fingerprint.
```kotlin
implementation("io.github.the-best-is-best:TBiometricStorage:1.0.1")
```

## Setup Compose

### 1. Add the dependency

```kotlin
    implementation("androidx.fragment:fragment-ktx:1.6.2")
```

### 2. Change ComposeActivity to FragmentActivity

```kotlin
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ...
        }
    }
}
```

### How to use

```kotlin
   LaunchedEffect(Dispatchers.Main) {
    val support = TBiometricStorage.checkIsSupported(context)
    if (support) {
        val auth = TBiometricStorage.authenticate(activity, context)
        if (auth) {
            TBiometricStorage.storeData(context, "key", "value")
        } else {
            Log.d("get v", "auth failed")
        }
    } else {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d("get v", "not active biometric")

        }else {
            Log.d("get v", "not support")
        }
    }
}
```