package leo14

import leo13.Stack
import leo13.stack

enum class Keyword {
	APPLY,
	ARROW,
	AS,
	BIT,
	BYTE,
	CHOICE,
	COMMENT,
	DEFINE,
	DELETE,
	DOING,
	EIGHT,
	EVERYTHING,
	EXIT,
	FIFTH,
	FIRST,
	FORGET,
	FOURTH,
	FUNCTION,
	GIVE,
	GIVES,
	GIVING,
	IS,
	IT,
	LAST,
	LEONARDO,
	MAKE,
	MATCH,
	NATIVE,
	NOTHING,
	NUMBER,
	ONE,
	PREVIOUS,
	SCRIPT,
	SECOND,
	SEVENTH,
	SIXTH,
	TEXT,
	THIRD,
	THIS,
	USE,
	ZERO,
}

val Keyword.string get() = name.toLowerCase()

val Keyword.englishString: String
	get() =
		string

val Keyword.polishString: String
	get() =
		when (this) {
			Keyword.APPLY -> "zastosuj"
			Keyword.ARROW -> "strzałka"
			Keyword.AS -> "jako"
			Keyword.BIT -> "bit"
			Keyword.BYTE -> "bajt"
			Keyword.CHOICE -> "wybór"
			Keyword.COMMENT -> "komentarz"
			Keyword.DEFINE -> "zdefiniuj"
			Keyword.DELETE -> "usuń"
			Keyword.DOING -> "robiąca"
			Keyword.EIGHT -> "osiem"
			Keyword.EVERYTHING -> "wszystko"
			Keyword.EXIT -> "wyjdź"
			Keyword.FIFTH -> "piąty"
			Keyword.FIRST -> "pierwszy"
			Keyword.FORGET -> "zapomnij"
			Keyword.FOURTH -> "czwarty"
			Keyword.FUNCTION -> "funkcja"
			Keyword.GIVE -> "daj"
			Keyword.GIVES -> "daje"
			Keyword.GIVING -> "dająca"
			Keyword.IS -> "jest"
			Keyword.IT -> "to"
			Keyword.LAST -> "ostatni"
			Keyword.LEONARDO -> "leonardo"
			Keyword.MAKE -> "zrób"
			Keyword.MATCH -> "dopasuj"
			Keyword.NATIVE -> "natywny"
			Keyword.NOTHING -> "nic"
			Keyword.NUMBER -> "liczba"
			Keyword.ONE -> "jeden"
			Keyword.PREVIOUS -> "poprzedni"
			Keyword.SCRIPT -> "skrypt"
			Keyword.SECOND -> "drugi"
			Keyword.SEVENTH -> "siódmy"
			Keyword.SIXTH -> "szósty"
			Keyword.TEXT -> "tekst"
			Keyword.THIRD -> "trzeci"
			Keyword.THIS -> "to"
			Keyword.USE -> "użyj"
			Keyword.ZERO -> "zero"
		}

val Keyword.germanString: String
	get() =
		when (this) {
			Keyword.APPLY -> TODO()
			Keyword.ARROW -> "pfeil"
			Keyword.AS -> "wie"
			Keyword.BIT -> "bit"
			Keyword.BYTE -> "byte"
			Keyword.CHOICE -> "wahl"
			Keyword.COMMENT -> "komentar"
			Keyword.DEFINE -> TODO()
			Keyword.DELETE -> "lösche"
			Keyword.DOING -> "machen"
			Keyword.EIGHT -> "acht"
			Keyword.EVERYTHING -> "alles"
			Keyword.EXIT -> TODO()
			Keyword.FIFTH -> "fünfte"
			Keyword.FIRST -> "erste"
			Keyword.FORGET -> "vergiss"
			Keyword.FOURTH -> "vierte"
			Keyword.FUNCTION -> "funktion"
			Keyword.GIVE -> "gib"
			Keyword.GIVES -> TODO()
			Keyword.GIVING -> "geben"
			Keyword.IS -> "ist"
			Keyword.IT -> "das"
			Keyword.LAST -> "letzte"
			Keyword.LEONARDO -> "leonardo"
			Keyword.MAKE -> "mach"
			Keyword.MATCH -> "verbinde"
			Keyword.NATIVE -> "heimisch"
			Keyword.NOTHING -> "nichts"
			Keyword.NUMBER -> "nummer"
			Keyword.ONE -> "eins"
			Keyword.PREVIOUS -> "vorherig"
			Keyword.SCRIPT -> "skript"
			Keyword.SECOND -> "zweite"
			Keyword.SEVENTH -> "siebte"
			Keyword.SIXTH -> "sechste"
			Keyword.TEXT -> "text"
			Keyword.THIRD -> "dritte"
			Keyword.THIS -> "es"
			Keyword.USE -> "benutze"
			Keyword.ZERO -> "null"
		}

infix fun Keyword.stringIn(language: Language) =
	when (language) {
		Language.ENGLISH -> englishString
		Language.POLISH -> polishString
		Language.GERMAN -> germanString
	}

val keywordStack: Stack<Keyword> = stack(*Keyword.values())