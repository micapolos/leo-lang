package leo.base

data class Tree<V>(
	val value: V,
	val previousSiblingOrNull: Tree<V>?,
	val lastChildOrNull: Tree<V>?)

fun <V> tree(): Tree<V>? =
	null

fun <V> tree(value: V, lastChildOrNull: Tree<V>? = null): Tree<V> =
	Tree(value, null, lastChildOrNull)

fun <V> Tree<V>?.plusSibling(value: V, lastChildOrNull: Tree<V>? = null): Tree<V> =
	Tree(value, this, lastChildOrNull)
