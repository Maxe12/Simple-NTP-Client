package main;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class Main {
    private static final String[] NTP_SERVERS = {"129.6.15.28"};
    private static final int WAIT_FOR_SERVER_RESPONSE = 5000;

    public static void main (String[] args) {
        TimeStamp time = getCurrentTime();
        System.out.println(time);
        assert time != null;
        Date timeDate = new Date(time.getTime());
        System.out.println(timeDate);
    }

    public static TimeStamp getCurrentTime() {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(WAIT_FOR_SERVER_RESPONSE);
        try {
            client.open();
            for (String server : NTP_SERVERS) {
                try {
                    InetAddress ioe = InetAddress.getByName(server);
                    TimeInfo info = client.getTime(ioe);
                    return TimeStamp.getNtpTime(info.getReturnTime());
                } catch (Exception e2) {
                    System.out.println("Can't get response from server: " + server + ".");
                }
            }
        } catch (SocketException se) {
            System.out.println("Can't open client session");
        } finally {
            client.close();
        }
        return null;
    }
}
