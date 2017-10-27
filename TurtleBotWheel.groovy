double wheelRadius = 33.0/2.0
wheelHeight=17.6
double radius = 6.0
CSG profile = new RoundedCube(	wheelRadius,// X dimention
				radius,// Y dimention
				wheelHeight//  Z dimention
				)
				.cornerRadius(radius)// sets the radius of the corner
				.toCSG()// converts it to a CSG tor display
				.toXMin()
				.toZMin()

//create a Cylinder
height = 22.5
CSG wheel_body = CSG.unionAll(
		Extrude.revolve(profile,(double)0,(int)40)
		)
                      		
CSG pin = new Cylinder(2.5, // Radius at the bottom
                       2.5, // Radius at the top
                       height, // Height
                       (int)30 //resolution
                       ).toCSG()

CSG wheel = wheel_body.difference(pin)

 return [wheel ]
