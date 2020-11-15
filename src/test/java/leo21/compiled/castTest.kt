package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.nativeTerm
import leo21.prim.prim
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.stringLine
import leo21.type.type
import kotlin.test.Test

class CastTest {
	@Test
	fun choice_1() {
		("a" lineTo compiled(12.0))
			.castOrNull("a" lineTo type(choice(stringLine, doubleLine)))
			.assertEqualTo(
				nativeTerm(prim(12.0)).eitherFirst
					.of("a" lineTo type(choice(stringLine, doubleLine)))
					.nonIdentityCast)
	}

	@Test
	fun choice_2() {
		("a" lineTo compiled("foo"))
			.castOrNull("a" lineTo type(choice(stringLine, doubleLine)))
			.assertEqualTo(
				nativeTerm(prim("foo")).eitherFirst.eitherSecond
					.of("a" lineTo type(choice(stringLine, doubleLine)))
					.nonIdentityCast)
	}
}