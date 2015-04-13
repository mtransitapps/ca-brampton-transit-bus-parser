package org.mtransit.parser.ca_brampton_transit_bus;

import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MDirectionType;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MSpec;
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
		System.out.printf("Generating Brampton Transit bus data...\n");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("Generating Brampton Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
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
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public long getRouteId(GRoute gRoute) {
		String routeId = gRoute.route_id;
		if (routeId != null && routeId.length() > 0 && Utils.isDigitsOnly(routeId)) {
			return Integer.valueOf(routeId); // using stop code as stop ID
		}
		Matcher matcher = DIGITS.matcher(routeId);
		matcher.find();
		return Long.parseLong(matcher.group());
	}

	private static final Pattern ROUTE = Pattern.compile("(route)", Pattern.CASE_INSENSITIVE);
	private static final String ROUTE_REPLACEMENT = " ";

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.route_long_name;
		routeLongName = ROUTE.matcher(routeLongName).replaceAll(ROUTE_REPLACEMENT);
		return MSpec.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR = "DB3935"; // RED (LOGO)

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String COLOR_274867 = "274867";
	private static final String COLOR_ED1C24 = "ED1C24";
	private static final String COLOR_8DC73F = "8DC73F";
	private static final String COLOR_009081 = "009081";
	private static final String COLOR_F26667 = "F26667";
	private static final String COLOR_9E76B4 = "9E76B4";
	private static final String COLOR_0066B3 = "0066B3";
	private static final String COLOR_0095DA = "0095DA";
	private static final String COLOR_56C5D0 = "56C5D0";
	private static final String COLOR_32327B = "32327B";
	private static final String COLOR_942976 = "942976";
	private static final String COLOR_F48473 = "F48473";
	private static final String COLOR_41827C = "41827C";
	private static final String COLOR_E4A024 = "E4A024";
	private static final String COLOR_005F8C = "005F8C";
	private static final String COLOR_A52868 = "A52868";
	private static final String COLOR_8FAA5E = "8FAA5E";
	private static final String COLOR_164E87 = "164E87";
	private static final String COLOR_F7931D = "F7931D";
	private static final String COLOR_BB7831 = "BB7831";
	private static final String COLOR_EC008C = "EC008C";
	private static final String COLOR_0071BC = "0071BC";
	private static final String COLOR_00907F = "00907F";
	private static final String COLOR_CA636C = "CA636C";
	private static final String COLOR_AD855A = "AD855A";
	private static final String COLOR_007F9E = "007F9E";
	private static final String COLOR_DC5942 = "DC5942";
	private static final String COLOR_79A342 = "79A342";
	private static final String COLOR_805281 = "805281";
	private static final String COLOR_00A88E = "00A88E";
	private static final String COLOR_CF8B2D = "CF8B2D";
	private static final String COLOR_029CAA = "029CAA";
	private static final String COLOR_007A5E = "007A5E";
	private static final String COLOR_A6664C = "A6664C";
	private static final String COLOR_00AEEF = "00AEEF";
	private static final String COLOR_C6168D = "C6168D";
	private static final String COLOR_2E3092 = "2E3092";
	private static final String COLOR_0161AB = "0161AB";

	@Override
	public String getRouteColor(GRoute gRoute) {
		Matcher matcher = DIGITS.matcher(gRoute.route_id);
		matcher.find();
		int routeId = Integer.parseInt(matcher.group());
		switch (routeId) {
		// @formatter:off
		case 1: return COLOR_2E3092;
		case 2: return COLOR_C6168D;
		case 3: return COLOR_00AEEF;
		case 4: return COLOR_A6664C;
		case 5: return COLOR_007A5E;
		case 7: return COLOR_029CAA;
		case 8: return COLOR_CF8B2D;
		case 9: return COLOR_00A88E;
		case 10: return COLOR_805281;
		case 11: return COLOR_79A342;
		case 12: return COLOR_DC5942;
		case 13: return COLOR_007F9E;
		case 14: return COLOR_AD855A;
		case 15: return COLOR_CA636C;
		case 16: return COLOR_00907F;
		case 17: return COLOR_0071BC;
		case 18: return COLOR_EC008C;
		case 19: return COLOR_BB7831;
		case 20: return COLOR_F7931D;
		case 21: return COLOR_164E87;
		case 23: return COLOR_8FAA5E;
		case 24: return COLOR_A52868;
		case 25: return COLOR_005F8C;
		case 26: return COLOR_029CAA;
		case 29: return COLOR_E4A024;
		case 30: return COLOR_41827C;
		case 31: return COLOR_F48473;
		case 32: return COLOR_942976;
		case 33: return COLOR_32327B;
		case 35: return COLOR_F7931D;
		case 40: return COLOR_56C5D0;
		case 50: return COLOR_0095DA;
		case 51: return COLOR_E4A024;
		case 52: return COLOR_0066B3;
		case 53: return COLOR_F48473;
		case 54: return COLOR_9E76B4;
		case 56: return COLOR_F26667;
		case 58: return COLOR_009081;
		case 92: return COLOR_8DC73F;
		case 115: return COLOR_274867;
		case 200: return COLOR_0161AB;
		case 201: return COLOR_0161AB;
		case 202: return COLOR_0161AB;
		case 203: return COLOR_0161AB;
		case 204: return COLOR_0161AB;
		case 205: return COLOR_0161AB;
		case 206: return COLOR_0161AB;
		case 207: return COLOR_0161AB;
		case 208: return COLOR_0161AB;
		case 209: return COLOR_0161AB;
		case 210: return COLOR_0161AB;
		case 211: return COLOR_0161AB;
		case 212: return COLOR_0161AB;
		case 213: return COLOR_0161AB;
		case 214: return COLOR_0161AB;
		case 215: return COLOR_0161AB;
		case 216: return COLOR_0161AB;
		case 501: return COLOR_ED1C24;
		case 502: return COLOR_ED1C24;
		case 505: return COLOR_ED1C24;
		case 511: return COLOR_ED1C24;
		// @formatter:on
		default:
			System.out.println("getRouteColor() > Unexpected route ID color '" + routeId + "' (" + gRoute + ")");
			System.exit(-1);
			return null;
		}
	}

	private static final String WESTBOUND = "westbound";
	private static final String WEST = "west";
	private static final String EASTBOUND = "eastbound";
	private static final String EAST = "east";
	private static final String SOUTHBOUND = "southbound";
	private static final String SOUTH = "south";
	private static final String NORTHBOUND = "northbound";
	private static final String NORTH = "north";

	private static final String TO = " to ";
	private static final String VIA = " via ";

	private static final String LOOP = "Loop";

	private static final String ENDS_WITH_AM = " am";
	private static final String ENDS_WITH_PM = " pm";
	private static final String PM = "PM";
	private static final String AM = "AM";

	@Override
	public void setTripHeadsign(MRoute route, MTrip mTrip, GTrip gTrip) {
		String gTripHeadsignLC = gTrip.trip_headsign.toLowerCase(Locale.ENGLISH);
		int indexOfTO = gTripHeadsignLC.indexOf(TO);
		if (indexOfTO >= 0) {
			gTripHeadsignLC = gTripHeadsignLC.substring(0, indexOfTO);
		}
		int indexOfVIA = gTripHeadsignLC.indexOf(VIA);
		if (indexOfVIA >= 0) {
			gTripHeadsignLC = gTripHeadsignLC.substring(0, indexOfVIA);
		}
		if (gTripHeadsignLC.endsWith(NORTH) || gTripHeadsignLC.endsWith(NORTHBOUND)) {
			mTrip.setHeadsignDirection(MDirectionType.NORTH);
			return;
		} else if (gTripHeadsignLC.endsWith(SOUTH) || gTripHeadsignLC.endsWith(SOUTHBOUND)) {
			mTrip.setHeadsignDirection(MDirectionType.SOUTH);
			return;
		} else if (gTripHeadsignLC.endsWith(EAST) || gTripHeadsignLC.endsWith(EASTBOUND)) {
			mTrip.setHeadsignDirection(MDirectionType.EAST);
			return;
		} else if (gTripHeadsignLC.endsWith(WEST) || gTripHeadsignLC.endsWith(WESTBOUND)) {
			mTrip.setHeadsignDirection(MDirectionType.WEST);
			return;
		}
		if (route.id == 7l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (route.id == 9l) {
			if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (route.id == 14l) {
			if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (route.id == 26l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 30l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (route.id == 32l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 33l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 50l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (route.id == 53l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 54l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 58l) {
			mTrip.setHeadsignString(LOOP, gTrip.direction_id);
			return;
		} else if (route.id == 501l) {
			if (gTrip.direction_id == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (gTrip.direction_id == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		}
		int directionId = gTrip.direction_id;
		String stationName = cleanTripHeadsign(gTrip.trip_headsign);
		if (gTripHeadsignLC.endsWith(ENDS_WITH_AM)) {
			stationName = AM;
		} else if (gTripHeadsignLC.endsWith(ENDS_WITH_PM)) {
			stationName = PM;
		}
		mTrip.setHeadsignString(stationName, directionId);
	}

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		return MSpec.cleanLabel(tripHeadsign);
	}

	private static final Pattern FIRST = Pattern.compile("(first )", Pattern.CASE_INSENSITIVE);
	private static final String FIRST_REPLACEMENT = "1st ";
	private static final Pattern SECOND = Pattern.compile("(second )", Pattern.CASE_INSENSITIVE);
	private static final String SECOND_REPLACEMENT = "2nd";
	private static final Pattern THIRD = Pattern.compile("(third )", Pattern.CASE_INSENSITIVE);
	private static final String THIRD_REPLACEMENT = "3rd ";
	private static final Pattern FOURTH = Pattern.compile("(fourth )", Pattern.CASE_INSENSITIVE);
	private static final String FOURTH_REPLACEMENT = "4th";
	private static final Pattern FIFTH = Pattern.compile("(fifth )", Pattern.CASE_INSENSITIVE);
	private static final String FIFTH_REPLACEMENT = "5th ";
	private static final Pattern SIXTH = Pattern.compile("(sixth )", Pattern.CASE_INSENSITIVE);
	private static final String SIXTH_REPLACEMENT = "6th ";
	private static final Pattern SEVENTH = Pattern.compile("(seventh )", Pattern.CASE_INSENSITIVE);
	private static final String SEVENTH_REPLACEMENT = "7th ";
	private static final Pattern EIGHTH = Pattern.compile("(eighth )", Pattern.CASE_INSENSITIVE);
	private static final String EIGHTH_REPLACEMENT = "8th ";
	private static final Pattern NINTH = Pattern.compile("(ninth )", Pattern.CASE_INSENSITIVE);
	private static final String NINTH_REPLACEMENT = "9th ";

	private static final Pattern AT = Pattern.compile("( at )", Pattern.CASE_INSENSITIVE);
	private static final String AT_REPLACEMENT = " / ";

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = AT.matcher(gStopName).replaceAll(AT_REPLACEMENT);
		gStopName = FIRST.matcher(gStopName).replaceAll(FIRST_REPLACEMENT);
		gStopName = SECOND.matcher(gStopName).replaceAll(SECOND_REPLACEMENT);
		gStopName = THIRD.matcher(gStopName).replaceAll(THIRD_REPLACEMENT);
		gStopName = FOURTH.matcher(gStopName).replaceAll(FOURTH_REPLACEMENT);
		gStopName = FIFTH.matcher(gStopName).replaceAll(FIFTH_REPLACEMENT);
		gStopName = SIXTH.matcher(gStopName).replaceAll(SIXTH_REPLACEMENT);
		gStopName = SEVENTH.matcher(gStopName).replaceAll(SEVENTH_REPLACEMENT);
		gStopName = EIGHTH.matcher(gStopName).replaceAll(EIGHTH_REPLACEMENT);
		gStopName = NINTH.matcher(gStopName).replaceAll(NINTH_REPLACEMENT);
		return MSpec.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(GStop gStop) {
		String stopId = gStop.stop_id;
		if (stopId != null && stopId.length() > 0 && Utils.isDigitsOnly(stopId)) {
			return Integer.valueOf(stopId);
		}
		try {
			Matcher matcher = DIGITS.matcher(stopId);
			matcher.find();
			return Integer.parseInt(matcher.group());
		} catch (Exception e) {
			System.out.println("Error while finding stop ID for " + gStop);
			e.printStackTrace();
			System.exit(-1);
			return -1;
		}
	}
}
