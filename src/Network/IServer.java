package Network;

public interface IServer extends ICommunication{
    /**
     * Gets an Array of IP Addresses
     * This method may get needed by using different operating systems, or multiple virtual machines
     * @return String Array of IP Addresses
     */
    String[] getAllIPAddress();

    /**
     * Gets the preferred outbound IP Address by creating a DatagramSocket
     * If this method is not returning the right IP Address use the getAllIPAddress Method and select the suitable
     * @return String with IP Address
     */
    String getIPAddress();

    /**
     * Server accepts attempting connections and creates Reader and Writers
     * @return Connected:    The server connected to the client
     *         Timeout:      No connection for 1 min
     *         ManualClose:  The connection offer from server got canceled manual
     *         ioException:  IOException appeared
     */
    Server.ConnectionStatus startSeverConnection();

}
