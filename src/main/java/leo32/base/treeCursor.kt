package leo32.base

import leo.base.Seq
import leo.base.fold
import leo.binary.Bit
import leo.binary.append
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
		is BranchTree -> traceTo(bit).cursorTo(tree.branch.at(bit))
	}

fun <T> TreeCursor<T>?.to(bitSeq: Seq<Bit>): TreeCursor<T>? =
	fold(bitSeq) { this?.to(it) }

inline fun <T> TreeCursor<T>.toWithDefault(bit: Bit, defaultFn: () -> T): TreeCursor<T> =
	when (tree) {
		is LeafTree -> traceTo(bit).cursorTo(defaultFn().leaf.tree)
		is BranchTree -> traceTo(bit).cursorTo(tree.branch.at(bit))
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

fun <T> Appendable.appendBits(cursor: TreeCursor<T>): Appendable =
	append("*").foldBits(cursor) {
		append("->").append(it)
	}

val <T> TreeCursor<T>.nextOrNull
	get() =
		when (tree) {
			is LeafTree -> traceOrNull?.nextCursorOrNull
			is BranchTree -> traceTo(zero.bit).cursorTo(tree.branch.at0)
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