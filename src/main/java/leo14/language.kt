package leo14

enum class Language {
	ENGLISH,
	POLISH,
}

val defaultLanguage = Language.ENGLISH

operator fun Language.get(keyword: Keyword): String =
	keyword stringIn this
