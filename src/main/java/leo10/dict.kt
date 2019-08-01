package leo10

import leo.base.*
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.isZero
import leo4.wordBitSeq
import leo9.Stack
import leo9.push
import leo9.stack

sealed class Dict<out T : Any>
data class EmptyDict<T : Any>(val empty: Empty) : Dict<T>()
data class LinkDict<T : Any>(val link: DictLink<T>) : Dict<T>()

sealed class DictLink<out T : Any>
data class LeafDictLink<T : Any>(val leaf: DictLeaf<T>) : DictLink<T>()
data class CaseDictLink<T : Any>(val case: DictCase<T>) : DictLink<T>()
data class BranchDictLink<T : Any>(val branch: DictBranch<T>) : DictLink<T>()

data class DictLeaf<out T : Any>(val value: T)
data class DictCase<out T : Any>(val bit: Bit, val link: DictLink<T>)
data class DictBranch<out T : Any>(val at0: DictLink<T>, val at1: DictLink<T>)

data class DictEntry<out T : Any>(val key: Stack<Bit>, val value: T)

// constructors

fun <T : Any> dict(empty: Empty): Dict<T> = EmptyDict(empty)
fun <T : Any> dict(link: DictLink<T>): Dict<T> = LinkDict(link)

fun <T : Any> link(leaf: DictLeaf<T>): DictLink<T> = LeafDictLink(leaf)
fun <T : Any> link(case: DictCase<T>): DictLink<T> = CaseDictLink(case)
fun <T : Any> link(branch: DictBranch<T>): DictLink<T> = BranchDictLink(branch)

fun <T : Any> leaf(value: T) = DictLeaf(value)
fun <T : Any> case(bit: Bit, dict: DictLink<T>) = DictCase(bit, dict)
fun <T : Any> branch(at0: DictLink<T>, at1: DictLink<T>) = DictBranch(at0, at1)

fun <T : Any> dictEntry(key: Stack<Bit>, value: T) = DictEntry(key, value)

// at

val <T : Any> Dict<T>.linkOrNull get() = (this as? LinkDict)?.link
val <T : Any> DictLink<T>.leafOrNull get() = (this as? LeafDictLink)?.leaf

fun <T : Any> DictLink<T>.at(bit: Bit) =
	when (this) {
		is CaseDictLink -> case.at(bit)
		is BranchDictLink -> branch.at(bit)
		is LeafDictLink -> null
	}

fun <T : Any> DictLink<T>.at(bitSeq: Seq<Bit>) = orNull.fold(bitSeq) { this?.at(it) }

fun <T : Any> DictCase<T>.at(bit: Bit) = notNullIf(bit == this.bit) { link }
fun <T : Any> DictBranch<T>.at(bit: Bit) = if (bit.isZero) at0 else at1

fun <T : Any> Dict<T>.at(bit: Bit) = linkOrNull?.at(bit)?.let { dict(it) } ?: dict(empty)
fun <T : Any> Dict<T>.at(name: String) = linkOrNull?.at(name.wordBitSeq)?.leafOrNull?.value

// remove

fun <T : Any> Dict<T>.remove(bit: Bit) =
	when (this) {
		is EmptyDict -> empty.dictRemove(bit)
		is LinkDict -> link.dictRemove(bit)
	}

fun <T : Any> Empty.dictRemove(bit: Bit) =
	dict<T>(this)

fun <T : Any> DictLink<T>.dictRemove(bit: Bit) =
	when (this) {
		is LeafDictLink -> dict(empty)
		is CaseDictLink -> case.dictRemove(bit)
		is BranchDictLink -> branch.dictRemove(bit)
	}

fun <T : Any> DictCase<T>.dictRemove(bit: Bit): Dict<T> =
	if (bit == this.bit) dict(empty)
	else dict(link(this))

fun <T : Any> DictBranch<T>.dictRemove(bit: Bit): Dict<T> =
	if (bit.isZero) dict(link(case(bit1, at1)))
	else dict(link(case(bit0, at0)))

// set link

fun <T : Any> Dict<T>.set(bit: Bit, link: DictLink<T>) =
	when (this) {
		is EmptyDict -> dict(link(case(bit, link)))
		is LinkDict -> this.link.dictSet(bit, link)
	}

fun <T : Any> DictLink<T>.dictSet(bit: Bit, link: DictLink<T>) =
	when (this) {
		is LeafDictLink -> dict(link(case(bit, link)))
		is CaseDictLink -> case.dictSet(bit, link)
		is BranchDictLink -> branch.dictSet(bit, link)
	}

fun <T : Any> DictCase<T>.dictSet(bit: Bit, link: DictLink<T>) =
	if (bit == this.bit) dict(link(case(bit, link)))
	else if (bit.isZero) dict(link(branch(link, this.link)))
	else dict(link(branch(this.link, link)))

fun <T : Any> DictBranch<T>.dictSet(bit: Bit, link: DictLink<T>) =
	if (bit.isZero) dict(link(branch(link, at1)))
	else dict(link(branch(at0, link)))

// set

fun <T : Any> Dict<T>.set(bit: Bit, dict: Dict<T>) =
	when (dict) {
		is EmptyDict -> remove(bit)
		is LinkDict -> set(bit, dict.link)
	}

// update

fun <T : Any> Dict<T>.update(bit: Bit, fn: Dict<T>.() -> Dict<T>) =
	set(bit, at(bit).fn())

fun <T : Any> Dict<T>.update(bitSeq: Seq<Bit>, fn: Dict<T>.() -> Dict<T>): Dict<T> =
	bitSeq.nodeOrNull.let { bitSeqNodeOrNull ->
		if (bitSeqNodeOrNull == null) fn()
		else update(bitSeqNodeOrNull.first) {
			update(bitSeqNodeOrNull.remaining, fn)
		}
	}

fun <T : Any> Dict<T>.set(name: String, value: T) =
	update(name.wordBitSeq) { dict(link(leaf(value))) }

// fold

val <T : Any> Dict<T>.entryStack
	get() =
		stack<DictEntry<T>>().foldEntries(this) { push(it) }

fun <R, T : Any> R.foldEntries(dict: Dict<T>, fn: R.(DictEntry<T>) -> R): R =
	foldEntries(stack(), dict, fn)

fun <R, T : Any> R.foldEntries(key: Stack<Bit>, dict: Dict<T>, fn: R.(DictEntry<T>) -> R): R =
	when (dict) {
		is EmptyDict -> this
		is LinkDict -> foldEntries(key, dict.link, fn)
	}

fun <R, T : Any> R.foldEntries(key: Stack<Bit>, link: DictLink<T>, fn: R.(DictEntry<T>) -> R): R =
	when (link) {
		is LeafDictLink ->
			fn(dictEntry(key, link.leaf.value))
		is CaseDictLink ->
			foldEntries(key.push(link.case.bit), link.case.link, fn)
		is BranchDictLink ->
			this
				.foldEntries(key.push(bit0), link.branch.at0, fn)
				.foldEntries(key.push(bit1), link.branch.at1, fn)
	}

val Dict<*>.size get() = 0.foldEntries(this) { inc() }
