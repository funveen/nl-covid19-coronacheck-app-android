# COVID-19 CoronaCheck Prototype - Android (Fork)

**I have set up this repository to help people who don't have access to the Google Play Store on their Android phones, but who would like to use the Dutch CoronaCheck app. Any deviation from the upstream repository or guidance given below comes without warranty. Use at your own risk!**

The app might be distributed on other channels (e.g. F-Droid) in the future. There is an issue discussing this in upstream minvws/nl-covid19-coronacheck-app-android#24

## Introduction
This repository contains a **fork** of the Android prototype of the Dutch COVID-19 CoronaCheck project. 

* The original/upstream Android app is located here: https://github.com/minvws/nl-covid19-coronacheck-app-android
* The iOS app can be found here: https://github.com/minvws/nl-covid19-coronacheck-app-ios

The project is currently an experimental prototype to explore technical possibilities.

## Build process

The following process worked for me to deploy the app on my Android phone without using Play Store.

1. Create a keystore for self-signing the app for productive use:
   `keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias YourAlias`
2. Create file *keystore.properties* with the following content:
   ```
   storePassword=YourPassword
   keyPassword=YourPassword
   keyAlias=YourAlias
   storeFile=../keystore.jks
   ```
3. Use provided gradle wrapper to build the different flavors of the apk:
   `./gradlew assemTstRelease assemAccRelease assemProdRelease bundleProdRelease`
4. Install the prod apk via *adb*:
   `adb install holder/build/outputs/apk/prod/release holder-v2.0.3-1000000-prod-release.apk`

A good starting point for building apks on the command line can be found here: https://developer.android.com/studio/build/building-cmdline

