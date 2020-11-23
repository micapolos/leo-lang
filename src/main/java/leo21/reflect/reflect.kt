package leo21.reflect

import leo.base.orIfNull
import leo14.Script
import leo14.ScriptLine
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.superclasses

val Any.refScriptLine: ScriptLine
	get() =
		when (this) {
			is Int -> line(literal(this))
			is Double -> line(literal(this))
			is String -> line(literal(this))
			is Boolean -> "boolean" lineTo script(if (this) "true" else "false")
			else -> this::class.reflectScriptLine(fieldsScript)
		}

val Any.fieldsScript: Script
	get() =
		script(*this::class.members
			.mapNotNull { it as? KProperty1<*, *> }
			.filter { it.isFinal }
			.map { it.scriptLine(this) }
			.toTypedArray())

fun KClass<*>.reflectScriptLine(bodyScript: Script): ScriptLine =
	(simpleName!! lineTo bodyScript).let { scriptLine ->
		superclasses
			.filter { it != Any::class && (it.isData || it.isSealed) && !it.isAbstract }
			.firstOrNull()
			?.reflectScriptLine(script(scriptLine)) ?: scriptLine
	}

fun KCallable<*>.scriptLine(any: Any): ScriptLine =
	call(any).orIfNull { "error" lineTo script() }.refScriptLine