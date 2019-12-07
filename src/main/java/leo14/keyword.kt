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
	COMPILE,
	COMPILER,
	DEFINE,
	DELETE,
	DOING,
	EIGHT,
	EVALUATE,
	EVALUATOR,
	EVERYTHING,
	EXIT,
	FIFTH,
	FIRST,
	FORGET,
	FOURTH,
	FUNCTION,
	GIVE,
	GIVEN,
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
	QUOTE,
	SCRIPT,
	SECOND,
	SEVENTH,
	SIXTH,
	TEXT,
	THIRD,
	THIS,
	UNQUOTE,
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
			Keyword.COMPILE -> "kompiluj"
			Keyword.COMPILER -> "kompilator"
			Keyword.DEFINE -> "zdefiniuj"
			Keyword.DELETE -> "usuń"
			Keyword.DOING -> "robiąca"
			Keyword.EIGHT -> "osiem"
			Keyword.EVALUATE -> "oblicz"
			Keyword.EVALUATOR -> "ewaluator"
			Keyword.EVERYTHING -> "wszystko"
			Keyword.EXIT -> "wyjdź"
			Keyword.FIFTH -> "piąty"
			Keyword.FIRST -> "pierwszy"
			Keyword.FORGET -> "zapomnij"
			Keyword.FOURTH -> "czwarty"
			Keyword.FUNCTION -> "funkcja"
			Keyword.GIVE -> "daj"
			Keyword.GIVEN -> "dany"
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
			Keyword.QUOTE -> "zacytuj"
			Keyword.SCRIPT -> "skrypt"
			Keyword.SECOND -> "drugi"
			Keyword.SEVENTH -> "siódmy"
			Keyword.SIXTH -> "szósty"
			Keyword.TEXT -> "tekst"
			Keyword.THIRD -> "trzeci"
			Keyword.THIS -> "to"
			Keyword.UNQUOTE -> "odcytuj"
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
			Keyword.COMPILE -> TODO()
			Keyword.COMPILER -> TODO()
			Keyword.DEFINE -> TODO()
			Keyword.DELETE -> "lösche"
			Keyword.DOING -> "machen"
			Keyword.EIGHT -> "acht"
			Keyword.EVALUATE -> TODO()
			Keyword.EVALUATOR -> TODO()
			Keyword.EVERYTHING -> "alles"
			Keyword.EXIT -> TODO()
			Keyword.FIFTH -> "fünfte"
			Keyword.FIRST -> "erste"
			Keyword.FORGET -> "vergiss"
			Keyword.FOURTH -> "vierte"
			Keyword.FUNCTION -> "funktion"
			Keyword.GIVE -> "gib"
			Keyword.GIVEN -> TODO()
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
			Keyword.QUOTE -> TODO()
			Keyword.SCRIPT -> "skript"
			Keyword.SECOND -> "zweite"
			Keyword.SEVENTH -> "siebte"
			Keyword.SIXTH -> "sechste"
			Keyword.TEXT -> "text"
			Keyword.THIRD -> "dritte"
			Keyword.THIS -> "es"
			Keyword.USE -> "benutze"
			Keyword.UNQUOTE -> TODO()
			Keyword.ZERO -> "null"
		}

infix fun Keyword.stringIn(language: Language) =
	when (language) {
		Language.ENGLISH -> englishString
		Language.POLISH -> polishString
		Language.GERMAN -> germanString
	}

val keywordStack: Stack<Keyword> = stack(*Keyword.values())