package leo.base

import kotlin.reflect.KClass

val <V : Any> KClass<V>.nullInstance
  get() =
    null as V?