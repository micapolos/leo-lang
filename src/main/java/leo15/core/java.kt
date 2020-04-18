package leo15.core

import leo14.ScriptLine
import leo14.untyped.typed.valueJavaScriptLine
import leo15.lambda.Term
import leo15.lambda.value
import leo15.lambda.valueTerm

val <T : Any> Class<T>.javaTyp: Typ<Java<T>>
	get() =
		Typ(valueJavaScriptLine) {
			Java<T>(this)
		}

val stringTyp = String::class.java.javaTyp
val intTyp = Integer::class.java.javaTyp

data class Java<T : Any>(override val term: Term) : Leo<Java<T>>() {
	override val typ: Typ<Java<T>> get() = term.value!!.run { (javaClass as Class<*>).javaTyp as Typ<Java<T>> }
	override val scriptLine: ScriptLine get() = term.value.valueJavaScriptLine
	val value: T get() = term.value as T
}

val <T : Any> T.anyJava get() = Java<T>(valueTerm)
val Int.java: Java<Integer> get() = anyJava as Java<Integer>
val String.java get() = anyJava
