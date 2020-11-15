package leo21.compiled

import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import leo13.firstEither
import leo14.lambda.eitherFirst
import leo14.lambda.eitherSecond
import leo14.lambda.nativeTerm
import leo21.prim.prim
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.emptyLines
import leo21.type.lineTo
import leo21.type.plus
import leo21.type.stringLine
import leo21.type.type
import kotlin.test.Test

class CastTest {
	@Test
	fun choice_1() {
		emptyLines
			.plus("a" lineTo type(choice(stringLine, doubleLine)))
			.cast("a" lineTo compiled(12.0))
			.assertEqualTo(
				nativeTerm(prim(12.0)).eitherFirst
					.of("a" lineTo type(choice(stringLine, doubleLine))))
	}

	@Test
	fun choice_2() {
		emptyLines
			.plus("a" lineTo type(choice(stringLine, doubleLine)))
			.cast("a" lineTo compiled("foo"))
			.assertEqualTo(
				nativeTerm(prim("foo")).eitherFirst.eitherSecond
					.of("a" lineTo type(choice(stringLine, doubleLine))))
	}
}