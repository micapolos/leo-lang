package leo15.lambda.java

import leo15.lambda.runtime.*

sealed class Java
object NullJava : Java()
data class PrintingJava(val label: String) : Java()
data class IntJava(val int: Int) : Java()
data class StringJava(val string: String) : Java()
object StringPlusStringJava : Java()
object IntPlusIntJava : Java()
object IntMinusIntJava : Java()
object IntTimesIntJava : Java()
object IntAndIntJava : Java()
object IntOrIntJava : Java()
object IntXorIntJava : Java()
object IntIfZero : Java()
object StringLengthJava : Java()
data class Int_PlusIntJava(val int: Int) : Java()
data class Int_MinusIntJava(val int: Int) : Java()
data class Int_TimesIntJava(val int: Int) : Java()
data class Int_AndIntJava(val int: Int) : Java()
data class Int_OrIntJava(val int: Int) : Java()
data class Int_XorIntJava(val int: Int) : Java()
data class String_PlusStringJava(val string: String) : Java()

val Int.java: Java get() = IntJava(this)
val String.java: Java get() = StringJava(this)
val Java.int: Int get() = (this as IntJava).int
val Java.string: String get() = (this as StringJava).string

fun Java.apply(rhs: Java): Atom<Java> =
	when (this) {
		is NullJava -> null!!
		is IntJava -> null!!
		is StringJava -> null!!
		is PrintingJava -> rhs.also { println("$label: $it") }.atom
		StringPlusStringJava -> String_PlusStringJava(rhs.string).atom
		IntPlusIntJava -> Int_PlusIntJava(rhs.int).atom
		IntMinusIntJava -> Int_MinusIntJava(rhs.int).atom
		IntTimesIntJava -> Int_TimesIntJava(rhs.int).atom
		IntAndIntJava -> Int_AndIntJava(rhs.int).atom
		IntOrIntJava -> Int_OrIntJava(rhs.int).atom
		IntXorIntJava -> Int_XorIntJava(rhs.int).atom
		IntIfZero -> lambda(lambda(if (rhs.int == 0) at(1) else at(0), term(value(NullJava))))
		StringLengthJava -> IntJava(rhs.string.length).atom
		is String_PlusStringJava -> StringJava(string + rhs.string).atom
		is Int_PlusIntJava -> IntJava(int + rhs.int).atom
		is Int_MinusIntJava -> IntJava(int - rhs.int).atom
		is Int_TimesIntJava -> IntJava(int * rhs.int).atom
		is Int_AndIntJava -> IntJava(int and rhs.int).atom
		is Int_OrIntJava -> IntJava(int or rhs.int).atom
		is Int_XorIntJava -> IntJava(int xor rhs.int).atom
	}
