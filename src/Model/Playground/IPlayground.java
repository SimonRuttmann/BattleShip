package Model.Playground;

public interface IPlayground {

    //You can only draw the Playground if the Playground is builded
    void drawPlayground();

    void resetAll();

    //You can only build the Playground if the Ships are created
    void buildPlayground();
}
