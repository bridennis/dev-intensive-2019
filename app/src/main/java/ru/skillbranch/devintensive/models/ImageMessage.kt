package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class ImageMessage (
        id: String,
        from: User?,
        chat: Chat,
        date: Date = Date(),
        var image: String?,
        isIncoming: Boolean = false
) : BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String {
        val action = if (isIncoming) "получил" else "отправил"

        return "${from?.firstName} $action изображение \"$image\" ${date.humanizeDiff()}"
    }
}