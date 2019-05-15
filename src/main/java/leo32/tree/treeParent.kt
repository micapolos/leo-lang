package leo32.tree

import leo.binary.Bit

data class TreeParent<out V>(
	val bit: Bit,
	val otherTreeOrNull: Tree<V>?,
	val parentOrNull: TreeParent<V>?)

fun <V> parent(bit: Bit, otherTreeOrNull: Tree<V>?, parentOrNull: TreeParent<V>?) =
	TreeParent(bit, otherTreeOrNull, parentOrNull)

