package leo15.lambda.java

sealed class Java
data class PrintingJava(val label: String) : Java()
data class IntJava(val int: Int) : Java()
data class StringJava(val string: String) : Java()
object StringPlusStringJava : Java()
object IntPlusIntJava : Java()
object IntMinusIntJava : Java()
object IntTimesIntJava : Java()
object StringLengthJava : Java()
data class Int_PlusIntJava(val int: Int) : Java()
data class Int_MinusIntJava(val int: Int) : Java()
data class Int_TimesIntJava(val int: Int) : Java()
data class String_PlusStringJava(val string: String) : Java()

val Int.java: Java get() = IntJava(this)
val String.java: Java get() = StringJava(this)
val Java.int: Int get() = (this as IntJava).int
val Java.string: String get() = (this as StringJava).string

fun Java.apply(rhs: Java): Java =
	when (this) {
		is PrintingJava -> rhs.also { println("$label: $it") }
		StringPlusStringJava -> String_PlusStringJava(rhs.string)
		IntPlusIntJava -> Int_PlusIntJava(rhs.int)
		IntMinusIntJava -> Int_MinusIntJava(rhs.int)
		IntTimesIntJava -> Int_TimesIntJava(rhs.int)
		StringLengthJava -> IntJava(rhs.string.length)
		is String_PlusStringJava -> StringJava(string + rhs.string)
		is Int_PlusIntJava -> IntJava(int + rhs.int)
		is Int_MinusIntJava -> IntJava(int - rhs.int)
		is Int_TimesIntJava -> IntJava(int * rhs.int)
		else -> null!!
	}
