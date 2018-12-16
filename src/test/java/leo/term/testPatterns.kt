package leo.term

import leo.bitWord
import leo.byteWord
import leo.oneWord
import leo.zeroWord

val bitPatternApplication =
	bitWord apply term(
		oneOf(
			term(zeroWord apply null),
			term(oneWord apply null)))

val bitPattern =
	term(bitPatternApplication)

val bytePattern =
	term(
		byteWord apply term(
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication))