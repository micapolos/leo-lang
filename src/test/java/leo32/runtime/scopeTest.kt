package leo32.runtime

class ScopeTest {
//	@Test
//	fun define() {
//		val bitType = term("bit" to term("either" to term("zero"), "either" to term("one")))
//
//		val scope = empty.scope
//			.defineType(term("bit" to term("zero")), bitType)
//			.defineType(term("bit" to term("one")), bitType)
//			.defineTemplate(
//				term("negate" to bitType),
//				template(
//					op(
//						switch(
//							term("bit" to term("zero")) gives term("bit" to term("one")),
//							term("bit" to term("one")) gives term("bit" to term("zero"))))))
//
//		scope.emptyTerm
//			.invoke(term("bit" to term("zero")))
//			.assertEqualTo(scope.emptyTerm.plus(term("bit" to term("zero"))))
//	}
//
//	@Test
//	fun defineType() {
//		val type = type("boolean" to type(either("false"), either("true")))
//		val scope = empty.scope.define(type)
//
//		scope
//			.type(term("boolean" to term("false")))
//			.assertEqualTo(type)
//	}
}