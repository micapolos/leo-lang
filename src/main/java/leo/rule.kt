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

fun Term<Value>.parseRule(localFunction: Function): Rule? =
	match(defineWord) { defineScript ->
		defineScript.match(itWord, isWord) { itScript, isScript ->
			itScript.parsePatternTerm.let { patternTerm ->
				isScript.parseSelectorTerm(patternTerm).let { template ->
					patternTerm returns Body(template, localFunction)
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