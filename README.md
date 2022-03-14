# Vanced Manager
[![Github All Releases](https://img.shields.io/github/downloads/YTVanced/VancedManager/total.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest) [![Github All Releases](https://img.shields.io/github/release/YTVanced/VancedManager.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest)

# **Now discontinued https://twitter.com/YTVanced/status/1503052250268286980**

Hi, when we released Vanced 15.05.54, people were upset because it used the .apks format, which was way harder to install than a traditional .apk file. Even though we wrote clear instructions on how to install the new Vanced build, people still couldn't figure it out.  
Then we thought, "why don't we make a manager for vanced, which will download, update and uninstall Vanced and MicroG, have an easy and understandable UI and be less than 10mb?" and that's how Vanced Manager was born.  
  
After 3 months of development, we are finally ready to introduce Vanced Manager to the masses. Vanced manager can easily install and uninstall vanced and microg, has various settings for customisation and better experience. The Manager comes with an easy-to-use interface  

##### Background download/installation feature is no longer supported due to problems with some ROMs, please do NOT report issues regarding background activity.

## Contributions
Pull requests should be made to the Dev branch as that is the working branch, master is for release code.

For anyone who wants to provide translations please submit them to https://crowdin.com/project/vanced-manager as we also use it for Vanced. Any issues with translations should be posted there too.

## Building 

<div>

[![Build](https://github.com/YTVanced/VancedManager/actions/workflows/debug.yml/badge.svg?branch=dev)](https://github.com/YTVanced/VancedManager/actions/workflows/debug.yml)

</div>

### Using Android Studio
Clone the repo, open it in Android Studio and build the app.

### Using command line
#### On Windows:
```powershell
.\gradlew.bat assembleDebug
```
#### On Linux/macOS:
```bash
chmod +x gradlew
./gradlew assembleDebug
```
