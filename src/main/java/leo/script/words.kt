package leo.script

import leo.falseWord
import leo.trueWord

val Boolean.word
	get() =
		if (this) trueWord else falseWord
