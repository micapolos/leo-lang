package leo14.lambda

import leo.base.assertEqualTo
import leo14.native.intPlusIntNative
import leo14.native.native
import kotlin.test.Test

class EvalTest {
	@Test
	fun native() {
		term("foo")
			.eval
			.assertEqualTo(term("foo"))
	}

	@Test
	fun testId() {
		id<String>()
			.invoke(term("foo"))
			.eval
			.assertEqualTo(term("foo"))
	}

	@Test
	fun oneOf() {
		fn(fn(arg1<Any>().invoke(term("foo"))))
			.eval
			.assertEqualTo(fn(fn(arg1<Any>().invoke(term("foo")))))
	}

	@Test
	fun oneOfGet() {
		fn(fn(arg1<Any>().invoke(term("foo"))))
			.invoke(fn(term("first")))
			.invoke(fn(term("second")))
			.eval
			.assertEqualTo(term("first"))

		fn(fn(arg0<Any>().invoke(term("foo"))))
			.invoke(fn(term("first")))
			.invoke(fn(term("second")))
			.eval
			.assertEqualTo(term("second"))
	}

	@Test
	fun oneOfId() {
		fn(fn(arg1<Any>()(id())))(id())(id())
			.eval
			.assertEqualTo(id())
	}

	@Test
	fun nativePlus() {
		term(intPlusIntNative)
			.invoke(term(native(2)))
			.invoke(term(native(3)))
			.nativeEval
			.assertEqualTo(term(native(5)))
	}
}