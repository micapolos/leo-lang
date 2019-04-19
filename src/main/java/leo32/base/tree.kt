@file:Suppress("unused")

package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo32.Seq32
import leo32.bitSeq

sealed class Tree<out T>

data class LeafTree<T>(
	val leaf: Leaf<T>) : Tree<T>()

data class BranchTree<T>(
	val branch: Branch<Tree<T>>) : Tree<T>()

fun <T> tree(value: T) =
	value.leaf.tree

fun <T> tree(at0: Tree<T>, at1: Tree<T>) =
	BranchTree(branch(at0, at1))

fun <T: Any> Empty.tree() =
	nullOf<T>().leaf.tree

val <T> Leaf<T>.tree: Tree<T>
	get() =
		LeafTree(this)

val <T> Branch<Tree<T>>.tree
	get() =
		BranchTree(this)

val <T> Tree<T>.leafOrNull
	get() =
		(this as? LeafTree<T>)?.leaf

val <T> Tree<T>.branchOrNull
	get() =
		(this as? BranchTree<T>)?.branch

val <T> Tree<T>.theValueOrNull
	get() =
		when (this) {
			is LeafTree -> leaf.value.the
			is BranchTree -> null
		}

val <T : Any> Tree<T?>.branch: Branch<Tree<T?>>
	get() =
		when (this) {
			is LeafTree -> (null as T?).leaf.tree.fullBranch
			is BranchTree -> branch
		}

fun <T> Tree<T>.at(bit: Bit): Tree<T>? =
	when (this) {
		is LeafTree -> null
		is BranchTree -> branch.at(bit)
	}

fun <T> Tree<T>.updateAt(bit: Bit, fn: Tree<T>.() -> Tree<T>): Tree<T> =
	when (this) {
		is LeafTree -> branch(bit, fn(), this).tree
		is BranchTree -> branch.update(bit, fn).tree
	}

fun <T> Tree<T>.at(bitSeq: Seq<Bit>): Tree<T>? =
	orNull.fold(bitSeq) { this?.at(it) }

fun <T : Any, R> Tree<T?>.updateEffect(bit: Bit, fn: Tree<T?>.() -> Effect<Tree<T?>, R>): Effect<Tree<T?>, R> =
	branch.updateEffect(bit, fn).mapTarget { tree }

fun <T : Any, R> Tree<T?>.updateEffect(bitSeq: Seq<Bit>, fn: Tree<T?>.() -> Effect<Tree<T?>, R>): Effect<Tree<T?>, R> {
	val seqNodeOrNull = bitSeq.seqNodeOrNull
	return if (seqNodeOrNull == null) fn()
	else updateEffect(seqNodeOrNull.first) {
		updateEffect(seqNodeOrNull.remaining, fn)
	}
}

fun <T> Tree<T>.updateWithDefault(bitSeq: Seq<Bit>, defaultFn: () -> T, fn: Tree<T>.() -> Tree<T>): Tree<T> =
	cursor.toWithDefault(bitSeq, defaultFn).update(fn).collapse

fun <T> Tree<T>.at32(seq32: Seq32): Tree<T>? =
	at(seq32.map { bitSeq }.flat)

fun <T: Any> Tree<T?>.put32(pair: Pair<Seq32, T>): Tree<T?> =
	updateWithDefault(pair.first.bitSeq, { null }) { pair.second.leaf.tree }

fun <T: Any> Tree<T?>.put(bitSeq: Seq<Bit>, value: T) =
	updateWithDefault(bitSeq, { null }) { value.leaf.tree }

val <T: Any> Tree<T?>.valueOrNull get() =
	leafOrNull?.value

fun <T: Any> Tree<T?>.seq32(valueSeq32: T.() -> Seq32): Seq32 =
	when (this) {
		is LeafTree -> 0.i32.onlySeq.then {
			valueOrNull?.valueSeq32().orIfNull {
				0.i32.onlySeq
			}
		}
		is BranchTree -> 1.i32.onlySeq.then {
			branch.seq32 {
				seq32(valueSeq32)
			}
		}
	}

fun <K, T : Any, R> R.foldKeyValuePairs(tree: Tree<T?>, key: K, keyFn: K.(Bit) -> K, fn: R.(Pair<K, T>) -> R): R =
	when (tree) {
		is LeafTree -> tree.leaf.value?.let { fn(key to it) } ?: this
		is BranchTree -> this
			.foldKeyValuePairs(tree.branch.at0, key.keyFn(zero.bit), keyFn, fn)
			.foldKeyValuePairs(tree.branch.at1, key.keyFn(one.bit), keyFn, fn)
	}

fun <T, R> R.fold(tree: Tree<T>, key: List<Bit>, fn: R.(Pair<List<Bit>, T>) -> R): R =
	when (tree) {
		is LeafTree -> fn(key to tree.leaf.value)
		is BranchTree -> this
			.fold(tree.branch.at0, key.add(zero.bit), fn)
			.fold(tree.branch.at1, key.add(one.bit), fn)
	}

fun <T, R> R.foldKeyed(tree: Tree<T>, fn: R.(Pair<List<Bit>, T>) -> R): R =
	fold(tree, list(), fn)

fun <T, R> R.foldValues(tree: Tree<T>, fn: R.(T) -> R): R =
	when (tree) {
		is LeafTree -> fn(tree.leaf.value)
		is BranchTree -> this
			.foldValues(tree.branch.at0, fn)
			.foldValues(tree.branch.at1, fn)
	}

val <T> Tree<T>.seq
	get() =
		cursor.treeSeq.filterMap { theValueOrNull }