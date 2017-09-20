if(args == null)
	args = [6.0,5.4,11]

LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])


double finalp = args.get(0)/2+printerOffset.getMM()

double finalf = args.get(1)+printerOffset.getMM()

CSG pshaft =new Cylinder(finalp,finalp,11,(int)30).toCSG() // a one line Cylinder
CSG flat = new Cube(	finalp*2,// X dimention
			finalf,// Y dimention
			args.get(2)//  Z dimention
			).toCSG()
			.toYMin()
			.toZMin()
			.movey(pshaft.getMinY())

return pshaft.intersect(flat)