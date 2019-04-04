package leo32.term

import leo.base.fold
import leo32.base.List
import leo32.base.list
import leo32.base.seq

data class Selector(
	val getterList: List<Getter>)

val List<Getter>.selector get() =
	Selector(this)

fun selector(vararg getters: Getter) =
	list(*getters).selector

fun Term.invoke(selector: Selector) =
	fold(selector.getterList.seq) { invoke(it) }

fun Selector.invoke(term: Term) =
	term.fold(getterList.seq) { invoke(it) }