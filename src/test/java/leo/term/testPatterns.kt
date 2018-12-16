package leo.term

import leo.*

val bitPattern =
	term(
		bitWord apply valueTerm(
			expander(
				oneOf(
					term(zeroWord apply null),
					term(oneWord apply null)))))

val unitsPattern =
	valueTerm(
		expander(
			oneOf(
				term(unitWord apply null),
				term(
					valueTerm(expander(recursion(back))),
					unitWord apply null))))

val naturalNumberPattern =
	valueTerm(
		expander(
			oneOf(
				term(
					naturalWord apply term(
						numberWord apply term(zeroWord))),
				term(
					valueTerm(expander(recursion(back))),
					plusWord apply term(oneWord)))))

