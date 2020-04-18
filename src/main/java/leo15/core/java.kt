package leo15.core

import leo14.untyped.typed.valueJavaScriptLine
import leo15.lambda.Term
import leo15.lambda.value
import leo15.lambda.valueTerm

val <T : Any> Class<T>.javaTyp: Typ<Java<T>>
	get() =
		Typ(valueJavaScriptLine) {
			Java<T>(this@javaTyp, this)
		}

val stringTyp = String::class.java.javaTyp
val intTyp = Integer::class.java.javaTyp

data class Java<T : Any>(val class_: Class<T>, override val term: Term) : Leo<Java<T>>() {
	override val typ: Typ<Java<T>> get() = class_.javaTyp
	val unsafeValue: T get() = term.value as T
}

val <T : Any> T.anyJava get() = Java(javaClass, valueTerm)
val Int.java get() = valueTerm of intTyp
val String.java get() = anyJava
