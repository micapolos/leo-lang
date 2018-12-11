package leo.term

import leo.Word
import leo.base.*

data class Operator<out V>(
	val termOrNull: Term<V>?,
	val application: Application<V>) {
	override fun toString() = string { append(it) }
}

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Operator<V> =
	Operator(this, word apply termOrNull)

val <V> Application<V>.operator: Operator<V>
	get() =
		nullOf<Term<V>>().apply(word, termOrNull)

fun <V> Operator<V>.plus(application: Application<V>): Operator<V> =
	Operator(onlyTerm, application)

fun <V> Appendable.append(operator: Operator<V>): Appendable =
	this
		.ifNotNull(operator.termOrNull) { term ->
			append(term).append(", ")
		}
		.append(operator.application)

fun <V, R> Operator<V>.map(fn: V.() -> R): Operator<R> =
	Operator(termOrNull?.map(fn), application.map(fn))

tailrec fun <V> Operator<V>.structureOrNull(reversedFieldStackOrNull: Stack<Field<V>>?): Structure<V>? {
	val fieldOrNull = application.fieldOrNull
	return if (fieldOrNull == null) null
	else {
		val acc = reversedFieldStackOrNull.push(fieldOrNull)
		if (termOrNull == null) acc.reverse.structure
		else termOrNull.operatorTermOrNull?.operator?.structureOrNull(acc)
	}
}

val <V> Operator<V>.structureOrNull: Structure<V>?
	get() =
		structureOrNull(null)
