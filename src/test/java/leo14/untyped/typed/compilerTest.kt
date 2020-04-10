package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.begin
import leo14.bigDecimal
import leo14.literal
import leo14.untyped.isName
import leo14.untyped.plusName
import kotlin.test.Test

class CompilerTest {
	@Test
	fun literal() {
		emptyCompiler
			.plus(literal("foo"))
			.assertEqualTo(emptyCompiler.set(textType.compiled("foo")))

		emptyCompiler
			.plus(literal(123))
			.assertEqualTo(emptyCompiler.set(numberType.compiled(123.bigDecimal)))
	}

	@Test
	fun name() {
		emptyCompiler
			.plus(begin("x"), emptyCompiled)
			.assertEqualTo(emptyCompiler.set(type("x").compiled(null)))
	}

	@Test
	fun field() {
		emptyCompiler
			.plus(begin("x"), textType.compiled("foo"))
			.assertEqualTo(emptyCompiler.set(type("x" lineTo textType).compiled("foo")))
	}

	@Test
	fun appendField() {
		emptyCompiler
			.set(textType.compiled("Hello, "))
			.plus(begin(plusName), textType.compiled("world!"))
			.assertEqualTo(
				emptyCompiler
					.set(
						type(textTypeLine, plusName lineTo textType)
							.compiled("Hello, " to "world!")))
	}

	@Test
	fun is_() {
		emptyCompiler
			.set(type("x").compiled(null))
			.plus(begin(isName), textType.compiled("foo"))
			.assertEqualTo(
				emptyCompiler
					.plus(definition(binding(type("x"), textType.compiled("foo")))))
	}
}