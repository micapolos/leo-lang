@file:Suppress("unused")

package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.bit
import leo.binary.one
import leo.binary.zero

sealed class Tree<out T>

data class LeafTree<T>(
	val leaf: Leaf<T>) : Tree<T>()

data class BranchTree<T>(
	val branch: Branch<Tree<T>?>) : Tree<T>()

fun <T> tree(value: T) =
	value.leaf.tree

fun <T> tree(at0: Tree<T>?, at1: Tree<T>?) =
	BranchTree(branch(at0, at1))

fun <T: Any> Empty.tree() =
	nullOf<T>().leaf.tree

val <T> Leaf<T>.tree: Tree<T>
	get() =
		LeafTree(this)

val <T> Branch<Tree<T>?>.tree
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

val <T> Tree<T>.branchForUpdate: Branch<Tree<T>?>
	get() =
		when (this) {
			is LeafTree -> nullOf<Tree<T>>().fullBranch
			is BranchTree -> branch
		}

fun <T> Tree<T>.at(bit: Bit): Tree<T>? =
	when (this) {
		is LeafTree -> null
		is BranchTree -> branch.at(bit)
	}

fun <T> Tree<T>.updateAt(bit: Bit, fn: Tree<T>?.() -> Tree<T>): Tree<T> =
	when (this) {
		is LeafTree -> branch(bit, fn(), this).tree
		is BranchTree -> branchForUpdate.update(bit, fn).tree
	}

fun <T> Tree<T>.at(bitSeq: Seq<Bit>): Tree<T>? =
	orNull.fold(bitSeq) { this?.at(it) }

fun <T> Tree<T>.updateWithDefault(bitSeq: Seq<Bit>, defaultFn: () -> T, fn: Tree<T>.() -> Tree<T>): Tree<T> =
	cursor.toWithDefault(bitSeq, defaultFn).update(fn).collapse

fun <T: Any> Tree<T?>.put(bitSeq: Seq<Bit>, value: T) =
	updateWithDefault(bitSeq, { null }) { value.leaf.tree }

fun <T : Any> Tree<T?>.putTree(bitSeq: Seq<Bit>, tree: Tree<T?>) =
	updateWithDefault(bitSeq, { null }) { tree }

fun <T : Any> Tree<T?>.update(bitSeq: Seq<Bit>, fn: Tree<T?>?.() -> T): Tree<T?> =
	put(bitSeq, at(bitSeq).fn())

fun <T : Any> Tree<T?>.updateTree(bitSeq: Seq<Bit>, fn: Tree<T?>?.() -> Tree<T?>): Tree<T?> =
	putTree(bitSeq, at(bitSeq).fn())

val <T: Any> Tree<T?>.valueOrNull get() =
	leafOrNull?.value

fun <K, T, R> R.foldKeyValuePairs(tree: Tree<T>, key: K, keyFn: K.(Bit) -> K, fn: R.(Pair<K, T>) -> R): R =
	when (tree) {
		is LeafTree -> fn(key to tree.leaf.value)
		is BranchTree -> this
			.ifNotNull(tree.branch.at0) { foldKeyValuePairs(it, key.keyFn(zero.bit), keyFn, fn) }
			.ifNotNull(tree.branch.at1) { foldKeyValuePairs(it, key.keyFn(one.bit), keyFn, fn) }
	}

fun <T, R> R.fold(tree: Tree<T>, key: List<Bit>, fn: R.(Pair<List<Bit>, T>) -> R): R =
	when (tree) {
		is LeafTree -> fn(key to tree.leaf.value)
		is BranchTree -> this
			.ifNotNull(tree.branch.at0) { fold(it, key.add(zero.bit), fn) }
			.ifNotNull(tree.branch.at1) { fold(it, key.add(one.bit), fn) }
	}

fun <T, R> R.foldKeyed(tree: Tree<T>, fn: R.(Pair<List<Bit>, T>) -> R): R =
	fold(tree, list(), fn)

fun <T, R> R.foldValues(tree: Tree<T>, fn: R.(T) -> R): R =
	when (tree) {
		is LeafTree -> fn(tree.leaf.value)
		is BranchTree -> this
			.ifNotNull(tree.branch.at0) { foldValues(it, fn) }
			.ifNotNull(tree.branch.at1) { foldValues(it, fn) }
	}

val <T> Tree<T>.seq
	get() =
		cursor.treeSeq.filterMap { theValueOrNull }

fun <T> Tree<T>.eq(tree: Tree<T>, fn: T.(T) -> Boolean): Boolean =
	when (this) {
		is LeafTree ->
			when (tree) {
				is LeafTree -> leaf.value.fn(tree.leaf.value)
				is BranchTree -> false
			}
		is BranchTree ->
			when (tree) {
				is LeafTree -> false
				is BranchTree -> branch.eq(tree.branch) { treeOrNull ->
					nullableEq(treeOrNull) { childTree ->
						eq(childTree, fn)
					}
				}
			}
	}

fun <T> Tree<T>.contains(tree: Tree<T>, fn: T.(T) -> Boolean): Boolean =
	when (this) {
		is LeafTree ->
			when (tree) {
				is LeafTree -> leaf.value.fn(tree.leaf.value)
				is BranchTree -> false
			}
		is BranchTree ->
			when (tree) {
				is LeafTree -> true
				is BranchTree -> branch.contains(tree.branch) { treeOrNull ->
					nullableContains(treeOrNull) { childTree ->
						contains(childTree, fn)
					}
				}
			}
	}

fun <T : Any> Tree<T?>.all(fn: T.() -> Boolean): Boolean =
	when (this) {
		is LeafTree -> leaf.value?.fn() ?: true
		is BranchTree -> branch.all { all(fn) }
	}
