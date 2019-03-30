package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.bitSeq
import leo.binary.utf8BitSeq

data class Dict(
	val intOrNullTree: Tree<Int?>,
	val nextInt: Int)

val emptyDict =
	Dict(nullOf<Int>().leaf.tree, 0)

// TODO: Optimize to lazy evaluation
val String.dictBitSeq: Seq<Bit>
	get() = this
		.replace("\\", "\\\\")
		.replace("\u0000", "\\0")
		.utf8BitSeq
		.then { 0.clampedByte.bitSeq }

fun Dict.effectAt(string: String): Effect<Dict, Int> =
	intOrNullTree.updateEffect(string.dictBitSeq) {
		when (this) {
			is LeafTree ->
				if (leaf.value == null) nextInt.orNull.leaf.tree.effect(nextInt)
				else effect(leaf.value)
			is BranchTree -> fail()
		}
	}.let { treeEffect ->
		if (treeEffect.value == nextInt)
			copy(
				intOrNullTree = treeEffect.target,
				nextInt = nextInt.inc()).effect(nextInt)
		else effect(treeEffect.value)
	}

fun Dict.at(string: String): Int =
	effectAt(string).value

fun Dict.with(string: String): Dict =
	effectAt(string).target