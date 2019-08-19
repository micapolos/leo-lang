package leo13.script

import leo.base.assertEqualTo
import leo13.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun compileName() {
		compiler()
			.push(script("foo" lineTo script()))
			.assertEqualTo(
				compiler()
					.with(script("foo" lineTo script()).exactTypedExpr))
	}

	@Test
	fun compileLine() {
		compiler()
			.push(script("foo" lineTo script("bar" lineTo script())))
			.assertEqualTo(
				compiler()
					.with(script("foo" lineTo script("bar" lineTo script())).exactTypedExpr))
	}

	@Test
	fun compileLink() {
		compiler()
			.push(script("foo" lineTo script(), "bar" lineTo script("goo" lineTo script())))
			.assertEqualTo(
				compiler()
					.with(script("foo" lineTo script(), "bar" lineTo script("goo" lineTo script())).exactTypedExpr))
	}

	@Test
	fun compileArgument() {
		compiler()
			.bind(type("foo" lineTo type()))
			.push(script("given" lineTo script()))
			.assertEqualTo(
				compiler()
					.bind(type("foo" lineTo type()))
					.with(expr(op(argument())) of type("foo" lineTo type())))
	}

	@Test
	fun compileExists() {
		compiler()
			.push(script("foo" lineTo script(), "exists" lineTo script()))
			.assertEqualTo(
				compiler()
					.plus(type("foo" lineTo type())))
	}

	@Test
	fun compileGives() {
		compiler()
			.push(script("foo" lineTo script(), "gives" lineTo script("bar" lineTo script())))
			.assertEqualTo(
				compiler()
					.plus(
						function(
							type("foo" lineTo type()),
							script("bar" lineTo script()).exactTypedExpr)))
	}

	@Test
	fun compileTypeResolution() {
		compiler()
			.plus(type("bit" lineTo type(choice("zero" lineTo type()), choice("one" lineTo type()))))
			.push(script("bit" lineTo script("one" lineTo script())))
			.assertEqualTo(
				compiler()
					.plus(type("bit" lineTo type(choice("zero" lineTo type()), choice("one" lineTo type()))))
					.with(
						script("bit" lineTo script("one" lineTo script())).expr of
							type("bit" lineTo type(choice("zero" lineTo type()), choice("one" lineTo type())))))
	}
}