package leo32.tree

import leo.binary.*
import leo3.*
import leo32.base.*

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

fun <V> Tree<V>.update(bit: Bit, fn: Tree<V>?.() -> Tree<V>): Tree<V> =
	when (this) {
		is LeafTree ->
			tree(link(bit, null.fn()))
		is LinkTree ->
			if (link.bit == bit) tree(link(bit, link.value.fn()))
			else tree(branch(bit, null.fn(), link.value))
		is BranchTree ->
			tree(branch(bit, branch.at(bit).fn(), branch.at(bit.inverse)))
	}

fun <V> Writer.write(tree: Tree<V>, writeValueFn: WriteValueFn<V>): Writer =
	when (tree) {
		is LeafTree -> write(bit(zero)).write(tree.leaf, writeValueFn)
		is LinkTree -> write(bit(one)).write(bit(zero)).write(tree.link) { write(it, writeValueFn) }
		is BranchTree -> write(bit(one)).write(bit(one)).write(tree.branch) { write(it, writeValueFn) }
	}

fun <V> Reader.readTree(readValue: ReadFn<V>): Read<Tree<V>> =
	readBit().let { readBit0 ->
		if (readBit0.value.isZero) readBit0.reader.readLeaf(readValue).map { tree(it) }
		else readBit0.reader.readBit().let { readBit1 ->
			if (readBit1.value.isZero) readBit1.reader.readLink { readTree(readValue) }.map { tree(it) }
			else readBit1.reader.readBranch { readTree(readValue) }.map { tree(it) }
		}
	}
