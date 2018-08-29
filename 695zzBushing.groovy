def offset = 0.6
def h=4
def outerrad = (13-offset+0.1)/2
def innerrad=(5+offset)/2
def halfRad = outerrad - ((outerrad-innerrad)/2)
def quaterRad= outerrad - ((outerrad-innerrad)/4)
CSG outer =new Cylinder(outerrad,h).toCSG() // a one line Cylinder
CSG hole =new Cylinder(innerrad,h).toCSG() // a one line Cylinder
CSG profile = new Cylinder(halfRad-offset, // Radius at the bottom
                      		quaterRad-offset, // Radius at the top
                      		h/2, // Height
                      		(int)30 //resolution
                      		).toCSG()//convert to CSG to display   
profile=profile.union(profile.rotx(180).movez(h))

def outerRace =outer.difference( profile.makeKeepaway(offset*2).movez(offset))
def innerRace = outer.intersect( profile).difference(hole)
return[outerRace,innerRace]