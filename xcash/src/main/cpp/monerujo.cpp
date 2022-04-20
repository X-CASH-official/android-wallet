/*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 * Copyright (c) 2017 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <inttypes.h>
#include "monerujo.h"
#include "wallet2_api.h"

//TODO explicit casting jlong, jint, jboolean to avoid warnings

#ifdef __cplusplus
extern "C"
{
#endif

#include <android/log.h>
#define LOG_TAG "WalletNDK"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG,__VA_ARGS__)

static JavaVM *cachedJVM;
static jclass class_ArrayList;
static jclass class_WalletListener;
static jclass class_TransactionInfo;
static jclass class_Transfer;
static jclass class_Ledger;
static jclass class_SubaddressRow;

std::mutex _listenerMutex;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    cachedJVM = jvm;
    LOGI("JNI_OnLoad");
    JNIEnv *jenv;
    if (jvm->GetEnv(reinterpret_cast<void **>(&jenv), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    //LOGI("JNI_OnLoad ok");

    class_ArrayList = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("java/util/ArrayList")));
    class_TransactionInfo = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("com/my/monero/model/TransactionInfo")));
    class_Transfer = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("com/my/monero/model/Transfer")));
    class_WalletListener = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("com/my/monero/model/WalletListener")));
    class_Ledger = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("com/my/monero/ledger/Ledger")));
    class_SubaddressRow = static_cast<jclass>(jenv->NewGlobalRef(
            jenv->FindClass("com/my/monero/model/SubaddressRow")));
    return JNI_VERSION_1_6;
}
#ifdef __cplusplus
}
#endif

int attachJVM(JNIEnv **jenv) {
    int envStat = cachedJVM->GetEnv((void **) jenv, JNI_VERSION_1_6);
    if (envStat == JNI_EDETACHED) {
        if (cachedJVM->AttachCurrentThread(jenv, nullptr) != 0) {
            LOGE("Failed to attach");
            return JNI_ERR;
        }
    } else if (envStat == JNI_EVERSION) {
        LOGE("GetEnv: version not supported");
        return JNI_ERR;
    }
    //LOGI("envStat=%i", envStat);
    return envStat;
}

void detachJVM(JNIEnv *jenv, int envStat) {
    //LOGI("envStat=%i", envStat);
    if (jenv->ExceptionCheck()) {
        jenv->ExceptionDescribe();
    }

    if (envStat == JNI_EDETACHED) {
        cachedJVM->DetachCurrentThread();
    }
}

struct MyWalletListener : XCash::WalletListener {
    jobject jlistener;

    MyWalletListener(JNIEnv *env, jobject aListener) {
        LOGD("Created MyListener");
        jlistener = env->NewGlobalRef(aListener);;
    }

    ~MyWalletListener() {
        LOGD("Destroyed MyListener");
    };

    void deleteGlobalJavaRef(JNIEnv *env) {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        env->DeleteGlobalRef(jlistener);
        jlistener = nullptr;
    }

    /**
 * @brief updated  - generic callback, called when any event (sent/received/block reveived/etc) happened with the wallet;
 */
    void updated() {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        LOGD("updated");
        JNIEnv *jenv;
        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jmethodID listenerClass_updated = jenv->GetMethodID(class_WalletListener, "updated", "()V");
        jenv->CallVoidMethod(jlistener, listenerClass_updated);

        detachJVM(jenv, envStat);
    }


    /**
     * @brief moneySpent - called when money spent
     * @param txId       - transaction id
     * @param amount     - amount
     */
    void moneySpent(const std::string &txId, uint64_t amount) {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        LOGD("moneySpent %"
                     PRIu64, amount);
        JNIEnv *jenv;
        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jstring _txId = jenv->NewStringUTF(txId.c_str());
        jlong h = static_cast<jlong>(amount);
        jmethodID listenerClass_moneySpent = jenv->GetMethodID(class_WalletListener, "moneySpent",
                                                             "(Ljava/lang/String;J)V");
        jenv->CallVoidMethod(jlistener, listenerClass_moneySpent,_txId, h);
        jenv->DeleteLocalRef(_txId);

        detachJVM(jenv, envStat);
    }

    /**
     * @brief moneyReceived - called when money received
     * @param txId          - transaction id
     * @param amount        - amount
     */
    void moneyReceived(const std::string &txId, uint64_t amount) {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        LOGD("moneyReceived %"
                     PRIu64, amount);
        JNIEnv *jenv;
        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jstring _txId = jenv->NewStringUTF(txId.c_str());
        jlong h = static_cast<jlong>(amount);
        jmethodID listenerClass_moneyReceived = jenv->GetMethodID(class_WalletListener, "moneyReceived",
                                                               "(Ljava/lang/String;J)V");
        jenv->CallVoidMethod(jlistener, listenerClass_moneyReceived,_txId, h);
        jenv->DeleteLocalRef(_txId);

        detachJVM(jenv, envStat);
    }

    /**
     * @brief unconfirmedMoneyReceived - called when payment arrived in tx pool
     * @param txId          - transaction id
     * @param amount        - amount
     */
    void unconfirmedMoneyReceived(const std::string &txId, uint64_t amount) {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        LOGD("unconfirmedMoneyReceived %"
                     PRIu64, amount);
        JNIEnv *jenv;
        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jstring _txId = jenv->NewStringUTF(txId.c_str());
        jlong h = static_cast<jlong>(amount);
        jmethodID listenerClass_unconfirmedMoneyReceived = jenv->GetMethodID(class_WalletListener, "unconfirmedMoneyReceived",
                                                                  "(Ljava/lang/String;J)V");
        jenv->CallVoidMethod(jlistener, listenerClass_unconfirmedMoneyReceived,_txId, h);
        jenv->DeleteLocalRef(_txId);

        detachJVM(jenv, envStat);
    }

    /**
     * @brief newBlock      - called when new block received
     * @param height        - block height
     */
    void newBlock(uint64_t height) {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        //LOGD("newBlock");
        JNIEnv *jenv;
        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jlong h = static_cast<jlong>(height);
        jmethodID listenerClass_newBlock = jenv->GetMethodID(class_WalletListener, "newBlock",
                                                             "(J)V");
        jenv->CallVoidMethod(jlistener, listenerClass_newBlock, h);

        detachJVM(jenv, envStat);
    }

/**
 * @brief refreshed - called when wallet refreshed by background thread or explicitly refreshed by calling "refresh" synchronously
 */
    void refreshed() {
        std::lock_guard<std::mutex> lock(_listenerMutex);
        if (jlistener == nullptr) return;
        LOGD("refreshed");
        JNIEnv *jenv;

        int envStat = attachJVM(&jenv);
        if (envStat == JNI_ERR) return;

        jmethodID listenerClass_refreshed = jenv->GetMethodID(class_WalletListener, "refreshed",
                                                              "()V");
        jenv->CallVoidMethod(jlistener, listenerClass_refreshed);
        detachJVM(jenv, envStat);
    }
};


//// helper methods
std::vector<std::string> java2cpp(JNIEnv *env, jobject arrayList) {

    jmethodID java_util_ArrayList_size = env->GetMethodID(class_ArrayList, "size", "()I");
    jmethodID java_util_ArrayList_get = env->GetMethodID(class_ArrayList, "get",
                                                         "(I)Ljava/lang/Object;");

    jint len = env->CallIntMethod(arrayList, java_util_ArrayList_size);
    std::vector<std::string> result;
    result.reserve(len);
    for (jint i = 0; i < len; i++) {
        jstring element = static_cast<jstring>(env->CallObjectMethod(arrayList,
                                                                     java_util_ArrayList_get, i));
        const char *pchars = env->GetStringUTFChars(element, NULL);
        result.emplace_back(pchars);
        env->ReleaseStringUTFChars(element, pchars);
        env->DeleteLocalRef(element);
    }
    return result;
}

jobject cpp2java(JNIEnv *env, std::vector<std::string> vector) {

    jmethodID java_util_ArrayList_ = env->GetMethodID(class_ArrayList, "<init>", "(I)V");
    jmethodID java_util_ArrayList_add = env->GetMethodID(class_ArrayList, "add",
                                                         "(Ljava/lang/Object;)Z");

    jobject result = env->NewObject(class_ArrayList, java_util_ArrayList_, vector.size());
    for (std::string &s: vector) {
        jstring element = env->NewStringUTF(s.c_str());
        env->CallBooleanMethod(result, java_util_ArrayList_add, element);
        env->DeleteLocalRef(element);
    }
    return result;
}

/// end helpers

#ifdef __cplusplus
extern "C"
{
#endif


/**********************************/
/********** WalletManager *********/
/**********************************/
JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_createWalletJ(JNIEnv *env, jobject instance,
                                                     jstring path, jstring password,
                                                     jstring language,
                                                     jint networkType) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    const char *_language = env->GetStringUTFChars(language, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);

    XCash::Wallet *wallet =
            XCash::WalletManagerFactory::getWalletManager()->createWallet(
                    std::string(_path),
                    std::string(_password),
                    std::string(_language),
                    _networkType);

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(language, _language);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_openWalletJ(JNIEnv *env, jobject instance,
                                                   jstring path, jstring password,
                                                   jint networkType) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);

    XCash::Wallet *wallet =
            XCash::WalletManagerFactory::getWalletManager()->openWallet(
                    std::string(_path),
                    std::string(_password),
                    _networkType);

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_recoveryWalletJ(JNIEnv *env, jobject instance,
                                                       jstring path, jstring password,
                                                       jstring mnemonic,
                                                       jint networkType,
                                                       jlong restoreHeight) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    const char *_mnemonic = env->GetStringUTFChars(mnemonic, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);

    XCash::Wallet *wallet =
            XCash::WalletManagerFactory::getWalletManager()->recoveryWallet(
                    std::string(_path),
                    std::string(_password),
                    std::string(_mnemonic),
                    _networkType,
                    (uint64_t) restoreHeight);

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(mnemonic, _mnemonic);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_createWalletFromKeysJ(JNIEnv *env, jobject instance,
                                                             jstring path, jstring password,
                                                             jstring language,
                                                             jint networkType,
                                                             jlong restoreHeight,
                                                             jstring addressString,
                                                             jstring viewKeyString,
                                                             jstring spendKeyString) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    const char *_language = env->GetStringUTFChars(language, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);
    const char *_addressString = env->GetStringUTFChars(addressString, NULL);
    const char *_viewKeyString = env->GetStringUTFChars(viewKeyString, NULL);
    const char *_spendKeyString = env->GetStringUTFChars(spendKeyString, NULL);

    XCash::Wallet *wallet =
            XCash::WalletManagerFactory::getWalletManager()->createWalletFromKeys(
                    std::string(_path),
                    std::string(_password),
                    std::string(_language),
                    _networkType,
                    (uint64_t) restoreHeight,
                    std::string(_addressString),
                    std::string(_viewKeyString),
                    std::string(_spendKeyString));

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(language, _language);
    env->ReleaseStringUTFChars(addressString, _addressString);
    env->ReleaseStringUTFChars(viewKeyString, _viewKeyString);
    env->ReleaseStringUTFChars(spendKeyString, _spendKeyString);
    return reinterpret_cast<jlong>(wallet);
}


// virtual void setSubaddressLookahead(uint32_t major, uint32_t minor) = 0;

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_createWalletFromDeviceJ(JNIEnv *env, jobject instance,
                                                               jstring path,
                                                               jstring password,
                                                               jint networkType,
                                                               jstring deviceName,
                                                               jlong restoreHeight,
                                                               jstring subaddressLookahead) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);
    const char *_deviceName = env->GetStringUTFChars(deviceName, NULL);
    const char *_subaddressLookahead = env->GetStringUTFChars(subaddressLookahead, NULL);

    XCash::Wallet *wallet =
            XCash::WalletManagerFactory::getWalletManager()->createWalletFromDevice(
                    std::string(_path),
                    std::string(_password),
                    _networkType,
                    std::string(_deviceName),
                    (uint64_t) restoreHeight,
                    std::string(_subaddressLookahead));

    env->ReleaseStringUTFChars(path, _path);
    env->ReleaseStringUTFChars(password, _password);
    env->ReleaseStringUTFChars(deviceName, _deviceName);
    env->ReleaseStringUTFChars(subaddressLookahead, _subaddressLookahead);
    return reinterpret_cast<jlong>(wallet);
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_WalletManager_walletExists(JNIEnv *env, jobject instance,
                                                    jstring path) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    bool exists =
            XCash::WalletManagerFactory::getWalletManager()->walletExists(std::string(_path));
    env->ReleaseStringUTFChars(path, _path);
    return static_cast<jboolean>(exists);
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_WalletManager_verifyWalletPassword(JNIEnv *env, jobject instance,
                                                            jstring keys_file_name,
                                                            jstring password,
                                                            jboolean watch_only) {
    const char *_keys_file_name = env->GetStringUTFChars(keys_file_name, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    bool passwordOk =
            XCash::WalletManagerFactory::getWalletManager()->verifyWalletPassword(
                    std::string(_keys_file_name), std::string(_password), watch_only);
    env->ReleaseStringUTFChars(keys_file_name, _keys_file_name);
    env->ReleaseStringUTFChars(password, _password);
    return static_cast<jboolean>(passwordOk);
}

//virtual int queryWalletHardware(const std::string &keys_file_name, const std::string &password) const = 0;
JNIEXPORT jint JNICALL
Java_com_my_monero_model_WalletManager_queryWalletDeviceJ(JNIEnv *env, jobject instance,
                                                          jstring keys_file_name,
                                                          jstring password) {
    const char *_keys_file_name = env->GetStringUTFChars(keys_file_name, NULL);
    const char *_password = env->GetStringUTFChars(password, NULL);
    XCash::Wallet::Device device_type;
    bool ok = XCash::WalletManagerFactory::getWalletManager()->
            queryWalletDevice(device_type, std::string(_keys_file_name), std::string(_password));
    env->ReleaseStringUTFChars(keys_file_name, _keys_file_name);
    env->ReleaseStringUTFChars(password, _password);
    if (ok)
        return static_cast<jint>(device_type);
    else
        return -1;
}

JNIEXPORT jobject JNICALL
Java_com_my_monero_model_WalletManager_findWallets(JNIEnv *env, jobject instance,
                                                   jstring path) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    std::vector<std::string> walletPaths =
            XCash::WalletManagerFactory::getWalletManager()->findWallets(std::string(_path));
    env->ReleaseStringUTFChars(path, _path);
    return cpp2java(env, walletPaths);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_WalletManager_getErrorString(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->errorString().c_str());
}

//TODO virtual bool checkPayment(const std::string &address, const std::string &txid, const std::string &txkey, const std::string &daemon_address, uint64_t &received, uint64_t &height, std::string &error) const = 0;

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_setDaemonAddressJ(JNIEnv *env, jobject instance,
                                                         jstring address) {
    const char *_address = env->GetStringUTFChars(address, NULL);
    XCash::WalletManagerFactory::getWalletManager()->setDaemonAddress(std::string(_address));
    env->ReleaseStringUTFChars(address, _address);
}

// returns whether the daemon can be reached, and its version number
JNIEXPORT jint JNICALL
Java_com_my_monero_model_WalletManager_getDaemonVersion(JNIEnv *env,
                                                        jobject instance) {
    uint32_t version;
    bool isConnected =
            XCash::WalletManagerFactory::getWalletManager()->connected(&version);
    if (!isConnected) version = 0;
    return version;
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_getBlockchainHeight(JNIEnv *env, jobject instance) {
    return XCash::WalletManagerFactory::getWalletManager()->blockchainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_getBlockchainTargetHeight(JNIEnv *env,
                                                                 jobject instance) {
    return XCash::WalletManagerFactory::getWalletManager()->blockchainTargetHeight();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_getNetworkDifficulty(JNIEnv *env, jobject instance) {
    return XCash::WalletManagerFactory::getWalletManager()->networkDifficulty();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_WalletManager_getBlockTarget(JNIEnv *env, jobject instance) {
    return XCash::WalletManagerFactory::getWalletManager()->blockTarget();
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_WalletManager_resolveOpenAlias(JNIEnv *env, jobject instance,
                                                        jstring address,
                                                        jboolean dnssec_valid) {
    const char *_address = env->GetStringUTFChars(address, NULL);
    bool _dnssec_valid = (bool) dnssec_valid;
    std::string resolvedAlias =
            XCash::WalletManagerFactory::getWalletManager()->resolveOpenAlias(
                    std::string(_address),
                    _dnssec_valid);
    env->ReleaseStringUTFChars(address, _address);
    return env->NewStringUTF(resolvedAlias.c_str());
}

//TODO static std::tuple<bool, std::string, std::string, std::string, std::string> checkUpdates(const std::string &software, const std::string &subdir);

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_WalletManager_closeJ(JNIEnv *env, jobject instance,
                                              jobject walletInstance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, walletInstance);
    bool closeSuccess = XCash::WalletManagerFactory::getWalletManager()->closeWallet(wallet,
                                                                                     false);
    if (closeSuccess) {
        MyWalletListener *walletListener = getHandle<MyWalletListener>(env, walletInstance,
                                                                       "listenerHandle");
        if (walletListener != nullptr) {
            walletListener->deleteGlobalJavaRef(env);
            delete walletListener;
        }
    }
    LOGD("wallet closed");
    return static_cast<jboolean>(closeSuccess);
}




/**********************************/
/************ Wallet **************/
/**********************************/

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getSeed(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->seed().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getSeedLanguage(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->getSeedLanguage().c_str());
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_setSeedLanguage(JNIEnv *env, jobject instance,
                                                jstring language) {
    const char *_language = env->GetStringUTFChars(language, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->setSeedLanguage(std::string(_language));
    env->ReleaseStringUTFChars(language, _language);
}

JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getStatusJ(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->status();
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getErrorString(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->errorString().c_str());
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_setPassword(JNIEnv *env, jobject instance,
                                            jstring password) {
    const char *_password = env->GetStringUTFChars(password, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    bool success = wallet->setPassword(std::string(_password));
    env->ReleaseStringUTFChars(password, _password);
    return static_cast<jboolean>(success);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getAddressJ(JNIEnv *env, jobject instance,
                                            jint accountIndex,
                                            jint addressIndex) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(
            wallet->address((uint32_t) accountIndex, (uint32_t) addressIndex).c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getPath(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->path().c_str());
}

JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_nettype(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->nettype();
}

//TODO virtual void hardForkInfo(uint8_t &version, uint64_t &earliest_height) const = 0;
//TODO virtual bool useForkRules(uint8_t version, int64_t early_blocks) const = 0;

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getIntegratedAddress(JNIEnv *env, jobject instance,
                                                     jstring payment_id) {
    const char *_payment_id = env->GetStringUTFChars(payment_id, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    std::string address = wallet->integratedAddress(_payment_id);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return env->NewStringUTF(address.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getSecretViewKey(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->secretViewKey().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getSecretSpendKey(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->secretSpendKey().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getPublicViewKey(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->publicViewKey().c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getPublicSpendKey(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->publicSpendKey().c_str());
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_store(JNIEnv *env, jobject instance,
                                      jstring path) {
    const char *_path = env->GetStringUTFChars(path, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    bool success = wallet->store(std::string(_path));
    if (!success) {
        LOGE("store() %s", wallet->errorString().c_str());
    }
    env->ReleaseStringUTFChars(path, _path);
    return static_cast<jboolean>(success);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getFilename(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return env->NewStringUTF(wallet->filename().c_str());
}

//    virtual std::string keysFilename() const = 0;

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_initJ(JNIEnv *env, jobject instance,
                                      jstring daemon_address,
                                      jlong upper_transaction_size_limit,
                                      jstring daemon_username, jstring daemon_password) {
    const char *_daemon_address = env->GetStringUTFChars(daemon_address, NULL);
    const char *_daemon_username = env->GetStringUTFChars(daemon_username, NULL);
    const char *_daemon_password = env->GetStringUTFChars(daemon_password, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    bool status = wallet->init(_daemon_address, (uint64_t) upper_transaction_size_limit,
                               _daemon_username,
                               _daemon_password);
    env->ReleaseStringUTFChars(daemon_address, _daemon_address);
    env->ReleaseStringUTFChars(daemon_username, _daemon_username);
    env->ReleaseStringUTFChars(daemon_password, _daemon_password);
    return static_cast<jboolean>(status);
}

//    virtual bool createWatchOnly(const std::string &path, const std::string &password, const std::string &language) const = 0;

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_setRestoreHeight(JNIEnv *env, jobject instance,
                                                 jlong height) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->setRefreshFromBlockHeight((uint64_t) height);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getRestoreHeight(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->getRefreshFromBlockHeight();
}

//    virtual void setRecoveringFromSeed(bool recoveringFromSeed) = 0;
//    virtual bool connectToDaemon() = 0;

JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getConnectionStatusJ(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->connected();
}
//TODO virtual void setTrustedDaemon(bool arg) = 0;
//TODO virtual bool trustedDaemon() const = 0;

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getBalance(JNIEnv *env, jobject instance,
                                           jint accountIndex) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->balance((uint32_t) accountIndex);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getBalanceAll(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->balanceAll();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getUnlockedBalance(JNIEnv *env, jobject instance,
                                                   jint accountIndex) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->unlockedBalance((uint32_t) accountIndex);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getUnlockedBalanceAll(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->unlockedBalanceAll();
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_isWatchOnly(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return static_cast<jboolean>(wallet->watchOnly());
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getBlockChainHeight(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->blockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getApproximateBlockChainHeight(JNIEnv *env,
                                                               jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->approximateBlockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getDaemonBlockChainHeight(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->daemonBlockChainHeight();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getDaemonBlockChainTargetHeight(JNIEnv *env,
                                                                jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->daemonBlockChainTargetHeight();
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_isSynchronized(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return static_cast<jboolean>(wallet->synchronized());
}

JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getDeviceTypeJ(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    XCash::Wallet::Device device_type = wallet->getDeviceType();
    return static_cast<jint>(device_type);
}


JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_lightWalletLogin(JNIEnv *env, jobject instance, jboolean isNewWallet) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    bool _isNewWallet = (bool) isNewWallet;
    return static_cast<jboolean>( wallet->lightWalletLogin(_isNewWallet));
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_delegateRegister(JNIEnv *env, jobject instance,
                                                  jstring  delegate_name,jstring  delegate_IP_address,jstring  block_verifier_messages_public_key) {

    const char *_delegate_name = env->GetStringUTFChars(delegate_name, NULL);
    const char *_delegate_IP_address = env->GetStringUTFChars(delegate_IP_address, NULL);
    const char *_block_verifier_messages_public_key = env->GetStringUTFChars(block_verifier_messages_public_key, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    std::string result = wallet->delegate_register(_delegate_name, _delegate_IP_address,_block_verifier_messages_public_key);
    env->ReleaseStringUTFChars(delegate_name, _delegate_name);
    env->ReleaseStringUTFChars(delegate_IP_address, _delegate_IP_address);
    env->ReleaseStringUTFChars(block_verifier_messages_public_key, _block_verifier_messages_public_key);
    return env->NewStringUTF(result.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_delegateUpdate(JNIEnv *env, jobject instance,
                                                 jstring  item,jstring  value) {

    const char *_item = env->GetStringUTFChars(item, NULL);
    const char *_value = env->GetStringUTFChars(value, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    std::string result = wallet->delegate_update(_item, _value);
    env->ReleaseStringUTFChars(item, _item);
    env->ReleaseStringUTFChars(value, _value);
    return env->NewStringUTF(result.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_vote(JNIEnv *env, jobject instance,jstring  value) {

    const char *_value = env->GetStringUTFChars(value, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    std::string result = wallet->vote(_value);
    env->ReleaseStringUTFChars(value, _value);
    return env->NewStringUTF(result.c_str());
}

//void cn_slow_hash(const void *data, size_t length, char *hash); // from crypto/hash-ops.h
//JNIEXPORT jbyteArray JNICALL
//Java_com_my_monero_util_KeyStoreHelper_slowHash(JNIEnv *env, jclass clazz,
//                                                       jbyteArray data, jint brokenVariant) {
//    char hash[HASH_SIZE];
//    jsize size = env->GetArrayLength(data);
//    if ((brokenVariant > 0) && (size < 200 /*sizeof(union hash_state)*/)) {
//        return nullptr;
//    }
//
//    jbyte *buffer = env->GetByteArrayElements(data, NULL);
//    switch (brokenVariant) {
//        case 1:
//            slow_hash_broken(buffer, hash, 1);
//            break;
//        case 2:
//            slow_hash_broken(buffer, hash, 0);
//            break;
//        default: // not broken
//            slow_hash(buffer, (size_t) size, hash);
//    }
//    env->ReleaseByteArrayElements(data, buffer, JNI_ABORT); // do not update java byte[]
//    jbyteArray result = env->NewByteArray(HASH_SIZE);
//    env->SetByteArrayRegion(result, 0, HASH_SIZE, (jbyte *) hash);
//    return result;
//}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getDisplayAmount(JNIEnv *env, jclass clazz,
                                                 jlong amount) {
    return env->NewStringUTF(XCash::Wallet::displayAmount(amount).c_str());
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getAmountFromString(JNIEnv *env, jclass clazz,
                                                    jstring amount) {
    const char *_amount = env->GetStringUTFChars(amount, NULL);
    uint64_t x = XCash::Wallet::amountFromString(_amount);
    env->ReleaseStringUTFChars(amount, _amount);
    return x;
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getAmountFromDouble(JNIEnv *env, jclass clazz,
                                                    jdouble amount) {
    return XCash::Wallet::amountFromDouble(amount);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_generatePaymentId(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(XCash::Wallet::genPaymentId().c_str());
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_isPaymentIdValid(JNIEnv *env, jclass clazz,
                                                 jstring payment_id) {
    const char *_payment_id = env->GetStringUTFChars(payment_id, NULL);
    bool isValid = XCash::Wallet::paymentIdValid(_payment_id);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return static_cast<jboolean>(isValid);
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_isAddressValid(JNIEnv *env, jclass clazz,
                                               jstring address, jint networkType) {
    const char *_address = env->GetStringUTFChars(address, NULL);
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);
    bool isValid = XCash::Wallet::addressValid(_address, _networkType);
    env->ReleaseStringUTFChars(address, _address);
    return static_cast<jboolean>(isValid);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getPaymentIdFromAddress(JNIEnv *env, jclass clazz,
                                                        jstring address,
                                                        jint networkType) {
    XCash::NetworkType _networkType = static_cast<XCash::NetworkType>(networkType);
    const char *_address = env->GetStringUTFChars(address, NULL);
    std::string payment_id = XCash::Wallet::paymentIdFromAddress(_address, _networkType);
    env->ReleaseStringUTFChars(address, _address);
    return env->NewStringUTF(payment_id.c_str());
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getMaximumAllowedAmount(JNIEnv *env, jclass clazz) {
    return XCash::Wallet::maximumAllowedAmount();
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_startRefresh(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->startRefresh();
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_pauseRefresh(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->pauseRefresh();
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_refresh(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return static_cast<jboolean>(wallet->refresh());
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_refreshAsync(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->refreshAsync();
}

//TODO virtual void setAutoRefreshInterval(int millis) = 0;
//TODO virtual int autoRefreshInterval() const = 0;

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_createTransactionJ(JNIEnv *env, jobject instance,
                                                   jstring dst_addr, jstring payment_id,
                                                   jlong amount, jint mixin_count,
                                                   jint priority,
                                                   jint accountIndex,jint privacy_settings) {

    const char *_dst_addr = env->GetStringUTFChars(dst_addr, NULL);
    const char *_payment_id = env->GetStringUTFChars(payment_id, NULL);

    XCash::PendingTransaction::Priority _priority =
            static_cast<XCash::PendingTransaction::Priority>(priority);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    XCash::PendingTransaction *tx=0;
    try {
        tx = wallet->createTransaction(_dst_addr, _payment_id,
                                       amount, (uint32_t) mixin_count,
                                       _priority,
                                       (uint32_t) accountIndex,{},privacy_settings);
    } catch (const std::exception &e) {

    }
    env->ReleaseStringUTFChars(dst_addr, _dst_addr);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return reinterpret_cast<jlong>(tx);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_createSweepTransaction(JNIEnv *env, jobject instance,
                                                       jstring dst_addr, jstring payment_id,
                                                       jint mixin_count,
                                                       jint priority,
                                                       jint accountIndex,jint privacy_settings) {

    const char *_dst_addr = env->GetStringUTFChars(dst_addr, NULL);
    const char *_payment_id = env->GetStringUTFChars(payment_id, NULL);
    XCash::PendingTransaction::Priority _priority =
            static_cast<XCash::PendingTransaction::Priority>(priority);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);

    XCash::optional<uint64_t> empty;

    XCash::PendingTransaction *tx=0;
    try {
        tx = wallet->createTransaction(_dst_addr, _payment_id,
                                       empty, (uint32_t) mixin_count,
                                       _priority,
                                       (uint32_t) accountIndex,{},privacy_settings);
    } catch (const std::exception &e) {

    }
    env->ReleaseStringUTFChars(dst_addr, _dst_addr);
    env->ReleaseStringUTFChars(payment_id, _payment_id);
    return reinterpret_cast<jlong>(tx);
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_createSweepUnmixableTransactionJ(JNIEnv *env,
                                                                 jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    XCash::PendingTransaction *tx=0;
    try {
        tx = wallet->createSweepUnmixableTransaction();
    } catch (const std::exception &e) {

    }
    return reinterpret_cast<jlong>(tx);
}

//virtual UnsignedTransaction * loadUnsignedTx(const std::string &unsigned_filename) = 0;
//virtual bool submitTransaction(const std::string &fileName) = 0;

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_disposeTransaction(JNIEnv *env, jobject instance,
                                                   jobject pendingTransaction) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    XCash::PendingTransaction *_pendingTransaction =
            getHandle<XCash::PendingTransaction>(env, pendingTransaction);
    wallet->disposeTransaction(_pendingTransaction);
}

//virtual bool exportKeyImages(const std::string &filename) = 0;
//virtual bool importKeyImages(const std::string &filename) = 0;


//virtual TransactionHistory * history() const = 0;
JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_getHistoryJ(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return reinterpret_cast<jlong>(wallet->history());
}

//virtual AddressBook * addressBook() const = 0;

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_Wallet_setListenerJ(JNIEnv *env, jobject instance,
                                             jobject javaListener) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->setListener(nullptr); // clear old listener
    // delete old listener
    MyWalletListener *oldListener = getHandle<MyWalletListener>(env, instance,
                                                                "listenerHandle");
    if (oldListener != nullptr) {
        oldListener->deleteGlobalJavaRef(env);
        delete oldListener;
    }
    if (javaListener == nullptr) {
        LOGD("null listener");
        return 0;
    } else {
        MyWalletListener *listener = new MyWalletListener(env, javaListener);
        wallet->setListener(listener);
        return reinterpret_cast<jlong>(listener);
    }
}

JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getDefaultMixin(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->defaultMixin();
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_setDefaultMixin(JNIEnv *env, jobject instance, jint mixin) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return wallet->setDefaultMixin(mixin);
}

JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_Wallet_setUserNote(JNIEnv *env, jobject instance,
                                            jstring txid, jstring note) {

    const char *_txid = env->GetStringUTFChars(txid, NULL);
    const char *_note = env->GetStringUTFChars(note, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);

    bool success = wallet->setUserNote(_txid, _note);

    env->ReleaseStringUTFChars(txid, _txid);
    env->ReleaseStringUTFChars(note, _note);

    return static_cast<jboolean>(success);
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getUserNote(JNIEnv *env, jobject instance,
                                            jstring txid) {

    const char *_txid = env->GetStringUTFChars(txid, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);

    std::string note = wallet->getUserNote(_txid);

    env->ReleaseStringUTFChars(txid, _txid);
    return env->NewStringUTF(note.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getTxKey(JNIEnv *env, jobject instance,
                                         jstring txid) {

    const char *_txid = env->GetStringUTFChars(txid, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);

    std::string txKey = wallet->getTxKey(_txid);

    env->ReleaseStringUTFChars(txid, _txid);
    return env->NewStringUTF(txKey.c_str());
}

//virtual void addSubaddressAccount(const std::string& label) = 0;
JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_addAccount(JNIEnv *env, jobject instance,
                                           jstring label) {

    const char *_label = env->GetStringUTFChars(label, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->addSubaddressAccount(_label);

    env->ReleaseStringUTFChars(label, _label);
}

//virtual std::string getSubaddressLabel(uint32_t accountIndex, uint32_t addressIndex) const = 0;
JNIEXPORT jstring JNICALL
Java_com_my_monero_model_Wallet_getSubaddressLabel(JNIEnv *env, jobject instance,
                                                   jint accountIndex, jint addressIndex) {

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);

    std::string label = wallet->getSubaddressLabel((uint32_t) accountIndex,
                                                   (uint32_t) addressIndex);

    return env->NewStringUTF(label.c_str());
}

//virtual void setSubaddressLabel(uint32_t accountIndex, uint32_t addressIndex, const std::string &label) = 0;
JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_setSubaddressLabel(JNIEnv *env, jobject instance,
                                                   jint accountIndex, jint addressIndex,
                                                   jstring label) {

    const char *_label = env->GetStringUTFChars(label, NULL);

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->setSubaddressLabel(accountIndex, addressIndex, _label);

    env->ReleaseStringUTFChars(label, _label);
}

// virtual size_t numSubaddressAccounts() const = 0;
JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getNumAccounts(JNIEnv *env, jobject instance) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return static_cast<jint>(wallet->numSubaddressAccounts());
}

//virtual size_t numSubaddresses(uint32_t accountIndex) const = 0;
JNIEXPORT jint JNICALL
Java_com_my_monero_model_Wallet_getNumSubaddresses(JNIEnv *env, jobject instance,
                                                   jint accountIndex) {
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    return static_cast<jint>(wallet->numSubaddresses(accountIndex));
}

//virtual void addSubaddress(uint32_t accountIndex, const std::string &label) = 0;
JNIEXPORT void JNICALL
Java_com_my_monero_model_Wallet_addSubaddress(JNIEnv *env, jobject instance,
                                              jint accountIndex,
                                              jstring label) {

    const char *_label = env->GetStringUTFChars(label, NULL);
    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
    wallet->addSubaddress(accountIndex, _label);
    env->ReleaseStringUTFChars(label, _label);
}



//virtual std::string signMessage(const std::string &message) = 0;
//virtual bool verifySignedMessage(const std::string &message, const std::string &addres, const std::string &signature) const = 0;

//virtual bool parse_uri(const std::string &uri, std::string &address, std::string &payment_id, uint64_t &tvAmount, std::string &tx_description, std::string &recipient_name, std::vector<std::string> &unknown_parameters, std::string &error) = 0;
//virtual bool rescanSpent() = 0;


// TransactionHistory
JNIEXPORT jint JNICALL
Java_com_my_monero_model_TransactionHistory_getCount(JNIEnv *env, jobject instance) {
    XCash::TransactionHistory *history = getHandle<XCash::TransactionHistory>(env,
                                                                                      instance);
    return history->count();
}

jobject newTransferInstance(JNIEnv *env, uint64_t amount, const std::string &address) {
    jmethodID c = env->GetMethodID(class_Transfer, "<init>",
                                   "(JLjava/lang/String;)V");
    jstring _address = env->NewStringUTF(address.c_str());
    jobject transfer = env->NewObject(class_Transfer, c, amount, _address);
    env->DeleteLocalRef(_address);
    return transfer;
}

jobject newTransferList(JNIEnv *env, XCash::TransactionInfo *info) {
    const std::vector<XCash::TransactionInfo::Transfer> &transfers = info->transfers();
    if (transfers.size() == 0) { // don't create empty Lists
        return nullptr;
    }
    // make new ArrayList
    jmethodID java_util_ArrayList_ = env->GetMethodID(class_ArrayList, "<init>", "(I)V");
    jmethodID java_util_ArrayList_add = env->GetMethodID(class_ArrayList, "add",
                                                         "(Ljava/lang/Object;)Z");
    jobject result = env->NewObject(class_ArrayList, java_util_ArrayList_, transfers.size());
    // create Transfer objects and stick them in the List
    for (const XCash::TransactionInfo::Transfer &s: transfers) {
        jobject element = newTransferInstance(env, s.amount, s.address);
        env->CallBooleanMethod(result, java_util_ArrayList_add, element);
        env->DeleteLocalRef(element);
    }
    return result;
}

jobject newTransactionInfo(JNIEnv *env, XCash::TransactionInfo *info) {
    jmethodID c = env->GetMethodID(class_TransactionInfo, "<init>",
                                   "(IZZJJJLjava/lang/String;JLjava/lang/String;IIJLjava/lang/String;Ljava/util/List;)V");
    jobject transfers = newTransferList(env, info);
    jstring _hash = env->NewStringUTF(info->hash().c_str());
    jstring _paymentId = env->NewStringUTF(info->paymentId().c_str());
    jstring _label = env->NewStringUTF(info->label().c_str());
    uint32_t subaddrIndex = 0;
    if (info->direction() == XCash::TransactionInfo::Direction_In)
        subaddrIndex = *(info->subaddrIndex().begin());
    jobject result = env->NewObject(class_TransactionInfo, c,
                                    info->direction(),
                                    info->isPending(),
                                    info->isFailed(),
                                    info->amount(),
                                    info->fee(),
                                    info->blockHeight(),
                                    _hash,
                                    static_cast<jlong> (info->timestamp()),
                                    _paymentId,
                                    info->subaddrAccount(),
                                    subaddrIndex,
                                    info->confirmations(),
                                    _label,
                                    transfers);
    env->DeleteLocalRef(transfers);
    env->DeleteLocalRef(_hash);
    env->DeleteLocalRef(_paymentId);
    return result;
}

jobject newSubaddressRow(JNIEnv *env, XCash::SubaddressRow *row) {
    jmethodID c = env->GetMethodID(class_SubaddressRow, "<init>",
                                   "(ILjava/lang/String;Ljava/lang/String;)V");
    auto rowId = static_cast<jint >(row->getRowId());
    jstring _address = env->NewStringUTF(row->getAddress().c_str());
    jstring _label = env->NewStringUTF(row->getLabel().c_str());
    jobject subaddressRow = env->NewObject(class_SubaddressRow, c, rowId, _address, _label);
    env->DeleteLocalRef(_address);
    env->DeleteLocalRef(_label);

    return subaddressRow;
}

#include <stdio.h>
#include <stdlib.h>

jobject cpp2java(JNIEnv *env, std::vector<XCash::TransactionInfo *> vector) {

    jmethodID java_util_ArrayList_ = env->GetMethodID(class_ArrayList, "<init>", "(I)V");
    jmethodID java_util_ArrayList_add = env->GetMethodID(class_ArrayList, "add",
                                                         "(Ljava/lang/Object;)Z");

    jobject arrayList = env->NewObject(class_ArrayList, java_util_ArrayList_, vector.size());
    for (XCash::TransactionInfo *s: vector) {
        jobject info = newTransactionInfo(env, s);
        env->CallBooleanMethod(arrayList, java_util_ArrayList_add, info);
        env->DeleteLocalRef(info);
    }
    return arrayList;
}

jobject cpp2java2(JNIEnv *env, std::vector<XCash::SubaddressRow *> vector) {

    jmethodID java_util_ArrayList_ = env->GetMethodID(class_ArrayList, "<init>", "(I)V");
    jmethodID java_util_ArrayList_add = env->GetMethodID(class_ArrayList, "add",
                                                         "(Ljava/lang/Object;)Z");

    jobject arrayList = env->NewObject(class_ArrayList, java_util_ArrayList_, vector.size());
    for (XCash::SubaddressRow *s: vector) {
        jobject row = newSubaddressRow(env, s);
        env->CallBooleanMethod(arrayList, java_util_ArrayList_add, row);
        env->DeleteLocalRef(row);
    }
    return arrayList;
}

JNIEXPORT jobject JNICALL
Java_com_my_monero_model_Wallet_getSubaddresses(JNIEnv *env, jobject instance,
                                                jint accountIndex) {

    XCash::Wallet *wallet = getHandle<XCash::Wallet>(env, instance);
//    size_t num = wallet->numSubaddresses(accountIndex);
    //wallet->subaddress()->getAll()[num]->getAddress().c_str()
    XCash::Subaddress *s = wallet->subaddress();
    s->refresh(accountIndex);
    std::vector<XCash::SubaddressRow *> v = s->getAll();
    return cpp2java2(env, v);
}

JNIEXPORT jobject JNICALL
Java_com_my_monero_model_TransactionHistory_refreshJ(JNIEnv *env, jobject instance) {
    XCash::TransactionHistory *history = getHandle<XCash::TransactionHistory>(env,
                                                                                      instance);
    history->refresh();
    return cpp2java(env, history->getAll());
}

// TransactionInfo is implemented in Java - no need here

JNIEXPORT jint JNICALL
Java_com_my_monero_model_PendingTransaction_getStatusJ(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return tx->status();
}

JNIEXPORT jstring JNICALL
Java_com_my_monero_model_PendingTransaction_getErrorString(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return env->NewStringUTF(tx->errorString().c_str());
}

// commit transaction or save to file if filename is provided.
JNIEXPORT jboolean JNICALL
Java_com_my_monero_model_PendingTransaction_commit(JNIEnv *env, jobject instance,
                                                   jstring filename, jboolean overwrite) {

    const char *_filename = env->GetStringUTFChars(filename, NULL);

    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    bool success = tx->commit(_filename, overwrite);

    env->ReleaseStringUTFChars(filename, _filename);
    return static_cast<jboolean>(success);
}


JNIEXPORT jlong JNICALL
Java_com_my_monero_model_PendingTransaction_getAmount(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return tx->amount();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_PendingTransaction_getDust(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return tx->dust();
}

JNIEXPORT jlong JNICALL
Java_com_my_monero_model_PendingTransaction_getFee(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return tx->fee();
}

// TODO this returns a vector of strings - deal with this later - for now return first one
JNIEXPORT jstring JNICALL
Java_com_my_monero_model_PendingTransaction_getFirstTxIdJ(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    std::vector<std::string> txids = tx->txid();
    if (!txids.empty())
        return env->NewStringUTF(txids.front().c_str());
    else
        return nullptr;
}


JNIEXPORT jlong JNICALL
Java_com_my_monero_model_PendingTransaction_getTxCount(JNIEnv *env, jobject instance) {
    XCash::PendingTransaction *tx = getHandle<XCash::PendingTransaction>(env, instance);
    return tx->txCount();
}


// these are all in XCash::Wallet - which I find wrong, so they are here!
//static void init(const char *argv0, const char *default_log_base_name);
//static void debug(const std::string &category, const std::string &str);
//static void info(const std::string &category, const std::string &str);
//static void warning(const std::string &category, const std::string &str);
//static void error(const std::string &category, const std::string &str);
JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_initLogger(JNIEnv *env, jclass clazz,
                                                  jstring argv0,
                                                  jstring default_log_base_name) {

    const char *_argv0 = env->GetStringUTFChars(argv0, NULL);
    const char *_default_log_base_name = env->GetStringUTFChars(default_log_base_name, NULL);

    XCash::Wallet::init(_argv0, _default_log_base_name);

    env->ReleaseStringUTFChars(argv0, _argv0);
    env->ReleaseStringUTFChars(default_log_base_name, _default_log_base_name);
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_logDebug(JNIEnv *env, jclass clazz,
                                                jstring category, jstring message) {

    const char *_category = env->GetStringUTFChars(category, NULL);
    const char *_message = env->GetStringUTFChars(message, NULL);

    XCash::Wallet::debug(_category, _message);

    env->ReleaseStringUTFChars(category, _category);
    env->ReleaseStringUTFChars(message, _message);
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_logInfo(JNIEnv *env, jclass clazz,
                                               jstring category, jstring message) {

    const char *_category = env->GetStringUTFChars(category, NULL);
    const char *_message = env->GetStringUTFChars(message, NULL);

    XCash::Wallet::info(_category, _message);

    env->ReleaseStringUTFChars(category, _category);
    env->ReleaseStringUTFChars(message, _message);
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_logWarning(JNIEnv *env, jclass clazz,
                                                  jstring category, jstring message) {

    const char *_category = env->GetStringUTFChars(category, NULL);
    const char *_message = env->GetStringUTFChars(message, NULL);

    XCash::Wallet::warning(_category, _message);

    env->ReleaseStringUTFChars(category, _category);
    env->ReleaseStringUTFChars(message, _message);
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_logError(JNIEnv *env, jclass clazz,
                                                jstring category, jstring message) {

    const char *_category = env->GetStringUTFChars(category, NULL);
    const char *_message = env->GetStringUTFChars(message, NULL);

    XCash::Wallet::error(_category, _message);

    env->ReleaseStringUTFChars(category, _category);
    env->ReleaseStringUTFChars(message, _message);
}

JNIEXPORT void JNICALL
Java_com_my_monero_model_WalletManager_setLogLevel(JNIEnv *env, jclass clazz,
                                                   jint level) {
    XCash::WalletManagerFactory::setLogLevel(level);
}

//
// Ledger Stuff
//

#include "device_io_monerujo.hpp"

/**
 * @brief LedgerExchange - exchange data with Ledger Device
 * @param command        - buffer for data to send
 * @param cmd_len        - length of send to send
 * @param response       - buffer for received data
 * @param max_resp_len   - size of receive buffer
 *
 * @return length of received data in response or -1 if error
 */
int LedgerExchange(
        unsigned char *command,
        unsigned int cmd_len,
        unsigned char *response,
        unsigned int max_resp_len) {
    LOGD("LedgerExchange");
    JNIEnv *jenv;
    int envStat = attachJVM(&jenv);
    if (envStat == JNI_ERR) return -1;

    jmethodID exchangeMethod = jenv->GetStaticMethodID(class_Ledger, "Exchange", "([B)[B");

    jsize sendLen = static_cast<jsize>(cmd_len);
    jbyteArray dataSend = jenv->NewByteArray(sendLen);
    jenv->SetByteArrayRegion(dataSend, 0, sendLen, (jbyte *) command);
    jbyteArray dataRecv = (jbyteArray) jenv->CallStaticObjectMethod(class_Ledger, exchangeMethod,
                                                                    dataSend);
    jenv->DeleteLocalRef(dataSend);
    if (dataRecv == nullptr) {
        detachJVM(jenv, envStat);
        LOGD("LedgerExchange SCARD_E_NO_READERS_AVAILABLE");
        return -1;
    }
    jsize len = jenv->GetArrayLength(dataRecv);
    LOGD("LedgerExchange SCARD_S_SUCCESS %d/%d", cmd_len, len);
    if (len <= max_resp_len) {
        jenv->GetByteArrayRegion(dataRecv, 0, len, (jbyte *) response);
        jenv->DeleteLocalRef(dataRecv);
        detachJVM(jenv, envStat);
        return static_cast<int>(len);;
    } else {
        jenv->DeleteLocalRef(dataRecv);
        detachJVM(jenv, envStat);
        LOGE("LedgerExchange SCARD_E_INSUFFICIENT_BUFFER");
        return -1;
    }
}

/**
 * @brief LedgerFind - find Ledger Device and return it's name
 * @param buffer - buffer for name of found device
 * @param len    - length of buffer
 * @return  0 - success
 *         -1 - no device connected / found
 *         -2 - JVM not found
 */
int LedgerFind(char *buffer, size_t len) {
    LOGD("LedgerName");
    JNIEnv *jenv;
    int envStat = attachJVM(&jenv);
    if (envStat == JNI_ERR) return -2;

    jmethodID nameMethod = jenv->GetStaticMethodID(class_Ledger, "Name", "()Ljava/lang/String;");
    jstring name = (jstring) jenv->CallStaticObjectMethod(class_Ledger, nameMethod);

    int ret;
    if (name != nullptr) {
        const char *_name = jenv->GetStringUTFChars(name, NULL);
        strncpy(buffer, _name, len);
        jenv->ReleaseStringUTFChars(name, _name);
        buffer[len - 1] = 0; // terminate in case _name is bigger
        ret = 0;
        LOGD("LedgerName is %s", buffer);
    } else {
        buffer[0] = 0;
        ret = -1;
    }

    detachJVM(jenv, envStat);
    return ret;
}

#ifdef __cplusplus
}
#endif
