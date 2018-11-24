package leo

val Unit.reflect: Field<Nothing>
	get() =
		unitWord fieldTo unitWord.term

val Field<Nothing>.parseUnit: Unit?
	get() =
		matchKey(unitWord) {
			matchWord(unitWord) {
				Unit
			}
		}