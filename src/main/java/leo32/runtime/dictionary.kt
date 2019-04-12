package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.Tree

data class Dictionary<out T: Any>(
	val tree: Tree<DictionaryEntry<T>?>) {
	override fun toString() = appendableString {
		it.append(this) { value ->
			append(value.string)
		}
	}
}

data class DictionaryEntry<out T : Any>(
	val key: Term,
	val value: T)

infix fun <T : Any> Term.entryTo(value: T) =
	DictionaryEntry(this, value)

val <T : Any> Tree<DictionaryEntry<T>?>.dictionary
	get() =
	Dictionary(this)

fun <T: Any> Empty.dictionary() =
	tree<DictionaryEntry<T>>().dictionary

fun <T: Any> Dictionary<T>.plus(key: TermField): Dictionary<T> =
	plus32(key.seq32)

fun <T: Any> Dictionary<T>.plus(key: Term): Dictionary<T> =
	plus32(key.seq32)

fun <T: Any> Dictionary<T>.plus32(key: Seq32): Dictionary<T> =
	tree.at32(key)?.dictionary.orIfNull { empty.dictionary() }

fun <T: Any> Dictionary<T>.put(key: Term, value: T) =
	put32(key.seq32, key entryTo value)

fun <T : Any> Dictionary<T>.put32(key: Seq32, entry: DictionaryEntry<T>): Dictionary<T> =
	tree.put32(key to entry).dictionary

val <T: Any> Dictionary<T>.valueOrNull get() =
	tree.leafOrNull?.value

fun <T : Any> Dictionary<T>.entryAt(term: Term): DictionaryEntry<T>? =
	tree.at32(term.seq32)?.valueOrNull

fun <T : Any> Dictionary<T>.at(term: Term): T? =
	entryAt(term)?.value

fun <T : Any> Appendable.append(entry: DictionaryEntry<T>, fn: Appendable.(T) -> Appendable): Appendable =
	append(entry.key).append(" to ").fn(entry.value)

fun <T : Any> Appendable.append(dictionary: Dictionary<T>, fn: Appendable.(T) -> Appendable): Appendable =
	this
		.append("dictionary")
		.foldValues(dictionary.tree) {
			if (it != null) append(".put(").append(it.key).append(" to ").fn(it.value).append(")")
			else this
		}

