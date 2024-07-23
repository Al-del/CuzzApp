#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cuzzapp_Show_1recepies_00024Keys_APIKeys(JNIEnv* env, jobject /* this is it */) {
    std::string api_key = "AIzaSyCY05brSzUg7_N9c4ONx4EWC5OHxFtcHVE";
    return env->NewStringUTF(api_key.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cuzzapp_Show_1recepies_00024Keys_App_1id(JNIEnv* env, jobject /* this is it */) {
    std::string app_id = "179c5048";
    return env->NewStringUTF(app_id.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cuzzapp_Show_1recepies_00024Keys_API_1edeman(JNIEnv* env, jobject /* this is it */) {
    std::string app_id = "eaa2c4366d4e55b3088a580edfaa5bd7";
    return env->NewStringUTF(app_id.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cuzzapp_Show_1recepies_00024Keys_API_1spoonacular(JNIEnv* env, jobject /* this is it */) {
    std::string app_id = "5df674c4fc0242e38d2d0dd5cd94ffac";
    return env->NewStringUTF(app_id.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cuzzapp_Show_1recepies_00024Keys_API_1chat_1gpt(JNIEnv* env, jobject /* this is it */) {
        std::string app_id = "YOUR_API_KEY";
    return env->NewStringUTF(app_id.c_str());
}