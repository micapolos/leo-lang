package leo14.typed.compiler

data class Dictionary(
	val action: String,
	val arrow: String,
	val `as`: String,
	val choice: String,
	val `do`: String,
	val does: String,
	val forget: String,
	val give: String,
	val `is`: String,
	val it: String,
	val match: String,
	val native: String,
	val remember: String)

val englishDictionary = Dictionary(
	action = "action",
	arrow = "arrow",
	`as` = "as",
	choice = "choice",
	`do` = "do",
	does = "does",
	forget = "forget",
	give = "give",
	`is` = "is",
	it = "it",
	match = "match",
	native = "native",
	remember = "remember")

val polishDictionary = Dictionary(
	action = "czynność",
	arrow = "strzałka",
	`as` = "jako",
	choice = "wybór",
	`do` = "zrób",
	does = "robi",
	forget = "zapomnij",
	give = "daj",
	`is` = "to",
	it = "że",
	match = "wybierz",
	native = "natywny",
	remember = "zapamiętaj")

val defaultDictionary = englishDictionary
