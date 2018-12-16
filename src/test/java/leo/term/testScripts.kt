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

val unitScript: Script =
	term(unitWord apply null)

val twoUnitsScript: Script =
	term(
		unitWord apply null,
		unitWord apply null)

val threeUnitsScript: Script =
	term(
		unitWord apply null,
		unitWord apply null,
		unitWord apply null)

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
