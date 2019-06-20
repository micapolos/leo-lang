package leo5.gen.kotlin

import leo.base.fail
import leo.base.ifNotNull
import leo.base.runIf
import leo5.script.*

fun Appendable.appendFile(script: Script) =
	appendFilePackage(script)

fun Appendable.appendFilePackage(script: Script) =
	ifNotNull(script.nonEmptyOrNull) { nonEmpty ->
		this
			.runIf(nonEmpty.line.string == "package") { appendPackageName(nonEmpty.line.script) }
			.appendFileImports(nonEmpty.line.script)
	}

fun Appendable.appendFileImports(script: Script) =
	ifNotNull(script.nonEmptyOrNull) { nonEmpty ->
		this
			.runIf(nonEmpty.line.string == "import") { appendPackageName(nonEmpty.line.script) }
			.appendFileImports(nonEmpty.line.script)
	}

fun Appendable.appendPackageName(script: Script): Appendable =
	script.matchNonEmpty { nonEmptyScript ->
		appendPath(nonEmptyScript, { append('.') }) {
			append(it)
		}
	}

fun Appendable.appendFile(line: Line) = when (line.string) {
	"struct" -> appendStruct(line.script)
	"switch" -> appendSwitch(line.script)
	"function" -> appendSwitch(line.script)
	else -> fail()
}

fun Appendable.appendStruct(script: Script) =
	script.match("name", "body") { name, body ->
		this
			.append("data class ")
			.appendName(name, true)
			.append("{\n")
			.appendStructBody(body)
	}

fun Appendable.appendName(script: Script, capitalize: Boolean = false): Appendable =
	script.matchLine { name, rhs ->
		append(if (capitalize) name.capitalize() else name)
			.run { if (rhs !is EmptyScript) appendName(rhs, true) else this }
	}

fun Appendable.appendFields(script: Script, comma: Boolean) =
	appendList(script) { appendField(it) }

tailrec fun Appendable.appendList(script: Script, fn: Appendable.(Line) -> Appendable): Appendable =
	when (script) {
		is EmptyScript -> this
		is NonEmptyScript -> fn(script.nonEmpty.line).appendList(script.nonEmpty.script, fn)
	}

fun Appendable.appendPath(script: Script, separatorFn: Appendable.() -> Appendable, fn: Appendable.(String) -> Appendable): Appendable =
	when (script) {
		is EmptyScript -> this
		is NonEmptyScript -> script.matchLine { name, path ->
			this
				.runIf(path !is EmptyScript, separatorFn)
				.fn(name)
				.appendPath(path, separatorFn, fn)
		}
	}

fun Appendable.appendStructBody(script: Script): Appendable =
	appendList(script) { appendStructBody(it) }

fun Appendable.appendStructBody(line: Line, first: Boolean = false): Appendable =
	this
		.run { if (first) append(',') else this }
		.append("\n\t")
		.run {
			line.match("field") { field ->
				appendField(field)
			}
		}

fun Appendable.appendField(script: Script): Appendable =
	script.match("name", "type") { name, type ->
		this
			.append("val ")
			.appendName(name)
			.append(": ")
			.appendName(type, true)
	}


fun Appendable.appendSwitch(script: Script) = TODO() as Appendable
fun Appendable.appendFunction(script: Script) = TODO() as Appendable
