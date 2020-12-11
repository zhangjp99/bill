package com.oss.Manager;

import com.oss.beans.Servers;
import com.oss.enums.Protocols;
import com.oss.enums.ServerGroups;

/*
 * 主要功能：
 * 1 根据参数返回一个特定的服务器参数配置对象。
 *
 **/
public class ServerManager {
	public Servers getServer(ServerGroups serverGroups) {
		switch (serverGroups) {
		case ws01:
			return setServers("", 443, "ws01.library.sh.cn", Protocols.https);
		case ws0a:
			//return setServers("", 443, "ws0a.library.sh.cn", Protocols.https);
			return setServers("", 443, "10.1.21.194", Protocols.https);
		case ws11:
			return setServers("", 443, "ws11.library.sh.cn", Protocols.https);
		default:
			return setServers("", 443, "ws11.library.sh.cn", Protocols.https);
		}
	}

	private Servers setServers(String ip, int port, String dns, Protocols protocols) {
		String url1 = "%s://%s:%s/mservices/api?";
		String url2 = "%s://%s:%s/mservices/token?";
		Servers servers = new Servers();
		servers.setPort(port);
		servers.setIp(ip);
		servers.setDns(dns);
		servers.setProtocols(protocols);
		String urlAPI = String.format(url1, (protocols == Protocols.https ? "https" : "http"), dns, port);
		String urlToken = String.format(url2, (protocols == Protocols.https ? "https" : "http"), dns, port);
		servers.setBaseUrlAPI(urlAPI);
		servers.setBaseUrlToken(urlToken);
		return servers;
	}
}
