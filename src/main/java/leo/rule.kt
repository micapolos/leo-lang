package leo

import leo.base.string

data class Rule(
	val patternTerm: Term<Pattern>,
	val body: Body) {
	override fun toString() = reflect.string
}

fun rule(patternTerm: Term<Pattern>, body: Body) =
	Rule(patternTerm, body)

infix fun Term<Pattern>.returns(body: Body) =
	Rule(this, body)

fun Term<Nothing>.parseRule(localFunction: Function): Rule? =
	parseItIsRule(localFunction)

fun Term<Nothing>.parseDefineItIsRule(localFunction: Function): Rule? =
	matchFieldKey(defineWord) {
		parseItIsRule(localFunction)
	}

fun Term<Nothing>.parseItIsRule(localFunction: Function): Rule? =
	matchFieldKeys(itWord, isWord) { itTerm, isTerm ->
		itTerm.parsePatternTerm.let { patternTerm ->
			rule(
				patternTerm,
				body(
					isTerm.parseSelectorTerm(patternTerm),
					localFunction))
		}
	}

// === reflect

val Rule.reflect: Field<Nothing>
	get() =
		ruleWord fieldTo term(
			patternWord fieldTo patternTerm.reflectMeta(Pattern::reflect),
			body.reflect)