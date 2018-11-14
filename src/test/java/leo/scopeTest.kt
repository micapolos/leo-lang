package leo

import leo.base.assertContains
import org.junit.Test

class ScopeTest {
	@Test
	fun byteStream_nullValueTerm() {
		Scope(oneWord, identityFunction, null)
			.byteStream
			.assertContains(
				Letter.O.byte,
				Letter.N.byte,
				Letter.E.byte)
	}

	@Test
	fun byteStream_nonNullValueTerm() {
		Scope(oneWord, identityFunction, term(twoWord))
			.byteStream
			.assertContains(
				Letter.O.byte,
				Letter.N.byte,
				Letter.E.byte,
				'('.toByte(),
				Letter.T.byte,
				Letter.W.byte,
				Letter.O.byte)
	}
}