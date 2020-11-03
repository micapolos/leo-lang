package leo21.typed

import leo.base.assertEqualTo
import leo.base.string
import leo13.linkTo
import leo14.lambda.first
import leo14.lambda.second
import leo21.term.nilTerm
import leo21.term.plus
import leo21.term.term
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.make
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.stringType
import leo21.type.struct
import leo21.type.type
import kotlin.test.Test

val term1 = term("lhs")
val term2 = term("rhs")

val staticLine1 = "x" lineTo type()
val staticLine2 = "y" lineTo type()

val staticType1 = type(staticLine1)
val staticType2 = type(staticLine2)

val dynamicLine1 = "x" lineTo stringType
val dynamicLine2 = "y" lineTo stringType

val dynamicType1 = type(dynamicLine1)
val dynamicType2 = type(dynamicLine2)

class TypedTermTest {
	@Test
	fun staticPlusStatic() {
		term1.of(staticType1)
			.plus(term2 of staticLine2)
			.assertEqualTo(nilTerm of staticType1.plus(staticLine2))
	}

	@Test
	fun staticPlusDynamic() {
		term1.of(staticType1)
			.plus(term2 of dynamicLine2)
			.assertEqualTo(term2 of staticType1.plus(dynamicLine2))
	}

	@Test
	fun dynamicPlusStatic() {
		term1.of(dynamicType1)
			.plus(term2 of staticLine2)
			.assertEqualTo(term1 of dynamicType1.plus(staticLine2))
	}

	@Test
	fun dynamicPlusDynamic() {
		term1.of(dynamicType1)
			.plus(term2 of dynamicLine2)
			.assertEqualTo(term1.plus(term2) of dynamicType1.plus(dynamicLine2))
	}

	@Test
	fun staticPlusStaticLink() {
		(term1 of staticType1.plus(staticLine2)).link
			.assertEqualTo((nilTerm of staticType1) linkTo (nilTerm of staticLine2))
	}

	@Test
	fun staticPlusDynamicLink() {
		(term1 of staticType1.plus(dynamicLine2)).link
			.assertEqualTo((nilTerm of staticType1) linkTo (term1 of dynamicLine2))
	}

	@Test
	fun dynamicPlusStaticLink() {
		(term1 of dynamicType1.plus(staticLine2)).link
			.assertEqualTo((term1 of dynamicType1) linkTo (nilTerm of staticLine2))
	}

	@Test
	fun dynamicPlusDynamicLink() {
		(term1 of dynamicType1.plus(dynamicLine2)).link
			.assertEqualTo((term1.first of dynamicType1) linkTo (term1.second of dynamicLine2))
	}

	@Test
	fun make() {
		term1.of(dynamicType1)
			.make("foo")
			.assertEqualTo(term1 of dynamicType1.make("foo"))
	}
}