package net.protocol.ip.ipv4;

/**
 * IPv4RoutingTable represents the systems routing table. It's a registry with 
 * all the IPv4Rute entries
 * @author JPG
 * @since
 */
public class IPv4RoutingTable {
	/** The table that holds the routes */
	private IPv4Route[] routes;
	
	/**
	 * Initializes the routing table
	 */
	public IPv4RoutingTable() {
		routes = new IPv4Route[10];
	}
	
	/**
	 * Returns the gateway that should be used to reach a particular destination
	 * @param	destination the destination we try to reach
	 * @return	the gateway to use for this destination
	 * @throws	IPv4Exception if this destination is not reachable
	 */
	public IPv4Address getRoute(IPv4Address destination) throws IPv4Exception {
		for (int i=0; i<routes.length; i++) {
			if (routes[i] != null)
				if (routes[i].includesDestination(destination))
					return routes[i].getGateway();
		}
		
		throw new IPv4Exception("No route to host");
	}
	
	/**
	 * Adds a route entry to this routing table
	 * @param	route the route entry to be added
	 */
	public void addRoute(IPv4Route route) {
		for (int i=0; i<routes.length; i++) {
			if (routes[i] == null) {
				routes[i] = route;
				return;
			}
		}
		IPv4Route[] tmp = new IPv4Route[routes.length+5];
		System.arraycopy(routes, 0, tmp, 0, routes.length);
		tmp[routes.length] = route;
		routes = tmp;
	}
	
	/**
	 * Removes a route entry from this routing table
	 * @param	route the route entry to be removed
	 */
	public void removeRoute(IPv4Route route) {
		for (int i=0; i<routes.length; i++) {
			if (routes[i] == route) {
				routes[i] = null;
				return;
			}
		}
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Available routes\n");
		for (int i=0; i<routes.length; i++) {
			if (routes[i] == null) continue;
			s.append(routes[i].toString()+"\n");
		}
		return s.toString();
	}
}
