package leo13.script

import leo.base.assertEqualTo
import leo13.*
import leo13.script.parser.error
import kotlin.test.Test

class CompilerTest {
	@Test
	fun pushOpening() {
		compiler("foo(")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(compiled() openerTo opening("foo")),
						compiled())))
	}

	@Test
	fun pushOpeningOpening() {
		compiler("foo(bar(")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(
							compiled() openerTo opening("foo"),
							compiled() openerTo opening("bar")),
						compiled())))
	}

	@Test
	fun pushOpeningOpeningClosing() {
		compiler("foo(bar()")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(compiled() openerTo opening("foo")),
						compiled("bar()".unsafeScript.typed))))
	}

	@Test
	fun pushOpeningOpeningClosingClosing() {
		compiler("foo(bar())")
			.assertEqualTo(
				compiler()
					.set(head("foo(bar())".unsafeScript.typed)))
	}

	@Test
	fun pushOpeningOpeningClosingClosingClosing() {
		compiler("foo(bar()))")
			.assertEqualTo(
				compiler("foo(bar())")
					.set(error(token(closing))))
	}

	@Test
	fun pushAppend() {
		compiler("zero()")
			.assertEqualTo(
				compiler().set(
					head(
						compiledOpeners(),
						compiled(
							context(),
							"zero()".unsafeScript.typed))))
	}

	@Test
	fun pushClosing() {
		compiler("zero())")
			.assertEqualTo(
				compiler("zero()")
					.set(error(token(closing))))
	}

	@Test
	fun pushOf_success() {
		compiler("zero()of(zero()or(one()))")
			.assertEqualTo(
				compiler()
					.set(head(typed(
						"zero()".unsafeScript.expr,
						"zero()or(one())".unsafeScript.type))))
	}

	@Test
	fun pushOf_error_typeMismatch() {
		compiler("zero()of(one())")
			.assertEqualTo(compiler("zero()of(one()").set(error(token(closing))))
	}

	@Test
	fun pushOf_error_dynamicType() {
		compiler("of(zero()of(zero()or(one())))")
			.assertEqualTo(compiler("of(zero()of(zero()or(one()))").set(error(token(closing))))
	}

	@Test
	fun pushExists_success() {
		compiler("zero()or(one())exists()")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(),
						compiled(
							context().plus("zero()or(one())".unsafeScript.type),
							typed()))))
	}

	@Test
	fun pushExists_error_dynamicLhs() {
		compiler("zero()of(zero()or(one()))exists()")
			.assertEqualTo(
				compiler("zero()of(zero()or(one()))exists(")
					.set(error(token(closing))))
	}

	@Test
	fun pushExists_error_nonEmptyRhs() {
		compiler("exists(zero())")
			.assertEqualTo(
				compiler("exists(zero()")
					.set(error(token(closing))))
	}

	@Test
	fun pushGives_success_minimum() {
		compiler("gives()")
			.assertEqualTo(
				compiler().set(
					context().plus(function(type(), typed())).compiled.head))
	}

	@Test
	fun pushGives_success() {
		compiler("zero()or(one())gives(bit())")
			.assertEqualTo(
				compiler().set(
					context().plus(
						function(
							"zero()or(one())".unsafeScript.type,
							"bit()".unsafeScript.typed)).compiled.head))
	}

	@Test
	fun pushGives_error_dynamicLhs() {
		compiler("zero()of(zero()or(one()))gives(bit())")
			.assertEqualTo(
				compiler("zero()of(zero()or(one()))gives(bit()")
					.set(error(token(closing))))
	}

	@Test
	fun pushGives_error_dynamicRhs() {
		compiler("gives(zero()of(zero()or(one())))")
			.assertEqualTo(
				compiler("gives(zero()of(zero()or(one()))")
					.set(error(token(closing))))
	}
}