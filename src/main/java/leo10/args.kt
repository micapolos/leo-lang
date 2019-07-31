package leo10

import leo.base.Empty

sealed class Args

data class EmptyArgs(
	val empty: Empty) : Args()

data class LinkArgs(
	val link: ArgsLink) : Args()

data class ArgsLink(
	val args: Args,
	val script: Script)

val Empty.args: Args get() = EmptyArgs(this)
val ArgsLink.args: Args get() = LinkArgs(this)
infix fun Args.linkTo(script: Script) = ArgsLink(this, script)
operator fun Args.plus(script: Script) = linkTo(script).args

fun Args.atOrNull(arg: Arg): Script? =
	when (this) {
		is EmptyArgs -> null
		is LinkArgs -> when (arg) {
			is LastArg -> link.script
			is PreviousArg -> link.args.atOrNull(arg.previous.arg)
		}
	}

fun Args.at(arg: Arg): Script = atOrNull(arg)!!