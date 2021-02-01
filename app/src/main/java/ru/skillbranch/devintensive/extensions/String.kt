package ru.skillbranch.devintensive.extensions

fun String.truncate(length: Int = 16): String {
    val trimmedString = this.trim()

    return if (trimmedString.length > length) trimmedString.substring(0, length).trimEnd() + "..."
        else trimmedString
}

fun String.stripHtml(): String {
    val html = Regex("<.*?>")
    val htmlEntity = Regex("&[\\w]{1,4}?;")
    val multiSpace = Regex(" {2,}")

    return this
            .replace(html, "")
            .replace(htmlEntity, "")
            .replace(multiSpace, " ")
}