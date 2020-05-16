///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 16/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This Class is used to hold useful functionalities that can be called upon
// in the main method
///////////////////////////////////////////////////////////////////////////////
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class HMethods {
	// -------------------------- Final Constants -------------------------- //
	public static final int INT_SIZE = 4;
	public static final int DOUBLE_SIZE = 8;
	public static final int BUILDING_NAME_SIZE = 63;
	public static final int STREET_ADDRESS_SIZE = 34;
	public static final int SUBURB_SIZE = 28;
	public static final int SPACE_USAGE_SIZE = 39;
	public static final int ACCESS_TYPE_SIZE = 32;
	public static final int ACCESS_DESC_SIZE = 81;
	public static final int LOCATION_SIZE = 27;
	public static final int PTR_TO_STREET_ADDRESS = (4*INT_SIZE)+BUILDING_NAME_SIZE;
	public static final int RECORD_SIZE = 
			10*INT_SIZE+2*DOUBLE_SIZE
			+BUILDING_NAME_SIZE
			+STREET_ADDRESS_SIZE
			+SUBURB_SIZE
			+SPACE_USAGE_SIZE
			+ACCESS_TYPE_SIZE
			+ACCESS_DESC_SIZE
			+LOCATION_SIZE;
		// --------------- Input Validation Flag Checks ---------------- //
	public static final int PAGE_SIZE_ARGUEMENT_LOAD = 0;
	public static final int STATISTICS_ARGUEMENT = 1;
	
	public static final int CENSUS_YEAR_ARGUEMENT_QUERY = 0;
	public static final int STREET_ADDRESS_ARGUEMENT_QUERY = 1;
	public static final int PAGE_SIZE_ARGUEMENT_QUERY = 2;

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
	public static final int HASH_TABLE_SIZE = 200000;
	public static final int BUCKET_SIZE_USED = 2;
	
	// Method to Validate the Input Arguements for hashload
	public boolean input_validation_hashload(String[] args) {
		boolean is_correct = false;
		boolean override = false;
		// Step 1: Check if number of Arguements is Correct
		if(args.length < 1 || args.length > 2) {
			System.err.println("Error - Incorrect Number of Arguements!");
			System.err.println("Format must be of the following:\njava hashload <pagesize>\nor");
			System.err.println("java hashload <pagesize> true (if statistics is required)");
		} else {
			// Step 2: Validate Input
			// Check if the pagesize was a number
			try {
				Integer.parseInt(args[PAGE_SIZE_ARGUEMENT_LOAD].trim());
		      
				// if Statics was Enabled, Check for "true" case
				if(args.length == 2) {
					if(!args[STATISTICS_ARGUEMENT].equals("true")) {
						System.err.println("Error - To Enable Statistics, enter true after <pagesize>!");
						System.err.println("java hashload <pagesize> true");
						override = true;
					}
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
	
	// Method to Validate the Input Arguements for hashquery
	public boolean input_validation_hashquery(String[] args) {
		boolean is_correct = false;
		boolean override = false;
		// Step 1: Check if number of Arguements is Correct
		if(args.length < 3 || args.length > 3) {
			System.err.println("Error - Incorrect Number of Arguements!");
			System.err.println("Format must be of the following:\njava hashquery <census year> <street address> <pagesize>");
		} else {
			// Step 2: Validate Input
			try {
				// Check that the pagesize arguement is a number
				Integer.parseInt(args[PAGE_SIZE_ARGUEMENT_QUERY].trim());
				// Check the the census year arguement is a Number 
				Integer.parseInt(args[CENSUS_YEAR_ARGUEMENT_QUERY].trim());
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
	public int page_size_load(String[] args) {
		return Integer.parseInt(args[PAGE_SIZE_ARGUEMENT_LOAD].trim());
	}
	// Method to Return True if Valid Input for Statistics
	public boolean does_require_statistics(String[] args) {
		return args.length == 2 ? true : false;
	}
	
	// Method to extract the Page Size
	public int page_size_query(String[] args) {
		return Integer.parseInt(args[PAGE_SIZE_ARGUEMENT_QUERY].trim());
	}
	// Method to extract the Census Year
	public int census_year_query(String[] args) {
		return Integer.parseInt(args[CENSUS_YEAR_ARGUEMENT_QUERY].trim());
	}
	// Method to extract the Street Address
	public String street_address_query(String[] args) {
		return args[STREET_ADDRESS_ARGUEMENT_QUERY];
	}
	
	
	// Method to return Hashvalue of Record
	public int record_to_hash(String street_address, int census_year) {
		// Turns Street Address to its equivalent hashCode (32 byte)
		int key = street_address.toLowerCase().hashCode();
		// Lecture 6 for Source of Hashing Function
		int result = (((PRIME_ONE*key + PRIME_TWO) % PRIME_THREE) + census_year) % HASH_TABLE_SIZE;
		// Get Correct Positive Value 
		return (result < 0) ? result*-1 : result;
	}
	
	// Method to Count the Total in the Duplicates Counted Column
	public int count_total_duplicate_keys(Map<Integer, Integer> dup_map) {
		 int count_total = 0;
        // Prepare Writing to File
		final long full_start_time = System.nanoTime();
		
    	// Go through the Duplicate Map
    	for(Map.Entry<Integer, Integer> entry : dup_map.entrySet()) {
			// Ensure Value > Bucket Size
			int entry_value = entry.getValue();
			entry_value = (entry_value > BUCKET_SIZE_USED) ? entry_value-BUCKET_SIZE_USED : 0; 
			// if Value is not 0, it was > BUCKET_SIZE_USED so Write
			if(entry_value != 0) {
				// This would lead to a Collision
				count_total+=entry_value;
			}
        }
	   
		final long full_end_time = System.nanoTime();
		// Required Outputs
		System.out.println("System - Time Taken to Calculate Initial Collisions: "+
				(float)(full_end_time-full_start_time)/1000000000+" seconds");
		
		return count_total;
	}
	
	// Method to Write a key, count pair of Duplicate Keys and their Counts
	public int write_duplicate_map(Map<Integer, Integer> dup_map) {
        int total_lines = 0;
        // Prepare Writing to File
		final long full_start_time = System.nanoTime();
		PrintWriter output = null;
        try {
        	// Setup the Output Stream
        	output = new PrintWriter(new FileWriter("log_count_of_duplicates.csv"));
        	// Go through the Duplicate Map
        	for(Map.Entry<Integer, Integer> entry : dup_map.entrySet()) {
    			// Ensure Value > Bucket Size
    			int entry_value = entry.getValue();
    			entry_value = (entry_value > BUCKET_SIZE_USED) ? entry_value-BUCKET_SIZE_USED : 0; 
    			// if Value is not 0, it was > BUCKET_SIZE_USED so Write
    			if(entry_value != 0) {
    				output.write(entry.getKey()+","+entry_value+"\n");
    				total_lines++;
    			}
            }
        	
    		final long full_end_time = System.nanoTime();

        	// Required Outputs
    		System.out.println("System - Time Taken to Write Duplicate Map to File: "+
    				(float)(full_end_time-full_start_time)/1000000000+" seconds");
    		
	    } catch (IOException e) {
	    	System.err.println("Error - Couldn't Write to log_count_of_duplicates.csv!");
	    } finally {
			// Closing output stream
			if (output != null) {
				output.close();
			}
	    }
				
		return total_lines;
    }
	
	// Method to strictly Write the Available Keys and Used Keys
	public int write_initial_unique_and_available_set(SortedSet<Integer> set) {
        // Prepare Writing to File
		final long full_start_time = System.nanoTime();
		PrintWriter output_uniq = null;
		PrintWriter output_avail = null;
		
        try {
        	// Setup the Output Streams
        	output_uniq = new PrintWriter(new FileWriter("log_initial_unique_keys.txt"));
        	output_avail = new PrintWriter(new FileWriter("log_initial_available_keys.txt"));
        	
        	// Go through the Set and Output to Write Streams
        	for(int i=0; i<HASH_TABLE_SIZE; i++) {
    			// if Unique Write to fn1
    			if(set.contains(i)) {
    				output_uniq.write(i+"\n");
    			}
    			// if Available Write to fn2 
    			else {
    				output_avail.write(i+"\n");
    			}
    		}
        	final long full_end_time = System.nanoTime();
        	
			// Required Outputs
			System.out.println("System - Time Taken to Write Unique and Available Files: "+
					(float)(full_end_time-full_start_time)/1000000000+" seconds");
			
	    } catch (IOException e) {
	    	System.err.println("Error - Couldn't Write to Unique and Available Files!");
	    } finally {
			// Closing output streams
			if (output_uniq != null) {
				output_uniq.close();
			}
			if (output_avail != null) {
				output_avail.close();
			}
	    }
        return set.size();
    }
	
	// Method to Write the Hash File Data to a Hash File 
	public void write_hash_file(Map<Integer, List<String>> hash_file_data, int page_size) {
        // Prepare Writing to File
		final long full_start_time = System.nanoTime();
		PrintWriter hash_file = null;
		
        try {
        	// Setup the Output Streams
        	hash_file = new PrintWriter(new FileWriter("hash."+page_size));
        	
        	// Go through the Hash Data and Output to Write Streams
        	for(int i=0; i<HASH_TABLE_SIZE; i++) {
    			// if the Hash File has the Key, Write out its key, file_offset
    			if(hash_file_data.containsKey(i)) {
    				List<String> hash_and_file_offset_pointers = hash_file_data.get(i);
    				int file_offset_size = hash_and_file_offset_pointers.size(); 
    				for(int j=0; j<file_offset_size; j++) {
    					hash_file.write(hash_and_file_offset_pointers.get(j)+"\n");	
    				}
    				
    				if(file_offset_size != BUCKET_SIZE_USED) {
    					int counter = file_offset_size;
    					while(counter < BUCKET_SIZE_USED) {
        					hash_file.write("\n");
        					counter++;
    					}
    				}
    			}
    			// if the Index didn't exist i.e., Vacant Slot, Write out 2 New Lines
    			else {
    				int counter = 0;
    				while(counter < BUCKET_SIZE_USED) {
    					hash_file.write("\n");
    					counter++;
					}
    			}
    		}
        	final long full_end_time = System.nanoTime();
        	
			// Required Outputs
			System.out.println("System - Time Taken to Write Hash File: "+
					(float)(full_end_time-full_start_time)/1000000000+" seconds");
			
	    } catch (IOException e) {
	    	System.err.println("Error - Couldn't Write to Hash File hash."+page_size+"!");
	    } finally {
			// Closing output streams
			if (hash_file != null) {
				hash_file.close();
			}
	    }
    }
	
	// Method to take in a Read Record Data (rr) to Create and Return the Record
	public Record hash_query_read_record(byte[] rr) {
		// Holds the Queried Record (qr)
		Record qr = new Record();
		// A Counter for the Buffer of Read Record
		int c = 0;
		
		qr.set_census_yr(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_block_id(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_prop_id(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_base_prop_id(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_building_name(byte_buffer_to_string(rr, c, BUILDING_NAME_SIZE));
		c+=BUILDING_NAME_SIZE;
		qr.set_street_address(byte_buffer_to_string(rr, c, STREET_ADDRESS_SIZE));
		c+=STREET_ADDRESS_SIZE;
		qr.set_suburb(byte_buffer_to_string(rr, c, SUBURB_SIZE));
		c+=SUBURB_SIZE;
		qr.set_construct_yr(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_refurbished_yr(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_num_floors(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_space_usage(byte_buffer_to_string(rr, c, SPACE_USAGE_SIZE));
		c+=SPACE_USAGE_SIZE;
		qr.set_access_type(byte_buffer_to_string(rr, c, ACCESS_TYPE_SIZE));
		c+=ACCESS_TYPE_SIZE;
		qr.set_access_desc(byte_buffer_to_string(rr, c, ACCESS_DESC_SIZE));
		c+=ACCESS_DESC_SIZE;
		qr.set_access_rating(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_bicycle_spaces(ByteBuffer.wrap(rr, c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_has_showers(ByteBuffer.wrap(rr,c, INT_SIZE).getInt());
		c+=INT_SIZE;
		qr.set_x_coor(ByteBuffer.wrap(rr, c, DOUBLE_SIZE).getDouble());
		c+=DOUBLE_SIZE;
		qr.set_y_coor(ByteBuffer.wrap(rr, c, DOUBLE_SIZE).getDouble());
		c+=DOUBLE_SIZE;
		qr.set_location(byte_buffer_to_string(rr, c, LOCATION_SIZE));
		c+=LOCATION_SIZE;
		
		// Returns the Queried Record
		return qr;
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