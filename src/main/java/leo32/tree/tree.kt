package leo32.tree

import leo.binary.Bit
import leo32.base.Branch
import leo32.base.Leaf
import leo32.base.Link
import leo32.base.at

sealed class Tree<out V>

data class LeafTree<V>(
	val leaf: Leaf<V>
) : Tree<V>()

data class LinkTree<V>(
	val link: Link<Tree<V>>
) : Tree<V>()

data class BranchTree<V>(
	val branch: Branch<Tree<V>>
) : Tree<V>()

fun <V> tree(leaf: Leaf<V>) = LeafTree(leaf)
fun <V> tree(link: Link<Tree<V>>) = LinkTree(link)
fun <V> tree(branch: Branch<Tree<V>>) = BranchTree(branch)

fun <V> Tree<V>.at(bit: Bit) =
	when (this) {
		is LeafTree -> null
		is LinkTree -> link.at(bit)
		is BranchTree -> branch.at(bit)
	}