package leo14

enum class Keyword {
	ARROW,
	AS,
	BIT,
	BYTE,
	CHOICE,
	COMMENT,
	DELETE,
	DO,
	DOES,
	DOING,
	EIGHT,
	EVERYTHING,
	FIFTH,
	FIRST,
	FORGET,
	FOURTH,
	FUNCTION,
	GIVE,
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
	REMEMBER,
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

val Keyword.englishString: String
	get() =
		name.toLowerCase()

val Keyword.polishString: String
	get() =
		when (this) {
			Keyword.ARROW -> "strzałka"
			Keyword.AS -> "jako"
			Keyword.BIT -> "bit"
			Keyword.BYTE -> "bajt"
			Keyword.CHOICE -> "wybór"
			Keyword.COMMENT -> "komentarz"
			Keyword.DELETE -> "usuń"
			Keyword.DO -> "zrób"
			Keyword.DOES -> "robi"
			Keyword.DOING -> "robić"
			Keyword.EIGHT -> "osiem"
			Keyword.EVERYTHING -> "wszystko"
			Keyword.FIFTH -> "piąty"
			Keyword.FIRST -> "pierwszy"
			Keyword.FORGET -> "zapomnij"
			Keyword.FOURTH -> "czwarty"
			Keyword.FUNCTION -> "funkcja"
			Keyword.GIVE -> "daj"
			Keyword.GIVING -> "dawać"
			Keyword.IS -> "jest"
			Keyword.IT -> "to"
			Keyword.LAST -> "ostatnia"
			Keyword.LEONARDO -> "leonardo"
			Keyword.MAKE -> "zrób"
			Keyword.MATCH -> "dopasuj"
			Keyword.NATIVE -> "natywny"
			Keyword.NOTHING -> "nic"
			Keyword.NUMBER -> "numer"
			Keyword.ONE -> "jeden"
			Keyword.PREVIOUS -> "poprzedni"
			Keyword.REMEMBER -> "zapamiętaj"
			Keyword.SCRIPT -> "sktypt"
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
			Keyword.ARROW -> "pfeil"
			Keyword.AS -> "wie"
			Keyword.BIT -> "bit"
			Keyword.BYTE -> "byte"
			Keyword.CHOICE -> "wahl"
			Keyword.COMMENT -> "komentar"
			Keyword.DELETE -> "lösche"
			Keyword.DO -> "mach"
			Keyword.DOES -> "macht"
			Keyword.DOING -> "machen"
			Keyword.EIGHT -> "acht"
			Keyword.EVERYTHING -> "alles"
			Keyword.FIFTH -> "fünfte"
			Keyword.FIRST -> "erste"
			Keyword.FORGET -> "vergiss"
			Keyword.FOURTH -> "vierte"
			Keyword.FUNCTION -> "funktion"
			Keyword.GIVE -> "gib"
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
			Keyword.REMEMBER -> "merke"
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
	}
