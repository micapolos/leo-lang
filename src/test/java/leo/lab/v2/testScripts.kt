package leo.lab.v2

import leo.*

val personScript =
	script(
		personWord to script(
			firstWord to script(nameWord to stringWord.script),
			lastWord to script(nameWord to stringWord.script),
			ageWord to script(numberWord)))
