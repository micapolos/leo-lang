package leo21.compiled

import leo.base.fold
import leo.base.notNullOrError
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.arg0
import leo14.lambda.do_
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.script
import leo14.lineTo
import leo14.plus
import leo21.prim.Prim
import leo21.type.ChoiceType
import leo21.type.RecurseType
import leo21.type.RecursiveType
import leo21.type.StructType
import leo21.type.Type
import leo21.type.resolve
import leo21.type.script
import leo21.type.type

data class Compiled(val term: Term<Prim>, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine
		get() =
			"compiled" lineTo term.script { reflectScriptLine }.plus("of" lineTo type.script)
}

infix fun Term<Prim>.of(type: Type) = Compiled(this, type)

fun compiled(compiled: StructCompiled) = Compiled(compiled.term, type(compiled.struct))
fun compiled(compiled: ChoiceCompiled) = Compiled(compiled.termOrNull!!, type(compiled.choice))

fun <R> Compiled.switch(
	structFn: (StructCompiled) -> R,
	choiceFn: (ChoiceCompiled) -> R
): R =
	when (type) {
		is StructType -> structFn(StructCompiled(term, type.struct))
		is ChoiceType -> choiceFn(ChoiceCompiled(term, type.choice))
		is RecursiveType -> term.of(type.recursive.resolve).switch(structFn, choiceFn)
		is RecurseType -> null!!
	}

fun compiled(vararg lines: LineCompiled): Compiled =
	compiled(emptyStructTyped.fold(lines) { plus(it) })

fun choiceTyped(fn: ChoiceCompiled.() -> ChoiceCompiled): Compiled =
	compiled(emptyChoiceCompiled.fn())

fun compiled(text: String) = compiled(line(text))
fun compiled(number: Double) = compiled(line(number))

val Compiled.structOrNull: StructCompiled? get() = switch({ it }, { null })
val Compiled.choiceOrNull: ChoiceCompiled? get() = switch({ null }, { it })

val Compiled.struct get() = structOrNull.notNullOrError("not struct")
val Compiled.choice get() = choiceOrNull.notNullOrError("not choice")

fun Compiled.getOrNull(name: String): Compiled? =
	structOrNull?.onlyLineOrNull?.rhsOrNull?.structOrNull?.lineOrNull(name)?.let { compiled(it) }

fun Compiled.lineOrNull(name: String): LineCompiled? =
	structOrNull?.lineOrNull(name)

fun Compiled.line(name: String): LineCompiled =
	lineOrNull(name)!!

fun Compiled.accessOrNull(name: String): Compiled? =
	lineOrNull(name)?.fieldCompiledOrNull?.rhsCompiled

fun Compiled.access(name: String): Compiled =
	compiled(line(name))

val Compiled.contentOrNull: Compiled?
	get() =
		structOrNull?.onlyLineOrNull?.rhsOrNull

fun Compiled.invokeOrNull(compiled: Compiled) = structOrNull?.onlyLineOrNull?.arrowCompiledOrNull?.invokeOrNull(compiled)

fun Compiled.get(name: String) = getOrNull(name).notNullOrError("no field")
fun Compiled.invoke(compiled: Compiled) = invokeOrNull(compiled).notNullOrError("invoke")
fun Compiled.make(name: String) = compiled(name lineTo this)
val Compiled.switch: SwitchCompiled get() = contentOrNull?.choiceOrNull.notNullOrError("not choice").switchCompiled

fun Compiled.plus(line: LineCompiled): Compiled = struct.plus(line).typed

fun Compiled.reference(f: Compiled.() -> Compiled): Compiled =
	arg0<Prim>().of(type).f().let { typed ->
		fn(typed.term).invoke(term).of(typed.type)
	}

fun Compiled.do_(compiled: Compiled): Compiled =
	term.do_(compiled.term).of(compiled.type)