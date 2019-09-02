package leo13.compiler

import leo.base.failIfOr
import leo13.fail
import leo13.script.*
import leo13.type.lineTo
import leo13.type.type
import leo13.type.unsafeType

data class DefineCompiler(
	val context: Context,
	val compiledScript: Script)

fun defineCompiler(context: Context, script: Script) = DefineCompiler(context, script)

fun DefineCompiler.push(scriptLine: ScriptLine): DefineCompiler =
	when (scriptLine.name) {
		"gives" -> pushGives(scriptLine.rhs)
		"contains" -> pushContains(scriptLine.rhs)
		"exists" -> pushExists(scriptLine.rhs)
		else -> pushOther(scriptLine)
	}

fun DefineCompiler.pushGives(script: Script): DefineCompiler =
	compiledScript.unsafeType.let { parameterType ->
		context
			.bind(type("given" lineTo parameterType))
			.unsafeCompile(script)
			.let { compiled ->
				defineCompiler(
					context.plus(function(parameterType, compiled)),
					script())
			}
	}

fun DefineCompiler.pushContains(script: Script): DefineCompiler =
	compiledScript.onlyLineOrNull!!.onlyNameOrNull!!.let { name ->
		script.unsafeType.let { type ->
			defineCompiler(
				context.plus(function(name, type(name lineTo type))),
				script())
		}
	}

// TODO: Maybe we don't need "exists" at all, if we have "contains"?
fun DefineCompiler.pushExists(script: Script): DefineCompiler =
	failIfOr(!script.isEmpty) {
		defineCompiler(
			context.plus(function("???", compiledScript.unsafeType)),
			script())
	}

fun DefineCompiler.pushOther(scriptLine: ScriptLine): DefineCompiler =
	defineCompiler(context, compiledScript.plus(scriptLine))

val DefineCompiler.finishedContext: Context
	get() =
		if (!compiledScript.isEmpty) fail("finish")
		else context
