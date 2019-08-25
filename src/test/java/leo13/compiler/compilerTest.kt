package leo13.compiler

import leo.base.assertEqualTo
import leo13.*
import leo13.script.*
import leo13.script.parser.error
import kotlin.test.Test

class CompilerTest {
	@Test
	fun pushOpening() {
		compiler("foo(")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(metable() openerTo opening("foo")),
						metable())))
	}

	@Test
	fun pushOpeningOpening() {
		compiler("foo(bar(")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(
							metable() openerTo opening("foo"),
							metable() openerTo opening("bar")),
						metable())))
	}

	@Test
	fun pushOpeningOpeningClosing() {
		compiler("foo(bar()")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(metable() openerTo opening("foo")),
						metable(false, compiled("bar()".unsafeScript.typed)))))
	}

	@Test
	fun pushOpeningOpeningClosingClosing() {
		compiler("foo(bar())")
			.assertEqualTo(
				compiler(
					head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context(),
								"foo(bar())".unsafeScript.typed))),
					null))
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
						metable(false, compiled("zero()".unsafeScript.typed)))))
	}

	@Test
	fun pushClosing() {
		compiler("zero())")
			.assertEqualTo(
				compiler("zero()")
					.set(error(token(closing))))
	}

	@Test
	fun pushMeta() {
		compiler("meta(")
			.assertEqualTo(
				compiler(
					head(
						compiledOpeners(),
						metable(
							true,
							compiled())),
					null))

		compiler("meta()")
			.assertEqualTo(compiler())

		compiler("meta(meta(")
			.assertEqualTo(
				compiler(
					head(
						compiledOpeners(
							opener(
								metable(
									true,
									compiled()),
								opening("meta"))),
						metable()),
					null))

		compiler("meta(meta()")
			.assertEqualTo(
				compiler(
					head(
						compiledOpeners(),
						metable(
							true,
							compiled(
								context(),
								typed(
									expr(op("meta" lineTo expr())),
									type("meta" lineTo type()))))),
					null))

		compiler("meta(meta())")
			.assertEqualTo(
				compiler(
					head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context(),
								typed(
									expr(op("meta" lineTo expr())),
									type("meta" lineTo type()))))),
					null))
	}

	@Test
	fun pushMetaFoo() {
		compiler("meta(foo())")
			.assertEqualTo(compiler().set(head("foo()".unsafeScript.typed)))
	}

	@Test
	fun pushOf() {
		compiler("of()")
			.assertEqualTo(compiler().set(head(typed())))
	}

	@Test
	fun pushMetaOf() {
		compiler("meta(of())")
			.assertEqualTo(compiler().set(head("of()".unsafeScript.typed)))
	}

	@Test
	fun pushOf_simple() {
		compiler("zero()of(zero())")
			.assertEqualTo(
				compiler()
					.set(head("zero()".unsafeScript.typed)))
	}

	@Test
	fun pushOf_complex() {
		compiler("zero()of(choice(either(zero())either(one())))")
			.assertEqualTo(
				compiler()
					.set(head(typed(
						"zero()".unsafeScript.expr,
						"choice(either(zero())either(one()))".unsafeType))))
	}

	@Test
	fun pushOf_moreComplex() {
		compiler("zero(foo())of(choice(either(zero(foo()))either(one(bar()))))")
			.assertEqualTo(
				compiler()
					.set(head(typed(
						"zero(foo())".unsafeScript.expr,
						"choice(either(zero(foo()))either(one(bar())))".unsafeType))))
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
	fun pushExists() {
		compiler("exists()")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context().plus(type()),
								typed())))))
	}

	@Test
	fun pushMetaExists() {
		compiler("meta(exists())")
			.assertEqualTo(compiler().set(head("exists()".unsafeScript.typed)))
	}

	@Test
	fun pushExists_complex() {
		compiler("zero()or(one())exists()")
			.assertEqualTo(
				compiler()
					.set(head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context().plus("zero()or(one())".unsafeType),
								typed())))))
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
	fun pushGives() {
		compiler("gives()")
			.assertEqualTo(
				compiler().set(
					head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context().plus(function(type(), typed())),
								typed())))))
	}

	@Test
	fun pushMetaGives() {
		compiler("meta(gives())")
			.assertEqualTo(compiler().set(head("gives()".unsafeScript.typed)))
	}

	@Test
	fun pushGives_complex() {
		compiler("zero()or(one())gives(bit())")
			.assertEqualTo(
				compiler().set(
					head(
						compiledOpeners(),
						metable(
							false,
							compiled(
								context().plus(function("zero()or(one())".unsafeType, "bit()".unsafeScript.typed)),
								typed())))))
	}

	@Test
	fun pushGives_metaDynamicRhs() {
		compiler("gives(zero()meta(of(zero()or(one()))))")
		compiler().set(
			head(
				compiledOpeners(),
				metable(
					false,
					compiled(
						context().plus(function("".unsafeType, "zero()of(zero()or(one()))".unsafeScript.typed)),
						typed()))))
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

	@Test
	fun pushPrevious() {
		compiler("x(one())y(two())z(three())previous()")
			.assertEqualTo(compiler()
				.set(head(typed(
					"x(one())y(two())z(three())".unsafeScript.expr.plus(op(lhs)),
					"x(one())y(two())".unsafeType))))
	}

	@Test
	fun pushLine() {
		compiler("x(one())y(two())z(three())line()")
			.assertEqualTo(compiler()
				.set(head(typed(
					"x(one())y(two())z(three())".unsafeScript.expr.plus(op(rhsLine)),
					"z(three())".unsafeType))))
	}

	@Test
	fun pushGet() {
		compiler("vec(x(zero())y(one()))x()")
			.assertEqualTo(compiler()
				.set(head(typed(
					"vec(x(zero())y(one()))".unsafeScript.expr.plus(op(leo13.script.get("x"))),
					"x(zero())".unsafeType))))
	}

	@Test
	fun pushSwitch_simple() {
		compiler("zero(foo())of(choice(either(zero(foo()))))switch(case(zero(bar())))")
			.assertEqualTo(compiler()
				.set(head(typed(
					"zero(foo())".unsafeExpr.plus(op(switch("zero" caseTo script("bar").expr))),
					"foo()bar()".unsafeType))))
	}

	@Test
	fun pushSwitch_dynamic() {
		compiler(
			"zero(foo())" +
				"of(choice(either(zero(foo()))either(one(bar()))))" +
				"switch(case(zero(meta(previous()zoo()previous())))case(one(meta(previous()zar()previous()))))")
			.assertEqualTo(compiler()
				.set(head(typed(
					"zero(foo())".unsafeExpr.plus(op(switch(
						"zero" caseTo expr(op(lhs), op("zoo" lineTo expr()), op(lhs)),
						"one" caseTo expr(op(lhs), op("zar" lineTo expr()), op(lhs))))),
					"".unsafeType))))
	}
}
