package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.append

data class TreeCursor<out T>(
	val parentOrNull: TreeTrace<T>?,
	val tree: Tree<T>) {
	override fun toString() = appendableString { it.appendBits(this) }
}

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

fun <T> TreeCursor<T>.toWithDefault(bit: Bit, defaultFn: () -> T): TreeCursor<T> =
	when (tree) {
		is LeafTree -> traceTo(bit).cursorTo(defaultFn().leaf.tree)
		is BranchTree -> traceTo(bit).cursorTo(tree.branch.at(bit))
	}

fun <T> TreeCursor<T>.toWithDefault(bitSeq: Seq<Bit>, defaultFn: () -> T): TreeCursor<T> =
	fold(bitSeq) { toWithDefault(it, defaultFn) }

tailrec fun <T, R> R.foldBits(cursor: TreeCursor<T>, fn: R.(Bit) -> R): R {
	return if (cursor.parentOrNull == null) this
	else fn(cursor.parentOrNull.bit).foldBits(cursor.parentOrNull.cursor, fn)
}

inline fun <T> TreeCursor<T>.update(fn: Tree<T>.() -> Tree<T>) =
	copy(tree = tree.fn())

val <T> TreeCursor<T>.collapse get() =
	collapse()

tailrec fun <T> TreeCursor<T>.collapse(): Tree<T> =
	if (parentOrNull == null) tree
	else parentOrNull.cursor.copy(tree = parentOrNull.cursor.tree.updateAt(parentOrNull.bit) { tree }).collapse()

fun <T> Appendable.appendBits(cursor: TreeCursor<T>): Appendable =
	append("*").foldBits(cursor) {
		append("->").append(it)
	}
