package leo.term

import leo.*

val personScript =
	script(
		term(
			personWord apply term(
				firstWord apply term(nameWord apply term(stringWord)),
				lastWord apply term(nameWord apply term(stringWord),
					ageWord apply term(numberWord)))))

val personLastNameSelector =
	selector(rhsGetter, lhsGetter, rhsGetter, rhsGetter)