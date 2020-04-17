<div align=middle>

<a align="center" href="https://x-network.io/xcash"><img src="header.png" alt="X-Cash Core"></a>



[![Release](https://img.shields.io/github/v/release/X-CASH-official/android-wallet?include_prereleases)](https://github.com/X-CASH-official/android-wallet/releases)
[![open issues](https://img.shields.io/github/issues-raw/X-CASH-official/android-wallet)](https://github.com/X-CASH-official/android-wallet/issues)
[![License: Apache-2.0](https://img.shields.io/github/license/X-CASH-official/android-wallet)](https://github.com/X-CASH-official-team/android-wallet/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/470575102203920395?logo=discord)](https://discordapp.com/invite/4CAahnd)

</div>

# X-Cash Android Mobile Wallet

📱 **Securely store and manage your XCASH on your mobile.**  
Easily send XCASH publicly or privately, manage your wallets, etc... from your phone!

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Download & Installation](#download--installation)
- [Features](#features)
- [License](#license)
- [Contributing](#contributing)
- [Documentation](#documentation)
- [Security](#security)
- [Acknowledgement](#acknowledgement)
- [System Requirements](#system-requirements)
- [Installing from source](#installing-from-source)
  - [Cloning the repository](#cloning-the-repository)
  - [Dependencies](#dependencies)
  - [Build Instructions](#build-instructions)

## Download & Installation

> ⚠️ Only download from the [official release page](https://github.com/X-CASH-official/android-wallet/releases).

Download the [latest release](https://github.com/X-CASH-official/android-wallet/releases), transfer the `.apk` file to your phone, and run it. You will need to force your phone to install the app as it is not recognized and distributed through the app store yet.

## Features

<div align=middle>
<img height=500px src="https://x-network.io/front/images/android-wallet.gif" alt="X-Cash Android Wallet">
</div>

**Multi-wallet Dashboard**  
From the dashboard, you can import, check, and manage all your XCASH's wallets.

**Address book**  
Manage your contacts and add wallet addresses from recurrent transfers.

**Check your payments on the go**  
See your payment history from anywhere, directly on your phone.

## License

> *Originally cloned from [m2049r](https://github.com/m2049r)'s [xmrwallet](https://github.com/m2049r/xmrwallet).*

**X-Cash GUI Wallet is an open-source project managed by the X-Cash Foundation**.  
Licensed under the Apache License, Version 2.0. View [License](LICENSE)

## Contributing

**Thank you for thinking of contributing! 😃**  
If you want to help out, check [CONTRIBUTING](https://github.com/X-CASH-official/.github/blob/master/CONTRIBUTING.md) for a set of guidelines and check our [opened issues](https://github.com/X-CASH-official/desktop-wallet/issues).

## Documentation

We are hosting our documentation on **GitBook** 👉 [**docs.xcash.foundation**](https://docs.xcash.foundation/)

> You can contribute directly on our [`gitbook-docs`](https://github.com/X-CASH-official/gitbook-docs) repository.

## Security

If you discover a security vulnerability, please send an e-mail to [security@xcash.foundation](mailto:security@xcash.foundation).  
All security vulnerabilities concerning the X-Cash blockchain will be promply addressed.

## Acknowledgement

🙏 A special thanks to [@snakewaypasser](https://github.com/snakewaypasser) for providing the first release of the wallet. Thanks to his tremendous work, we are now able to store XCASH directly on Android phones !

## System Requirements

#### Mobile

`Android 5.0+` (API 21+）

## Installing from source

### Cloning the repository

```bash
git clone https://github.com/X-CASH-official/android-wallet.git
```

### Dependencies

#### Libraries

**Note:** The libraries mandatory the `.apk` already built. If you wish to update them or rebuild, run the build script at: 

```bash
android-wallet/xcash-libs/build.sh
```

> The build script will install the build tools, the android NDK and build all of the nessary libraries. 

#### JDK

```bash
sudo apt install -y openjdk-8-jdk
```

#### Android Studio

You can download Android Studio here: https://developer.android.com/studio) or run: 
```bash
sudo snap install android-studio --classic
```

#### NDK

Open `Android Studio` and verify that the Native Development Kit (NDK) is installed.

Go to `Files > Settings` and type `SDK`in the search box, then click on the `SDK Tools`tab.

If the NDK is not installed, you can check the box and click on `Apply`.

Once done, re-sync `gradle` (elephant icon on the top right)


### Build Instructions

#### Sign your `.apk` (optional)

Open a terminal window in `android-studio` and run:
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

Replace `my-alias` by your name.

#### Build the APK

Go to `Build > Generate signed key file or APK`, select `APK` and click `Next`. Give your keyfile and select `release. Check box for V1 and V2 signature and click finish.

The app will be built in :
```android-wallet/app/release/app-release.apk```
