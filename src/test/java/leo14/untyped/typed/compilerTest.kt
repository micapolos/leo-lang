package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.begin
import leo14.literal
import leo14.untyped.isName
import leo14.untyped.plusName
import kotlin.test.Test

class CompilerTest {
	@Test
	fun literal() {
		emptyCompiler
			.plus(literal("foo"))
			.assertEqualTo(emptyCompiler.set("foo".compiled))

		emptyCompiler
			.plus(literal(123))
			.assertEqualTo(emptyCompiler.set(123.compiled))
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
			.set("Hello, ".compiled)
			.plus(begin(plusName), "world!".compiled)
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
			.plus(begin(isName), "foo".compiled)
			.assertEqualTo(
				emptyCompiler
					.plus(definition(binding(type("x"), "foo".compiled))))
	}
}