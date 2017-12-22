package org.mtransit.parser.ca_brampton_transit_bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.Utils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MDirectionType;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTripStop;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.mt.data.MTrip;

// http://www.brampton.ca/EN/city-hall/opengov/open-data-catalogue/Pages/Welcome.aspx
// http://www.brampton.ca/EN/City-Hall/OpenGov/Open-Data-Catalogue/Documents/Google_Transit.zip
public class BramptonTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-brampton-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new BramptonTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("\nGenerating Brampton Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("\nGenerating Brampton Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (gTrip.getTripHeadsign().equalsIgnoreCase("SORRY...NOT IN SERVICE")) {
			return true;
		}
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public boolean excludeRoute(GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public long getRouteId(GRoute gRoute) {
		return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
	}

	private static final Pattern ROUTE = Pattern.compile("(route)", Pattern.CASE_INSENSITIVE);
	private static final String ROUTE_REPLACEMENT = " ";

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongName();
		routeLongName = ROUTE.matcher(routeLongName).replaceAll(ROUTE_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR = "DB3935"; // RED (LOGO)

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public String getRouteColor(GRoute gRoute) {
		int rsn = Integer.parseInt(gRoute.getRouteShortName());
		switch (rsn) {
		// @formatter:off
		case 1: return "2E3092";
		case 2: return "C6168D";
		case 3: return "00AEEF";
		case 4: return "A6664C";
		case 5: return "007A5E";
		case 6: return "8B4A8A";
		case 7: return "029CAA";
		case 8: return "CF8B2D";
		case 9: return "00A88E";
		case 10: return "805281";
		case 11: return "79A342";
		case 12: return "DC5942";
		case 13: return "007F9E";
		case 14: return "AD855A";
		case 15: return "CA636C";
		case 16: return "00907F";
		case 17: return "0071BC";
		case 18: return "EC008C";
		case 19: return "BB7831";
		case 20: return "F7931D";
		case 21: return "164E87";
		case 23: return "8FAA5E";
		case 24: return "A52868";
		case 25: return "005F8C";
		case 26: return "029CAA";
		case 29: return "E4A024";
		case 30: return "41827C";
		case 31: return "F48473";
		case 32: return "942976";
		case 33: return "32327B";
		case 35: return "F7931D";
		case 36: return "2E917D";
		case 40: return "56C5D0";
		case 50: return "0095DA";
		case 51: return "E4A024";
		case 52: return "0066B3";
		case 53: return "F48473";
		case 54: return "9E76B4";
		case 55: return "F16567";
		case 56: return "F26667";
		case 57: return "1E6649";
		case 58: return "009081";
		case 60: return "1B62B7";
		case 92: return "8DC73F";
		case 104: return "DB2375";
		case 115: return "274867";
		case 185: return "04567E";
		case 199: return "04567E";
		case 200: return "0161AB";
		case 201: return "0161AB";
		case 202: return "0161AB";
		case 203: return "0161AB";
		case 204: return "0161AB";
		case 205: return "0161AB";
		case 206: return "0161AB";
		case 207: return "0161AB";
		case 208: return "0161AB";
		case 209: return "0161AB";
		case 210: return "0161AB";
		case 211: return "0161AB";
		case 212: return "0161AB";
		case 213: return "0161AB";
		case 214: return "0161AB";
		case 215: return "0161AB";
		case 216: return "0161AB";
		case 217: return "0161AB";
		case 501: return "EC2027";
		case 502: return "EC2027";
		case 505: return "EC2027";
		case 511: return "EC2027";
		case 561: return "EC2027";
		// @formatter:on
		default:
			if (isGoodEnoughAccepted()) {
				return null;
			}
			System.out.printf("\nUnexpected route color '%s'\n", gRoute);
			System.exit(-1);
			return null;
		}
	}

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()));
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	private static final String WESTBOUND = "westbound";
	private static final String WEST = "west";
	private static final String WB = "wb";
	private static final String WB_ = " wb-";
	private static final String EASTBOUND = "eastbound";
	private static final String EAST = "east";
	private static final String EB = "eb";
	private static final String EB_ = " eb-";
	private static final String SOUTHBOUND = "southbound";
	private static final String SOUTH = "south";
	private static final String SB = "sb";
	private static final String SB_ = " sb-";
	private static final String NORTHBOUND = "northbound";
	private static final String NORTH = "north";
	private static final String NB = "nb";
	private static final String NB_ = " nb-";

	private static final String TO = " to ";
	private static final String VIA = " via ";

	private static final String LOOP = "Loop";

	private static final String LOOP_LC = "loop";

	private static final String ENDS_WITH_AM = " am";
	private static final String ENDS_WITH_PM = " pm";
	private static final String PM = "PM";
	private static final String AM = "AM";

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String gTripHeadsignLC = gTrip.getTripHeadsign().toLowerCase(Locale.ENGLISH);
		int indexOfTO = gTripHeadsignLC.indexOf(TO);
		if (indexOfTO >= 0) {
			gTripHeadsignLC = gTripHeadsignLC.substring(0, indexOfTO);
		}
		int indexOfVIA = gTripHeadsignLC.indexOf(VIA);
		if (indexOfVIA >= 0) {
			gTripHeadsignLC = gTripHeadsignLC.substring(0, indexOfVIA);
		}
		if (gTripHeadsignLC.endsWith(NORTH) || gTripHeadsignLC.endsWith(NORTHBOUND) || gTripHeadsignLC.endsWith(NB) || gTripHeadsignLC.contains(NB_)) {
			mTrip.setHeadsignDirection(MDirectionType.NORTH);
			return;
		} else if (gTripHeadsignLC.endsWith(SOUTH) || gTripHeadsignLC.endsWith(SOUTHBOUND) || gTripHeadsignLC.endsWith(SB) || gTripHeadsignLC.contains(SB_)) {
			mTrip.setHeadsignDirection(MDirectionType.SOUTH);
			return;
		} else if (gTripHeadsignLC.endsWith(EAST) || gTripHeadsignLC.endsWith(EASTBOUND) || gTripHeadsignLC.endsWith(EB) || gTripHeadsignLC.contains(EB_)) {
			mTrip.setHeadsignDirection(MDirectionType.EAST);
			return;
		} else if (gTripHeadsignLC.endsWith(WEST) || gTripHeadsignLC.endsWith(WESTBOUND) || gTripHeadsignLC.endsWith(WB) || gTripHeadsignLC.contains(WB_)) {
			mTrip.setHeadsignDirection(MDirectionType.WEST);
			return;
		}
		if (gTripHeadsignLC.endsWith(LOOP_LC)) {
			mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
			return;
		}
		if (isGoodEnoughAccepted()) {
			if (mRoute.getId() == 1l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.EAST);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.WEST);
					return;
				}
			} else if (mRoute.getId() == 7l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.NORTH);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 9l) {
				if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.WEST);
					return;
				}
			} else if (mRoute.getId() == 14l) {
				if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 15l) {
				if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 18l) {
				if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 26l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 30l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.NORTH);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 32l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 33l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 50l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.NORTH);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 53l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 54l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 57l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.NORTH);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.SOUTH);
					return;
				}
			} else if (mRoute.getId() == 58l) {
				mTrip.setHeadsignString(LOOP, gTrip.getDirectionId());
				return;
			} else if (mRoute.getId() == 501l) {
				if (gTrip.getDirectionId() == 0) {
					mTrip.setHeadsignDirection(MDirectionType.EAST);
					return;
				} else if (gTrip.getDirectionId() == 1) {
					mTrip.setHeadsignDirection(MDirectionType.WEST);
					return;
				}
			}
		}
		if (gTripHeadsignLC.endsWith(ENDS_WITH_AM)) {
			mTrip.setHeadsignString(AM, gTrip.getDirectionId());
			return;
		} else if (gTripHeadsignLC.endsWith(ENDS_WITH_PM)) {
			mTrip.setHeadsignString(PM, gTrip.getDirectionId());
			return;
		}
		mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), gTrip.getDirectionId());
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		if (isGoodEnoughAccepted()) {
			return super.mergeHeadsign(mTrip, mTripToMerge);
		}
		System.out.printf("\nUnexpected trips to merge %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public int getStopId(GStop gStop) {
		String stopId = gStop.getStopId();
		if (stopId != null && stopId.length() > 0 && Utils.isDigitsOnly(stopId)) {
			return Integer.valueOf(stopId);
		}
		try {
			Matcher matcher = DIGITS.matcher(stopId);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group());
			}
			System.out.printf("\nUnexpected stop ID  %s!\n", gStop);
			System.exit(-1);
			return -1;
		} catch (Exception e) {
			System.out.printf("\nError while finding stop ID for %s.\n", gStop);
			e.printStackTrace();
			System.exit(-1);
			return -1;
		}
	}
}
