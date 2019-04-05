package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class ValueTest {
	@Test
	fun syntax() {
		val scope = empty.scope
			.define(term("not" to term("true")) to function("false"))
			.define(term("not" to term("false")) to function("true"))
			.define(term("true" to term(), "negate" to term()) to function("false"))
			.define(term("false" to term(), "negate" to term()) to function("true"))
			.define(term("kura" to term()) to function("jajko"))
			.define(term("jajko" to term()) to function("kura"))
			.define(term("fixpoint" to term()) to function("fixpoint"))

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