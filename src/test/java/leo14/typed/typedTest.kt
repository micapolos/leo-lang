package leo14.typed

import leo.base.assertEqualTo
import leo14.lambda.*
import kotlin.test.Test

class TypedTest {
	@Test
	fun isStatic() {
		type().isStatic.assertEqualTo(true)
		type("foo").isStatic.assertEqualTo(true)
		type("foo" fieldTo type("bar")).isStatic.assertEqualTo(true)
		type(choice("foo" optionTo type(), "bar" optionTo type())).isStatic.assertEqualTo(false)
		nativeType.isStatic.assertEqualTo(false)
		type("foo" fieldTo nativeType).isStatic.assertEqualTo(false)
	}

	@Test
	fun dynamicPlusDynamic() {
		term("foo").of(nativeType)
			.plus(term("bar").of(nativeLine))
			.assertEqualTo(term("foo").plus(term("bar"))
				.of(nativeType.plus(nativeLine)))
	}

	@Test
	fun dynamicPlusStatic() {
		term("foo").of(nativeType)
			.plus(term("bar").of(line("bar")))
			.assertEqualTo(
				term("foo").of(nativeType.plus(line("bar"))))
	}

	@Test
	fun staticPlusDynamic() {
		term("foo").of(type("foo"))
			.plus(term("bar").of(nativeLine))
			.assertEqualTo(
				term("bar").of(type("foo").plus(nativeLine)))
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
			.of(type("zero" lineTo nativeType))
			.castTermTo(type(choice("zero", "one")))
			.assertEqualTo(fn(fn(arg1<Any>().invoke(term("lhs")))))
	}
}