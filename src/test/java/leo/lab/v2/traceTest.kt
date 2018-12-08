package leo.lab.v2

import leo.base.assertEqualTo
import leo.childWord
import leo.parentWord
import leo.twoWord
import org.junit.Test

class TraceTest {
	@Test
	fun parent_direct() {
		trace(pattern(parentWord caseTo pattern()))
			.plus(parent.jump)
			.plus(pattern(childWord caseTo pattern()))
			.parentOrNull
			.assertEqualTo(trace(pattern(parentWord caseTo pattern())))
	}

	@Test
	fun parent_indirect() {
		trace(pattern(parentWord caseTo pattern()))
			.plus(parent.jump)
			.plus(pattern(childWord caseTo pattern()))
			.plus(sibling.jump)
			.plus(pattern(twoWord caseTo pattern()))
			.parentOrNull
			.assertEqualTo(trace(pattern(parentWord caseTo pattern())))
	}
}