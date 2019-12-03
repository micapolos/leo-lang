package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.*
import leo14.native.native
import kotlin.test.Test

class TypedTest {
	@Test
	fun isStatic() {
		type().isStatic.assertEqualTo(true)
		type("foo").isStatic.assertEqualTo(true)
		type("foo" fieldTo type("bar")).isStatic.assertEqualTo(true)
		type(choice("foo" optionTo type(), "bar" optionTo type())).isStatic.assertEqualTo(false)
		numberType.isStatic.assertEqualTo(false)
		type("foo" fieldTo numberType).isStatic.assertEqualTo(false)
	}

	@Test
	fun dynamicPlusDynamic() {
		term("foo").of(numberType)
			.plus(term("bar").of(numberLine))
			.assertEqualTo(term("foo").plus(term("bar"))
				.of(numberType.plus(numberLine)))
	}

	@Test
	fun dynamicPlusStatic() {
		term("foo").of(numberType)
			.plus(term("bar").of(line("bar")))
			.assertEqualTo(
				term("foo").of(numberType.plus(line("bar"))))
	}

	@Test
	fun staticPlusDynamic() {
		term("foo").of(type("foo"))
			.plus(term("bar").of(numberLine))
			.assertEqualTo(
				term("bar").of(type("foo").plus(numberLine)))
	}

	@Test
	fun staticPlusStatic() {
		term("foo").of(type("foo"))
			.plus(term("bar").of(line("bar")))
			.assertEqualTo(
				id<String>().of(type("foo").plus(line("bar"))))
	}

	@Test
	fun castChoice() {
		term("lhs")
			.of(type("zero" lineTo numberType))
			.castTermTo(type(choice("zero", "one")))
			.assertEqualTo(fn(fn(arg1<Any>().invoke(term("lhs")))))
	}

	@Test
	fun resolve() {
		typed(
			"point" lineTo typed(
				"x" lineTo leo14.typed.compiler.natives.typed(native(10)),
				"y" lineTo leo14.typed.compiler.natives.typed(native(11))),
			"x" lineTo typed())
			.resolve
			.assertEqualTo(
				typed(
					"point" lineTo typed(
						"x" lineTo leo14.typed.compiler.natives.typed(native(10)),
						"y" lineTo leo14.typed.compiler.natives.typed(native(11))))
					.resolve("x" fieldTo typed()))
	}

	@Test
	fun resolveField_access() {
		val typed = typed(
			"point" lineTo typed(
				"x" lineTo leo14.typed.compiler.natives.typed(native(10)),
				"y" lineTo leo14.typed.compiler.natives.typed(native(11))))

		typed
			.resolve("x" fieldTo typed())
			.assertEqualTo(typed.resolveAccess("x"))
	}

//	@Test
//	fun resolveField_wrap() {
//		val typed = typed(
//			"x" lineTo typed(native(10)),
//			"y" lineTo typed(native(11)))
//
//		typed
//			.resolve("point" fieldTo typed())
//			.assertEqualTo(typed.resolveWrap("point"))
//	}
}