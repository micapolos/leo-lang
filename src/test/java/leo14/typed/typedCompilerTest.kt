package leo14.typed

import leo.base.assertEqualTo
import leo13.stack
import leo14.*
import leo14.lambda.*
import kotlin.test.Test

class TypedCompilerTest {
	val lit: (Literal) -> Any = { it.any }

	@Test
	fun empty() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script())
			.assertEqualTo(typed<Any>())
	}

	@Test
	fun literal() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script(literal("foo")))
			.assertEqualTo(typed<Any>().plusNative(term("foo")))
	}

	@Test
	fun simple() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("foo" lineTo script()))
			.assertEqualTo(id<Any>() of type("foo" fieldTo type()))
	}

	@Test
	fun field() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("foo" lineTo script(literal("bar"))))
			.assertEqualTo(term("bar") of type("foo" fieldTo nativeType))
	}

	@Test
	fun fields() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script(
				"x" lineTo script(literal("foo")),
				"y" lineTo script(literal("bar"))))
			.assertEqualTo(
				term("foo").plus(term("bar")) of
					type(
						"x" fieldTo nativeType,
						"y" fieldTo nativeType))
	}

	@Test
	fun access2() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("x" lineTo script()))
			.assertEqualTo(term("lhs").first.first of type("x" fieldTo nativeType))
	}

	@Test
	fun access1() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("y" lineTo script()))
			.assertEqualTo(term("lhs").first.second of type("y" fieldTo nativeType))
	}

	@Test
	fun access0() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("z" lineTo script()))
			.assertEqualTo(term("lhs").second of type("z" fieldTo nativeType))
	}

	@Test
	fun access2_skipStatic() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo type("static"),
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("x" lineTo script()))
			.assertEqualTo(term("lhs").first of type("x" fieldTo nativeType))
	}

	@Test
	fun accessNative() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" lineTo nativeType,
					nativeLine,
					"z" lineTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("native" lineTo script()))
			.assertEqualTo(term("lhs").first.second of nativeType)
	}

	@Test
	fun wrap() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("v" lineTo script()))
			.assertEqualTo(term("lhs") of type("v" fieldTo type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType))))
	}

	@Test
	fun letItBe() {
		typed<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(
				script(
					"let" lineTo script("native"),
					"give" lineTo script(literal("egg")),
					line(literal("chicken"))))
			.assertEqualTo(fn(arg0<Any>().invoke(term("chicken"))).invoke(fn(term("egg"))) of type(nativeLine))
	}

	@Test
	fun compileDelete() {
		term("lhs")
			.of(nativeType)
			.plusCompiler(stack(), lit, ret())
			.compile<Any>(script("delete"))
			.assertEqualTo(typed<Any>())
	}

	@Test
	fun compileChoice() {
		term("lhs").of(type("bit" lineTo type(choice("zero", "one"))))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<String>(script("choice"))
			.assertEqualTo(term("lhs") of type(choice("zero", "one")))
	}

	@Test
	fun of() {
		term("lhs")
			.of(type("zero"))
			.run {
				plusCompiler(stack(), lit) { resultCompiler(it) }
					.compile<String>(
						script(
							"of" lineTo script(
								"choice" lineTo script(
									"zero" lineTo script(),
									"one" lineTo script()))))
					.assertEqualTo(of(type(choice("zero", "one"))))
			}
	}

	@Test
	fun deepOf() {
		term("lhs")
			.of(type("bit" lineTo type("zero")))
			.run {
				plusCompiler(stack(), lit) { resultCompiler(it) }
					.compile<String>(
						script(
							"of" lineTo script(
								"bit" lineTo script(
									"choice" lineTo script(
										"zero" lineTo script(),
										"one" lineTo script())))))
					.assertEqualTo(of(type("bit" lineTo type(choice("zero", "one")))))
			}
	}

	@Test
	fun compileOption() {
		term<Any>("lhs")
			.of("circle" optionTo type("radius"))
			.plusOptionCompiler(null, stack(), lit, ret())
			.compile<Any>("circle" lineTo script("plus" lineTo script("one")))
			.assertEqualTo(
				term<Any>("lhs")
					.invoke(id())
					.of(
						type(
							"radius" lineTo type(),
							"plus" lineTo type("one"))))
	}
}
