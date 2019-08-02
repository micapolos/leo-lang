package leo11

import leo.base.Empty
import leo9.Stack
import leo9.any
import leo9.map
import leo9.stack

sealed class Pattern
data class EmptyPattern(val empty: Empty) : Pattern()
data class ChoicePattern(val choice: PatternChoice) : Pattern()

data class PatternChoice(val linkStack: Stack<PatternLink>)

data class PatternLink(val name: String, val args: PatternArgs)

sealed class PatternArgs
data class EmptyPatternArgs(val empty: Empty) : PatternArgs()
data class ChoicePatternArgs(val choice: PatternChoice) : PatternArgs()
data class PairPatternArgs(val pair: PatternArgsPair) : PatternArgs()

data class PatternArgsPair(val lhs: PatternChoice, val rhs: PatternChoice)

// --- constructor

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(choice: PatternChoice): Pattern = ChoicePattern(choice)
fun choice(linkStack: Stack<PatternLink>) = PatternChoice(linkStack)
fun link(name: String, args: PatternArgs) = PatternLink(name, args)

fun patternArgs(empty: Empty): PatternArgs = EmptyPatternArgs(empty)
fun args(choice: PatternChoice): PatternArgs = ChoicePatternArgs(choice)
fun args(pair: PatternArgsPair): PatternArgs = PairPatternArgs(pair)

fun pair(lhs: PatternChoice, rhs: PatternChoice) = PatternArgsPair(lhs, rhs)


// --- matching

fun Pattern.matches(script: Script): Boolean =
	when (this) {
		is EmptyPattern -> script is EmptyScript
		is ChoicePattern -> choice.matches(script)
	}

fun PatternChoice.matches(script: Script): Boolean =
	linkStack.any { matches(script) }

fun PatternLink.matches(script: Script): Boolean =
	when (script) {
		is EmptyScript -> false
		is LinkScript -> matches(script.link)
	}

fun PatternLink.matches(link: ScriptLink): Boolean =
	name == link.name && args.matches(link.args)

fun PatternArgs.matches(args: ScriptArgs) =
	when (this) {
		is EmptyPatternArgs -> args is EmptyScriptArgs
		is ChoicePatternArgs -> (args is LinkScriptArgs) && choice.matches(args.lhs)
		is PairPatternArgs -> (args is PairScriptArgs) && pair.lhs.matches(args.lhs) && pair.rhs.matches(args.rhs)
	}

// --- parsing

val Script.pattern: Pattern
	get() =
		when (this) {
			is EmptyScript -> pattern(empty)
			is LinkScript -> pattern(link.patternChoice)
		}

val ScriptLink.patternChoice: PatternChoice
	get() =
		choice(
			linkStackOrNull("either")
				?.map { patternLink }
				?: stack(patternLink))

val ScriptLink.patternLink: PatternLink
	get() =
		link(name, args.patternArgs)

val ScriptArgs.patternArgs: PatternArgs
	get() =
		when (this) {
			is EmptyScriptArgs -> patternArgs(empty)
			is LinkScriptArgs -> args(link.patternChoice)
			is PairScriptArgs -> args(pair(pair.lhs.patternChoice, pair.rhs.patternChoice))
		}
