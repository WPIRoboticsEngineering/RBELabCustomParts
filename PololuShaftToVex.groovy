
double vshaftSize = 3.12
double finalV = vshaftSize+0.37

double pshaftSize = 6.0/2.0
double finalp = pshaftSize+0.27

double fshaftSize = 5.34
double finalf = fshaftSize+0.25

CSG pshaft =new Cylinder(finalp,finalp,11,(int)30).toCSG() // a one line Cylinder
CSG flat = new Cube(	finalp*2,// X dimention
			finalf,// Y dimention
			11//  Z dimention
			).toCSG()
			.toYMin()
			.toZMin()
			.movey(pshaft.getMinY())

CSG vshaft =  new RoundedCube(	finalV,// X dimention
				finalV,// Y dimention
				11//  Z dimention
				)
				.cornerRadius(0.5)// sets the radius of the corner
				.toCSG()// converts it to a CSG tor display
				.toZMax()
				.movez(-1)
pshaft=pshaft.intersect(flat)

CSG coupler = new Cube(	12,// X dimention
			12,// Y dimention
			20//  Z dimention
			).toCSG()
			.difference([pshaft,vshaft])
			.rotx(-90)
			.toZMin()
return [coupler]