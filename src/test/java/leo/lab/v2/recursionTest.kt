package leo.lab.v2

import leo.*
import leo.base.assertEqualTo
import kotlin.test.Test

class RecursionTest {
	private val testTrace = trace(function(parentWord caseTo function()))
		.plus(parent.jump).plus(function(childWord caseTo function()))
		.plus(sibling.jump).plus(function(oneWord caseTo function()))
		.plus(sibling.jump).plus(function(twoWord caseTo function()))
		.plus(parent.jump).plus(function(leafWord caseTo function()))

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