package com.inlurker.komiq.model.recognition.helper

fun String.reverseAndCombineLines(): String {
    // Split the input string into lines and filter out any empty lines
    val lines = this.split("\n").filter { it.isNotBlank() }

    // Reverse the list of lines
    val reversedLines = lines.reversed()

    // Combine the reversed lines into a single string without spaces
    return reversedLines.joinToString("")
}

fun String.removeSpaces(): String {
    return this.replace(" ", "")
}

fun String.hasOnlyGarbage(): Boolean {
    // This regex checks if the string does not contain any Unicode letter or number.
    // `\\p{L}` matches any letter from any language, and `\\p{N}` matches any numeric digit.
    return !this.any { it.toString().matches(Regex("[\\p{L}\\p{N}]")) }
}