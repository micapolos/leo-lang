package leo.script

import leo.base.EnumBit
import leo.base.isZero
import leo.falseWord
import leo.oneWord
import leo.trueWord
import leo.zeroWord

val Boolean.word
	get() =
		if (this) trueWord else falseWord

val EnumBit.word
	get() =
		if (isZero) zeroWord else oneWord