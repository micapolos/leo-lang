package leo19.compiler

import leo.base.fold
import leo.base.map
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lineSeq
import leo19.type.Case
import leo19.type.Field
import leo19.type.Type
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.giving
import leo19.type.plus
import leo19.type.recurse
import leo19.type.recursive
import leo19.type.type

data class TypeCompiler(val recursiveDepth: Int)

val emptyTypeCompiler = TypeCompiler(0)
val TypeCompiler.pushRecursive get() = TypeCompiler(recursiveDepth.inc())

val Script.type get() = emptyTypeCompiler.type(this)

val ScriptField.field
	get() =
		string fieldTo rhs.type

val ScriptField.case
	get() =
		string caseTo rhs.type

fun TypeCompiler.type(script: Script): Type =
	to(type()).fold(script.lineSeq.reverse) {
		first to first.plusType(second, it)
	}.second

fun TypeCompiler.plusType(type: Type, scriptLine: ScriptLine): Type =
	plusType(type, scriptLine.fieldOrNull!!)

fun TypeCompiler.plusType(type: Type, scriptField: ScriptField): Type =
	if (type == type()) type(scriptField)
	else plusRawType(type, scriptField)

fun TypeCompiler.plusRawType(type: Type, scriptField: ScriptField): Type =
	when (scriptField.string) {
		"giving" -> type.giving(type(scriptField.rhs))
		else -> type.plus(field(scriptField))
	}

fun TypeCompiler.type(scriptField: ScriptField): Type =
	when (scriptField.string) {
		"choice" -> choice(*scriptField.rhs.lineSeq.map { case(fieldOrNull!!) }.reverse.toList().toTypedArray())
		"recurse" -> if (recursiveDepth > 0) recurse(0) else error("not recursive")
		"recursive" -> recursive(pushRecursive.type(scriptField.rhs))
		else -> plusRawType(type(), scriptField)
	}

fun TypeCompiler.field(scriptField: ScriptField): Field =
	scriptField.string fieldTo type(scriptField.rhs)

fun TypeCompiler.case(scriptField: ScriptField): Case =
	scriptField.string caseTo type(scriptField.rhs)
