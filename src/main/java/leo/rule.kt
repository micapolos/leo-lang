package leo

import leo.base.string

data class Rule(
    val pattern: Pattern,
    val template: Template) {
  override fun toString() = reflect.string
}

infix fun Pattern.returns(template: Template) =
    Rule(this, template)

fun Rule.apply(argument: Script): Script? =
    if (!argument.matches(pattern)) null
    else template.apply(argument)

val Script.parseRule: Rule?
  get() =
    match(defineWord) { defineScript ->
      defineScript.match(itWord, isWord) { itScript, isScript ->
        itScript.parsePattern.let { pattern ->
          isScript.parseTemplate(pattern).let { template ->
            pattern returns template
          }
        }
      }
    }

// === reflect

val Rule.reflect: Field<Nothing>
  get() =
    ruleWord fieldTo term(
        pattern.reflect,
        template.reflect
    )