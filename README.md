<div align=middle>

<a align="center" href="https://x-network.io/xcash"><img src="header.png" alt="X-Cash Core"></a>

[![Release](https://img.shields.io/github/v/release/X-CASH-official/android-wallet)](https://github.com/X-CASH-official/android-wallet/releases)
[![chat](https://img.shields.io/discord/470575102203920395?logo=discord)](https://discordapp.com/invite/4CAahnd)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg?style=flat)](https://opensource.org/licenses/MIT)

</div>

# X-Cash Android Mobile Wallet

📱 **Securely store and manage your XCASH on your mobile.**  
Easily end XCASH publicly or privately, manage your wallets, etc... from your phone!

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Download](#download)
- [Features](#features)
- [License](#license)
- [Contributing](#contributing)
- [Documentation](#documentation)
- [Security](#security)
- [Acknowledgement](#acknowledgement)
- [System Requirements](#system-requirements)
- [Installing from source](#installing-from-source)
  - [Dependencies](#dependencies)
  - [Develop](#develop)

## Download

Check the [latest release](https://github.com/X-CASH-official/android-wallet/releases)

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

> *Cloned from [m2049r](https://github.com/m2049r)'s [xmrwallet](https://github.com/m2049r/xmrwallet).*

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

`Android 4.2+` (API 17+）

#### Native

`arm64-v8a`,`armeabi-v7a`

## Installing from source

### Dependencies

[Android Studio](https://developer.android.com/studio) 

### Develop

#### Clone repository

```bash
git clone https://github.com/X-CASH-official/android-wallet.git
```

Install JDK
`sudo apt install openjdk-8-jdk`

Install android studio
`sudo snap install android-studio --classic`

#### Open with Android Studio

Open project in Android Studio

install the NDK
```
File -> Settings
Type "SDK" in the search box
click on the "SDK Tools" tab
Check the box next to NDK (side by side)
Then click on apply

when it is done, resync gradle by pressing the elephant icon at the top right

```

Build APK
Create a key file to sign your APK

```
Open a android studio terminal (should have a tab on the bottom) and run this command
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
Fill it out and then this is your key file
```

Create a signed APK
```
Build -> Gnerate signed key file or APK
Select APK and click next
Select your keyfile and type in the password. For key alias use "my-alias" and click next
Select release and check the box for V1 and V2 signature versions and click finish
The apk will be located in 
```
