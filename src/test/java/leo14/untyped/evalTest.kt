package leo14.untyped

import leo14.*
import leo14.line
import leo14.lineTo
import java.awt.Point
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertFailsWith

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
				assertEvalsTo(
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
				"doing" lineTo script("given")),
			"doing" lineTo script())
			.assertEvalsTo(
				"doing" lineTo script("given"))
	}

	@Test
	fun accessNative() {
		script(
			"hello" lineTo script(line(literal(123)), "int"(), "native"()),
			"native" lineTo script())
			.assertEvalsTo("native" lineTo script(literal(123.toString())))
	}

	@Test
	fun thisGivesThat() {
		script(
			"x" lineTo script(),
			"is" lineTo script(literal(10)))
			.assertEvalsTo()
	}

	@Test
	fun thisGivesThatAndAccess() {
		script(
			"x" lineTo script(),
			"is" lineTo script(literal(10)),
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
	fun pattern() {
		val rule = script(
			"either" lineTo script(
				"quote" lineTo script(
					"false" lineTo script(),
					"true" lineTo script())),
			"type" lineTo script(),
			"does" lineTo script("boolean"))

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
				"type" lineTo script("maybe" lineTo script()))
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
	fun contents_empty() {
		script(
			"contents" lineTo script())
			.assertEvalsToThis
	}

	@Test
	fun contents_complex() {
		leo(
			"x"("foo"),
			"y"("bar"),
			"contents"())
			.assertEvalsTo(leo("contents"("x"("foo"), "y"("bar"))))
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
	fun doing() {
		script(
			"foo" lineTo script(),
			"is" lineTo script("bar"),
			"doing" lineTo script(
				"zoo" lineTo script(),
				"is" lineTo script("zar"),
				"append" lineTo script("foo"),
				"append" lineTo script("zoo")))
			.assertEvalsTo(
				"doing" lineTo script(
					"zoo" lineTo script(),
					"is" lineTo script("zar"),
					"append" lineTo script("foo"),
					"append" lineTo script("zoo")))
	}

	@Test
	fun useFunction() {
		leo(
			1,
			"and"(2),
			"use"(
				"doing"(
					"number"(),
					"plus"("and"(), "number"()))))
			.assertEvalsTo(leo(3))
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
			"x"(), "is"(5),
			"quote"("y"(), "is"("x"(), "minus"(3))),
			"compile"(),
			"x"("x"()),
			"y"("y"()))
			.assertEvalsTo("x"(5), "y"(2))
	}

	@Test
	fun nativeClass() {
		script(
			line(literal("java.lang.StringBuilder")),
			"class"(),
			"native"())
			.assertEvalsTo("native" lineTo script(literal(java.lang.StringBuilder::class.java.toString())))
	}

	@Test
	fun textNativeNew() {
		script(
			line(literal("java.awt.Point")),
			"class"(),
			"native"(),
			"new"(
				"it"(1, "int"(), "native"()),
				"it"(2, "int"(), "native"())))
			.assertEvalsTo("native"(Point(1, 2).toString()))
	}

	@Test
	fun textNativeGet() {
		script(
			line(literal("java.awt.Point")),
			"class"(),
			"native"(),
			"new"(),
			"get"("x"))
			.assertEvalsTo("native" lineTo script(literal("0")))
	}

	@Test
	fun textNativeStaticGet() {
		script(
			line(literal("java.lang.Integer")),
			"class"(),
			"native"(),
			"static"(),
			"get"("MAX_VALUE"))
			.assertEvalsTo("native" lineTo script(literal(Integer.MAX_VALUE.toString())))
	}

	@Test
	fun javaInvoke() {
		script(
			line(literal("java.lang.StringBuilder")),
			"class"(),
			"native"(),
			"new"(),
			"invoke" lineTo script(
				line(literal("append")),
				"it" lineTo script(line(literal("Hello, world!")), "string"(), "native"())))
			.assertEvalsTo(
				"native" lineTo script(
					literal(StringBuilder().append("Hello, world!").toString())))
	}

	@Test
	fun nativeStaticInvoke() {
		leo(
			"java.lang.String",
			"class"(),
			"native"(),
			"static"(),
			"invoke"(
				"it"("valueOf"),
				"it"(PI, "double"(), "native"())))
			.assertEvalsTo("native"(java.lang.String.valueOf(PI).toString()))
	}

	@Test
	fun anyInvoke() {
		script(
			line(literal("java.lang.StringBuilder")),
			"class"(),
			"native"(),
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
		leo("foo"(), "it"()).assertEvalsTo(leo("foo"()))
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
				"plus" lineTo script("y")))
			.assertEvalsToThis
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
				"equals" lineTo script(literal(5))))
			.assertEvalFails(
				script(
					"error" lineTo script(
						line(literal(2)),
						"plus" lineTo script(literal(2)),
						"equals" lineTo script(literal(4)),
						"expected" lineTo script(literal(5)))))
	}

	@Test
	fun assert_success() {
		script(
			line(literal("Hello, ")),
			"assert" lineTo script(
				line(literal(2)),
				"plus" lineTo script(literal(2)),
				"equals" lineTo script(literal(4))),
			"plus" lineTo script(literal("world!")))
			.assertEvalsTo(
				script(literal("Hello, world!")))
	}

	@Test
	fun assert_invalidSyntax() {
		script(
			line(literal("Hello, ")),
			"assert" lineTo script("foo"))
			.assertEvalFails(leo("assert"("foo"())))
	}

	@Test
	fun anythingMatch() {
		script(
			"anything" lineTo script(),
			"increment" lineTo script(),
			"does" lineTo script("match"),
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
	fun switch_number() {
		script(
			"x" lineTo script(literal(10)),
			"match" lineTo script(
				"number" lineTo script("number")))
			.assertEvalsTo(script(literal(10)))
	}

	@Test
	fun switch_text() {
		script(
			"x" lineTo script(literal("Hello")),
			"match" lineTo script(
				"text" lineTo script("text")))
			.assertEvalsTo(script(literal("Hello")))
	}

	@Test
	fun switch_name() {
		script(
			"shape" lineTo script(
				"circle" lineTo script(
					"radius" lineTo script(literal(10)))),
			"match" lineTo script(
				"circle" lineTo script(
					"circle" lineTo script(),
					"radius" lineTo script(),
					"number" lineTo script())))
			.assertEvalsTo(script(literal(10)))
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
			"does" lineTo script("ok"),
			"age" lineTo script(literal(10)))
			.assertEvalsTo(script("ok"))
	}

	@Test
	fun either_matching2() {
		script(
			"either" lineTo script(
				"age" lineTo script("number"),
				"name" lineTo script("text")),
			"does" lineTo script("ok"),
			"name" lineTo script(literal("Miko")))
			.assertEvalsTo(script("ok"))
	}

	@Test
	fun either_mismatch() {
		script(
			"either" lineTo script(
				"age" lineTo script("number"),
				"name" lineTo script("text")),
			"is" lineTo script("ok"),
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
					"is" lineTo script(literal(1)))))
			.assertEvalsTo()
	}

	@Test
	fun expands_apply() {
		script(
			"defx" lineTo script(),
			"expands" lineTo script(
				"quote" lineTo script(
					"x" lineTo script(),
					"is" lineTo script(literal(1)))),
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
					"is" lineTo script(literal(1)))),
			"defx" lineTo script(),
			"x" lineTo script())
			.assertEvalsTo(script(literal(1)))
	}

	@Test
	fun scope() {
		script(
			"x" lineTo script(),
			"is" lineTo script(literal(1)),
			"y" lineTo script(),
			"does" lineTo script(literal(1)),
			"z" lineTo script(),
			"expands" lineTo script(literal(1)),
			"scope" lineTo script())
			.assertEvalsTo(
				"x" lineTo script(),
				"is" lineTo script(literal(1)),
				"y" lineTo script(),
				"does" lineTo script(literal(1)),
				"z" lineTo script(),
				"expands" lineTo script(literal(1)))
	}

	@Test
	fun object_() {
		script(
			"zero"(),
			"plus"("one"()),
			"object"())
			.assertEvalsTo("one"())
	}

	@Test
	fun link() {
		script(
			"zero"(),
			"plus"("one"()),
			"link"())
			.assertEvalsTo("plus"())
	}

	@Test
	fun subject() {
		script(
			"zero"(),
			"plus"("one"()),
			"subject"())
			.assertEvalsTo("zero"())
	}

	@Test
	fun resolve() {
		script(
			"quote"(1, "plus"(2, "times"(3))),
			"resolve"())
			.assertEvalsTo(script(literal(7)))
	}

	@Test
	fun evaluate_doesNotCompile() {
		script(
			"quote"("x"(), "is"(1)),
			"evaluate"(),
			"x"())
			.assertEvalsTo("x"())
	}

	@Test
	fun given_postfix() {
		leo("foo"(), "given"()).assertEvalsTo(leo("given"("foo"())))
	}

	@Test
	fun given_prefix() {
		script("given"("foo"())).assertEvalsToThis
	}

	@Test
	fun reflect() {
		leo(
			"foo"(), "reflect"(), "is"(123),
			"foo"())
			.assertEvalsTo(leo(123))
	}

	@Test
	fun do_repeat() {
		leo(
			1,
			"times"(6, "factorial"()),
			"do"(
				"times"(), "factorial"(), "number"(), "equals"(1),
				"match"(
					"true"("number"()),
					"false"(
						"number"(),
						"times"("times"(), "factorial"(), "number"()),
						"times"("times"(), "factorial"(), "number"(), "minus"(1), "factorial"()),
						"repeat"()))))
			.assertEvalsTo(leo(720))
	}

	@Test
	fun does_repeat() {
		leo(
			"number"(),
			"times"("number"(), "factorial"()),
			"does"(
				"times"(), "factorial"(), "number"(), "equals"(1),
				"match"(
					"true"("number"()),
					"false"(
						"number"(),
						"times"("times"(), "factorial"(), "number"()),
						"times"("times"(), "factorial"(), "number"(), "minus"(1), "factorial"()),
						"repeat"()))),
			1, "times"(6, "factorial"()))
			.assertEvalsTo(leo(720))
	}

	@Test
	fun numberText() {
		leo(0, "text"()).assertEvalsTo(leo("0"))
		leo(123, "text"()).assertEvalsTo(leo("123"))
		leo(3.14, "text"()).assertEvalsTo(leo("3.14"))
		leo(-3.14, "text"()).assertEvalsTo(leo("-3.14"))
	}

	@Test
	fun textNumber() {
		leo("0", "number"()).assertEvalsTo(leo(0))
		leo("123", "number"()).assertEvalsTo(leo(123))
		leo("3.14", "number"()).assertEvalsTo(leo(3.14))
		leo("-3.14", "number"()).assertEvalsTo(leo(-3.14))
		leo("foo", "number"()).assertEvalsTo(leo("number"("foo")))
	}

	@Test
	fun nothing() {
		leo("nothing"()).assertEvalsTo(leo())
	}

	@Test
	fun intNative() {
		leo(123, "int"(), "native"()).assertEvalsTo(leo("native"("123")))
	}

	@Test
	fun textWord() {
		leo("foo", "word"()).assertEvalsTo(leo("foo"()))
	}

	@Test
	fun textWord_invalid() {
		leo("not-a-word", "word"()).assertEvalsTo(leo("word"("not-a-word")))
	}

	@Test
	fun wordText() {
		leo("foo"(), "word"(), "text"()).assertEvalsTo(leo("foo"))
	}

	@Test
	fun scriptText() {
		leo(1, "plus"("2"), "script"(), "text"())
			.assertEvalsTo(
				if (useDots) leo("1.plus \"2\"")
				else leo("1\nplus \"2\""))
	}

	@Test
	fun do_() {
		leo(
			"Hello, ",
			"and"("world!"),
			"do"(
				"text"(),
				"plus"(
					"and"(),
					"text"())))
			.assertEvalsTo(leo("Hello, world!"))
	}

	@Test
	fun does() {
		leo(
			"text"(),
			"join"("text"()),
			"does"(
				"text"(),
				"plus"(
					"join"(),
					"text"())),
			"Hello, ", "join"("world!"))
			.assertEvalsTo(leo("Hello, world!"))
	}

	@Test
	fun leoSubject() {
		leo(
			"zero",
			"and"("one"),
			"leo"(), "subject"())
			.assertEvalsTo(leo("zero"))
	}

	@Test
	fun leoObject() {
		leo(
			"zero",
			"and"("one"),
			"leo"(), "object"())
			.assertEvalsTo(leo("one"))
	}

	@Test
	fun leoHead() {
		leo(
			"zero",
			"and"("one"),
			"leo"(), "head"())
			.assertEvalsTo(leo("and"("one")))
	}

	@Test
	fun leoWord() {
		leo(
			"zero",
			"and"("one"),
			"leo"(), "word"())
			.assertEvalsTo(leo("and"()))
	}

	@Test
	fun leoScript() {
		leo(
			"zero",
			"and"(1),
			"leo"(), "script"())
			.assertEvalsTo(
				leo("script"("line"("list"(
					"zero",
					"field"(
						"name"("and"),
						"script"("line"("list"(1)))))))))
	}

	@Test
	fun apply() {
		leo("quote"(1, "plus"(2)), "apply"())
			.assertEvalsTo(leo(3))
	}

	@Test
	fun apply_notDeep() {
		leo("quote"(1, "plus"(2, "plus"(3))), "apply"())
			.assertEvalsTo(leo(1, "plus"(2, "plus"(3))))
	}

	@Test
	fun recurse() {
		leo(
			5,
			"do"(
				"number"(),
				"equals"(0),
				"match"(
					"true"(0),
					"false"(
						"number"(),
						"minus"(1),
						"recurse"()))))
			.assertEvalsTo(leo(0))
	}

	@Test
	fun recurse_stackOverflow() {
		assertFailsWith(StackOverflowError::class) {
			leo(
				1000000,
				"do"(
					"number"(),
					"equals"(0),
					"match"(
						"true"(0),
						"false"(
							"number"(),
							"minus"(1),
							"recurse"()))))
				.eval
		}
	}

	@Test
	fun recurse_lazy() {
		leo("do"("lazy"("foo"("recurse"()))))
			.assertEvalsTo(leo("lazy"("foo"("recurse"()))))
	}
}
