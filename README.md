# Vanced Manager
<div>

[![Github All Releases](https://img.shields.io/github/downloads/YTVanced/VancedManager/total.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest) [![Github All Releases](https://img.shields.io/github/release/YTVanced/VancedManager.svg?style=for-the-badge)](https://github.com/YTVanced/VancedManager/releases/latest)

</div>

Hi, when we released Vanced 15.05.54, people were upset because it used the .apks format, which was way harder to install than a traditional .apk file. Even though we wrote clear instructions on how to install the new Vanced build, people still couldn't figure it out.  
Then we thought, "why don't we make a manager for vanced, which will download, update and uninstall Vanced and MicroG, have an easy and understandable UI and be less than 10mb?" and that's how Vanced Manager was born.  
  
After 3 months of development, we are finally ready to introduce Vanced Manager to the masses. Vanced manager can easily install and uninstall vanced and microg, has various settings for customisation and better experience. The Manager comes with an easy-to-use interface  

##### Background download/installation feature is no longer supported due to problems with some ROMs, please do NOT report issues regarding background activity.

## Contributions
Pull requests should be made to the Dev branch as that is the working branch, master is for release code.

For anyone who wants to provide translations please submit them to https://crowdin.com/project/vanced-manager as we also use it for YouTube Vanced. Any issues with translations should be posted there too.

## TODO
- [ ] Clean up the ViewModel and DataModel code
- [ ] Migrate to Jetpack Compose when it's officially released

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

## Vanced FAQ
Vanced FAQ (from the faq branch) now available on the playstore!

<a href='https://play.google.com/store/apps/details?id=com.vanced.faq&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' height="85" src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>
