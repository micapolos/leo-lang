package leo13

import leo.base.Nat
import leo.base.fold
import leo.base.inc
import leo.base.nat
import leo.binary.zero

data class Quoter(val evaluator: Evaluator, val depthNat: Nat)

fun quoter(evaluator: Evaluator = evaluator(), depthNat: Nat = nat(zero)) = Quoter(evaluator, depthNat)

val Quoter.quote: Quoter
	get() =
		copy(depthNat = depthNat.inc)

fun Quoter.plus(sentence: Sentence): Quoter =
	fold(sentence.scriptLineSeq) { plus(it) }

fun Quoter.plus(line: SentenceScriptLine): Quoter = TODO()
//	when (line.word) {
//		quoteWord -> plusQuote(line.script)
//		unquoteWord -> plusUnquote(line.script)
//		else -> plusOther(line)
//	}

fun Quoter.plusQuote(script: SentenceScript): Quoter = TODO()
//	quote.run {
//		if (depthNat == nat(zero)) plus(quoteWord lineTo sentence)
//		else plus(sentence)
//	}

fun Quoter.plusUnquote(sentence: Sentence): Quoter =
	TODO()

fun Quoter.plusOther(line: SentenceScriptLine): Quoter = TODO()
//	if (depthNat == nat(0)) copy(evaluator = evaluator.plus(line))
//	else copy(evaluator = evaluator.append(line))