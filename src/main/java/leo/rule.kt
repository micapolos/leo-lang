package leo

import leo.base.string

data class Rule(
	val pattern: Term<Pattern>,
	val body: Body) {
	override fun toString() = reflect.string
}

fun rule(patternTerm: Term<Pattern>, body: Body) =
	Rule(patternTerm, body)

infix fun Term<Pattern>.returns(body: Body) =
	Rule(this, body)

fun Term<*>.parseRule(localFunction: Function): Rule? =
	match(defineWord) { defineTerm ->
		defineTerm.match(itWord, isWord) { itTerm, isTerm ->
			itTerm.parsePatternTerm.let { patternTerm ->
				isTerm.parseSelectorTerm(patternTerm).let { selectorTerm ->
					patternTerm returns Body(selectorTerm, localFunction)
				}
			}
		}
	}

// === reflect

val Rule.reflect: Field<Value>
	get() =
		ruleWord fieldTo term(
			pattern.reflect { oneOf -> term(oneOf.reflect) },
			body.reflect)