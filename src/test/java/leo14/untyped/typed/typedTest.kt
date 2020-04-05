package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.lambda.runtime.valueInvoke
import leo14.untyped.intName
import kotlin.test.Test

class TypedTest {
	@Test
	fun selfTyped() {
		1.valueSelfTyped.typedAssertEqualTo(typed(selfType) { 1 })
		"foo".valueSelfTyped.typedAssertEqualTo(typed(selfType) { "foo" })
	}

	@Test
	fun typed() {
		"foo"(2.typed).typed.run {
			type.assertEqualTo("foo"(intName))
			value.assertEqualTo(2)
		}
	}

	@Test
	fun apply() {
		eval("typed"(1)).typedValue.assertEqualTo(1)
	}

	@Test
	fun ints() {
		eval(2.typed, "plus"(3.typed)).typedValue.assertEqualTo(5)
	}

	@Test
	fun valueTypedFunction() {
		"int".valueTypedFunction { it.valueApply("plus"(it)) }.value.valueInvoke(10).assertEqualTo(20)
	}
}