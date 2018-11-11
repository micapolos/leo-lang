package leo

import leo.base.*

data class Template(
    val term: Term<Selector>) {
  override fun toString() = reflect.string
}

val Term<Selector>.template
  get() =
    Template(this)

fun template(term: Term<Selector>) =
    term.template

fun Template.apply(script: Script): Script =
    invoke(script.term)!!.script

fun <V> Template.invoke(argument: Term<V>): Term<V>? =
    term.invoke(argument)

fun <V> Term<Selector>.invoke(argument: Term<V>): Term<V>? =
    when (this) {
      is Term.Meta -> this.invoke(argument)
      is Term.Identifier -> this.invoke()
      is Term.Structure -> this.invoke(argument)
    }

fun <V> Term.Meta<Selector>.invoke(argument: Term<V>): Term<V>? =
    value.invoke(argument)

fun <V> Term.Identifier<Selector>.invoke(): Term<V> =
    term(word)

fun <V> Term.Structure<Selector>.invoke(argument: Term<V>): Term<V>? =
    fieldStack.reverse.foldTop { field ->
      field.invoke(argument)?.stack
    }.andPop { stack, field ->
      field.invoke(argument)?.let { invokedField ->
        stack.push(invokedField)
      }
    }?.term

fun <V> Field<Selector>.invoke(argument: Term<V>): Field<V>? =
    value.invoke(argument)?.let { value ->
      key fieldTo value
    }

// === script parsing

fun Script.parseTemplate(pattern: Pattern): Template =
    parseSelector(pattern)?.metaTerm?.template ?: when (term) {
      is Term.Identifier -> template(term(term.word))
      is Term.Structure ->
        term.fieldStack.reverse
            .foldTop { it.parseTemplateField(pattern).stack }
            .andPop { stack, field -> stack.push(field.parseTemplateField(pattern)) }
            .term
            .template
      is Term.Meta -> fail
    }

fun Field<Nothing>.parseTemplateField(pattern: Pattern) =
    key fieldTo value.script.parseTemplate(pattern).term

// === reflect

val Template.reflect: Field<Nothing>
  get() =
    templateWord fieldTo term(
	    term.fieldReflect(Selector::reflect))