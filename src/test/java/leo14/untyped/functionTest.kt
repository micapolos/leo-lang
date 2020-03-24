package leo14.untyped

import leo.base.assertEqualTo
import leo14.invoke
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun applyAppends() {
		function(
			context(),
			script(givenName lineTo script()))
			.apply(thunk(value("foo" lineTo value())))
			.assertEqualTo(
				thunk(
					value(
						givenName lineTo value(
							"foo" lineTo value()))))
	}

	@Test
	fun applyResolvesContext() {
		function(
			context(
				rule(
					pattern(
						thunk(
							value(
								"foo" lineTo value()))),
					body(
						thunk(
							value(
								"bar" lineTo value()))))),
			script("foo" lineTo script()))
			.apply(thunk(value("goo" lineTo value())))
			.assertEqualTo(thunk(value("bar")))
	}

	@Test
	fun lazyRecursion() {
		val function = function(
			context(),
			script("lazy"("do"("recurse"()))),
			recursive = true)
		val param = thunk(value())

		function
			.apply(param)
			.assertEqualTo(
				thunk(lazy(
					context(
						function.recurseRule,
						param.givenRule),
					script("do"("recurse"())))))

		function
			.apply(param)
			.force
			.assertEqualTo(
				thunk(lazy(
					context(
						function.recurseRule,
						param.givenRule),
					script("do"("recurse"())))))
	}
}