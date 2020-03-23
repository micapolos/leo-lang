package leo14.untyped

import leo14.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun raw() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script(
				"one" lineTo script()))
			.assertEvalsToThis
	}

	@Test
	fun numberPlusNumber() {
		script(
			line(literal(10)),
			"plus" lineTo script(literal(20)))
			.assertEvalsTo(line(literal(30)))
	}

	@Test
	fun numberMinusNumber() {
		script(
			line(literal(30)),
			"minus" lineTo script(literal(20)))
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun numberTimesNumber() {
		script(
			line(literal(2)),
			"times" lineTo script(literal(3)))
			.assertEvalsTo(line(literal(6)))
	}

	@Test
	fun deepMath() {
		script(
			line(literal(2)),
			"plus" lineTo script(
				line(literal(3)),
				"times" lineTo script(literal(4))))
			.assertEvalsTo(line(literal(14)))
	}

	@Test
	fun textPlusText() {
		script(
			line(literal("Hello, ")),
			"plus" lineTo script(literal("world!")))
			.assertEvalsTo(line(literal("Hello, world!")))
	}

	@Test
	fun access() {
		val point = script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))))

		point
			.plus("x" lineTo script())
			.assertEvalsTo("x" lineTo script(literal(10)))

		point
			.plus("y" lineTo script())
			.assertEvalsTo("y" lineTo script(literal(20)))
	}

	@Test
	fun autoMake() {
		script(
			"x" lineTo script(literal(10)),
			"y" lineTo script(literal(20)),
			"point" lineTo script())
			.run {
				if (!autoMake) assertEvalsToThis
				else assertEvalsTo(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20))))
			}
	}

	@Test
	fun get() {
		val point = script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))))

		point
			.plus("get" lineTo script("x" lineTo script()))
			.assertEvalsTo("x" lineTo script(literal(10)))

		point
			.plus("get" lineTo script("y" lineTo script()))
			.assertEvalsTo("y" lineTo script(literal(20)))

		point
			.plus("get" lineTo script("z" lineTo script()))
			.assertEvalsToThis
	}

	@Test
	fun accessNumber() {
		script(
			"x" lineTo script(literal(10)),
			"number" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun accessText() {
		script(
			"x" lineTo script(literal("foo")),
			"text" lineTo script())
			.assertEvalsTo(line(literal("foo")))
	}

	@Test
	fun accessFunction() {
		script(
			"my" lineTo script(
				"function" lineTo script("given")),
			"function" lineTo script())
			.assertEvalsTo(
				"function" lineTo script("given"))
	}

	@Test
	fun accessNative() {
		script(
			"hello" lineTo script(
				line(literal(123)),
				"native" lineTo script("int")),
			"native" lineTo script())
			.assertEvalsTo("native" lineTo script(literal(123.toString())))
	}

	@Test
	fun thisGivesThat() {
		script(
			"x" lineTo script(),
			"gives" lineTo script(literal(10)))
			.assertEvalsTo()
	}

	@Test
	fun thisGivesThatAndAccess() {
		script(
			"x" lineTo script(),
			"gives" lineTo script(literal(10)),
			"x" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun thisAsThat() {
		script(
			line(literal(10)),
			"as" lineTo script("foo"))
			.assertEvalsTo()
	}

	@Test
	fun thisAsThatAndAccess() {
		script(
			line(literal(10)),
			"as" lineTo script("foo"),
			"foo" lineTo script())
			.assertEvalsTo(line(literal(10)))
	}

	@Test
	fun thisDoesThat() {
		script(
			"number" lineTo script(),
			"does" lineTo script(
				"plus" lineTo script(literal(1))))
			.assertEvalsTo()
	}

	@Test
	fun thisDoesThatAndAccess() {
		script(
			"number" lineTo script(),
			"does" lineTo script(
				"given" lineTo script()),
			line(literal(10)))
			.assertEvalsTo(script("given" lineTo script(literal(10))))
	}

	@Test
	fun thisGivesThatRecursiveAndAccess() {
		script(
			"number" lineTo script(),
			"factorial" lineTo script(),
			"does" lineTo script(
				"if" lineTo script(
					"given" lineTo script(),
					"number" lineTo script(),
					"equals" lineTo script(literal(1))),
				"then" lineTo script(literal(1)),
				"else" lineTo script(
					"given" lineTo script(),
					"number" lineTo script(),
					"times" lineTo script(
						"given" lineTo script(),
						"number" lineTo script(),
						"minus" lineTo script(literal(1)),
						"do" lineTo script("recurse")))),
			line(literal(6)),
			"factorial" lineTo script())
			.assertEvalsTo(script(literal(720)))
	}

	@Test
	fun pattern() {
		val rule = script(
			"false" lineTo script(),
			"or" lineTo script("true"),
			"type" lineTo script(),
			"does" lineTo script(
				"replace" lineTo script("boolean")))

		rule
			.plus(
				"false" lineTo script(),
				"type" lineTo script())
			.assertEvalsTo(line("boolean"))

		rule
			.plus(
				"true" lineTo script(),
				"type" lineTo script())
			.assertEvalsTo(line("boolean"))

		rule
			.plus(
				"maybe" lineTo script(),
				"type" lineTo script())
			.assertEvalsTo(
				"maybe" lineTo script(),
				"type" lineTo script())
	}

	@Test
	fun fieldAppendValue() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))),
			"append" lineTo script(
				"z" lineTo script(literal(30))))
			.assertEvalsTo(
				"point" lineTo script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20)),
					"z" lineTo script(literal(30))))
	}

	@Test
	fun anythingChangeToAnything() {
		script(
			"foo" lineTo script(),
			"replace" lineTo script("bar"))
			.assertEvalsTo(script("bar"))
	}

	@Test
	fun anythingDelete() {
		script(
			"minus" lineTo script(),
			"delete" lineTo script())
			.assertEvalsTo()
	}

	@Test
	fun head() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"),
			"head" lineTo script())
			.assertEvalsTo(
				"plus" lineTo script("one"))
	}

	@Test
	fun head_empty() {
		script(
			"head" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun tail() {
		script(
			"zero" lineTo script(),
			"plus" lineTo script("one"),
			"tail" lineTo script())
			.assertEvalsTo("zero" lineTo script())
	}

	@Test
	fun tail_empty() {
		script(
			"tail" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun content() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))),
			"content" lineTo script())
			.assertEvalsTo(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20)))
	}

	@Test
	fun contents_empty() {
		script(
			"contents" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun contents_complex() {
		script(
			"x" lineTo script("foo"),
			"y" lineTo script("bar"),
			"contents" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun function() {
		script(
			"foo" lineTo script(),
			"gives" lineTo script("bar"),
			"function" lineTo script(
				"zoo" lineTo script(),
				"gives" lineTo script("zar"),
				"append" lineTo script("foo"),
				"append" lineTo script("zoo")))
			.assertEvalsTo(
				"function" lineTo script(
					"zoo" lineTo script(),
					"gives" lineTo script("zar"),
					"append" lineTo script("foo"),
					"append" lineTo script("zoo")))
	}

	@Test
	fun functionApply() {
		script(
			"function" lineTo script("given"),
			"apply" lineTo script("foo"))
			.assertEvalsTo(
				script(
					"given" lineTo script(
						"foo" lineTo script())))
	}

	@Test
	fun equals() {
		script(
			"foo" lineTo script(),
			"equals" lineTo script("foo"))
			.assertEvalsTo(script("yes"))

		script(
			"foo" lineTo script(),
			"equals" lineTo script("bar"))
			.assertEvalsTo(script("no"))
	}

	@Test
	fun ifThenElse_yes() {
		script(
			"if" lineTo script("yes"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsTo(script("hurray"))
	}

	@Test
	fun ifThenElse_no() {
		script(
			"if" lineTo script("no"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsTo(script("booo"))
	}

	@Test
	fun ifThenElse_other() {
		script(
			"if" lineTo script("other"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsToThis
	}

//	@Test
//	fun switch_number() {
//		script(
//			"a" lineTo script(literal(1)),
//			"switch" lineTo script(
//				"number" lineTo script(
//					"plus" lineTo script(literal(2))),
//				"text" lineTo script(
//					"plus" lineTo script(literal("world!")))))
//			.assertEvalsTo(line(literal(3)))
//	}
//
//	@Test
//	fun switch_text() {
//		script(
//			"a" lineTo script(literal("Hello, ")),
//			"switch" lineTo script(
//				"number" lineTo script(
//					"plus" lineTo script(literal(2))),
//				"text" lineTo script(
//					"plus" lineTo script(literal("world!")))))
//			.assertEvalsTo(line(literal("Hello, world!")))
//	}
//
//	@Test
//	fun switch_name() {
//		script(
//			"a" lineTo script("foo"),
//			"switch" lineTo script(
//				"foo" lineTo script("first"),
//				"bar" lineTo script("second")))
//			.assertEvalsTo(
//				"foo" lineTo script(),
//				"first" lineTo script())
//	}
//
//	@Test
//	fun switch_nothing() {
//		script(
//			"a" lineTo script(),
//			"switch" lineTo script(
//				"nothing" lineTo script("first"),
//				"bar" lineTo script("second")))
//			.assertEvalsTo("first" lineTo script())
//	}
//
//	@Test
//	fun switch_mismatch() {
//		script(
//			"a" lineTo script("goo"),
//			"switch" lineTo script(
//				"foo" lineTo script("first"),
//				"bar" lineTo script("second")))
//			.assertEvalsToThis
//	}

	@Test
	fun quote() {
		script(
			"quote" lineTo script(
				line(literal(1)),
				"plus" lineTo script(literal(2))))
			.assertEvalsTo(
				line(literal(1)),
				"plus" lineTo script(literal(2)))
	}

	@Test
	fun quoteUnquote() {
		script(
			"quote" lineTo script(
				"unquote" lineTo script(
					line(literal(1)),
					"plus" lineTo script(literal(2)))))
			.assertEvalsTo(line(literal(3)))
	}

	@Test
	fun compile() {
		script(
			"x" lineTo script(),
			"gives" lineTo script(literal(5)),
			"compile" lineTo script(
				"quote" lineTo script(
					"y" lineTo script(),
					"gives" lineTo script(
						"x" lineTo script(),
						"minus" lineTo script(literal(3))))),
			"x" lineTo script("x"),
			"y" lineTo script("y"))
			.assertEvalsTo(
				"x" lineTo script(literal(5)),
				"y" lineTo script(literal(2)))
	}

	@Test
	fun nativeClass() {
		script(
			line(literal("java.lang.StringBuilder")),
			"native" lineTo script("class"))
			.assertEvalsTo("native" lineTo script(literal(java.lang.StringBuilder::class.java.toString())))
	}

	@Test
	fun textNativeNew() {
		script(
			line(literal("java.lang.StringBuilder")),
			"native" lineTo script("new"))
			.assertEvalsTo("native" lineTo script(literal(java.lang.StringBuilder().toString())))
	}

	@Test
	fun javaInvoke() {
		script(
			line(literal("java.lang.StringBuilder")),
			"native" lineTo script("class"),
			"invoke" lineTo script(literal("newInstance")),
			"invoke" lineTo script(
				line(literal("append")),
				"it" lineTo script(
					line(literal("Hello, world!")),
					"native" lineTo script("string"))))
			.assertEvalsTo(
				"native" lineTo script(
					literal(StringBuilder().append("Hello, world!").toString())))
	}

	@Test
	fun anyInvoke() {
		script(
			line(literal("java.lang.StringBuilder")),
			"native" lineTo script("class"),
			"invoke" lineTo script(literal("newInstance")))
			.assertEvalsTo("native" lineTo script(literal(StringBuilder().toString())))
	}

	@Test
	fun _this() {
		script(
			line(literal(1)),
			"this" lineTo script())
			.assertEvalsTo("this" lineTo script(literal(1)))
	}

	@Test
	fun it_empty() {
		script(
			"foo" lineTo script(),
			"it" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun it_value() {
		script(
			"foo" lineTo script(),
			"it" lineTo script(literal(123)))
			.assertEvalsTo(
				"foo" lineTo script(),
				line(literal(123)))
	}

	@Test
	fun it_complex() {
		script(
			"foo" lineTo script(),
			"it" lineTo script(
				"x" lineTo script(),
				"y" lineTo script()))
			.assertEvalsToThis
	}

	@Test
	fun fold() {
		script(
			line(literal(100)),
			"fold" lineTo script(
				"numbers" lineTo script(
					line(literal(1)),
					line(literal(2)),
					line(literal(3)),
					line(literal(4)),
					line(literal(5)))),
			"doing" lineTo script(
				"function" lineTo script(
					"given" lineTo script(),
					"number" lineTo script(),
					"plus" lineTo script(
						"folded" lineTo script(),
						"number" lineTo script()))))
			.assertEvalsTo(line(literal(115)))
	}

	@Test
	fun last() {
		script(
			"numbers" lineTo script(
				line(literal(1)),
				line(literal(2)),
				line(literal(3))),
			"last" lineTo script())
			.assertEvalsTo("last" lineTo script(literal(3)))
	}

	@Test
	fun previous() {
		script(
			"numbers" lineTo script(
				line(literal(1)),
				line(literal(2)),
				line(literal(3))),
			"previous" lineTo script())
			.assertEvalsTo(
				"previous" lineTo script(
					"numbers" lineTo script(
						line(literal(1)),
						line(literal(2)))))
	}

	@Test
	fun assert() {
		script(
			"assert" lineTo script(
				line(literal(2)),
				"plus" lineTo script(literal(2)),
				"gives" lineTo script(literal(5))))
			.assertEvalFails(
				script(
					"error" lineTo script(
						line(literal(2)),
						"plus" lineTo script(literal(2)),
						"gives" lineTo script(literal(4)),
						"expected" lineTo script(literal(5)))))
	}
}