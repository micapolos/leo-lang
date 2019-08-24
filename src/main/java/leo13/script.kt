package leo13

import leo.base.*
import leo13.script.completedTokenStackOrNull
import leo13.script.parser.completedScriptOrNull
import leo13.script.parser.parser
import leo13.script.parser.push
import leo13.script.push
import leo13.script.tokenizer
import leo9.*
import leo9.Stack

data class Script(val lineStack: Stack<ScriptLine>) {
	override fun toString() = indentedCode
}

data class ScriptLine(val name: String, val rhs: Script) {
	override fun toString() = script(this).toString()
}

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = script.toString()
}

data class ScriptOpener(val lhs: Script, val opening: Opening) {
	override fun toString() = asScript.toString()
	val asScript
		get() = script(
			"lhs" lineTo lhs.asScript,
			"opening" lineTo opening.asScript)
}

data class ScriptHead(val openerStack: Stack<ScriptOpener>, val script: Script) {
	override fun toString() = asScript.toString()
	val asScript
		get() = script(
			"openers" lineTo openerStack.asScript { "opener" lineTo asScript },
			"script" lineTo script.asScript)
}

data class ScriptLinkLine(val name: String, val rhs: ScriptLink)
data class ScriptArrow(val lhs: Script, val rhs: Script)

val Script.isEmpty get() = lineStack.isEmpty

val Stack<ScriptLine>.script get() = Script(this)
fun Script.plus(line: ScriptLine) = lineStack.push(line).script
fun script(vararg lines: ScriptLine) = stack(*lines).script
infix fun String.lineTo(rhs: Script) = ScriptLine(this, rhs)
infix fun String.lineTo(rhs: ScriptLink) = ScriptLinkLine(this, rhs)
fun link(lhs: Script, line: ScriptLine) = ScriptLink(lhs, line)
infix fun Script.arrowTo(rhs: Script) = ScriptArrow(this, rhs)
val Script.onlyLineOrNull get() = lineStack.onlyOrNull

fun scriptHead(vararg openers: ScriptOpener) = ScriptHead(stack(*openers), script())
infix fun Script.openerTo(opening: Opening) = ScriptOpener(this, opening)
fun head(openerStack: Stack<ScriptOpener>, script: Script) = ScriptHead(openerStack, script)
val Script.head get() = head(stack(), this)

val Script.linkOrNull
	get() =
		lineStack.linkOrNull?.let { link ->
			link(link.stack.script, link.value)
		}

val ScriptLink.script
	get() =
		lhs.lineStack.push(line).script

fun Script.arrowOrNull(name: String) =
	linkOrNull?.let { link ->
		notNullIf(link.line.name == name) {
			link.lhs arrowTo link.line.rhs
		}
	}

// --- code

val Script.code: String
	get() = appendableString { appendable ->
		appendable.fold(lineStack.reverse) { line ->
			appendable.append(line.code)
		}
	}

val ScriptLine.code get() = "$name(${rhs.code})"

val ScriptLink.code get() = "$lhs$line"

// --- access int

data class ScriptAccess(val line: ScriptLine, val int: Int)

val Script.accessOrNull: ScriptAccess?
	get() =
		onlyLineOrNull?.accessOrNull

val ScriptLine.accessOrNull
	get() =
		rhs.accessOrNull(name, 0)

fun Script.accessOrNull(name: String, int: Int): ScriptAccess? =
	lineStack.mapFirst { accessOrNull(name, int) }

fun ScriptLine.accessOrNull(name: String, int: Int) =
	notNullIf(name == this.name) {
		ScriptAccess(this, int)
	}

fun Script.accessOrNull(name: String): Script? =
	onlyLineOrNull?.rhs?.lineStack?.mapOnly {
		notNullIf(name == this.name) {
			script(this)
		}
	}

// --- normalization

val Script.normalize: Script
	get() =
		script().fold(lineStack.reverse) { plusNormalized(it.normalize) }

val ScriptLine.normalize
	get() =
		name lineTo rhs.normalize

fun Script.plusNormalized(line: ScriptLine) =
	if (line.rhs.isEmpty) script(line.name lineTo this)
	else plus(line)

val Script.linkLineOrNull: ScriptLinkLine?
	get() =
		linkOrNull?.linkLineOrNull

val ScriptLink.linkLineOrNull: ScriptLinkLine?
	get() =
		if (lhs.isEmpty) line.rhs.linkOrNull?.let { line.name lineTo it }
		else if (line.rhs.isEmpty) lhs.linkOrNull?.let { line.name lineTo it }
		else null

// --- isSingleLine

val Script.isSingleLine: Boolean
	get() =
		linkOrNull?.isSingleLine ?: true

val ScriptLink.isSingleLine
	get() =
		lhsIsSingleLine && line.isSingleLine

val ScriptLink.lhsIsSingleLine
	get() =
		lhs.linkOrNull?.line?.rhs?.isEmpty ?: true

val ScriptLine.isSingleLine
	get() =
		rhs.isSingleLine

// --- appendable

val Script.indentedCode get() = appendableString { it.append(this, 0.indent) }

fun Appendable.append(script: Script, indent: Indent): Appendable =
	script.linkOrNull?.let { append(it, indent) } ?: this

fun Appendable.append(scriptLink: ScriptLink, indent: Indent): Appendable =
	this
		.append(scriptLink.lhs, indent)
		.run {
			scriptLink.lhs.linkOrNull?.let { lhsLink ->
				if (lhsLink.line.rhs.isEmpty) append(' ').append(scriptLink.line, indent)
				else append('\n').append(indent).append(scriptLink.line, indent)
			} ?: append(scriptLink.line, indent)
		}

fun Appendable.append(line: ScriptLine, indent: Indent): Appendable =
	append(line.name).appendRhs(line.rhs, indent)

fun Appendable.appendRhs(script: Script, indent: Indent): Appendable =
	if (script.isEmpty) this
	else if (script.isSingleLine) append(": ").append(script, indent)
	else append('\n').append(indent.inc).append(script, indent.inc)

// --- token seq

val Script.tokenSeq: Seq<Token>
	get() =
		lineStack.reverse.seq.mapFlat { tokenSeq }

val ScriptLine.tokenSeq: Seq<Token>
	get() =
		flatSeq(
			seq(token(opening(name))),
			rhs.tokenSeq,
			seq(token(closing)))

val nullScript = script("null" lineTo script())
val nativeScript = script("native" lineTo script())

val Script.asScript
	get() =
		if (isEmpty) nullScript
		else if (this == nullScript) script("meta" lineTo this)
		else this

fun <V> Stack<V>.asScript(fn: V.() -> ScriptLine) =
	if (isEmpty) nullScript
	else map { fn() }.script

fun <V : Any> V?.orNullAsScript(fn: V.() -> Script) =
	this?.fn() ?: nullScript

fun unsafeScript(string: String) =
	tokenizer()
		.push(string)
		.completedTokenStackOrNull!!
		.let { tokenStack ->
			parser()
				.fold(tokenStack.reverse) { push(it) }
				.completedScriptOrNull!!
		}

fun ScriptHead.plus(token: Token): ScriptHead? =
	when (token) {
		is OpeningToken -> plus(token.opening)
		is ClosingToken -> plus(token.closing)
	}

fun ScriptHead.plus(opening: Opening): ScriptHead =
	head(openerStack.push(script openerTo opening), script())

fun ScriptHead.plus(closing: Closing): ScriptHead? =
	openerStack.linkOrNull?.let { openerStackLink ->
		openerStackLink.value.let { opener ->
			head(openerStackLink.stack, opener.lhs.plus(opener.opening.name lineTo script))
		}
	}

val ScriptHead.completeScriptOrNull
	get() =
		notNullIf(openerStack.isEmpty) { script }