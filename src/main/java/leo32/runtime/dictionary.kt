package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo32.base.*
import leo32.base.Tree

data class Dictionary<out T: Any>(
	val tree: Tree<T?>)

val <T : Any> Tree<T?>.dictionary
	get() =
	Dictionary(this)

fun <T: Any> Empty.dictionary() =
	tree<T>().dictionary

fun <T: Any> Dictionary<T>.plus(key: TermField): Dictionary<T> =
	plus(key.bitSeq)

fun <T: Any> Dictionary<T>.plus(key: Term): Dictionary<T> =
	plus(key.bitSeq)

fun <T : Any> Dictionary<T>.plus(key: Seq<Bit>): Dictionary<T> =
	tree.at(key)?.dictionary.orIfNull { empty.dictionary() }

fun <T: Any> Dictionary<T>.put(key: Term, value: T) =
	put(key.bitSeq, value)

fun <T : Any> Dictionary<T>.put(key: Seq<Bit>, value: T): Dictionary<T> =
	tree.put(key, value).dictionary

val <T: Any> Dictionary<T>.valueOrNull get() =
	tree.leafOrNull?.value

fun <T : Any> Dictionary<T>.at(term: Term): T? =
	tree.at(term.bitSeq)?.valueOrNull

fun <T : Any> Dictionary<T>.at(field: TermField): T? =
	tree.at(field.bitSeq)?.valueOrNull

fun <T : Any> Appendable.append(dictionary: Dictionary<T>, fn: Appendable.(T) -> Appendable): Appendable =
	this
		.append("dictionary")
		.foldPairs(dictionary) { (key, value) ->
			this
				.append(".put(")
				.append(key)
				.append(", ")
				.fn(value)
				.append(")")
		}

fun <T : Any, R> R.foldPairs(dictionary: Dictionary<T>, fn: R.(Pair<Term, T>) -> R): R =
	foldKeyed(dictionary.tree) { (bitList, valueOrNull) ->
		ifNotNull(valueOrNull) { value ->
			fn(empty.bitReader.fold(bitList.seq) { plus(it)!! }.termOrNull!! to value)
		}
	}