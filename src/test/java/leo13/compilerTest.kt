package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class CompilerTest {
	@Test
	fun empty() {
		compile(script())
			.assertEqualTo(script())
	}

	@Test
	fun name() {
		compile(
			script("one" lineTo script()))
			.assertEqualTo(script("one" lineTo script()))
	}

	@Test
	fun rhsLine() {
		compile(
			script("one" lineTo script("two" lineTo script())))
			.assertEqualTo(script("one" lineTo script("two" lineTo script())))
	}

	@Test
	fun lhsLine() {
		compile(
			script(
				"one" lineTo script(),
				"two" lineTo script()))
			.assertEqualTo(
				script(
					"one" lineTo script(),
					"two" lineTo script()))
	}

	@Test
	fun link() {
		compile(
			script(
				"one" lineTo script(),
				"plus" lineTo script(
					"two" lineTo script())))
			.assertEqualTo(
				script(
					"one" lineTo script(),
					"plus" lineTo script(
						"two" lineTo script())))
	}

	@Test
	fun simpleSwitch() {
		val switchLine =
			"switch" lineTo script(
				"case" lineTo script(
					"one" lineTo script(
						"jeden" lineTo script()),
					"two" lineTo script(
						"dwa" lineTo script())))

//		interpret(script("one" lineTo script(), switchLine))
//			.assertEqualTo(script("jeden" lineTo script()))
//
//		interpret(script("two" lineTo script(), switchLine))
//			.assertEqualTo(script("dwa" lineTo script()))
//
//		interpret(script("three" lineTo script(), switchLine))
//			.assertEqualTo(script("dwa" lineTo script()))
	}
}