package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.lambda2.invoke
import leo14.lambda2.nil
import leo14.lambda2.pair
import leo14.lambda2.valueTerm
import leo14.untyped.typed.*
import java.awt.Point
import kotlin.test.Test

class CompiledTest {
	@Test
	fun updateOrNull() {
		"foo".compiled
			.updateOrNull { it }!!
			.eval
			.assertEqualTo("foo".compiled)
	}

	@Test
	fun linkOrNull() {
		compiled("foo".nativeCompiledLine, "bar".nativeCompiledLine)
			.linkOrNull!!
			.run { lhs.plus(line) }
			.eval
			.assertEqualTo(compiled("foo".nativeCompiledLine, "bar".nativeCompiledLine))
	}

	@Test
	fun textCompiled() {
		"Hello, world!"
			.compiledLine
			.assertEqualTo(textTypeLine.compiled("Hello, world!".valueTerm))

		"Hello, world!"
			.compiled
			.assertEqualTo(textType.compiled(pair.invoke(nil).invoke("Hello, world!".valueTerm)))
	}

	@Test
	fun matchText() {
		"Hello, world!"
			.compiled
			.matchText { compiled(textTypeLine.compiled(this)) }!!
			.eval
			.assertEqualTo("Hello, world!".compiled)
	}

	@Test
	fun nativeCompiled() {
		Point(10, 20)
			.nativeCompiledLine
			.assertEqualTo(nativeTypeLine.compiled(Point(10, 20).valueTerm))

		Point(10, 20)
			.nativeCompiled
			.assertEqualTo(nativeType.compiled(pair.invoke(nil).invoke(Point(10, 20).valueTerm)))
	}

	@Test
	fun matchNative() {
		Point(10, 20)
			.nativeCompiled
			.matchNative {
				nativeCompiled
			}!!
			.eval
			.assertEqualTo(Point(10, 20).nativeCompiled)
	}

	@Test
	fun matchInfix() {
		compiled("Hello, ".compiledLine, "and" lineTo "world!".compiled)
			.matchInfix("and") { rhs ->
				plus("and" lineTo rhs)
			}!!
			.eval
			.assertEqualTo(compiled("Hello, ".compiledLine, "and" lineTo "world!".compiled))
	}

	@Test
	fun matchPrefix() {
		compiled("name" lineTo "foo".compiled)
			.matchPrefix("name") { compiled("surname" lineTo this) }!!
			.eval
			.assertEqualTo(compiled("surname" lineTo "foo".compiled))
	}

	@Test
	fun compiledLines() {
		compiled("Hello, ".compiledLine, "world!".compiledLine)
			.assertEqualTo(
				textType
					.plus(textTypeLine)
					.compiled(pair("Hello, ".compiled.term)("world!".compiledLine.term)))
	}

	@Test
	fun matchLink() {
		compiled("foo".nativeCompiledLine, "bar".nativeCompiledLine)
			.matchLink { plus(it) }!!
			.eval
			.assertEqualTo(compiled("foo".nativeCompiledLine, "bar".nativeCompiledLine))
	}

	@Test
	fun evaluatesOnce() {
		compiled("Hello, ".compiledLine, "plus" lineTo "world!".compiled)
			.matchInfix("plus") { plus("minus" lineTo it) }!!
			.eval
			.assertEqualTo(compiled("Hello, ".compiledLine, "minus" lineTo "world!".compiled))
	}

	@Test
	fun functions() {
		numberType
			.does(type(numberTypeLine, textTypeLine)) { plus("done".compiledLine) }
			.run {
				invokeOrNull(123.compiled)!!
					.eval
					.assertEqualTo(compiled(123.compiledLine, "done".compiledLine))

				invokeOrNull("123".compiled).assertNull
			}
	}
}