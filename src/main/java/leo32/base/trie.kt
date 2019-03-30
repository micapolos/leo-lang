package leo32.base

import leo.base.fail
import leo.base.intHsbMaskInt
import leo.binary.Bit
import leo.binary.bit
import leo.binary.isZero

sealed class Trie<T>

data class LeafTrie<T>(
	val leaf: Leaf<T>) : Trie<T>()

data class BranchTrie<T>(
	val branch: Branch<Trie<T>>) : Trie<T>()

val <T> T.fullTrie
	get() =
		trie(intHsbMaskInt)

val <T> Leaf<T>.trie
	get() =
		LeafTrie(this)

val <T> Branch<Trie<T>>.trie
	get() =
		BranchTrie(this)

fun <T> T.trie(mask: Int): Trie<T> =
	if (mask == 0) leaf.trie
	else trie(mask.ushr(1)).fullBranch.trie

val <T> Trie<T>.leaf
	get() =
		(this as LeafTrie<T>).leaf

val <T> Trie<T>.branch
	get() =
		(this as BranchTrie<T>).branch

fun <T> Trie<T>.at(bit: Bit): Trie<T> =
	when (this) {
		is LeafTrie -> fail()
		is BranchTrie -> branch.at(bit)
	}

fun <T> Trie<T>.at(index: Int, mask: Int): T =
	if (mask == 0) leaf.value
	else at(index.and(mask).bit).at(index, mask.ushr(1))

fun <T> Trie<T>.at(index: Int): T =
	at(index, intHsbMaskInt)

fun <T> Trie<T>.put(bit: Bit, mask: Bit, trie: Trie<T>): Trie<T> =
	when (this) {
		is LeafTrie ->
			fail()
		is BranchTrie ->
			if (mask.isZero) trie.fullBranch.trie
			else branch.put(bit, trie).trie
	}

fun <T> Trie<T>.put(index: Int, mask: Int, bitMask: Int, value: T): Trie<T> =
	if (bitMask == 0) value.leaf.trie
	else put(
		index.and(bitMask).bit,
		mask.and(bitMask).bit,
		at(index.and(bitMask).bit).put(index, mask, bitMask.ushr(1), value))

fun <T> Trie<T>.put(index: Int, mask: Int, value: T): Trie<T> =
	put(index, mask, intHsbMaskInt, value)
