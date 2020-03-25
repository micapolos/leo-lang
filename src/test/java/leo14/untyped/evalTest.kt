package leo14.untyped

import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
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

//	@Test
//	fun thisGivesThatRecursiveAndAccess() {
//		script(
//			"number" lineTo script(),
//			"factorial" lineTo script(),
//			"does" lineTo script(
//				"if" lineTo script(
//					"given" lineTo script(),
//					"number" lineTo script(),
//					"equals" lineTo script(literal(1))),
//				"then" lineTo script(literal(1)),
//				"else" lineTo script(
//					"given" lineTo script(),
//					"number" lineTo script(),
//					"times" lineTo script(
//						"given" lineTo script(),
//						"number" lineTo script(),
//						"minus" lineTo script(literal(1)),
//						"do" lineTo script("recurse")))),
//			line(literal(6)),
//			"factorial" lineTo script())
//			.assertEvalsTo(script(literal(720)))
//	}

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
	fun name() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))),
			"name" lineTo script(),
			"text" lineTo script())
			.assertEvalsTo(script(literal("point")))
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
	fun functionApply_nonRecursive() {
		script(
			"function" lineTo script("recurse"),
			"apply" lineTo script())
			.assertEvalsTo(script("recurse"))
	}

	@Test
	fun equals() {
		script(
			"foo" lineTo script(),
			"equals" lineTo script("foo"))
			.assertEvalsTo(script("boolean" lineTo script("true")))

		script(
			"foo" lineTo script(),
			"equals" lineTo script("bar"))
			.assertEvalsTo(script("boolean" lineTo script("false")))
	}

	@Test
	fun ifThenElse_other() {
		script(
			"if" lineTo script("other"),
			"then" lineTo script("hurray"),
			"else" lineTo script("booo"))
			.assertEvalsToThis
	}

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
	fun it_line() {
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
	fun assert_error() {
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

	@Test
	fun assert_success() {
		script(
			line(literal("Hello, ")),
			"assert" lineTo script(
				line(literal(2)),
				"plus" lineTo script(literal(2)),
				"gives" lineTo script(literal(4))),
			"plus" lineTo script(literal("world!")))
			.assertEvalsTo(
				script(literal("Hello, world!")))
	}

	@Test
	fun assert_invalidSyntax() {
		script(
			line(literal("Hello, ")),
			"assert" lineTo script("foo"))
			.assertEvalsToThis
	}

	@Test
	fun anythingMatch() {
		script(
			"anything" lineTo script(),
			"increment" lineTo script(),
			"gives" lineTo script("match"),
			"foo" lineTo script(),
			"increment" lineTo script())
			.assertEvalsTo(script("match"))
	}

	@Test
	fun lazy() {
		script(
			"lazy" lineTo script(
				"minus" lineTo script(literal(1))))
			.assertEvalsToThis
	}

	@Test
	fun force_lazy() {
		script(
			"lazy" lineTo script(
				"minus" lineTo script(literal(1))),
			"force" lineTo script())
			.assertEvalsTo(script(literal(-1)))
	}

	@Test
	fun force_strict() {
		script(
			"minus" lineTo script(literal(1)),
			"force" lineTo script())
			.assertEvalsTo(script(literal(-1)))
	}

	@Test
	fun do_() {
		script(
			line(literal(10)),
			"do" lineTo script(
				"minus" lineTo script(
					"given" lineTo script(),
					"number" lineTo script())))
			.assertEvalsTo(script(literal(-10)))
	}

	@Test
	fun switch_number() {
		script(
			"x" lineTo script(literal(10)),
			"match" lineTo script(
				"number" lineTo script("matching")))
			.assertEvalsTo("matching" lineTo script(literal(10)))
	}

	@Test
	fun switch_text() {
		script(
			"x" lineTo script(literal("Hello")),
			"match" lineTo script(
				"text" lineTo script("matching")))
			.assertEvalsTo("matching" lineTo script(literal("Hello")))
	}

	@Test
	fun switch_name() {
		script(
			"x" lineTo script("circle"),
			"match" lineTo script(
				"circle" lineTo script("matching")))
			.assertEvalsTo("matching" lineTo script("circle"))
	}

	@Test
	fun switch_ordering() {
		script(
			"x" lineTo script("circle"),
			"match" lineTo script(
				"circle" lineTo script("first"),
				"circle" lineTo script("second")))
			.assertEvalsTo(script("second"))
	}

	@Test
	fun switch_mismatch() {
		script(
			"x" lineTo script("circle"),
			"match" lineTo script("square" lineTo script()))
			.assertEvalsToThis
	}

	@Test
	fun either_matching1() {
		script(
			"either" lineTo script(
				"age" lineTo script("number"),
				"name" lineTo script("text")),
			"gives" lineTo script("ok"),
			"age" lineTo script(literal(10)))
			.assertEvalsTo(script("ok"))
	}

	@Test
	fun either_matching2() {
		script(
			"either" lineTo script(
				"age" lineTo script("number"),
				"name" lineTo script("text")),
			"gives" lineTo script("ok"),
			"name" lineTo script(literal("Miko")))
			.assertEvalsTo(script("ok"))
	}

	@Test
	fun either_mismatch() {
		script(
			"either" lineTo script(
				"age" lineTo script("number"),
				"name" lineTo script("text")),
			"gives" lineTo script("ok"),
			"height" lineTo script(literal(180)))
			.assertEvalsTo(script("height" lineTo script(literal(180))))
	}

	@Test
	fun expands() {
		script(
			"defx" lineTo script(),
			"expands" lineTo script(
				"quote" lineTo script(
					"x" lineTo script(),
					"gives" lineTo script(literal(1)))))
			.assertEvalsTo()
	}

	@Test
	fun expands_apply() {
		script(
			"defx" lineTo script(),
			"expands" lineTo script(
				"quote" lineTo script(
					"x" lineTo script(),
					"gives" lineTo script(literal(1)))),
			"defx" lineTo script())
			.assertEvalsTo()
	}

	@Test
	fun expands_apply_get() {
		script(
			"defx" lineTo script(),
			"expands" lineTo script(
				"quote" lineTo script(
					"x" lineTo script(),
					"gives" lineTo script(literal(1)))),
			"defx" lineTo script(),
			"x" lineTo script())
			.assertEvalsTo(script(literal(1)))
	}
}