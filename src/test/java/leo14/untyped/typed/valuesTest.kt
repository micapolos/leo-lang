package leo14.untyped.typed

import leo.base.assertEqualTo
import leo13.stack
import leo14.number
import java.awt.Point
import kotlin.test.Test

@Suppress("ReplaceToWithInfixForm")
class ValuesTest {
	@Test
	fun primitives() {
		2.number.applyMinusNumber.assertEqualTo((-2).number)
		2.number.to(3.number).applyNumberPlusNumber.assertEqualTo(5.number)
		5.number.to(3.number).applyNumberMinusNumber.assertEqualTo(2.number)
		2.number.to(3.number).applyNumberTimesNumber.assertEqualTo(6.number)
		2.number.applyTextNumber.assertEqualTo("2")

		"Hello, ".to("world!").applyTextPlusText.assertEqualTo("Hello, world!")
		"Hello, world!".applyNumberLengthText.assertEqualTo(13.number)
	}


	@Test
	fun valueArray() {
		stack("foo", 10)
			.valueArray
			.toList()
			.assertEqualTo(listOf("foo", 10))
	}

	@Test
	fun arrayAt() {
		arrayOf("foo", 10)
			.arrayAt(0)
			.assertEqualTo("foo")
	}

	@Test
	fun arrayValue() {
		arrayOf("foo", 10)
			.arrayValue
			.assertEqualTo(stack("foo", 10))
	}

	// === Reflection ===

	@Test
	fun stringClass() {
		"java.lang.String"
			.stringClass
			.assertEqualTo(java.lang.String::class.java)
	}

	@Test
	fun classField() {
		Integer::class.java
			.classField("MAX_VALUE")
			.assertEqualTo(Integer::class.java.getField("MAX_VALUE"))
	}

	@Test
	fun classConstructor() {
		Point::class.java
			.classConstructor(stack(Integer.TYPE, Integer.TYPE))
			.assertEqualTo(Point::class.java.getConstructor(Integer.TYPE, Integer.TYPE))
	}

	@Test
	fun classMethod() {
		Point::class.java
			.classMethod(stack("move", stack(Integer.TYPE, Integer.TYPE)))
			.assertEqualTo(Point::class.java.getMethod("move", Integer.TYPE, Integer.TYPE))
	}

	@Test
	fun fieldGet() {
		Point::class.java.getField("x")
			.fieldGet(Point(10, 20))
			.assertEqualTo(10)
	}

	@Test
	fun constructorInvoke() {
		Point::class.java.getConstructor(Integer.TYPE, Integer.TYPE)
			.constructorInvoke(stack(10, 20))
			.assertEqualTo(Point(10, 20))
	}

	@Test
	fun methodInvoke() {
		java.lang.String::class.java.getMethod("substring", Integer.TYPE, Integer.TYPE)
			.methodInvoke(stack("Hello, world!", stack(7, 12)))
			.assertEqualTo("world")
	}

	@Test
	fun arrays() {
		nullValue.valueListSize.assertEqualTo(0)
		null.to("foo").valueListSize.assertEqualTo(1)
		null.to("foo").to("bar").valueListSize.assertEqualTo(2)

		nullValue.valueListAsArray.toList().assertEqualTo(listOf())
		null.to("foo").valueListAsArray.toList().assertEqualTo(listOf("foo"))
		null.to("foo").to("bar").valueListAsArray.toList().assertEqualTo(listOf("foo", "bar"))
	}
}