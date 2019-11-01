package leo13.js2

data class Fn(var args: Args, val seq: Seq)

fun fn(args: Args, seq: Seq) = Fn(args, seq)
val Fn.code get() = "function(${args.code}) { ${seq.returnCode} }"