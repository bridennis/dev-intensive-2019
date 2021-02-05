package ru.skillbranch.devintensive.models

import java.util.*

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    private val wrongAnswerLimit = 3
    private var wrongAnswers = 0

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        try {
            question.validateAnswerFormat(answer)
        } catch (e: NotValidAnswerFormatException) {
            return (e.message ?: "") + "\n" + question.question to status.color
        }

        return if (question.answers.contains(answer.toLowerCase(Locale.ROOT))) {
            question = question.nextQuestion()

            "Отлично - ты справился\n" +
                    question.question to status.color
        } else {
            var restartMessage = ""

            if (++wrongAnswers >= wrongAnswerLimit) {
                wrongAnswers = 1

                status = Status.NORMAL
                question = Question.NAME

                restartMessage = ". Давай все по новой"
            } else {
                status = status.nextStatus()
            }

            "Это неправильный ответ$restartMessage\n" +
                    question.question to status.color
        }
    }

    @Suppress("unused")
    enum class Status(var color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[ this.ordinal + 1 ]
            } else {
                values()[ 0 ]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateAnswerFormat(answer: String) {
                if (answer[0].toUpperCase() != answer[0])
                    throw NotValidAnswerFormatException("Имя должно начинаться с заглавной буквы")
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateAnswerFormat(answer: String) {
                if (answer[0].toLowerCase() != answer[0])
                    throw NotValidAnswerFormatException(
                            "Профессия должна начинаться со строчной буквы"
                    )
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validateAnswerFormat(answer: String) {
                if (answer.contains(Regex("\\d+")))
                    throw NotValidAnswerFormatException("Материал не должен содержать цифр")
            }
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validateAnswerFormat(answer: String) {
                if (answer.toIntOrNull() == null)
                    throw NotValidAnswerFormatException(
                            "Год моего рождения должен содержать только цифры"
                    )
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswerFormat(answer: String) {
                if (answer.length != 7 || answer.toIntOrNull() == null)
                throw NotValidAnswerFormatException("Серийный номер содержит только цифры, и их 7")
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswerFormat(answer: String) {
            }
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswerFormat(answer: String)
    }

    class NotValidAnswerFormatException(message:String): Exception(message)
}
