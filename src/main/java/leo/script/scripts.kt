package leo.script

import leo.booleanWord

val Boolean.script
	get() =
		nullScript.plus(booleanWord, nullScript.plus(word))