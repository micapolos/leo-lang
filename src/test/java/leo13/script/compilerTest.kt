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
			.plus(type("bit" lineTo type(choice("zero" caseTo type(), "one" caseTo type()))))
			.let { compiler ->
				compiler
					.push(script("bit" lineTo script("one" lineTo script())))
					.assertEqualTo(
						compiler
							.with(
								script("bit" lineTo script("one" lineTo script())).expr of
									type("bit" lineTo type(choice("zero" caseTo type(), "one" caseTo type())))))
			}
	}

	@Test
	fun compileTypeResolutionAndFunction() {
		compiler()
			.plus(type("bit" lineTo type(choice("zero" caseTo type(), "one" caseTo type()))))
			.plus(
				function(
					type("bit" lineTo type(choice("zero" caseTo type(), "one" caseTo type()))),
					expr(op(argument())).plus(op("applied" lineTo expr())) of type("applied" lineTo type())))
			.let { compiler ->
				compiler
					.push(script("bit" lineTo script("one" lineTo script())))
					.assertEqualTo(
						compiler
							.with(
								script("bit" lineTo script("one" lineTo script()))
									.expr.plus(op(call(expr(op(argument()), op("applied" lineTo expr())))))
									of type("applied" lineTo type())))
			}
	}

	@Test
	fun scriptCompileName() {
		script("foo" lineTo script())
			.compile
			.assertEqualTo(
				script("foo" lineTo script()))
	}

	@Test
	fun scriptCompileLine() {
		script("foo" lineTo script("bar" lineTo script()))
			.compile
			.assertEqualTo(
				script("foo" lineTo script("bar" lineTo script())))
	}

	@Test
	fun scriptCompile() {
		script(
			"bit" lineTo script(
				"zero" lineTo script()),
			"negate" lineTo script(),
			"gives" lineTo script(
				"bit" lineTo script(
					"one" lineTo script())),
			"bit" lineTo script(
				"zero" lineTo script()),
			"negate" lineTo script())
			.compile
			.assertEqualTo(
				script(
					"bit" lineTo script(
						"one" lineTo script())))
	}
}