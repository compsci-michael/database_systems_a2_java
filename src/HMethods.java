import java.math.BigInteger;

///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This Class is used to hold useful functionalities that can be called upon
// in the main method
///////////////////////////////////////////////////////////////////////////////

public class HMethods {
	// -------------------------- Final Constants -------------------------- //
		// --------------- Input Validation Flag Checks ---------------- //
	public static final int PAGE_SIZE_ARGUEMENT = 0;
	public static final int HEAP_FILE_ARGUEMENT = 1;
	
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
	
		// -------------------- Hashing Properties --------------------- //
	private static final int PRIME_ONE = 438439;
	private static final int PRIME_TWO = 34723753;
	private static final int PRIME_THREE = 376307;
		// -------------- Suitable Size for 70% Occupancy -------------- //
	private static final int HASH_TABLE_SIZE = 200000;
	
	
	// Method to return Hashvalue of Record
	public int record_to_hash(String hash, int census_year) {
		//int key = string_to_hash(hash);
		int key = string_to_hash(hash);
		// Lecture 6 for Source of Hashing Function
		int result = (((PRIME_ONE*key + PRIME_TWO) % PRIME_THREE) + census_year) % HASH_TABLE_SIZE;
		// Get Correct Positive Value 
		return (result < 0) ? result*-1 : result;
	}
	
	// Sourced and Adapated from: 
	// https://stackoverflow.com/questions/37580741/seemingly-easy-fnv1-hashing
	//-implementation-results-in-a-lot-of-collisions
	public int string_to_hash(String s) {
        final BigInteger FNV_offset_basis = new BigInteger("14695981039346656037");
        final BigInteger FNV_prime = new BigInteger("1099511628211");

        BigInteger hash = new BigInteger(FNV_offset_basis.toString());

        for (int i = 0; i < s.length(); i++) {
            int charValue = s.charAt(i);

            hash = hash.multiply(FNV_prime);
            hash = hash.xor(BigInteger.valueOf((int) charValue & 0xffff));
        }

        return hash.intValue();
    }
	
	/*
	// adapted from String.hashCode()
	public long string_to_hash(String string) {
	  long h = 1125899906842597L; // prime
	  int len = string.length();

	  for (int i = 0; i < len; i++) {
	    h = 31*h + string.charAt(i);
	  }
	  return h;
	}
	*/
	/*
	public int string_to_hash(String hash) {
		int result = 0;
		for(int i=0; i<hash.length(); i++) {
			result+=(int)hash.charAt(i);
		}
		return result;
	}
	*/
	
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
		      if(!args[HEAP_FILE_ARGUEMENT].equals("heap."+args[PAGE_SIZE_ARGUEMENT].trim())) {
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
	// Method to extract the Heap File name
	public String heap_file(String[] args) {
		return args[HEAP_FILE_ARGUEMENT];
	}
	
	/*
	// Method to Print out Contents of HashMap
	public void print_hash_map(HashMap<String, Record> data) {
		for (String key : data.keySet()) {
			data.get(key).record_display();
		}
	}
	*/
	
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
	
	// Method to Return a String from a Byte Buffer
	public String byte_buffer_to_string(byte[] data, int byte_offset, int req_offset) {
		String value = "";
		for(int i=byte_offset; i<byte_offset+req_offset;i++) {
			// Don't append '#' to the String
			if((char)data[i] == '#') {
				break;
			} else {
				value+=(char)data[i];
			}
		}
		return value;	
	}
}