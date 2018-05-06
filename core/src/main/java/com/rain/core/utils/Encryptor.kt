package com.rain.core.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

@Throws(NoSuchAlgorithmException::class)
fun hash256(data: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    messageDigest.update(data.toByteArray())
    return bytesToHex(messageDigest.digest())
}

fun bytesToHex(bytes: ByteArray): String {
    val result = StringBuffer()
    for (b in bytes) result.append(Integer.toString((b and 0xff.toByte()) + 0x100, 16)
            .substring(1))
    return result.toString()
}
