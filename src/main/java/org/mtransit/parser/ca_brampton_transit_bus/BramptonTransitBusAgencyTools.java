package org.mtransit.parser.ca_brampton_transit_bus;

import static org.mtransit.commons.RegexUtils.DIGITS;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://geohub.brampton.ca/pages/brampton-transit
// https://brampton.maps.arcgis.com/home/item.html?id=a355aabd5a8c490186bdce559c9c75fb
// https://www.arcgis.com/sharing/rest/content/items/a355aabd5a8c490186bdce559c9c75fb/data
// OLD: https://www.brampton.ca/EN/City-Hall/OpenGov/Open-Data-Catalogue/Documents/Google_Transit.zip
// https://transitfeeds.com/p/brampton-transit/
// https://www.transit.land/feeds/f-dpz2-bramptontransit
public class BramptonTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new BramptonTransitBusAgencyTools().start(args);
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Brampton Transit";
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		final String tripHeadsign = gTrip.getTripHeadsignOrDefault().toLowerCase(Locale.ENGLISH);
		if (tripHeadsign.contains("not in service")) {
			return true;
		}
		return super.excludeTrip(gTrip);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public boolean defaultRouteIdEnabled() {
		return true;
	}

	@Override
	public boolean useRouteShortNameForRouteId() {
		return true;
	}

	@Override
	public boolean defaultAgencyColorEnabled() {
		return true;
	}

	private static final String AGENCY_COLOR = "DB3935"; // RED (LOGO)

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	// https://www.brampton.ca/EN/residents/transit/plan-your-trip/Pages/Schedules-and-Maps.aspx
	@Override
	public @Nullable String provideMissingRouteColor(@NotNull GRoute gRoute) {
		final int rsn = Integer.parseInt(gRoute.getRouteShortName());
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
		case 27: return "C8158C";
		case 28: return "ED5C22";
		case 29: return "E4A024";
		case 30: return "41827C";
		case 31: return "F48473";
		case 32: return "942976";
		case 33: return "32327B";
		case 35: return "F7931D";
		case 36: return "2E917D";
		case 40: return "56C5D0";
		case 41: return "B77832";
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
		case 81: return "BA7832";
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
		case 218: return null; // TODO?
		case 501: return "EC2027";
		case 502: return "EC2027";
		case 505: return "EC2027";
		case 511: return "EC2027";
		case 561: return "EC2027";
		// @formatter:on
		default:
			throw new MTLog.Fatal("Unexpected route color for '%s'", gRoute.toStringPlus());
		}
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	@NotNull
	@Override
	public List<Integer> getDirectionTypes() {
		return Arrays.asList(
				MTrip.HEADSIGN_TYPE_DIRECTION,
				MTrip.HEADSIGN_TYPE_STRING
		);
	}

	private static final Pattern PARSE_HEAD_SIGN_ = Pattern.compile("(^\\d+([a-z]?)((\\s+)(\\w+))+((-| - | to )(.*))?$)", Pattern.CASE_INSENSITIVE);
	private static final String PARSE_HEAD_SIGN_KEEP_BOUND = "$5";

	@NotNull
	@Override
	public String cleanDirectionHeadsign(int directionId, boolean fromStopName, @NotNull String directionHeadSign) {
		directionHeadSign = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, directionHeadSign, getIgnoreWords());
		//noinspection deprecation
		directionHeadSign = CleanUtils.removePoints(directionHeadSign); // before parse
		directionHeadSign = PARSE_HEAD_SIGN_.matcher(directionHeadSign).replaceAll(PARSE_HEAD_SIGN_KEEP_BOUND);
		return CleanUtils.cleanLabel(directionHeadSign);
	}

	private static final String PARSE_HEAD_SIGN_KEEP_LETTER_AND_DISTINCT = "$2 $8";

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, tripHeadsign, getIgnoreWords());
		//noinspection deprecation
		tripHeadsign = CleanUtils.removePoints(tripHeadsign); // before parse
		tripHeadsign = PARSE_HEAD_SIGN_.matcher(tripHeadsign).replaceAll(PARSE_HEAD_SIGN_KEEP_LETTER_AND_DISTINCT);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@NotNull
	private String[] getIgnoreWords() {
		return new String[]{
				"EB", "WB", "NB", "SB",
				"AM", "PM",
				"BCC", "CAA", "CC", "ETR", "FCCC", "GO", "GSK", "PS", "ROP", "RR", "RRX", "VMC"
		};
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, gStopName, getIgnoreWords());
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection deprecation
		final String stopId = gStop.getStopId();
		if (!stopId.isEmpty() && CharUtils.isDigitsOnly(stopId)) {
			return Integer.parseInt(stopId);
		}
		try {
			final Matcher matcher = DIGITS.matcher(stopId);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group());
			}
			throw new MTLog.Fatal("Unexpected stop ID  %s!", gStop);
		} catch (Exception e) {
			throw new MTLog.Fatal(e, "Error while finding stop ID for %s.", gStop);
		}
	}
}
