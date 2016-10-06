# WM
Basic seating organization program, useful for first come first serve priority seating.

### Technologies and Credits
JRE build 1.8.0_101-b13
Gradle 2.2.1
See gradle file for dependencies used

## Assumptions, Approach and Design
This program was designed with the intention of being a modular piece of a larger system. To this end, I left the exact prioritization scheme of seating to the outside deveoper; the program is designed to seat customers by an arbitrary first-come-first serve priority. The internal data structure is a list where the seat at index = 0 is arbitrarily the best seat in the house and seat index = n - 1 is the worst seat. This program is best used with an outside mapping to the "true" best to worst seats in the house (distance from stage, distance from group, etc).

```
### Priority Example:

[20 empty seats]

after 5 holds,

[5 holds], [15 empty]

after 10 more holds,

[5 holds], [10 holds], [5 empty]

after first 5 holds expire,

[5 empty], [10 holds], [5 empty]

after first 7 more holds,

[5 holds], [10 holds], [2 holds], [3 empty]
```

I also designed the system to be secure for the customers in terms of privacy. Seats are distinguished and managed in groupings by a randomly generated ID. The design assumes that the user is not in demand of precise real time information beyond at the moment the API call is handled. Updating holds for expiration is synchronous to API calls, not real time.

## How to Start Server on localhost:8080
In your console, build the project with Gradle from the root directory (with build.gradle).
```
gradle build
```
Then run the build.
```
gradle bootRun
```
Now you can start accessing the API.
## How to Use API
The endpoint (GET request)
```
localhost:8080/seats
```
should return,
```
{
  "emptySeats": 50
}
```
initially (the empty VenueService constructor allocateds 50 seats with an expiration time of 5 seconds). 

A POST request to the same endpoint should include a parameter (numSeats) to hold the number of seats to hold.

```
http://localhost:8080/seats?numSeats=12
```

holds best 12 seats and returns JSON that might look like,

```
{
  "startTime": 1475784020141,
  "takenSeats": [
    {
      "blockStart": 1,
      "blockEnd": 3,
      "blockSize": 3
    },
    {
      "blockStart": 6,
      "blockEnd": 8,
      "blockSize": 3
    }
  ],
  "groupSize": 6,
  "groupID": "47673d75-71a6-49e3-94cd-26e330a99b1b"
}
```

which contains the start time of the hold and an array of seat blocks which are held at the time of the request. Note these blockStart and blockEnd seats are inclusive, that is, when blockStart and blockEnd are equal, the blockSize is 0. The groupID returned is used to track existing groups, useful for reservations using,

```
http://localhost:8080/seats?ID=47673d75-71a6-49e3-94cd-26e330a99b1b
```

which returns (if the hold has yet to expire),

```
{
  "startTime": -1475784020141,
  "takenSeats": [
    {
      "blockStart": 1,
      "blockEnd": 3,
      "blockSize": 3
    },
    {
      "blockStart": 6,
      "blockEnd": 8,
      "blockSize": 3
    }
  ],
  "groupSize": 6,
  "groupID": "47673d75-71a6-49e3-94cd-26e330a99b1b"
}
```

Notice the startTime is now a negative value. This denotes a reservation has been made for the group.

## Considerations
There is no mechanism to cancel reservations or holds manually, this is best for a system with a no refund policy.
