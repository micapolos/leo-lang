package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.eitherFirst2
import leo14.lambda.eitherSecond2
import leo14.lambda.nativeTerm
import leo21.prim.prim
import leo21.type.choice
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberLine
import leo21.type.recursive
import leo21.type.stringLine
import leo21.type.type
import kotlin.test.Test

class CastTest {
	@Test
	fun choice_1() {
		compiled("a" lineTo compiled(12.0))
			.castOrNull(type("a" lineTo type(line(choice(stringLine, numberLine)))))
			.assertEqualTo(
				nativeTerm(prim(12.0)).eitherFirst2
					.of(type("a" lineTo type(line(choice(stringLine, numberLine)))))
					.nonIdentityCast)
	}

	@Test
	fun choice_2_last() {
		compiled("a" lineTo compiled("foo"))
			.castOrNull(type("a" lineTo type(line(choice(stringLine, numberLine)))))
			.assertEqualTo(
				nativeTerm(prim("foo")).eitherSecond2
					.of(type("a" lineTo type(line(choice(stringLine, numberLine)))))
					.nonIdentityCast)
	}

	@Test
	fun choice_2_middle() {
		compiled("a" lineTo compiled("foo"))
			.castOrNull(type("a" lineTo type(line(choice("x" lineTo type(), stringLine, numberLine)))))
			.assertEqualTo(
				nativeTerm(prim("foo")).eitherFirst2.eitherSecond2
					.of(type("a" lineTo type(line(choice("x" lineTo type(), stringLine, numberLine)))))
					.nonIdentityCast)
	}

	@Test
	fun recursive() {
		compiled("empty" lineTo compiled())
			.castOrNull(type(line(recursive("empty" lineTo type()))))
			.assertEqualTo(compiled(line(recursive("empty" lineTo compiled()))).identityCast)
	}
}