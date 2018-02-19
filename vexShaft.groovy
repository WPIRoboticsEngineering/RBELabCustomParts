if(args== null)
	args = [11]

LengthParameter printerOffset = new LengthParameter("printerOffset",0.6,[2,0.001])

double vshaftSize = 3.12
double finalV = vshaftSize+(printerOffset.getMM())


CSG vshaft =  new RoundedCube(	finalV,// X dimention
				finalV,// Y dimention
				args.get(0)//  Z dimention
				)
				.cornerRadius(0.5)// sets the radius of the corner
				.toCSG()// converts it to a CSG tor display
				
return 	vshaft			