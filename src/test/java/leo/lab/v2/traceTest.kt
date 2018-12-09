package leo.lab.v2

import leo.base.assertEqualTo
import leo.childWord
import leo.parentWord
import leo.twoWord
import org.junit.Test

class TraceTest {
	@Test
	fun parent_direct() {
		trace(function(parentWord caseTo function()))
			.plus(parent.jump)
			.plus(function(childWord caseTo function()))
			.parentOrNull
			.assertEqualTo(trace(function(parentWord caseTo function())))
	}

	@Test
	fun parent_indirect() {
		trace(function(parentWord caseTo function()))
			.plus(parent.jump)
			.plus(function(childWord caseTo function()))
			.plus(sibling.jump)
			.plus(function(twoWord caseTo function()))
			.parentOrNull
			.assertEqualTo(trace(function(parentWord caseTo function())))
	}
}