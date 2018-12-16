package leo.term

import leo.*

val bitPattern =
	term(
		bitWord apply valueTerm(
			matcher(
				oneOf(
					term(zeroWord apply null),
					term(oneWord apply null)))))

val unitStackPattern: Pattern =
	term(
		stackWord apply valueTerm(
			matcher(
				oneOf(
					term(emptyWord),
					valueTerm(
						matcher(
							oneOf(
								term(unitWord apply null),
								term(
									valueTerm(matcher(recursion(back))),
									unitWord apply null))))))))

val naturalNumberPattern: Pattern =
	valueTerm(
		matcher(
			oneOf(
				term(
					naturalWord apply term(
						numberWord apply term(zeroWord))),
				term(
					valueTerm(matcher(recursion(back))),
					plusWord apply term(oneWord)))))

