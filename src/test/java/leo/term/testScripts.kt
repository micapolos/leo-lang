package leo.term

import leo.*

val personScript =
	script(
		personWord apply script(
			firstWord apply script(nameWord apply stringWord.script),
			lastWord apply script(nameWord apply stringWord.script),
			ageWord apply numberWord.script))
