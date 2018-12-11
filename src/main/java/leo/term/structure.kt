package leo.term

import leo.base.*

data class Structure<out V>(
	val fieldStack: Stack<Field<V>>) {
	override fun toString() = string { append(it) }
}

val <V> Stack<Field<V>>.structure: Structure<V>
	get() =
		Structure(this)

fun <V, R> Structure<V>.map(fn: V.() -> R): Structure<R> =
	fieldStack.map { it.map(fn) }.structure

val <V> Structure<V>.listOrNull: List<V>?
	get() =
		fieldStack.mapOrNull(Field<V>::listItemOrNull)?.list

val <V> Structure<V>.term: Term<V>
	get() =
		fieldStack.map(Field<V>::application).reverse.let { applicationStack ->
			applicationStack.head.term.fold(applicationStack.tail, Term<V>::plus)
		}

fun <V> Appendable.append(structure: Structure<V>): Appendable =
	append(structure.term)