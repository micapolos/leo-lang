package leo.script

import leo.binary.Bit
import leo.binary.isZero
import leo.falseWord
import leo.oneWord
import leo.trueWord
import leo.zeroWord

val Boolean.word
	get() =
		if (this) trueWord else falseWord

val Bit.word
	get() =
		if (isZero) zeroWord else oneWord