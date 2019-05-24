package leo32.base

import leo.base.Seq
import leo.base.then
import leo.binary.Bit
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import leo3.*

sealed class Node<out V>
data class LinkNode<V>(val link: Link<V>) : Node<V>()
data class BranchNode<V>(val branch: Branch<V>) : Node<V>()

fun <V> node(link: Link<V>): Node<V> = LinkNode(link)
fun <V> node(branch: Branch<V>): Node<V> = BranchNode(branch)

val <V> Node<V>.linkOrNull get() = (this as? LinkNode)?.link
val <V> Node<V>.branchOrNull get() = (this as? BranchNode)?.branch

fun <V : Any> Node<V>.at(bit: Bit) =
	when (this) {
		is LinkNode -> link.at(bit)
		is BranchNode -> branch.at(bit)
	}

fun <V> Writer.write(node: Node<V>, writeValueFn: WriteValueFn<V>) =
	when (node) {
		is LinkNode -> write0.write(node.link, writeValueFn)
		is BranchNode -> write1.write(node.branch, writeValueFn)
	}

fun <V> Node<V>.bitSeq(valueBitSeqFn: V.() -> Seq<Bit>): Seq<Bit> =
	Seq {
		when (this) {
			is LinkNode -> bit(zero).then(link.bitSeq(valueBitSeqFn))
			is BranchNode -> bit(one).then(branch.bitSeq(valueBitSeqFn))
		}
	}