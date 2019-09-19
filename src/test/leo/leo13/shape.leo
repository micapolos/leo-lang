define: circle has: radius: number

define: rectangle has
	width: number
	height: number

define: shape has
	either: circle
	either: rectangle

define: circle area gives
	pi number
	times: given circle radius number
	times: given circle radius number

define: rectangle area gives
	given rectangle width number
	times: given rectangle height number
