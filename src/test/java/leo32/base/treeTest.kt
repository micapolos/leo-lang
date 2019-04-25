package leo32.base

import leo.base.*
import leo.binary.bit
import leo.binary.one
import leo.binary.randomBit
import leo.binary.zero
import kotlin.test.Test

class TreeTest {
	@Test
	fun longCursor() {
		0.leaf.tree.cursor
			.iterate(1000000) { toWithDefault(randomBit) { 0 } }
			.update { 128.leaf.tree }
			.collapse
			.assertNotNull
	}

	@Test
	fun eq() {
		val tree1 = empty
			.tree<Int>()
			.put(seq(zero.bit, one.bit), 1)
			.put(seq(one.bit, one.bit), 2)

		val tree2 = empty
			.tree<Int>()
			.put(seq(one.bit, one.bit), 2)
			.put(seq(zero.bit, one.bit), 1)

		val tree3 = empty
			.tree<Int>()
			.put(seq(one.bit, one.bit), 1)
			.put(seq(zero.bit, one.bit), 1)

		tree1.eq(tree1) { this == it }.assertEqualTo(true)
		tree2.eq(tree2) { this == it }.assertEqualTo(true)
		tree3.eq(tree3) { this == it }.assertEqualTo(true)

		tree1.eq(tree2) { this == it }.assertEqualTo(true)
		tree1.eq(tree3) { this == it }.assertEqualTo(false)
		tree2.eq(tree3) { this == it }.assertEqualTo(false)
	}
}