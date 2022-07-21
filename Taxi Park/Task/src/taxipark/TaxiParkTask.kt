package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val drivers = this.trips.map { it.driver }.toSet()

    return this.allDrivers.filterNot {drivers.contains(it)}.toSet()
}
/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val allPassengers = this.allPassengers
    val passengerByTrips = this.trips.map { it.passengers }
    val containsFaithfulPassenger: (List<Set<Passenger>>, Passenger) -> Boolean = {
            passengerTripSet: List<Set<Passenger>>, passenger: Passenger ->
        var count = 0
        passengerTripSet.forEach {
            if (it.contains(passenger)) count++
        }
        count >= minTrips
    }
    return allPassengers.filter { containsFaithfulPassenger(passengerByTrips, it)}.toSet()
}
/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val allPassengers = this.allPassengers
    val passengerByTrips = this.trips.filter { it.driver == driver }.map { it.passengers }
    val containsFaithfulPassenger: (List<Set<Passenger>>, Passenger) -> Boolean = {
            passengerTripSet: List<Set<Passenger>>, passenger: Passenger ->
        var count = 0
        passengerTripSet.forEach {
            if (it.contains(passenger)) count++
        }
        count > 1
    }
    return allPassengers.filter { containsFaithfulPassenger(passengerByTrips, it)}.toSet()
}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val allPassengers = this.allPassengers
    val passengerByTripsDiscount = this.trips.filter { (it.discount != null && it.discount > 0.0) }.map { it.passengers }
    val passengerByTripsNoDiscount = this.trips.filter { (it.discount == null) || (it.discount == 0.0) }.map { it.passengers }

    return allPassengers.filter { x ->
        var discount = 0
        var noDiscount = 0
        passengerByTripsDiscount.forEach{
            if (it.contains(x)) discount++
        }
        passengerByTripsNoDiscount.forEach{
            if (it.contains(x)) noDiscount++
        }

        discount > noDiscount
    }.toSet()

}
/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val mappedTripsToRange = this.trips.groupBy {
        with(it.duration / 10 * 10) { this until this + 10 }
    }
    return mappedTripsToRange.maxBy { it.value.size }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val income = this.trips.map { it.cost }.sum()

    //calculating 20% of the drivers in the taxi park
    val numTopDrivers = (this.allDrivers.size * 0.2).toInt()

    val mappedDriversToIncome = this.trips.map{it.driver to it.cost}.groupBy{it.first}.mapValues { it.value.sumByDouble { it.second } }
    if ( income == 0.0 || mappedDriversToIncome.values.sortedDescending().take(numTopDrivers).sum() < income * 0.8) {
        return false
    }
    return true
}