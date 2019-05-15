package leo32.tree

import leo.binary.Bit
import leo.binary.inverse
import leo32.base.branch

data class TreeCursor<out V>(
	val tree: Tree<V>,
	val parentTraceOrNull: TreeParentTrace<V>?)

fun <V> treeCursor(tree: Tree<V>, parentTraceOrNull: TreeParentTrace<V>?) =
	TreeCursor(tree, parentTraceOrNull)

val <V> TreeCursor<V>.back
	get() = parentTraceOrNull?.let { parentTrace ->
		if (parentTrace.otherTreeOrNull == null)
			treeCursor(
				tree(link(parentTrace.bit, tree)),
				parentTrace.parentTraceOrNull)
		else
			treeCursor(
				tree(branch(parentTrace.bit, tree, parentTrace.otherTreeOrNull)),
				parentTrace.parentTraceOrNull)
	}

fun <V> TreeCursor<V>.at(bit: Bit) =
	tree.at(bit)?.let { treeAtBit ->
		treeCursor(treeAtBit, treeParentTrace(bit, tree.at(bit.inverse), parentTraceOrNull))
	}
