package th.ac.sut.com.gpstracklocation.constant;

/**
 * Created by Nirvana on 8/3/2559.
 */
public class Constant {

    public static final String host = "192.168.1.112";
    public static final String port = "8888";
    public static final String applicationName = "MyLocation";

    public static final String url = host + ":" + port + "/" + applicationName + "/";

    //save location
    public static final String saveLocation = "saveLocation";
    public static final String getSaveLocation = url + saveLocation;

}
