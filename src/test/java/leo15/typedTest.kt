package leo15

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.bigDecimal
import leo15.lambda.*
import java.awt.Point
import kotlin.test.Test

class TypedTest {
	@Test
	fun emptyPlus() {
		emptyTyped
			.plus(10.typedLine)
			.assertEqualTo(numberType.typed(10.typedLine.term))
	}

	@Test
	fun nonEmptyPlus() {
		10.typed
			.plus(20.typedLine)
			.assertEqualTo(
				type(numberTypeLine, numberTypeLine)
					.typed(pairTerm.invoke(10.typedLine.term).invoke(20.typedLine.term)))
	}

	@Test
	fun linkOrNull_emptyLhs() {
		10.typed
			.linkOrNull
			.assertEqualTo(emptyTyped linkTo 10.typedLine)
	}

	@Test
	fun linkOrNull_nonEmptyLhs() {
		leo15.typed(10.typedLine, 20.typedLine)
			.linkOrNull
			.assertEqualTo(
				numberType.typed(leo15.typed(10.typedLine, 20.typedLine).term.invoke(firstTerm)) linkTo
					numberTypeLine.typed(leo15.typed(10.typedLine, 20.typedLine).term.invoke(secondTerm)))
	}

	@Test
	fun textCompiled() {
		"Hello, world!"
			.typedLine
			.assertEqualTo(textName lineTo "Hello, world!".valueJavaTyped)

		"Hello, world!"
			.typed
			.assertEqualTo(leo15.typed(textName lineTo "Hello, world!".valueJavaTyped))
	}

	@Test
	fun matchText() {
		"Hello, world!"
			.typed
			.matchText { leo15.typed(textName lineTo javaTyped) }!!
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
			.assertEqualTo(javaType.typed(Point(10, 20).valueTerm))
	}

	@Test
	fun matchNative() {
		Point(10, 20)
			.valueJavaTyped
			.matchJava {
				javaTyped
			}!!
			.eval
			.assertEqualTo(Point(10, 20).valueJavaTyped)
	}

	@Test
	fun matchInfix() {
		leo15.typed("Hello, ".typedLine, "and" lineTo "world!".typed)
			.matchInfix("and") { rhs ->
				plus("and" lineTo rhs)
			}!!
			.eval
			.assertEqualTo(leo15.typed("Hello, ".typedLine, "and" lineTo "world!".typed))
	}

	@Test
	fun matchPrefix() {
		leo15.typed("name" lineTo "foo".typed)
			.matchPrefix("name") { leo15.typed("surname" lineTo this) }!!
			.eval
			.assertEqualTo(leo15.typed("surname" lineTo "foo".typed))
	}

	@Test
	fun compiledLines() {
		leo15.typed("Hello, ".typedLine, "world!".typedLine)
			.assertEqualTo(
				textType
					.plus(textTypeLine)
					.typed(pairTerm("Hello, ".typed.term)("world!".typedLine.term)))
	}

	@Test
	fun matchLink() {
		leo15.typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine)
			.matchLink { plus(it) }!!
			.eval
			.assertEqualTo(leo15.typed("foo".valueJavaTypedLine, "bar".valueJavaTypedLine))
	}

	@Test
	fun evaluatesOnce() {
		leo15.typed("Hello, ".typedLine, "plus" lineTo "world!".typed)
			.matchInfix("plus") { plus("minus" lineTo it) }!!
			.eval
			.assertEqualTo(leo15.typed("Hello, ".typedLine, "minus" lineTo "world!".typed))
	}

	@Test
	fun functions() {
		numberType
			.does(type(numberTypeLine, textTypeLine)) { plus("done".typedLine) }
			.run {
				invokeOrNull(123.typed)!!
					.eval
					.assertEqualTo(leo15.typed(123.typedLine, "done".typedLine))

				invokeOrNull("123".typed).assertNull
			}
	}

	@Test
	fun make() {
		"foo".typed.make("name")
			.assertEqualTo(leo15.typed("name" lineTo "foo".typed))
	}

	@Test
	fun literalJava() {
		leo15.typed(javaName lineTo "foo".typed).applyGet!!.eval.assertEqualTo("foo".valueJavaTyped)
		leo15.typed(javaName lineTo 10.typed).applyGet!!.eval.assertEqualTo(10.bigDecimal.valueJavaTyped)
	}
}