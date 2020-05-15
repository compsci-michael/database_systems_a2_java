import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

///////////////////////////////////////////////////////////////////////////////
// File Written by: Michael A (s3662507) (Last Edit: 13/05/2020)
// Database Systems - Assignment 02
// Purpose of this Class:
// This is the Driver Class which contains the Main method for loading the
// java heap file and creating a Hashfile
///////////////////////////////////////////////////////////////////////////////
public class hashquery {
	// Must be able to execute the following: java dbload -p pagesize datafile
	public static void main(String[] args) {
		final long full_start_time = System.nanoTime();
		// Set up Helper Methods
		HMethods hm = new HMethods();
		// Store the Page Size
		int page_size;

		// Step 1: Validate the Input
		boolean correct_input = hm.input_validation_hashquery(args);
		if(correct_input) {
			// Stores the Page Size
			page_size = hm.page_size(args);
			// Load from the filename "heap.<page_size>"
			String heap_file_name = "heap."+page_size;
			
			final long full_end_time = System.nanoTime();
			
			// Required Outputs				

			System.out.println("System - Time Taken to Execute Script: "+
			(float)(full_end_time-full_start_time)/1000000000+" seconds");
		}
	}
}
