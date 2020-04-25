package leo15.lambda.java

import leo.base.ifNotNull
import leo14.literalString
import leo15.lambda.runtime.*

val Java.code
	get(): String =
		when (this) {
			is PrintingJava -> "fn { it.also { println(${"$label: \$it".literalString}) } }"
			is IntJava -> int.literalString
			is StringJava -> string.literalString
			StringPlusStringJava -> "fn { x -> fn { y -> (x as String) + (y as String) } }"
			IntPlusIntJava -> "fn { x -> fn { y -> (x as Int) + (y as Int) } }"
			IntMinusIntJava -> "fn { x -> fn { y -> (x as Int) - (y as Int) } }"
			IntTimesIntJava -> "fn { x -> fn { y -> (x as Int) * (y as Int) } }"
			StringLengthJava -> "fn { (it as String).length }"
			is Int_PlusIntJava -> "fn { ${int.literalString} + (it as Int) }"
			is Int_MinusIntJava -> "fn { ${int.literalString} - (it as Int) }"
			is Int_TimesIntJava -> "fn { ${int.literalString} * (it as Int) }"
			is String_PlusStringJava -> "fn {${string.literalString} + (it as String) }"
		}

val Term<Java>.code: String get() = code(0)

fun Term<Java>.code(depth: Int): String =
	atom.code(depth).ifNotNull(applicationOrNull) { plus(it.code(depth)) }

fun Application<Java>.code(depth: Int): String =
	".invoke(${term.code(depth)})".ifNotNull(applicationOrNull) { plus(it.code(depth)) }

fun Atom<Java>.code(depth: Int): String =
	when (this) {
		is IndexAtom -> index.varCode(depth)
		is ValueAtom -> value.code
		is LambdaAtom -> "fn { ${depth.varCode} -> ${body.code(depth.inc())} }"
	}

val Int.varCode: String get() = "v$this"
fun Int.varCode(depth: Int): String = (depth - this - 1).varCode
