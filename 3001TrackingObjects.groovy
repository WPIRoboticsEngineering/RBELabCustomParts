LengthParameter boltLength		= new LengthParameter("Bolt Length",10,[180,10])
LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])
printerOffset.setMM(0.9)
double pinHeight = 5


boltLength.setMM(pinHeight*2)
//The tracking object
CSG sphere = new Sphere((1.1*25.4)/2)// Spheres radius
				.toCSG()// convert to CSG to display
				.toZMin()
				.movez(pinHeight)
CSG simpleSyntax =new Cylinder(7,7,pinHeight*2,(int)30).toCSG() // a one line Cylinder

CSG bolt = Vitamins.get("capScrew","M3")
			.rotx(180)
			.movez(-2)
CSG boltKeepaway = bolt.toolOffset(printerOffset.getMM())

double coasterThickness= -bolt.getMinZ()

CSG coaster =new Cylinder(50,50,coasterThickness,(int)30).toCSG() // a one line Cylinder
				.toZMax()
				.difference(boltKeepaway)
CSG coloredObject = simpleSyntax.union(sphere)
					.difference(bolt
								.toolOffset(printerOffset.getMM()-0.1)
								)

return [coloredObject,coaster]
				
				
