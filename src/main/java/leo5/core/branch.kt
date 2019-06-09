package leo5.core

data class Branch(val selector: Function, val pair: FunctionPair)

fun branch(selector: Function, pair: FunctionPair) = Branch(selector, pair)
fun Branch.invoke(parameter: Value) = pair.at(selector.invoke(parameter).bit).invoke(parameter)
