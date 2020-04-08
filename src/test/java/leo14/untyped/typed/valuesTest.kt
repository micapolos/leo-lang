package leo14.untyped.typed

import leo.base.assertEqualTo
import leo13.stack
import java.awt.Point
import kotlin.test.Test

class ValuesTest {
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
}