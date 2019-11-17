package leo14

import leo.base.assertEqualTo
import leo14.typed.compiler.polishDictionary
import leo14.typed.nativeEval
import kotlin.test.Test

class EvalTest {
	@Test
	fun evalEmpty() {
		script()
			.eval
			.assertEqualTo(script())
	}

	@Test
	fun evalStatic() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.eval
			.assertEqualTo(
				script(
					"vec" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
	}

	@Test
	fun evalGive() {
		script(
			"zero" lineTo script(),
			"give" lineTo script("one"))
			.eval
			.assertEqualTo(script("one"))
	}

	@Test
	fun evalAs() {
		script(
			"zero" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")))
			.eval
			.assertEqualTo(script("zero"))
	}

	@Test
	fun evalMatchFirst() {
		script(
			"zero" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar")))))
			.eval
			.assertEqualTo(script("foo"))
	}

	@Test
	fun evalMatchSecond() {
		script(
			"one" lineTo script(),
			"as" lineTo script("choice" lineTo script("zero", "one")),
			"match" lineTo script(
				"zero" lineTo script(
					"foo" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar"))),
				"one" lineTo script(
					"bar" lineTo script(),
					"as" lineTo script("choice" lineTo script("foo", "bar")))))
			.eval
			.assertEqualTo(script("bar"))
	}

	@Test
	fun evalGetFirst() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"x" lineTo script())
			.eval
			.assertEqualTo(script("x" lineTo script("zero")))
	}

	@Test
	fun evalGetSecond() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"y" lineTo script())
			.eval
			.assertEqualTo(script("y" lineTo script("one")))
	}

	@Test
	fun evalWrap() {
		script(
			"vec" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"z" lineTo script())
			.eval
			.assertEqualTo(
				script(
					"z" lineTo script(
						"vec" lineTo script(
							"x" lineTo script("zero"),
							"y" lineTo script("one")))))
	}

//	@Test
//	fun evalAction() {
//		script(
//			"action" lineTo script(
//				"it" lineTo script("zero"),
//				"does" lineTo script("plus" lineTo script("one"))))
//			.eval
//			.assertEqualTo(
//				script(
//					"action" lineTo script(
//						"it" lineTo script("zero"),
//						"gives" lineTo script(
//							"zero" lineTo script(),
//							"plus" lineTo script("one")))))
//	}
//
//	@Test
//	fun evalActionDo() {
//		script(
//			"action" lineTo script(
//				"it" lineTo script("zero"),
//				"does" lineTo script("plus" lineTo script("one"))),
//			"do" lineTo script("zero"))
//			.eval
//			.assertEqualTo(
//				script(
//					"zero" lineTo script(),
//					"plus" lineTo script("one")))
//	}

	@Test
	fun rememberItIsAndRemind() {
		script(
			"remember" lineTo script(
				"it" lineTo script("zero"),
				"is" lineTo script("one")),
			"zero" lineTo script())
			.eval
			.assertEqualTo(script("one"))
	}

	@Test
	fun rememberAndForget() {
		script(
			"remember" lineTo script(
				"it" lineTo script("zero"),
				"is" lineTo script("one")),
			"forget" lineTo script("zero"),
			"zero" lineTo script())
			.eval
			.assertEqualTo(script("zero"))
	}

	@Test
	fun simulateHasUsingRememberItIs() {
		script(
			"remember" lineTo script(
				"it" lineTo script("zero"),
				"is" lineTo script(
					"zero" lineTo script(),
					"as" lineTo script(
						"choice" lineTo script("zero", "one")))),
			"remember" lineTo script(
				"it" lineTo script("one"),
				"is" lineTo script(
					"one" lineTo script(),
					"as" lineTo script(
						"choice" lineTo script("zero", "one")))),
			"remember" lineTo script(
				"it" lineTo script(
					"choice" lineTo script("zero", "one"),
					"type" lineTo script()),
				"is" lineTo script(
					"bit" lineTo script())),
			"zero" lineTo script(),
			"type" lineTo script())
			.eval
			.assertEqualTo(script("bit"))
	}

	@Test
	fun evalUsingPolish() {
		script(
			"zapamiętaj" lineTo script(
				"że" lineTo script("zero"),
				"to" lineTo script("jeden")),
			"zero" lineTo script())
			.evalUsing(polishDictionary)
			.assertEqualTo(script("jeden"))
	}

	@Test
	fun evalNativeString() {
		script(line(literal("foo")))
			.nativeEval
			.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun evalNativeInt() {
		script(line(literal(2)))
			.nativeEval
			.assertEqualTo(script(literal(2)))
	}

	@Test
	fun evalNativeDouble() {
		script(line(literal(2.0)))
			.nativeEval
			.assertEqualTo(script(literal(2.0)))
	}

	@Test
	fun evalIntPlus() {
		script(
			line(literal(2)),
			"plus" lineTo script(literal(3)))
			.nativeEval
			.assertEqualTo(script(literal(5)))
	}

	@Test
	fun evalDoublePlus() {
		script(
			line(literal(2.0)),
			"plus" lineTo script(literal(3.0)))
			.nativeEval
			.assertEqualTo(script(literal(5.0)))
	}
}