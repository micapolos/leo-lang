package leo21.compiled

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.Link
import leo13.linkOrNull
import leo13.linkTo
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.arg0
import leo14.lambda.do_
import leo14.lambda.first
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.script
import leo14.lambda.second
import leo14.lineTo
import leo14.orError
import leo14.plus
import leo15.dsl.*
import leo21.prim.Prim
import leo21.term.nilTerm
import leo21.term.plus
import leo21.type.Type
import leo21.type.isEmpty
import leo21.type.isStatic
import leo21.type.matches
import leo21.type.plus
import leo21.type.script
import leo21.type.type

data class Compiled(val term: Term<Prim>, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine
		get() =
			"compiled" lineTo term.script { reflectScriptLine }.plus("of" lineTo type.script)
}

val emptyCompiled = Compiled(nilTerm, type())
infix fun Term<Prim>.of(type: Type) = Compiled(this, type)

fun compiledArg(index: Int, type: Type) = Compiled(arg(index), type)

fun compiled(vararg lines: LineCompiled): Compiled =
	emptyCompiled.fold(lines) { plus(it) }

fun choiceTyped(fn: ChoiceCompiled.() -> ChoiceCompiled): Compiled =
	compiled(line(emptyChoiceCompiled.fn()))

fun compiled(text: String) = compiled(line(text))
fun compiled(number: Double) = compiled(line(number))
fun compiled(int: Int) = compiled(int.toDouble())

val Compiled.choiceOrNull: ChoiceCompiled? get() = onlyLineOrNull?.choiceCompiledOrNull
val Compiled.arrowOrNull: ArrowCompiled? get() = onlyLineOrNull?.arrowCompiledOrNull

val Compiled.choice get() = choiceOrNull.notNullOrError("not choice")
val Compiled.arrow get() = arrowOrNull.orError { expected { function } }

fun Compiled.getOrNull(name: String): Compiled? =
	onlyLineOrNull?.rhsOrNull?.lineOrNull(name)?.let { compiled(it) }

fun Compiled.line(name: String): LineCompiled =
	lineOrNull(name).notNullOrError("no field")

fun Compiled.lineOrNull(name: String): LineCompiled? =
	linkOrNull?.let { link ->
		if (link.head.line.matches(name)) link.head
		else link.tail.lineOrNull(name)
	}

fun Compiled.accessOrNull(name: String): Compiled? =
	lineOrNull(name)?.fieldCompiledOrNull?.rhsCompiled

fun Compiled.access(name: String): Compiled =
	compiled(line(name))

val Compiled.contentOrNull: Compiled?
	get() =
		onlyLineOrNull?.rhsOrNull

fun Compiled.invokeOrNull(compiled: Compiled) = onlyLineOrNull?.arrowCompiledOrNull?.invokeOrNull(compiled)

fun Compiled.get(name: String) = getOrNull(name).notNullOrError("no field")
fun Compiled.invoke(compiled: Compiled) = invokeOrNull(compiled).notNullOrError("invoke")
fun Compiled.make(name: String) = compiled(name lineTo this)
val Compiled.switch: SwitchCompiled get() = contentOrNull?.choiceOrNull.notNullOrError("not choice").switchCompiled

fun Compiled.plus(compiled: LineCompiled): Compiled =
	Compiled(
		if (type.isStatic)
			if (compiled.line.isStatic) nilTerm
			else compiled.term
		else
			if (compiled.line.isStatic) term
			else term.plus(compiled.term),
		type.plus(compiled.line))

val Compiled.linkOrNull: Link<Compiled, LineCompiled>?
	get() =
		type.lineStack.linkOrNull?.let { link ->
			if (link.stack.type.isStatic)
				if (link.value.isStatic) Compiled(nilTerm, link.stack.type) linkTo LineCompiled(nilTerm, link.value)
				else Compiled(nilTerm, link.stack.type) linkTo LineCompiled(term, link.value)
			else
				if (link.value.isStatic) Compiled(term, link.stack.type) linkTo LineCompiled(nilTerm, link.value)
				else Compiled(term.first, link.stack.type) linkTo LineCompiled(term.second, link.value)
		}

val Compiled.link: Link<Compiled, LineCompiled> get() = linkOrNull!!

fun Compiled.reference(f: Compiled.() -> Compiled): Compiled =
	arg0<Prim>().of(type).f().let { typed ->
		fn(typed.term).invoke(term).of(typed.type)
	}

fun Compiled.do_(compiled: Compiled): Compiled =
	term.do_(compiled.term).of(compiled.type)

fun Compiled.apply(compiled: Compiled): Compiled =
	arrow.invoke(compiled)

val Compiled.onlyLineOrNull: LineCompiled?
	get() =
		linkOrNull?.let { link ->
			notNullIf(link.tail.isEmpty) {
				link.head
			}
		}

val Compiled.isEmpty: Boolean
	get() =
		type.isEmpty

val Link<Compiled, LineCompiled>.compiled: Compiled get() = tail
val Link<Compiled, LineCompiled>.lineCompiled: LineCompiled get() = head
