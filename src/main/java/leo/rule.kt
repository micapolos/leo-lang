package leo

import leo.base.string

data class Rule(
	val pattern: Pattern,
	val body: Body) {
	override fun toString() = reflect.string
}

infix fun Pattern.returns(body: Body) =
	Rule(this, body)

fun Script.parseRule(localFunction: Function): Rule? =
		match(defineWord) { defineScript ->
			defineScript.match(itWord, isWord) { itScript, isScript ->
				itScript.parsePattern.let { pattern ->
					isScript.parseTemplate(pattern).let { template ->
						pattern returns Body(template, localFunction)
					}
				}
			}
		}

// === reflect

val Rule.reflect: Field<Nothing>
	get() =
		ruleWord fieldTo term(
			pattern.reflect,
			body.reflect)