package leo7

import leo.base.assertEqualTo
import leo.base.fold
import leo.base.orNull
import kotlin.test.Test

class TokenParserTest {
	@Test
	fun read() {
		newTokenParser
			.orNull
			.fold("zero(") { this?.read(it) }!!
			.parsedTokenOrNull
			.assertEqualTo(zeroWord.begin.token)

		newTokenParser
			.orNull
			.fold(")") { this?.read(it) }!!
			.parsedTokenOrNull
			.assertEqualTo(end.token)
	}
}
