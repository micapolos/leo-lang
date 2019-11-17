package leo14.typed.compiler

data class Dictionary(
	val action: String,
	val arrow: String,
	val `as`: String,
	val choice: String,
	val comment: String,
	val delete: String,
	val `do`: String,
	val does: String,
	val forget: String,
	val give: String,
	val giving: String,
	val `is`: String,
	val it: String,
	val match: String,
	val native: String,
	val nothing: String,
	val remember: String)

val englishDictionary = Dictionary(
	action = "action",
	arrow = "arrow",
	`as` = "as",
	choice = "choice",
	comment = "comment",
	delete = "delete",
	`do` = "do",
	does = "does",
	forget = "forget",
	give = "give",
	giving = "giving",
	`is` = "is",
	it = "it",
	match = "match",
	native = "native",
	nothing = "nothing",
	remember = "remember")

val polishDictionary = Dictionary(
	action = "czynność",
	arrow = "strzałka",
	`as` = "jako",
	choice = "wybór",
	comment = "komentarz",
	delete = "usuń",
	`do` = "zrób",
	does = "robi",
	forget = "zapomnij",
	give = "daj",
	giving = "dająca",
	`is` = "to",
	it = "że",
	match = "wybierz",
	native = "natywny",
	nothing = "nic",
	remember = "zapamiętaj")

val defaultDictionary = englishDictionary
