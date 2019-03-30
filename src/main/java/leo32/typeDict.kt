package leo32

import leo32.base.Dict
import leo32.base.emptyDict
import leo32.base.put

data class TypeDict(
	val dict: Dict<String>)

val Dict<String>.typeDict
	get() =
		TypeDict(this)

val emptyTypeDict =
	emptyDict<String>().typeDict

fun TypeDict.put(value: String, type: String) =
	dict.put(value, type).typeDict

val leoTypeDict =
	emptyTypeDict
		.put("bit zero   ", "either bit zero    either bit one    ")
		.put("bit one   ", "either bit zero    either bit one    ")
