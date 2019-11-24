package leo14

enum class Digit {
	ZERO,
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	NINE
}

val digitList = Digit.values().toList()
val Digit.int get() = ordinal
val Int.digitOrNull get() = digitList.getOrNull(this)
val Digit.char get() = '0' + int
val Char.digitOrNull get() = minus('0').digitOrNull
