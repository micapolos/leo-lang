package leo32.runtime

import leo.base.*
import leo32.base.*
import leo32.base.List

data class Key(
	val i32List: List<I32>) {
	override fun toString() = appendableString { it.append(this) }
}

val List<I32>.key get() =
	Key(this)

val Empty.key get() =
	list<I32>().key

fun Key.plus(i32: I32) =
	i32List.add(i32).key

fun key(string: String) =
	empty.key.fold(string.codePointSeq) { plus(it.i32) }

fun Appendable.append(key: Key): Appendable =
	fold(key.i32List.seq) { append(it.int.codePointString) }

val Key.i32Seq get() =
	i32List.seq

fun <V: Any> Empty.keyDict() =
	dict<Key, V> { i32Seq.dictKey }
