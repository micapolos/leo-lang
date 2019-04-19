package leo32.base

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class TreeCursorTest {
	@Test
	fun leafNextOrNull() {
		tree(1).cursor.nextOrNull.assertEqualTo(null)
	}

	@Test
	fun branchNextOrNull() {
		val tree = tree(tree(1), tree(2))

		tree
			.cursor
			.nextOrNull
			.assertEqualTo(tree.cursor traceTo zero.bit cursorTo tree(1))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull
			.assertEqualTo(tree.cursor traceTo one.bit cursorTo tree(2))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull
			.assertEqualTo(null)
	}

	@Test
	fun deepBranchNextOrNull() {
		val tree = tree(tree(tree(1), tree(2)), tree(3))

		tree
			.cursor
			.nextOrNull!!
			.tree
			.assertEqualTo(tree(tree(1), tree(2)))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull!!
			.tree
			.assertEqualTo(tree(1))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull!!
			.tree
			.assertEqualTo(tree(2))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull!!
			.tree
			.assertEqualTo(tree(3))

		tree
			.cursor
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull!!
			.nextOrNull
			.assertEqualTo(null)
	}

	@Test
	fun nextLeafOrNull() {
		val tree = tree(tree(tree(1), tree(2)), tree(3))

		tree
			.cursor
			.nextLeafOrNull!!
			.tree
			.assertEqualTo(tree(1))

		tree
			.cursor
			.nextLeafOrNull!!
			.nextLeafOrNull!!
			.tree
			.assertEqualTo(tree(2))

		tree
			.cursor
			.nextLeafOrNull!!
			.nextLeafOrNull!!
			.nextLeafOrNull!!
			.tree
			.assertEqualTo(tree(3))

		tree
			.cursor
			.nextLeafOrNull!!
			.nextLeafOrNull!!
			.nextLeafOrNull!!
			.nextLeafOrNull
			.assertEqualTo(null)
	}
}