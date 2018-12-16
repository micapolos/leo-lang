package leo.term

import leo.*

val personScript: Script =
	term(
		personWord apply term(
			firstWord apply term(nameWord apply term(stringWord)),
			lastWord apply term(nameWord apply term(stringWord),
				ageWord apply term(numberWord))))

val personLastNameSelector =
	selector(rhsGetter, lhsGetter, rhsGetter, rhsGetter)

val emptyUnitStackScript: Script =
	term(stackWord apply term(emptyWord))

val oneUnitStackScript: Script =
	term(
		stackWord apply term(
			unitWord apply null))

val twoUnitsStackScript: Script =
	term(
		stackWord apply term(
			unitWord apply null,
			unitWord apply null))

val naturalNumberZeroScript: Script =
	term(
		naturalWord apply term(
			numberWord apply term(
				zeroWord)))

val naturalNumberOneScript: Script =
	term(
		naturalWord apply term(numberWord apply term(zeroWord)),
		plusWord apply term(oneWord))

val naturalNumberTwoScript: Script =
	term(
		naturalWord apply term(numberWord apply term(zeroWord)),
		plusWord apply term(oneWord),
		plusWord apply term(oneWord))
