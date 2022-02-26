# Vanced Manager
<div>

[![Github All Releases](https://img.shields.io/github/downloads/YTVanced/VancedManager/total.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest) [![Github All Releases](https://img.shields.io/github/release/YTVanced/VancedManager.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest)

</div>

## Introduction

Hi, when we released Vanced 15.05.54, people were upset because it used the .apks format, which was burdensome to install than a traditional .apk file. Even though we wrote clear instructions on how to install the new Vanced build, people still couldn't figure it out.  

Then we thought, "why don't we make a manager for vanced, which will download, update and uninstall Vanced and MicroG, have an easy and user-friendly UI and be less than 10mb?" and that's how Vanced Manager was born.  
  
After 3 months of development, we are finally ready to Introduce [Vanced Manager](https://github.com/YTVanced/VancedManager) to the masses!!

## Features

- Vanced manager can easily install and uninstall Vanced and MicroG.
- It has various settings for customization and better experience. 
- The Manager comes with an easy-to-use Interface.  

</br>

<div class="note">
  <p><strong>NOTE: </strong>Background download/installation feature is no longer supported due to problems with some ROMs, please <b>DO NOT</b> report issues regarding background activity.</p>
</div>

<!-- ##### Background download/installation feature is no longer supported due to problems with some ROMs, please do NOT report issues regarding background activity. -->

## Contributions
Pull Requests should be made to the [Dev](https://github.com/YTVanced/VancedManager) Branch as that is the working branch, master is for Release code only.

For anyone who wants to provide translations please submit them to this [link](https://crowdin.com/project/vanced-manager) as we also use it for YouTube Vanced. Any issues with translations should be posted there too.

## TODO
- [ ] Clean up the ViewModel and DataModel code
- [ ] Migrate to Jetpack Compose when it's officially released

## Building

<div>

[![Build](https://github.com/YTVanced/VancedManager/actions/workflows/debug.yml/badge.svg?branch=dev)](https://github.com/YTVanced/VancedManager/actions/workflows/debug.yml)

</div>

## Using Android Studio
Clone the Repository, open it in Android Studio and build the application.

## Google Advanced Protection Program
If you are using this feature on your Google account, you must either disable it or log out from your Google account before installing Youtube Vanced via Vanced Manager.
The Google Advanced Protection Program does not allow the installation of apps from unknown sources. These security measures are tied to the protected account and not the device. After the installation, you will be able to log back in or enroll again into the program.

## Using Command Line
#### On Windows:
```powershell
.\gradlew.bat assembleDebug
```
#### On Linux/macOS:
```bash
chmod +x gradlew
./gradlew assembleDebug
```

