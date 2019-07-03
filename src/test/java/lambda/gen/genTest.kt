package lambda.gen

import lambda.invoke
import lambda.term
import leo.base.assertEqualTo
import kotlin.test.Test

class GenTest {

	@Test
	fun all() {
		term { x -> term { y -> x(y) } }.js.assertEqualTo("function(v0) { return function(v1) { return v0(v1); }; }")
		term { x -> term { y -> x(y) } }.kotlin.assertEqualTo("term { v0 -> term { v1 -> v0(v1) } }")
		term { x -> term { y -> x(y) } }.java.assertEqualTo("term(v0 -> term(v1 -> v0.apply(v1)))")
		term { x -> term { y -> x(y) } }.haskell.assertEqualTo("\\v0 -> \\v1 -> v0 v1")
		term { x -> term { y -> x(y) } }.lisp.assertEqualTo("(lambda v0 (lambda v1 (v0 v1)))")
		term { x -> term { y -> x(y) } }.leo.assertEqualTo("v0 -> v1 -> v0 v1")
	}
}
