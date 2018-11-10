package leo

import leo.base.assertEqualTo
import leo.base.string
import leo.beginToken
import leo.endToken
import leo.oneWord
import leo.token
import kotlin.test.Test

class TokenTest {
	@Test
	fun string_meta() {
		token(1).string.assertEqualTo("token meta value todo")
	}

	@Test
	fun string_identifier() {
		token<Nothing>(oneWord).string.assertEqualTo("token identifier word one")
	}

	@Test
	fun string_begin() {
		beginToken<Nothing>().string.assertEqualTo("token begin")
	}

	@Test
	fun string_end() {
		endToken<Nothing>().string.assertEqualTo("token end")
	}
}