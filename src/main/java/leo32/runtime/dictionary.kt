package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.orIfNull
import leo32.Seq32
import leo32.base.*

data class Dictionary<out T: Any>(
	val tree: Tree<T?>)

val <T: Any> Tree<T?>.dictionary get() =
	Dictionary(this)

fun <T: Any> Empty.dictionary() =
	tree<T>().dictionary

fun <T: Any> Dictionary<T>.plus(key: TermField): Dictionary<T> =
	plus32(key.seq32)

fun <T: Any> Dictionary<T>.plus(key: Term): Dictionary<T> =
	plus32(key.seq32)

fun <T: Any> Dictionary<T>.plus32(key: Seq32): Dictionary<T> =
	tree.at32(key)?.dictionary.orIfNull { empty.dictionary() }

fun <T: Any> Dictionary<T>.put(key: Term, value: T) =
	put32(key.seq32, value)

fun <T: Any> Dictionary<T>.put32(key: Seq32, value: T): Dictionary<T> =
	tree.put32(key to value).dictionary

val <T: Any> Dictionary<T>.valueOrNull get() =
	tree.leafOrNull?.value

fun <T: Any> Dictionary<T>.at(term: Term): T? =
	tree.at32(term.seq32)?.valueOrNull