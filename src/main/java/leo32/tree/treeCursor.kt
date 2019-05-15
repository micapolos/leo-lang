package leo32.tree

import leo.binary.Bit
import leo.binary.inverse
import leo32.base.branch
import leo32.base.link
import leo32.base.other

data class TreeCursor<out V>(
	val tree: Tree<V>,
	val parentOrNull: TreeParent<V>?)

fun <V> cursor(tree: Tree<V>, parentOrNull: TreeParent<V>? = null) =
	TreeCursor(tree, parentOrNull)

val <V> TreeCursor<V>.back
	get() = parentOrNull?.let { parentTrace ->
		if (parentTrace.otherTreeOrNull == null)
			cursor(
				tree(link(parentTrace.bit, tree)),
				parentTrace.parentOrNull)
		else
			cursor(
				tree(branch(
					link(parentTrace.bit, tree),
					other(parentTrace.otherTreeOrNull))),
				parentTrace.parentOrNull)
	}

fun <V> TreeCursor<V>.at(bit: Bit) =
	tree.at(bit)?.let { treeAtBit ->
		cursor(
			treeAtBit,
			parent(
				bit,
				tree.at(bit.inverse),
				parentOrNull))
	}
