///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This Class is used to hold useful functionalities that can be called upon
// in the main method
///////////////////////////////////////////////////////////////////////////////
import java.util.HashMap;

public class HMethods {
	// -------------------------- Final Constants -------------------------- //
		// --------------- Input Validation Flag Checks ---------------- //
	public static final int PAGE_SIZE_ARGUEMENT = 0;
	public static final int HEAP_FILE_ARGUMENT = 1;
	
		// ------------------- Fields in Flat File --------------------- //
	public static final int CENSUS_YR = 0;
	public static final int BLOCK_ID = 1;
	public static final int PROP_ID = 2;
	public static final int BASE_PROP_ID = 3;
	public static final int BUILDING_NAME = 4;
	public static final int STREET_ADDRESS = 5;
	public static final int SUBURB= 6;
	public static final int CONSTRUCT_YR = 7;
	public static final int REFURBISHED_YR = 8;
	public static final int NUM_FLOORS = 9;
	public static final int SPACE_USAGE = 10;
	public static final int ACCESS_TYPE = 11;
	public static final int ACCESS_DESC = 12;
	public static final int ACCESS_RATING = 13;
	public static final int BICYCLE_SPACES = 14;
	public static final int HAS_SHOWERS = 15;
	public static final int X_COOR = 16;
	public static final int Y_COOR = 17;
	public static final int LOCATION = 18;
	
	// ---------------------- Hashing Properties ----------------------- //
	private static final int PRIME_ONE = 438439;
	private static final int PRIME_TWO = 34723753;
	private static final int PRIME_THREE = 376307;
	
	// Method to Validate the Input Arguements for hashload
	public boolean input_validation_hashload(String[] args) {
		boolean is_correct = false;
		boolean override = false;
		// Step 1: Check if number of Arguements is Correct
		if(args.length < 2 || args.length > 2) {
			System.err.println("Error - Not Enough Arguements!");
			System.err.println("Format must be of the following: <pagesize>\n");
		} else {
			// Step 2: Validate Input
			// Check if the pagesize was a number
			try {
		      Integer.parseInt(args[PAGE_SIZE_ARGUEMENT].trim());
		      
		      // Step 3: check that the Heap File has the same Page Size (.4096)
		      if(!args[HEAP_FILE_ARGUMENT].equals("heap."+args[PAGE_SIZE_ARGUEMENT].trim())) {
		    	  System.err.println("Error - The Page Sizes Do Not Match!");
		    	  override = true;
		      }
		    } catch (NumberFormatException nfe) {
		      System.err.println("Error - That wasn't a Number!");
		      override = true;
		    }
			if(!override) {
				is_correct = true;
			} else {
				System.err.println("Please try again!\n");
			}
		}
		// Step 3: Return Value
		return is_correct;	
	}
	
	
	// Method to extract the Page Size
	public int page_size(String[] args) {
		return Integer.parseInt(args[PAGE_SIZE_ARGUEMENT].trim());
	}
	
	// Method to Print out Contents of HashMap
	public void print_hash_map(HashMap<String, Record> data) {
		for (String key : data.keySet()) {
			data.get(key).record_display();
		}
	}
	
	// Method to Fill a Character Array full of # and Insert the String
	public String char_fill(String s, int size) {
		StringBuilder result = new StringBuilder(s); 
		// Append "#" n times
		for(int i=result.length(); i<size; i++){
			result.append("#");
		}
		// Return a String
		return result.toString();
	}
	
	// Method to Create a Record
	public Record create_record(String[] data, int line_number) {
		Record new_record = new Record();
		boolean record_failed = false;
		try {
			// Do NULL Checks are Replace them with Integer.MAX_VALUE
			if(data[CONSTRUCT_YR].equals("")) data[CONSTRUCT_YR] = Integer.toString(Integer.MAX_VALUE);
			if(data[REFURBISHED_YR].equals("")) data[REFURBISHED_YR] = Integer.toString(Integer.MAX_VALUE);
			if(data[ACCESS_RATING].equals(""))data[ACCESS_RATING] = Integer.toString(Integer.MAX_VALUE);
			if(data[BICYCLE_SPACES].equals("")) data[BICYCLE_SPACES] = Integer.toString(Integer.MAX_VALUE);
			if(data[HAS_SHOWERS].equals("")) data[HAS_SHOWERS] = Integer.toString(Integer.MAX_VALUE);
			if(data[X_COOR].equals("")) data[X_COOR] = Integer.toString(Integer.MAX_VALUE);
			if(data[Y_COOR].equals("")) data[Y_COOR] = Integer.toString(Integer.MAX_VALUE);

			new_record.set_census_yr(Integer.parseInt(data[CENSUS_YR].trim()));
			new_record.set_block_id(Integer.parseInt(data[BLOCK_ID].trim()));
			new_record.set_prop_id(Integer.parseInt(data[PROP_ID].trim()));
			new_record.set_base_prop_id(Integer.parseInt(data[BASE_PROP_ID].trim()));
			new_record.set_construct_yr(Integer.parseInt(data[CONSTRUCT_YR].trim()));
			new_record.set_refurbished_yr(Integer.parseInt(data[REFURBISHED_YR].trim()));
			new_record.set_num_floors(Integer.parseInt(data[NUM_FLOORS].trim()));
			new_record.set_access_rating(Integer.parseInt(data[ACCESS_RATING].trim()));
			new_record.set_bicycle_spaces(Integer.parseInt(data[BICYCLE_SPACES].trim()));
			new_record.set_has_showers(Integer.parseInt(data[HAS_SHOWERS].trim()));
			new_record.set_x_coor(Float.parseFloat(data[X_COOR].trim()));
			new_record.set_y_coor(Float.parseFloat(data[Y_COOR].trim()));
		} catch(NumberFormatException nfe) {
			System.err.println("Error - Record Discarded "
		      		+ "- Record Contained Non-Numerical Value at Line: "+line_number+"! ");
			record_failed = true;
		}
		if(record_failed) {
			return null;
		} else {
			new_record.set_building_name(data[BUILDING_NAME]);
			new_record.set_street_address(data[STREET_ADDRESS]);
			new_record.set_suburb(data[SUBURB]);
			new_record.set_space_usage(data[SPACE_USAGE]);
			new_record.set_access_type(data[ACCESS_TYPE]);
			new_record.set_access_desc(data[ACCESS_DESC]);
			new_record.set_location(data[LOCATION]);
			return new_record;
		}
	}
	
	// Method to Return a String from a Byte Buffer
	public String byte_buffer_to_string(byte[] data, int byte_offset, int req_offset) {
		String value = "";
		for(int i=byte_offset; i<byte_offset+req_offset;i++) {
			value+=(char)data[i];
		}
		return value;	
	}
}