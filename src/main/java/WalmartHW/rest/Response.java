package WalmartHW.rest;

import WalmartHW.service.VenueService;

public class Response {
	
	private int emptySeats;
	
	public Response(VenueService v) {
		emptySeats = v.findSeats();
	}
	
	public int getEmptySeats() {
	    return emptySeats;
	}
}