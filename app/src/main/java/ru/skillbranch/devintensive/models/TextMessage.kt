package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class TextMessage (
        id: String,
        from: User?,
        chat: Chat,
        isIncoming: Boolean = false,
        date: Date = Date(),
        var text: String?
) : BaseMessage(id, from, chat, isIncoming, date) {
    override fun formatMessage(): String {
        val action = if (isIncoming) "получил" else "отправил"

        return "${from?.firstName} $action сообщение \"$text\" ${date.humanizeDiff()}"
    }
}