package Model.Playground;

import Model.Ship.IShip;
import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;

public class OwnPlayground extends AbstactPlayground implements IOwnPlayground{

    public OwnPlayground(int playgroundsize) {
        super(playgroundsize);
    }


    //This is the shoot method, when we get hit

    public ShotResponse shoot(Point pos_shot) {

        //Hit
        if (Field[pos_shot.getX()][pos_shot.getY()] instanceof ShipPart){

            //Decrease Hit Points of the Ship
            ShipPart hitShipPart = (ShipPart)Field[pos_shot.getX()][pos_shot.getY()];
            IShip hitShip = hitShipPart.getOwner();
            hitShip.sethitPoints( hitShip.gethitPoints() -1 );

            //Mark shipPart as destroyed
            hitShipPart.setPart("Destroyed");

            //Ship sunken
            if ( hitShip.gethitPoints() <= 0){
                IShip.getShipList().remove(hitShip);

                //Game won
                if ( IShip.getShipList().size() == 0){
                    this.gameLost = true;
                    return new ShotResponse (true,true,true);
                }

                //Game still running
                return new ShotResponse(false, true, true);

            }
            //Ship still alive
            else{
                return new ShotResponse( false, true,false);
            }

            //ShotResponse response = new ShotResponse();
        }

        //Water
        if (Field[pos_shot.getX()][pos_shot.getY()] instanceof Water){
            ShotResponse response = new ShotResponse(false,false,false);
            //Set ShotWater
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();

            return response;
        }
        return null;
    }


    //This methods build us our playground at the beginning
    @Override
    public void buildPlayground() {
        this.shipsplaced = IShip.getAmount();

        //For every Ship in the ShipList insert the affiliated ship parts and fill the left fields with water

        for ( IShip Element : IShip.getShipList()) {

            //Position returns two Points, Start and End
            Point[] shipposition = Element.getPosition();
            int xStart = shipposition[0].getX();
            int yStart = shipposition[0].getY();

            int xEnd = shipposition[1].getX();
            int yEnd = shipposition[1].getY();

            //Insert Ship parts
            //Ship vertical
            if (xStart == xEnd) {
                Field[xStart][yStart] = new ShipPart("start vertical", Element);
                Field[xStart][yEnd] = new ShipPart("end vertical", Element);
                for (int i = yStart+1; i < yEnd; i++) {
                    Field[xStart][i] = new ShipPart("middle vertical", Element);
                }
            }
            //Ship horizontal
            else {
                Field[xStart][yStart] = new ShipPart("start horizontal", Element);
                Field[xEnd][yStart] = new ShipPart("end horizontal", Element);
                for (int i = xStart+1; i < xEnd; i++) {
                    Field[i][yStart] = new ShipPart("middle horizontal", Element);
                }
            }

        }

            for ( int x = 1; x <= this.playgroundsize; x++)
            {
                for ( int y = 1; y <= this.playgroundsize; y++)
                {
                    System.out.print("Hello");


                    //Field is empty
                    if (Field[x][y] == null){
                        Field[x][y] = new Water();
                    }

                }
            }


    }

}
