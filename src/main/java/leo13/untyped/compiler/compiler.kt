package leo13.untyped.compiler

import leo.base.ifOrNull
import leo13.untyped.expression.get
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.getOrNull
import leo13.untyped.isEmpty

data class Compiler(
	val patternArrows: PatternArrows,
	val match: Match)

fun Compiler.plus(line: MatchLine): Compiler =
	when (line.name) {
		else -> plusOther(line)
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
	TODO()

fun Compiler.set(match: Match) =
	copy(match = match)
