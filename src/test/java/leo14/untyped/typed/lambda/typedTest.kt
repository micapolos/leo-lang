package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.bigDecimal
import leo14.lambda2.invoke
import leo14.lambda2.nil
import leo14.lambda2.pair
import leo14.lambda2.valueTerm
import leo14.untyped.javaName
import leo14.untyped.textName
import leo14.untyped.typed.*
import java.awt.Point
import kotlin.test.Test

class TypedTest {
	@Test
	fun updateOrNull() {
		"foo".typed
			.updateOrNull { it }!!
			.eval
			.assertEqualTo("foo".typed)
	}

	@Test
	fun linkOrNull() {
		typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine)
			.linkOrNull!!
			.run { lhs.plus(line) }
			.eval
			.assertEqualTo(typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine))
	}

	@Test
	fun textCompiled() {
		"Hello, world!"
			.typedLine
			.assertEqualTo(textName lineTo "Hello, world!".valueJavaTyped)

		"Hello, world!"
			.typed
			.assertEqualTo(typed(textName lineTo "Hello, world!".valueJavaTyped))
	}

	@Test
	fun matchText() {
		"Hello, world!"
			.typed
			.matchText { typed(textName lineTo javaTyped) }!!
			.eval
			.assertEqualTo("Hello, world!".typed)
	}

	@Test
	fun javaCompiled() {
		Point(10, 20)
			.valueJavaTypedLine
			.assertEqualTo(javaTypeLine.typed(Point(10, 20).valueTerm))

		Point(10, 20)
			.valueJavaTyped
			.assertEqualTo(javaType.typed(pair.invoke(nil).invoke(Point(10, 20).valueTerm)))
	}

	@Test
	fun matchNative() {
		Point(10, 20)
			.valueJavaTyped
			.matchNative {
				javaTyped
			}!!
			.eval
			.assertEqualTo(Point(10, 20).valueJavaTyped)
	}

	@Test
	fun matchInfix() {
		typed("Hello, ".typedLine, "and" lineTo "world!".typed)
			.matchInfix("and") { rhs ->
				plus("and" lineTo rhs)
			}!!
			.eval
			.assertEqualTo(typed("Hello, ".typedLine, "and" lineTo "world!".typed))
	}

	@Test
	fun matchPrefix() {
		typed("name" lineTo "foo".typed)
			.matchPrefix("name") { typed("surname" lineTo this) }!!
			.eval
			.assertEqualTo(typed("surname" lineTo "foo".typed))
	}

	@Test
	fun compiledLines() {
		typed("Hello, ".typedLine, "world!".typedLine)
			.assertEqualTo(
				textType
					.plus(textTypeLine)
					.typed(pair("Hello, ".typed.term)("world!".typedLine.term)))
	}

	@Test
	fun matchLink() {
		typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine)
			.matchLink { plus(it) }!!
			.eval
			.assertEqualTo(typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine))
	}

	@Test
	fun evaluatesOnce() {
		typed("Hello, ".typedLine, "plus" lineTo "world!".typed)
			.matchInfix("plus") { plus("minus" lineTo it) }!!
			.eval
			.assertEqualTo(typed("Hello, ".typedLine, "minus" lineTo "world!".typed))
	}

	@Test
	fun functions() {
		numberType
			.does(type(numberTypeLine, textTypeLine)) { plus("done".typedLine) }
			.run {
				invokeOrNull(123.typed)!!
					.eval
					.assertEqualTo(typed(123.typedLine, "done".typedLine))

				invokeOrNull("123".typed).assertNull
			}
	}

	@Test
	fun make() {
		"foo".typed.make("name")
			.assertEqualTo(typed("name" lineTo "foo".typed))
	}

	@Test
	fun literalJava() {
		typed(javaName lineTo "foo".typed).applyGet!!.eval.assertEqualTo("foo".valueJavaTyped)
		typed(javaName lineTo 10.typed).applyGet!!.eval.assertEqualTo(10.bigDecimal.valueJavaTyped)
	}
}