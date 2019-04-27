package leo32.base

import leo.base.Seq
import leo.base.emptySeq
import leo.base.fold
import leo.base.then
import leo.binary.Bit
import leo.binary.bit
import leo.binary.zero

data class TreeCursor<out T>(
	val traceOrNull: TreeTrace<T>?,
	val tree: Tree<T>)

val <T> Tree<T>.cursor get() =
	null.cursorTo(this)

infix fun <T> TreeTrace<T>?.cursorTo(tree: Tree<T>): TreeCursor<T> =
	TreeCursor(this, tree)

infix fun <T> TreeCursor<T>.to(bit: Bit): TreeCursor<T>? =
	when (tree) {
		is LeafTree -> null
		is BranchTree -> tree.branch.at(bit)?.let { traceTo(bit).cursorTo(it) }
	}

fun <T> TreeCursor<T>?.to(bitSeq: Seq<Bit>): TreeCursor<T>? =
	fold(bitSeq) { this?.to(it) }

inline fun <T> TreeCursor<T>.toWithDefault(bit: Bit, defaultFn: () -> T): TreeCursor<T> =
	when (tree) {
		is LeafTree -> traceTo(bit).cursorTo(defaultFn().toLeaf.tree)
		is BranchTree -> traceTo(bit).cursorTo(tree.branch.at(bit) ?: defaultFn().toLeaf.tree)
	}

fun <T> TreeCursor<T>.toWithDefault(bitSeq: Seq<Bit>, defaultFn: () -> T): TreeCursor<T> =
	fold(bitSeq) { toWithDefault(it, defaultFn) }

tailrec fun <T, R> R.foldBits(cursor: TreeCursor<T>, fn: R.(Bit) -> R): R {
	return if (cursor.traceOrNull == null) this
	else fn(cursor.traceOrNull.bit).foldBits(cursor.traceOrNull.cursor, fn)
}

inline fun <T> TreeCursor<T>.update(fn: Tree<T>.() -> Tree<T>) =
	copy(tree = tree.fn())

val <T> TreeCursor<T>.collapse get() =
	collapse()

tailrec fun <T> TreeCursor<T>.collapse(): Tree<T> =
	if (traceOrNull == null) tree
	else traceOrNull.cursor.copy(tree = traceOrNull.cursor.tree.updateAt(traceOrNull.bit) { tree }).collapse()

val <T> TreeCursor<T>.nextOrNull
	get() =
		when (tree) {
			is LeafTree -> traceOrNull?.nextCursorOrNull
			is BranchTree -> tree.branch.at0?.let { traceTo(zero.bit).cursorTo(it) }
		}

tailrec fun <T> TreeCursor<T>.nextLeafOrNull(): TreeCursor<T>? {
	val nextOrNull = nextOrNull
	return when {
		nextOrNull == null -> null
		nextOrNull.tree.leafOrNull == null -> nextOrNull.nextLeafOrNull()
		else -> nextOrNull
	}
}

val <T> TreeCursor<T>.nextLeafOrNull
	get() =
		nextLeafOrNull()

val <T> TreeCursor<T>.treeSeq: Seq<Tree<T>>
	get() =
		Seq {
			tree.then(nextLeafOrNull?.treeSeq ?: emptySeq())
		}
