package leo.term

import leo.base.Stack
import leo.base.map
import leo.base.string
import leo.theWord

data class List<out V>(
	val termStack: Stack<Term<V>>) {
	override fun toString() = string { append(it) }
}

val <V> Stack<Term<V>>.list
	get() =
		List(this)

val <V> List<V>.structure: Structure<V>
	get() =
		termStack.map { theWord fieldTo it }.structure

fun <V> Appendable.append(list: List<V>): Appendable =
	append(list.structure)