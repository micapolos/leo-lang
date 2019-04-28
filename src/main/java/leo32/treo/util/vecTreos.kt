package leo32.treo.util

import leo32.treo.Treo
import leo32.treo.Var
import leo32.treo.capture
import leo32.treo.treo

fun treo(vec2: Vec2<Var>, treo: Treo) = treo(vec2.hi, treo(vec2.lo, treo))
fun treo(vec4: Vec4<Var>, treo: Treo) = treo(vec4.hi, treo(vec4.lo, treo))
fun treo(vec8: Vec8<Var>, treo: Treo) = treo(vec8.hi, treo(vec8.lo, treo))
fun treo(vec16: Vec16<Var>, treo: Treo) = treo(vec16.hi, treo(vec16.lo, treo))
fun treo(vec32: Vec32<Var>, treo: Treo) = treo(vec32.hi, treo(vec32.lo, treo))
fun treo(vec64: Vec64<Var>, treo: Treo) = treo(vec64.hi, treo(vec64.lo, treo))

fun capture(vec2: Vec2<Var>, treo: Treo) = capture(vec2.hi, capture(vec2.lo, treo))
fun capture(vec4: Vec4<Var>, treo: Treo) = capture(vec4.hi, capture(vec4.lo, treo))
fun capture(vec8: Vec8<Var>, treo: Treo) = capture(vec8.hi, capture(vec8.lo, treo))
fun capture(vec16: Vec16<Var>, treo: Treo) = capture(vec16.hi, capture(vec16.lo, treo))
fun capture(vec32: Vec32<Var>, treo: Treo) = capture(vec32.hi, capture(vec32.lo, treo))
fun capture(vec64: Vec64<Var>, treo: Treo) = capture(vec64.hi, capture(vec64.lo, treo))
