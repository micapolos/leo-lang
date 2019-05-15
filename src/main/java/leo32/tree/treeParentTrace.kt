package leo32.tree

import leo.binary.Bit

data class TreeParentTrace<out V>(
	val bit: Bit,
	val otherTreeOrNull: Tree<V>?,
	val parentTraceOrNull: TreeParentTrace<V>?)

fun <V> treeParentTrace(bit: Bit, otherTreeOrNull: Tree<V>?, parentTraceOrNull: TreeParentTrace<V>?) =
	TreeParentTrace(bit, otherTreeOrNull, parentTraceOrNull)

