package leo15.lambda.runtime.java

import leo.base.ifNotNull
import leo14.literalString
import leo15.lambda.runtime.*

val Java.code
	get(): String =
		when (this) {
			NullJava -> "null"
			is PrintingJava -> "fn { it.also { println(${"$label: \$it".literalString}) } }"
			is IntJava -> int.literalString
			is StringJava -> string.literalString
			StringPlusStringJava -> "fn { x -> fn { y -> (x as String) + (y as String) } }"
			IntPlusIntJava -> "fn { x -> fn { y -> (x as Int) + (y as Int) } }"
			IntMinusIntJava -> "fn { x -> fn { y -> (x as Int) - (y as Int) } }"
			IntTimesIntJava -> "fn { x -> fn { y -> (x as Int) * (y as Int) } }"
			IntIncJava -> "fn { (it as Int).inc() }"
			IntDecJava -> "fn { (it as Int).dec() }"
			IntInvJava -> "fn { (it as Int).inv() }"
			IntAndIntJava -> "fn { x -> fn { y -> (x as Int) and (y as Int) } }"
			IntOrIntJava -> "fn { x -> fn { y -> (x as Int) or (y as Int) } }"
			IntXorIntJava -> "fn { x -> fn { y -> (x as Int) xor (y as Int) } }"
			IntIfZero -> "fn { i -> if ((i as Int) == 0) ... }"
			StringLengthJava -> "fn { (it as String).length }"
			is Int_PlusIntJava -> "fn { ${int.literalString} + (it as Int) }"
			is Int_MinusIntJava -> "fn { ${int.literalString} - (it as Int) }"
			is Int_TimesIntJava -> "fn { ${int.literalString} * (it as Int) }"
			is Int_AndIntJava -> "fn { ${int.literalString} and (it as Int) }"
			is Int_OrIntJava -> "fn { ${int.literalString} or (it as Int) }"
			is Int_XorIntJava -> "fn { ${int.literalString} xor (it as Int) }"
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
