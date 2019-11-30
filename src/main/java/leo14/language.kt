package leo14

import leo13.first

enum class Language {
	ENGLISH,
	GERMAN,
	POLISH,
}

val defaultLanguage = Language.ENGLISH

operator fun Language.get(keyword: Keyword): String =
	keyword stringIn this

fun Language.keywordOrNull(string: String): Keyword? =
	keywordStack.first { it.stringIn(this) == string }

infix fun String.keywordOrNullIn(language: Language): Keyword? =
	language.keywordOrNull(this)