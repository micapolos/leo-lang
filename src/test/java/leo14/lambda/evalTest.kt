package leo14.lambda

import leo.base.assertEqualTo
import leo14.native.*
import kotlin.test.Test
import kotlin.test.assertFails

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
	fun invokeIgnoreArgument() {
		fn(term("foo"))
			.invoke(term("bar"))
			.eval
			.assertEqualTo(term("foo"))
	}

	@Test
	fun innerInvoke() {
		fn(arg0<String>().invoke(term("foo")))
			.invoke(fn(term("bar")))
			.eval
			.assertEqualTo(term("bar"))
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
		fn(fn(arg1<Any>().invoke(id())))
			.invoke(id())
			.invoke(id())
			.eval
			.assertEqualTo(id())
	}

	@Test
	fun nativePlus() {
		term(numberPlusNumberNative)
			.invoke(term(native(2)))
			.invoke(term(native(3)))
			.nativeEval
			.assertEqualTo(term(native(5)))
	}

	@Test
	fun stackOverflow() {
		assertFails {
			val fn =
				fn(
					fn(
						arg1<Native>()
							.invoke(arg1())
							.invoke(term(numberIncNative).invoke(arg0()))))
			fn(arg0<Native>().invoke(arg0()))
				.invoke(fn)
				.invoke(term(native(0)))
				.nativeEval
				.assertEqualTo(null)
		}
	}
}