define
	pi number gives: number

	number times: number
	gives: number

	circle has: radius: number

	rectangle has
		width: number
		height: number

	shape has
		either: circle
		either: rectangle

	circle area gives
		pi number
		times: given circle radius number
		times: given circle radius number

	rectangle area gives
		given rectangle width number
		times: given rectangle height number

	shape area gives
		given shape switch
			circle: area
			rectangle: area
