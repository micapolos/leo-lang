package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import org.junit.Test

class RecursionTest {
	private val testTrace = trace(pattern(parentWord caseTo pattern()))
		.plus(parent.jump).plus(pattern(childWord caseTo pattern()))
		.plus(sibling.jump).plus(pattern(oneWord caseTo pattern()))
		.plus(sibling.jump).plus(pattern(twoWord caseTo pattern()))
		.plus(parent.jump).plus(pattern(leafWord caseTo pattern()))

	@Test
	fun apply_parent() {
		recursion(parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!)
	}

	@Test
	fun apply_sibling_parent() {
		recursion(sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!.siblingOrNull!!)
	}

	@Test
	fun apply_parent_sibling_parent() {
		recursion(parent.jump, sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!.siblingOrNull!!.parentOrNull!!)
	}

	@Test
	fun apply_sibling_sibling_parent() {
		recursion(sibling.jump, sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!.siblingOrNull!!.siblingOrNull!!)
	}

	@Test
	fun apply_parent_sibling_sibling_parent() {
		recursion(parent.jump, sibling.jump, sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!.siblingOrNull!!.siblingOrNull!!.parentOrNull!!)
	}

	@Test
	fun apply_sibling_sibling_sibling_parent() {
		recursion(sibling.jump, sibling.jump, sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(null)
	}

	@Test
	fun apply_parent_parent_sibling_sibling_parent() {
		recursion(parent.jump, parent.jump, sibling.jump, sibling.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(null)
	}

	@Test
	fun applyParentParent() {
		recursion(parent.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(testTrace.parentOrNull!!.parentOrNull!!)
	}

	@Test
	fun apply_parent_parent_parent() {
		recursion(parent.jump, parent.jump, parent.jump)
			.apply(testTrace)
			.assertEqualTo(null)
	}
}