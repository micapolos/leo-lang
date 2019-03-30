package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.bitSeq
import leo.binary.zero

data class Dict<out T>(
	val tree: Tree<T?>)

val <T : Any> Tree<T?>.dict
	get() =
		Dict(this)

fun <T : Any> emptyDict() =
	nullOf<T>().leaf.tree.dict

fun <T : Any> dict(vararg pairs: Pair<String, T?>) =
	emptyDict<T>().fold(pairs) { put(it.first, it.second) }

val dictEscapeByte = 27.clampedByte
val dictEscapedZeroByte = 1.clampedByte

val Byte.dictEscape: Seq<Byte>
	get() =
		when (this) {
			zero.byte -> seq(dictEscapeByte, dictEscapedZeroByte)
			dictEscapeByte -> seq(dictEscapeByte, dictEscapeByte)
			else -> seq(this)
		}

val Seq<Byte>.byteDictEscape: Seq<Byte>
	get() =
		map { dictEscape }.flatten

val Seq<Byte>.byteDictKey: Seq<Byte>
	get() =
		byteDictEscape.then { zero.byte.onlySeq }

val String.dictBitSeq: Seq<Bit>
	get() = utf8ByteArray.asList().seq.byteDictKey.map { bitSeq }.flatten

fun <T : Any> Dict<T>.at(string: String): T? =
	tree.at(string.dictBitSeq)?.leafOrNull?.value

fun <T : Any> Dict<T>.put(string: String, value: T?): Dict<T> =
	tree.update(string.dictBitSeq) { value.leaf.tree }.dict
