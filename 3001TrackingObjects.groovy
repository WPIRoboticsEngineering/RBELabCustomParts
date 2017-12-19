LengthParameter boltLength		= new LengthParameter("Bolt Length",10,[180,10])
LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])
printerOffset.setMM(0.9)
double pinHeight = 5
LengthParameter thickness 				= new LengthParameter("Material Thickness",11.88,[10,1])

boltLength.setMM(pinHeight*2)
double sphereRad= (1.1*25.4)/2
//The tracking object
CSG sphere = new Sphere(sphereRad)// Spheres radius
				.toCSG()// convert to CSG to display
				.toZMin()
				.movez(pinHeight)
CSG simpleSyntax =new Cylinder(7,7,pinHeight*2,(int)30).toCSG() // a one line Cylinder

CSG bolt = Vitamins.get("capScrew","M3")
			.rotx(180)
			.movez(-2)
			.toolOffset(printerOffset.getMM()-0.1)
CSG boltKeepaway = CSG.unionAll([bolt,
							bolt.movez(-3),
							bolt.movez(-6),
							bolt.movez(-9)])


double coasterThickness= 11.76

CSG coaster =new Cylinder(25,25,coasterThickness,(int)30).toCSG() // a one line Cylinder
				.toZMax()
				.difference(boltKeepaway)
CSG calibration = new Cylinder(sphereRad,sphereRad,thickness.getMM(),(int)30).toCSG() // a one line Cylinder
				.toZMax()
				.movez(-coasterThickness)
				.difference(boltKeepaway)
CSG coloredObject = simpleSyntax.union(sphere)
					.difference(boltKeepaway)
CSG calibrationCoaster=coaster.union(calibration)
return [coloredObject,coaster,calibrationCoaster].collect{it.movez(coasterThickness)}
				
				
