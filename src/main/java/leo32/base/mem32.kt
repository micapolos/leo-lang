package leo32.base

import leo.base.Empty
import leo.base.fail

data class Mem32(
	val i32Tree: Tree<I32>)

val Tree<I32>.mem32
	get() =
		Mem32(this)

@Suppress("unused")
val Empty.mem32
	get() =
		emptyMem32Tree.mem32

fun Mem32.at(index: I32): I32 =
	i32Tree.mem32At(index, hsbI32)

fun Mem32.updateAt(index: I32, fn: I32.() -> I32): Mem32 =
	i32Tree.mem32UpdateAt(index, hsbI32, fn).mem32

fun Mem32.put(index: I32, value: I32): Mem32 =
	updateAt(index) { value }

// === i32 tree ===

val emptyMem32Tree =
	0.i32.leaf.tree

fun Tree<I32>.mem32At(index: I32, mask: I32): I32 =
	when (this) {
		is LeafTree -> leaf.value
		is BranchTree ->
			if (mask.isZero) fail()
			else branch.at(index.and(mask).bit).mem32At(index, mask.shr1)
	}

fun Tree<I32>.mem32UpdateAt(index: I32, mask: I32, fn: I32.() -> I32): Tree<I32> =
	if (mask.isZero)
		when (this) {
			is LeafTree -> leaf.value.fn().leaf.tree
			is BranchTree -> fail()
		}
	else
		index.and(mask).bit.let { bit ->
			mask.shr1.let { maskShr1 ->
				when (this) {
					is LeafTree ->
						branch(bit, mem32UpdateAt(index, maskShr1, fn), emptyMem32Tree)
					is BranchTree ->
						branch.update(bit) { mem32UpdateAt(index, maskShr1, fn) }
				}.tree
			}
		}
