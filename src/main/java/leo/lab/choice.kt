package leo.lab

import leo.base.*
import leo.choiceWord
import leo.eitherWord

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
	fieldStack.top.matches(choiceStructureTerm.fieldStack.top) &&
		if (fieldStack.pop == null) choiceStructureTerm.fieldStack.pop == null
		else choiceStructureTerm.fieldStack.pop != null &&
			fieldStack.pop.term.matches(choiceStructureTerm.fieldStack.pop.term)

fun Field<Nothing>.matches(choiceField: Field<Choice>) =
	word == choiceField.word &&
		if (termOrNull == null) choiceField.termOrNull == null
		else choiceField.termOrNull != null && termOrNull.matches(choiceField.termOrNull)

// === parsing

val Term<Nothing>.parseChoice: Choice?
	get() =
		structureTermOrNull?.run {
			true.fold(fieldStack.stream) { field ->
				this && field.word == eitherWord && field.termOrNull != null
			}.let { isOneOf ->
				if (!isOneOf) null
				else fieldStack
					.reverse
					.stream
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
		fieldStack
			.reverse
			.stream
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