package leo32.base

import leo.base.Seq
import leo.base.fold
import leo.base.orNull
import leo.base.seqNodeOrNull
import leo.binary.Bit

sealed class Tree<T>

data class LeafTree<T>(
	val leaf: Leaf<T>) : Tree<T>()

data class BranchTree<T>(
	val branch: Branch<Tree<T>>) : Tree<T>()

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

fun <T> Tree<T>.at(bitSeq: Seq<Bit>): Tree<T>? =
	orNull.fold(bitSeq) { this?.at(it) }

fun <T : Any, R> Tree<T?>.updateEffect(bit: Bit, fn: Tree<T?>.() -> Effect<Tree<T?>, R>): Effect<Tree<T?>, R> =
	branch.updateEffect(bit, fn).mapTarget { tree }

fun <T : Any, R> Tree<T?>.updateEffect(bitSeq: Seq<Bit>, fn: Tree<T?>.() -> Effect<Tree<T?>, R>): Effect<Tree<T?>, R> =
	bitSeq.seqNodeOrNull.let { seqNodeOrNull ->
		if (seqNodeOrNull == null) fn()
		else branch.updateEffect(seqNodeOrNull.first) {
			updateEffect(seqNodeOrNull.remaining, fn)
		}.mapTarget { tree }
	}
