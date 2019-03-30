package leo32

import leo32.base.Dict
import leo32.base.emptyDict
import leo32.base.put

data class FnDict(
	val dict: Dict<Fn>)

val Dict<Fn>.fnDict
	get() =
		FnDict(this)

val emptyFnDict =
	emptyDict<Fn>().fnDict

fun FnDict.put(key: String, fn: Fn) =
	dict.put(key, fn).fnDict

val leoFnDict =
	emptyFnDict
		.put("either bit zero    either bit one    inverse  ", Fn)