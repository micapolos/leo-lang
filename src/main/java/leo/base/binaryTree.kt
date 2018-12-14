package leo.base

sealed class BinaryTree<out V>

data class LeafBinaryTree<out V>(
	val value: V) : BinaryTree<V>()

data class InnerBinaryTree<out V>(
	val zeroBinaryTree: BinaryTree<V>,
	val oneBinaryTree: BinaryTree<V>) : BinaryTree<V>()

fun <V> binaryTree(value: V): BinaryTree<V> =
	LeafBinaryTree(value)

fun <V> binaryTree(zeroBinaryTree: BinaryTree<V>, oneBinaryTree: BinaryTree<V>): BinaryTree<V> =
	InnerBinaryTree(zeroBinaryTree, oneBinaryTree)

fun <V, R> BinaryTree<V>.map(fn: (V) -> R): BinaryTree<R> =
	when (this) {
		is LeafBinaryTree -> binaryTree(fn(value))
		is InnerBinaryTree -> binaryTree(zeroBinaryTree.map(fn), oneBinaryTree.map(fn))
	}

fun BinaryTree<*>.isLike(binaryTree: BinaryTree<*>): Boolean =
	when (this) {
		is LeafBinaryTree -> binaryTree is LeafBinaryTree
		is InnerBinaryTree -> binaryTree is InnerBinaryTree
			&& zeroBinaryTree.isLike(binaryTree.zeroBinaryTree)
			&& oneBinaryTree.isLike(binaryTree.oneBinaryTree)
	}