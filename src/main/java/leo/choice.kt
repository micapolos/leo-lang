package leo

import leo.base.*

data class Choice(
	val choiceTermStack: Stack<Term<Choice>>) {
	override fun toString() = reflect.string
}

val Stack<Term<Choice>>.oneOf
	get() =
		Choice(this)

fun choice(choiceTerm: Term<Choice>, vararg choiceTerms: Term<Choice>) =
	stack(choiceTerm, *choiceTerms).oneOf

// === matching

fun Term<Nothing>.matches(choice: Choice): Boolean =
	choice.choiceTermStack.top { oneOfTerm ->
		matches(oneOfTerm)
	} != null

// === matching

fun Term<Nothing>.matches(choiceTerm: Term<Choice>): Boolean =
	when (choiceTerm) {
		is Term.Meta -> matches(choiceTerm)
		is Term.Structure -> this is Term.Structure && this.matches(choiceTerm)
	}

fun Term<Nothing>.matches(choiceMetaTerm: Term.Meta<Choice>): Boolean =
	matches(choiceMetaTerm.value)

fun Term.Structure<Nothing>.matches(choiceStructureTerm: Term.Structure<Choice>): Boolean =
	topField.matches(choiceStructureTerm.topField) &&
		if (lhsTermOrNull == null) choiceStructureTerm.lhsTermOrNull == null
		else choiceStructureTerm.lhsTermOrNull != null &&
			lhsTermOrNull.matches(choiceStructureTerm.lhsTermOrNull)

fun Field<Nothing>.matches(choiceField: Field<Choice>) =
	word == choiceField.word &&
		if (termOrNull == null) choiceField.termOrNull == null
		else choiceField.termOrNull != null && termOrNull.matches(choiceField.termOrNull)

// === parsing

val Term<Nothing>.parseChoice: Choice?
	get() =
		structureTermOrNull?.run {
			true.fold(fieldStream) { field ->
				this && field.word == eitherWord && field.termOrNull != null
			}.let { isOneOf ->
				if (!isOneOf) null
				else fieldStream
					.reverse
					.foldFirst { field -> field.termOrNull!!.parseChoiceTerm.onlyStack }
					.foldNext { field -> push(field.termOrNull!!.parseChoiceTerm) }
					.oneOf
			}
		}

val Term<Nothing>.parseChoiceTerm: Term<Choice>
	get() =
		when (this) {
			is Term.Meta -> fail
			is Term.Structure -> parseChoice?.metaTerm ?: parseStructureChoiceTerm
		}

val Term.Structure<Nothing>.parseStructureChoiceTerm: Term<Choice>
	get() =
		fieldStream
			.reverse
			.foldFirst { field -> field.parseChoiceField.onlyStack }
			.foldNext { field -> push(field.parseChoiceField) }
			.term

val Field<Nothing>.parseChoiceField: Field<Choice>
	get() =
		word fieldTo termOrNull?.parseChoiceTerm

// === reflect

val Choice.reflect: Field<Nothing>
	get() =
		choiceWord fieldTo choiceTermStack.reflect { choiceTerm ->
			choiceTerm.reflect(Choice::reflect)
		}