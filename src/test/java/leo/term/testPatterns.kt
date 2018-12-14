package leo.term

import leo.bitWord
import leo.byteWord
import leo.oneWord
import leo.zeroWord

val bitPatternApplication =
	bitWord apply pattern(
		oneOf(
			pattern(zeroWord apply null),
			pattern(oneWord apply null)))

val bitPattern =
	pattern(bitPatternApplication)

val bytePattern =
	pattern(
		byteWord apply pattern(
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication,
			bitPatternApplication))