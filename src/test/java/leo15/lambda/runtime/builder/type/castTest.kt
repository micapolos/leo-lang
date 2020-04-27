package leo15.lambda.runtime.builder.type

import leo.base.X
import leo.base.assertEqualTo
import leo15.lambda.runtime.builder.choiceTerm
import leo15.lambda.runtime.builder.term
import kotlin.test.Test

class CastTest {
	@Test
	fun struct() {
		cast(
			type("point"("x"(type("int")), "y"(type("int")))),
			term(null),
			type("point"("x"(type("int")), "y"(type("int")))),
			null)
			.assertEqualTo(Cast(null))
	}

	@Test
	fun choice() {
		cast(
			type<X>("boolean"("false"())),
			term(null),
			type("boolean"(type(choice("false"(), "true"())))),
			null)
			.assertEqualTo(Cast(choiceTerm(2, 0, term(null))))
	}

	@Test
	fun recursion_equal() {
		cast(
			type<X>(recursive(type(choice("stop"(), "recurse"(type(recurse)))))),
			term(null),
			type(recursive(type(choice("stop"(), "recurse"(type(recurse)))))),
			null)
			.assertEqualTo(Cast(null))
	}

	@Test
	fun recursion_recurse() {
		cast(
			type<X>(choice("stop"(), "recurse"(type(recurse)))),
			term(null),
			type(recursive(type(choice("stop"(), "recurse"(type(recurse)))))),
			null)
			.assertEqualTo(Cast(null))
	}
}