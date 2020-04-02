package leo.stak

import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script

fun <T : Any> Stak<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"stak" lineTo script(
		nodeOrNull?.scriptLine(fn) ?: "node" lineTo script("null"))

fun <T : Any> Node<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"node" lineTo script(
		"value" lineTo value.fn(),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))

fun <T : Any> Link<T>.scriptLine(fn: T.() -> Script): ScriptLine =
	"link" lineTo script(
		node.scriptLine(fn),
		linkOrNull?.scriptLine(fn) ?: "link" lineTo script("null"))