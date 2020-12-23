package Network;

public interface IServer extends ICommunication{
    /**
     * Gets an Array of IP Addresses
     * @return String Array of IP Addresses
     */
    String[] getAllIPAddress();

    /**
     * Gets the preferred outbound IP Address creating a DatagramSocket
     * If this method is not returning the right IP Address use the getAllIPAddress Method and select the suitable
     * @return String with IP Address
     */
    String getIPAddress();

    /**
     * Server accepts attempting connections and creates Reader and Writers
     * @return State of Connection, returns true if the server connected and false if an timeout occured
     */
    boolean startSeverConnection();

}
