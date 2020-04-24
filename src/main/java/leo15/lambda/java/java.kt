package leo15.lambda.java

sealed class Java
data class PrintingJava(val label: String) : Java()
data class IntJava(val int: Int) : Java()
data class StringJava(val string: String) : Java()
object PlusJava : Java()
object MinusJava : Java()
object TimesJava : Java()
object LengthJava : Java()
data class IntPlusJava(val int: Int) : Java()
data class IntMinusJava(val int: Int) : Java()
data class IntTimesJava(val int: Int) : Java()
data class StringPlusJava(val string: String) : Java()

val Int.java: Java get() = IntJava(this)
val String.java: Java get() = StringJava(this)

fun Java.apply(rhs: Java): Java =
	when (this) {
		is PrintingJava -> rhs.also { println("$label: $it") }
		PlusJava -> when (rhs) {
			is IntJava -> IntPlusJava(rhs.int)
			is StringJava -> StringPlusJava(rhs.string)
			else -> null
		}
		MinusJava -> when (rhs) {
			is IntJava -> IntMinusJava(rhs.int)
			else -> null
		}
		TimesJava -> when (rhs) {
			is IntJava -> IntTimesJava(rhs.int)
			else -> null
		}
		LengthJava -> when (rhs) {
			is StringJava -> IntJava(rhs.string.length)
			else -> null
		}
		is IntPlusJava -> when (rhs) {
			is IntJava -> IntJava(int.plus(rhs.int))
			else -> null
		}
		is IntMinusJava -> when (rhs) {
			is IntJava -> IntJava(int.minus(rhs.int))
			else -> null
		}
		is IntTimesJava -> when (rhs) {
			is IntJava -> IntJava(int.times(rhs.int))
			else -> null
		}
		is StringPlusJava -> when (rhs) {
			is StringJava -> StringJava(string.plus(rhs.string))
			else -> null
		}
		else -> null
	}!!
