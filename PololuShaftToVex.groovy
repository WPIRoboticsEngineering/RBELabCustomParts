double measuredVSize = 2.75
double vshaftSize = 3.12
double finalV = vshaftSize+(vshaftSize-measuredVSize)

double measuredpSize = 3.46/2
double pshaftSize = 2
double finalp = pshaftSize+(pshaftSize-measuredpSize)

double measuredfSize = 3.15
double fshaftSize = 3.4
double finalf = fshaftSize+(fshaftSize-measuredfSize)

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

CSG coupler = new Cube(	7.5,// X dimention
			7.5,// Y dimention
			20//  Z dimention
			).toCSG()
			.difference([pshaft,vshaft])
			.rotx(90)
			.toZMin()
return [coupler]