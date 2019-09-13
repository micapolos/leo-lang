package leo13.untyped.compiler

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.untyped.*
import leo13.untyped.expression.replace
import leo13.untyped.expression.get
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.value.value

data class Compiler(
	val patternArrows: PatternArrows,
	val match: Match)

fun Compiler.plus(line: MatchLine): Compiler? =
	when (line.name) {
		setName -> plusSet(line.rhs)
		forgetName -> plusForget(line.rhs)
		else -> plusOther(line)
	}

fun Compiler.plusSet(rhs: Match): Compiler? =
//	rhs.pattern.lineStackOrNull?.let { lineStack ->
//		orNullFold(lineStack.reverse.seq) { plusSet(it) }
//	}
	TODO()

fun Compiler.plusSet(line: MatchLine): Compiler? =
	match
		.pattern
		.setOrNull(line.patternLine)
		?.let { setPattern ->
			set(
				match
					.expression
					.plus(leo13.untyped.expression.set(line.expressionLine).op)
					.match(setPattern))
		}

fun Compiler.plusForget(rhs: Match): Compiler? =
	notNullIf(rhs.pattern.isEmpty) {
		set(
			match
				.expression
				.plus(replace(value()).op)
				.match(pattern()))
	}

fun Compiler.plusOther(line: MatchLine): Compiler =
	null
		?: plusGetOrNull(line)
		?: append(line)

fun Compiler.plusGetOrNull(line: MatchLine): Compiler? =
	ifOrNull(match.pattern.isEmpty) {
		line
			.rhs
			.pattern
			.getOrNull(line.name)
			?.let { set(match.expression.plus(get(line.name).op).match(it)) }
	}

fun Compiler.append(line: MatchLine): Compiler =
	set(match.plus(line))

fun Compiler.set(match: Match) =
	copy(match = match)
