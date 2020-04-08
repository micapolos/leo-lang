package leo14.untyped.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class BlockTest {
	@Test
	fun apply() {
		constant(10).block
			.apply { inc() }
			.assertEqualTo(constant(11).block)

		dynamic { 10 }.assertEvaluatedOnce.block
			.apply { inc() }
			.value
			.assertEqualTo(11)
	}

	@Test
	fun apply2() {
		constant(10).block
			.apply(constant(20).block) { this + it }
			.assertEqualTo(constant(30).block)

		constant(10).block
			.apply(dynamic { 20 }.assertEvaluatedOnce.block) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatedOnce.block
			.apply(constant(20).block) { this + it }
			.value
			.assertEqualTo(30)

		dynamic { 10 }.assertEvaluatedOnce.block
			.apply(dynamic { 20 }.assertEvaluatedOnce.block) { this + it }
			.value
			.assertEqualTo(30)
	}
}