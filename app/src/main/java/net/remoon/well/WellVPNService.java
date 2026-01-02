package net.remoon.well;

import android.content.Intent;
import android.net.VpnService;
import net.remoon.well.cmd.Cmd;
import inet.ipaddr.IPAddressString;
import org.json.JSONArray;

import java.net.URL;

public class WellVPNService extends VpnService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            setupVpn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void setupVpn() throws Exception {
        Builder builder = new Builder();
        builder.setSession("well-net");
        JSONArray routes = new JSONArray(Cmd.getRoutes());
        for (int i = 0; i < routes.length(); i++) {
            String route = routes.get(i).toString();
            IPAddressString pf = new IPAddressString(route);
            java.net.InetAddress addr = pf.getAddress().toInetAddress();
            Integer prefix = pf.getNetworkPrefixLength();
            builder.addAddress(addr, prefix);
        }
        builder.setMtu(2400);
        builder.setBlocking(true);
        builder.addDisallowedApplication("net.remoon.well");
        android.os.ParcelFileDescriptor tun = builder.establish();
        int fd = tun.detachFd();
        Cmd.startWireGuard(fd);
    }



    @Override
    public void onDestroy() {
        Cmd.stopWireGuard();
        super.onDestroy();
    }
}
