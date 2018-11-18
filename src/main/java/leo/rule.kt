package leo

import leo.base.string

data class Rule(
	val choiceTerm: Term<Choice>,
	val body: Body) {
	override fun toString() = reflect.string
}

fun rule(patternTerm: Term<Choice>, body: Body) =
	Rule(patternTerm, body)

infix fun Term<Choice>.returns(body: Body) =
	Rule(this, body)

fun Term<Nothing>.parseRule(localFunction: Function): Rule? =
	match(defineWord) { defineTerm ->
		defineTerm?.match(itWord, isWord) { itTerm, isTerm ->
			itTerm?.parseChoiceTerm?.let { choiceTerm ->
				isTerm?.parseSelectorTerm(choiceTerm)?.let { selectorTerm ->
					rule(choiceTerm, body(selectorTerm, localFunction))
				}
			}
		}
	}

// === reflect

val Rule.reflect: Field<Nothing>
	get() =
		ruleWord fieldTo term(
			choiceTerm.reflect(Choice::reflect),
			body.reflect)