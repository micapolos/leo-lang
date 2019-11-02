package leo13.js2

data class Fn(var args: Args, val block: Block)

fun fn(args: Args, block: Block) = Fn(args, block)
val Fn.exprCode get() = "function(${args.code}) ${block.code}"