package leo14

enum class Language {
	ENGLISH,
	GERMAN,
	POLISH,
}

val defaultLanguage = Language.GERMAN

operator fun Language.get(keyword: Keyword): String =
	keyword stringIn this
