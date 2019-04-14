package leo32.runtime

import leo.base.*
import leo.binary.Bit
import leo32.Seq32
import leo32.base.*
import leo32.base.List
import leo32.base.Tree
import leo32.bitSeq32
import leo32.string32

data class Dictionary<out T: Any>(
	val tree: Tree<T?>) {
	override fun toString() = appendableString {
		it.append(this) { value ->
			append(value.string)
		}
	}
}

val <T : Any> Tree<T?>.dictionary
	get() =
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

fun <T : Any> Dictionary<T>.put32(key: Seq32, value: T): Dictionary<T> =
	tree.put32(key to value).dictionary

val <T: Any> Dictionary<T>.valueOrNull get() =
	tree.leafOrNull?.value

fun <T : Any> Dictionary<T>.at(term: Term): T? =
	tree.at32(term.seq32)?.valueOrNull

fun <T : Any> Dictionary<T>.at(field: TermField): T? =
	tree.at32(field.seq32)?.valueOrNull

fun <T : Any> Appendable.append(dictionary: Dictionary<T>, fn: Appendable.(T) -> Appendable): Appendable =
	this
		.append("dictionary")
		.foldKeyed(dictionary.tree) { (key, valueOrNull) ->
			ifNotNull(valueOrNull) { value ->
				this
					.append(".put(")
					.appendKey(key)
					.append(", ")
					.fn(value)
					.append(")")
			}
		}

fun Appendable.appendKey(key: List<Bit>): Appendable =
	append(key.seq.bitSeq32.string32)