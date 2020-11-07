package Model.Playground;

import Model.Ship.IShip;
import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;

import java.util.ArrayList;

public class EnemyPlayground extends AbstactPlayground implements IEnemyPlayground {
    private int counterShipDestroyed = 0;

    public EnemyPlayground(int playgroundsize) {
        super(playgroundsize);
    }

    @Override
    public void buildPlayground() {
        this.shipsplaced = IShip.getAmount();

        for (int x = 1; x <= this.playgroundsize; x++) {
            for (int y = 1; y <= this.playgroundsize; y++) {
                System.out.print("Hello");


                //Field is empty
                if (Field[x][y] == null) {
                    Field[x][y] = new Water();
                }

            }
        }


    }

    //Muss sich Schiffe gegnerische Schiffe merken können um unmögliche Felder als Wasser zu markieren, soweit sie noch nicht beschossenes Wasser sind
    //Muss zudem die gegnerisch versunkenen Schiffe zählen um ein gewonnenes Spiel auszugeben
    @Override
    public ShotResponse shoot(Point pos_shot, boolean shipHit, boolean shipSunken) {
        //Ship hit and sunken
        if (shipSunken) {
            counterShipDestroyed++;
            if (counterShipDestroyed == this.shipsplaced) {
                this.gameWon = true;
                return new ShotResponse(true, null);
            }
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", null);
            //finde heraus wie das schiff stand
            //setze alle umliegenden Felder auf ShotWater();
            int shotX = pos_shot.getX();
            int shotY = pos_shot.getY();


            Point[] destroyedShip = new Point[9];
            //Enemy Ship horizontal placed
            if (Field[shotX + 1][shotY] instanceof ShipPart || Field[shotX - 1][shotY] instanceof ShipPart) {
                //ermittle Schiffpositionen

                //Schiff is horizontal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle rechte und linke positionen
                int i = shotX;
                int count = 4;


                //  _ _ _ _ X _ _ _ _

                boolean hit = true;

                //Ermittle HitPositionen rechts davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count++;
                        i++;
                    } else {
                        hit = false;
                    }
                }

                i = shotX;
                count = 4;
                hit = true;
                //Ermittle HitPositinen links davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count--;
                        i--;
                    } else {
                        hit = false;
                    }
                }




            }

            //Enemy Ship vertical placed
            if (Field[shotX][shotY + 1] instanceof ShipPart || Field[shotX][shotY - 1] instanceof ShipPart) {

                //Schiff is vertikal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle obere und untere positionen
                int i = shotY;
                int count = 4;


                //  _
                //  _
                //  _
                //  _
                //  Y
                //  _
                //  _
                //  _
                //  _

                boolean hit = true;

                //Ermittle HitPositionen oben davon
                while (hit) {
                    if (Field[shotX][i] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count--;
                        i--;
                    } else {
                        hit = false;
                    }
                }

                i = shotY;
                count = 4;
                hit = true;

                //Ermittle HitPositinen links davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count++;
                        i++;
                    } else {
                        hit = false;
                    }
                }


            }

            //Die Schiffsposition ist jetzt im PointArray destroyedShip
            ArrayList<Point> ImpossiblePositions = this.setImpossibleFieldsToWater(destroyedShip);

            return new ShotResponse(false, ImpossiblePositions);
        }
        //Ship only hit
        else if (shipHit) {
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", null);
            return new ShotResponse(false, null);
        }
        //No Hit
        else {
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();
            return new ShotResponse(false, null);
        }

    }



    //Der Punkt is gesetzt -> markiere als ShotWater alle UmliegendenFelder, wenn nicht ShipPart und nicht ShotWater, also nur wenn es Wasser ist -> shotWater
    private ArrayList<Point> setImpossibleFieldsToWater(Point[] destroyedShip) {
        ArrayList<Point> ImpossiblePostions = new ArrayList<>();
        for (Point point : destroyedShip) {
            if (Field[destroyedShip[0].getX()][destroyedShip[0].getY()] != null) {

                //Oben links    x-1 y+1
                if (Field[point.getX() - 1][point.getY() - 1] instanceof Water){
                    Field[point.getX() - 1][point.getY() - 1] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX() - 1, point.getY() - 1));
                }
                //Mitte links   x-1 y+0
                if (Field[point.getX() - 1][point.getY()] instanceof Water) {
                    Field[point.getX() - 1][point.getY()] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX() - 1, point.getY()));
                }
                //Unten links   x-1 y+1
                if (Field[point.getX() - 1][point.getY() + 1] instanceof Water) {
                    Field[point.getX() - 1][point.getY() + 1] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX() - 1, point.getY() + 1));
                }
                //Unten mitte   x+0 y-1
                if (Field[point.getX()][point.getY() + 1] instanceof Water) {
                    Field[point.getX()][point.getY() + 1] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX(), point.getY() + 1));
                }
                //unten rechts  x-1 y+1
                if (Field[point.getX() + 1][point.getY() + 1] instanceof Water){
                    Field[point.getX() + 1][point.getY() + 1] = new ShotWater();
                    ImpossiblePostions.add(new Point (point.getX()+1, point.getY() +1 ));
                }
                //Mitte rechts  x-1 y+0
                if (Field[point.getX() + 1][point.getY()] instanceof Water) {
                    Field[point.getX() + 1][point.getY()] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX() + 1, point.getY()));
                }
                //oben rechts   x-1 y-1
                if (Field[point.getX() + 1][point.getY() - 1] instanceof Water){
                    Field[point.getX() + 1][point.getY() - 1] = new ShotWater();
                    ImpossiblePostions.add(new Point (point.getX()+1, point.getY() -1 ));
                }
                //Mitte oben    x+0 y-1
                if (Field[point.getX()][point.getY() - 1] instanceof Water) {
                    Field[point.getX()][point.getY() - 1] = new ShotWater();
                    ImpossiblePostions.add(new Point(point.getX(), point.getY() - 1));
                }
            }
        }
        return ImpossiblePostions;
    }
}