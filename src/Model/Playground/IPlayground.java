package Model.Playground;

public interface IPlayground {

    //You can only build the Playground if the Ships are created
    //The Amount of Ships placed in the Playground is saved
    void buildPlayground();


    //You can only draw the Playground if the Playground is built
    void drawPlayground();

    //Sets all Fields in Drawable Field [][] to null
    //Sets the Amount of Ships placed in the Playground to 0
    //After the Playground is reset it needs to be instantiated and build again.
    void resetAll();

}
