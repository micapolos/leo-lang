package leo13.script

import leo.base.*
import leo13.*
import leo13.Empty
import leo13.Stack
import leo13.token.*

val scriptReader: Reader<Script> = reader(scriptName) { this }
val scriptWriter: Writer<Script> = writer(scriptName) { this }

data class Script(val lineStack: Stack<ScriptLine>) : ObjectScripting() {
	override fun toString() = indentedCode
	override val scriptingLine get() = scriptName lineTo this
	val scriptableName get() = scriptName
	val scriptableBody get() = this
	val scriptableLine get() = scriptingLine
}

data class ScriptOpener(val lhs: Script, val opening: Opening) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "opener" lineTo script(lhs.scriptableLine, opening.asScriptLine)
}

data class ScriptHead(val openerStack: Stack<ScriptOpener>, val script: Script) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "head" lineTo script(
			"openers" lineTo openerStack.asScript { asScriptLine },
			script.scriptableLine)
}

data class ScriptLinkLine(val name: String, val rhs: ScriptLink)
data class ScriptArrow(val lhs: Script, val rhs: Script)

val Script.isEmpty get() = lineStack.isEmpty
val Script.unsafeEmpty: Empty
	get() =
		if (isEmpty) leo13.empty
		else fail("expected" lineTo script("empty"))

val Stack<ScriptLine>.script get() = Script(this)
fun Script.plus(line: ScriptLine) = lineStack.push(line).script
fun Script.plus(script: Script) = fold(script.lineStack.reverse) { plus(it) }
fun script(vararg lines: ScriptLine) = leo13.stack(*lines).script
fun script(name: String, vararg names: String) = script(name).fold(names) { plus(it lineTo script()) }
infix fun String.lineTo(rhs: Script) = ScriptLine(this, rhs)
val String.scriptLine get() = lineTo(script())
fun script(name: String) = script(name.scriptLine)
fun scriptLine(name: String) = name.scriptLine
infix fun String.lineTo(rhs: ScriptLink) = ScriptLinkLine(this, rhs)
fun link(lhs: Script, line: ScriptLine) = ScriptLink(lhs, line)
infix fun Script.arrowTo(rhs: Script) = ScriptArrow(this, rhs)
val Script.onlyLineOrNull get() = lineStack.onlyOrNull
val ScriptLine.script get() = script(this)
val Script.lineSeq get() = lineStack.reverse.seq

val Script.unsafeOnlyLine: ScriptLine
	get() =
		onlyLineOrNull
			?: fail("only" lineTo script("line"))

fun scriptHead(vararg openers: ScriptOpener) = ScriptHead(leo13.stack(*openers), script())
infix fun Script.openerTo(opening: Opening) = ScriptOpener(this, opening)
fun head(openerStack: Stack<ScriptOpener>, script: Script) = ScriptHead(openerStack, script)
val Script.head get() = head(leo13.stack(), this)

val Script.linkOrNull
	get() =
		lineStack.linkOrNull?.let { link ->
			link(link.stack.script, link.value)
		}

val Script.unsafeLink: ScriptLink
	get() =
		linkOrNull ?: fail("empty")

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

val Script.isName: Boolean
	get() =
		onlyLineOrNull?.isName ?: false

val Script.isNames: Boolean
	get() =
		linkOrNull?.isNames ?: false

val ScriptLink.isNames
	get() =
		(lhs.isEmpty || lhs.isNames) && line.isName

val Script.isSingleLine: Boolean
	get() =
		linkOrNull?.isSingleLine ?: true

val ScriptLink.isSingleLine
	get() =
		lhsIsSingleLine && !lhs.isDeep && line.isSingleLine

val ScriptLink.lhsIsSingleLine
	get() =
		lhs.linkOrNull?.line?.rhs?.isEmpty ?: true

val ScriptLine.isSingleLine
	get() =
		rhs.isSingleLine

val Script.isDeep: Boolean
	get() =
		linkOrNull?.isDeep ?: false

val ScriptLink.isDeep: Boolean
	get() =
		lhs.isDeep || !line.rhs.isEmpty

val ScriptLine.isName get() = rhs.isEmpty

// --- appendable

val Script.indentedCode get() = appendableString { it.append(this, 0.indent) }

fun Appendable.append(script: Script, indent: Indent): Appendable =
	script.linkOrNull?.let { append(it, indent) } ?: this

fun Appendable.append(scriptLink: ScriptLink, indent: Indent): Appendable =
	this
		.append(scriptLink.lhs, indent)
		.run {
			scriptLink
				.lhs
				.linkOrNull
				?.let { lhsLink ->
					if (lhsLink.isNames && scriptLink.line.isName) append(' ').append(scriptLink.line, indent)
					else append('\n').append(indent).append(scriptLink.line, indent)
				}
				?: append(scriptLink.line, indent)
		}

fun Appendable.append(line: ScriptLine, indent: Indent): Appendable =
	append(line.name).appendRhs(line.rhs, indent)

fun Appendable.appendRhs(script: Script, indent: Indent): Appendable =
	script
		.linkOrNull
		?.let { link ->
			if (link.lhs.isEmpty || link.isNames) append(": ").append(script, indent)
			else append('\n').append(indent.inc).append(script, indent.inc)
		}
		?: this

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

val nullScriptLine = "null" lineTo script()
val nullScript = script(nullScriptLine)
val nativeScriptLine = "native" lineTo script()

fun <V> Stack<V>.asScript(fn: V.() -> ScriptLine) =
	if (isEmpty) script(nullScriptLine)
	else map { fn() }.script

fun <V> Stack<V>.asScriptLine(name: String, fn: V.() -> ScriptLine) =
	name lineTo asScript(fn)

fun unsafeScript(string: String) =
	tokenizer()
		.push(string)
		.completedTokensOrThrow
		.let { tokens ->
			parser()
				.fold(tokens.stack.reverse) { push(it) }
				.completedScriptOrNull
				.notNullOrError("parser")
		}

val String.unsafeScript get() = leo13.script.unsafeScript(this)
val String.unsafeScriptLine get() = unsafeScript.onlyLineOrNull.notNullOrError("only line")

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

fun <V> Stack<V>.script(fn: V.() -> ScriptLine) =
	map { fn() }.script

fun <V> Stack<V>.noneScript(fn: V.() -> ScriptLine) =
	if (isEmpty) script(noneName)
	else script(fn)

val Script.emptyIfEmpty
	get() =
		if (isEmpty) script(emptyName)
		else this

val todoScriptLine = "todo" lineTo script()
