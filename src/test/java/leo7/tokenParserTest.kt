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
			.fold("jajeczko(") { this?.read(it) }!!
			.parsedTokenOrNull
			.assertEqualTo("jajeczko".wordOrNull!!.begin.token)
	}
}
