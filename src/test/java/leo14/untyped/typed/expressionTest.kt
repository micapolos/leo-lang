package leo14.untyped.typed

import leo.base.assertEqualTo
import leo13.stack
import java.awt.Point
import kotlin.test.Test

class ExpressionTest {
	@Test
	fun valueArray() {
		expression { stack(expression { "foo" }, expression { 10 }) }
			.valueArray
			.value
			.run { this as Array<*> }
			.toList()
			.assertEqualTo(listOf("foo", 10))
	}

	@Test
	fun arrayAt() {
		expression { arrayOf("foo", 10) }
			.arrayAt(expression { 0 })
			.value
			.assertEqualTo("foo")
	}

	@Test
	fun arrayValue() {
		expression { arrayOf("foo", 10) }
			.arrayValue
			.value
			.assertEqualTo(stack(expression { "foo" }, expression { 10 }))
	}

	// === Reflection ===

	@Test
	fun stringClass() {
		expression { "java.lang.String" }
			.stringClass
			.value
			.assertEqualTo(java.lang.String::class.java)
	}

	@Test
	fun classField() {
		expression { Integer::class.java }
			.classField(expression { "MAX_VALUE" })
			.value
			.assertEqualTo(Integer::class.java.getField("MAX_VALUE"))
	}

	@Test
	fun classConstructor() {
		expression { Point::class.java }
			.classConstructor(expression { stack(expression { Integer.TYPE }, expression { Integer.TYPE }) })
			.value
			.assertEqualTo(Point::class.java.getConstructor(Integer.TYPE, Integer.TYPE))
	}

	@Test
	fun classMethod() {
		expression { Point::class.java }
			.classMethod(
				expression {
					stack(
						expression { "move" },
						expression {
							stack(
								expression { Integer.TYPE },
								expression { Integer.TYPE })
						})
				})
			.value
			.assertEqualTo(Point::class.java.getMethod("move", Integer.TYPE, Integer.TYPE))
	}

	@Test
	fun fieldGet() {
		expression { Point::class.java.getField("x") }
			.fieldGet(expression { Point(10, 20) })
			.value
			.assertEqualTo(10)
	}

	@Test
	fun constructorInvoke() {
		expression { Point::class.java.getConstructor(Integer.TYPE, Integer.TYPE) }
			.constructorInvoke(expression { stack(expression { 10 }, expression { 20 }) })
			.value
			.assertEqualTo(Point(10, 20))
	}

	@Test
	fun methodInvoke() {
		expression { java.lang.String::class.java.getMethod("substring", Integer.TYPE, Integer.TYPE) }
			.methodInvoke(
				expression {
					stack(
						expression { "Hello, world!" },
						expression {
							stack(
								expression { 7 },
								expression { 12 })
						})
				})
			.value
			.assertEqualTo("world")
	}
}