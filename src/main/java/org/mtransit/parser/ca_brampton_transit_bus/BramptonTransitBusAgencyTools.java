package org.mtransit.parser.ca_brampton_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://geohub.brampton.ca/pages/data/
// https://geohub.brampton.ca/pages/brampton-transit
// https://www.brampton.ca//EN/City-Hall/OpenGov/Open-Data-Catalogue/Pages/Data-Set-Details.aspx?ItemID=1
// https://www.brampton.ca/EN/City-Hall/OpenGov/Open-Data-Catalogue/Documents/Google_Transit.zip
public class BramptonTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-brampton-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new BramptonTransitBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating Brampton Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating Brampton Transit bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		final String tripHeadsign = gTrip.getTripHeadsignOrDefault().toLowerCase(Locale.ENGLISH);
		if (tripHeadsign.contains("not in service")) {
			return true;
		}
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
	}

	private static final String AGENCY_COLOR = "DB3935"; // RED (LOGO)

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor())) {
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
			case 27: return null; // TODO ?
			case 28: return null; // TODO ?
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
			case 81: return null; // TODO ?
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
				throw new MTLog.Fatal("Unexpected route color for '%s'", gRoute);
			}
		}
		return super.getRouteColor(gRoute);
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		mTrip.setHeadsignString(
				cleanTripHeadsign(gTrip.getTripHeadsignOrDefault()),
				gTrip.getDirectionIdOrDefault()
		);
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

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		throw new MTLog.Fatal("Unexpected trips to merge %s & %s!", mTrip, mTripToMerge);
	}

	private static final Pattern PARSE_HEAD_SIGN_ = Pattern.compile("(^\\d+([a-z]?)((\\s+)(\\w+))+((-| - | to )(.*))?$)", Pattern.CASE_INSENSITIVE);
	private static final String PARSE_HEAD_SIGN_KEEP_BOUND = "$5";

	@NotNull
	@Override
	public String cleanDirectionHeadsign(boolean fromStopName, @NotNull String directionHeadSign) {
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

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public int getStopId(@NotNull GStop gStop) {
		//noinspection deprecation
		String stopId = gStop.getStopId();
		if (stopId.length() > 0 && CharUtils.isDigitsOnly(stopId)) {
			return Integer.parseInt(stopId);
		}
		try {
			Matcher matcher = DIGITS.matcher(stopId);
			if (matcher.find()) {
				return Integer.parseInt(matcher.group());
			}
			throw new MTLog.Fatal("Unexpected stop ID  %s!", gStop);
		} catch (Exception e) {
			throw new MTLog.Fatal(e, "Error while finding stop ID for %s.", gStop);
		}
	}
}
