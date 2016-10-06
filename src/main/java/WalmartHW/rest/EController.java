package WalmartHW.rest;

import WalmartHW.service.VenueService;
import WalmartHW.model.SeatGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EController {

	@Autowired
    private VenueService v;

    @RequestMapping(method=RequestMethod.GET, value="/seats")
    public Response emptySeats() {
        return new Response(v);
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/seats")
    public SeatGroup holdSeats(@RequestParam(value="numSeats", defaultValue="1") int numSeats) {
        return v.holdSeats(numSeats);
    }
    
    @RequestMapping(method=RequestMethod.PUT, value="/seats")
    public SeatGroup reserveSeats(@RequestParam(value="ID", defaultValue="") String ID) {
        return v.reserveSeats(ID);
    }
}