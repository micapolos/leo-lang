package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ValueTest {
	@Test
	fun syntax() {
		val booleanType = type(either("false"), either("true"))

		val scope = empty.scope
			.define(term("not" to term("true")) to (booleanType to template("false")))
			.define(term("not" to term("false")) to (booleanType to template("true")))
			.define(term("true" to term(), "negate" to term()) to (booleanType to template("false")))
			.define(term("false" to term(), "negate" to term()) to (booleanType to template("true")))
			.define(term("kura" to term()) to (type("jajko") to template("jajko")))
			.define(term("jajko" to term()) to (type("kura") to template("kura")))
			.define(term("fixpoint" to term()) to (type("fixpoint") to template("fixpoint")))

		scope.value
			.term.assertEqualTo(term())

		scope.value
			.plus(term("circle"))
			.term
			.assertEqualTo(term("circle"))

		scope.value
			.plus(term("not" to term("false")))
			.term
			.assertEqualTo(term("true"))

		scope.value
			.plus(term("not" to term("true")))
			.term
			.assertEqualTo(term("false"))

		scope.value
			.plus("true" to term())
			.plus("negate" to term())
			.term
			.assertEqualTo(term("false"))

		scope.value
			.plus("true" to term())
			.plus("negate" to term())
			.plus("negate" to term())
			.term
			.assertEqualTo(term("true"))

		scope.value
			.plus("kura" to term())
			.term
			.assertEqualTo(term("jajko"))

		scope.value
			.plus("kura" to term())
			.term
			.assertEqualTo(term("jajko"))

		scope.value
			.plus("jajko" to term())
			.term
			.assertEqualTo(term("kura"))

		scope.value
			.plus("fixpoint" to term())
			.term
			.assertEqualTo(term("fixpoint"))
	}
}