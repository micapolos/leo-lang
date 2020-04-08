package leo14.untyped.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class BlockTest {
	@Test
	fun doApply1() {
		constant(10).block
			.doApply { inc() }
			.assertEqualTo(constant(11).block)

		dynamic { 10 }.assertEvaluatesOnce.block
			.doApply { inc() }
			.value
			.assertEqualTo(11)
	}

	@Test
	fun doApply2() {
		constant(10).block
			.doApply(constant(20).block) { this + it }
			.assertEqualTo(constant(30).block)

		constant(10).block
			.doApply(dynamic { 20 }.assertEvaluatesOnce.block) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatesOnce.block
			.doApply(constant(20).block) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatesOnce.block
			.doApply(dynamic { 20 }.assertEvaluatesOnce.block) { this + it }
			.value
			.assertEqualTo(30)
	}
}