package com.dbab.compose_places_api_sample.extensions

import android.content.pm.PackageManager



fun PackageManager.getMetadataKey(
    packageName: String,
    keyName: String
): String {
    val ai = this.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    val bundle = ai.metaData
    val key: String = bundle.getString(keyName)!!
    return key
}