package leo3

import leo.base.Empty
import leo32.base.*

data class Dict<out V : Any>(
	val tree: Tree<V?>)

fun <V : Any> dict(tree: Tree<V?>) = Dict(tree)

fun <V : Any> Empty.dict() = dict<V>(tree())

fun <V : Any> Dict<V>.put(word: Word, value: V): Dict<V> =
	dict(tree.put(word.bitSeq, value))

fun <V : Any> Dict<V>.at(word: Word): V? =
	tree.at(word.bitSeq)?.valueOrNull
