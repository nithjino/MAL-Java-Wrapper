package com.bunu.MAL;
/*
import com.bunu.MAL.MAL_API;

this import would be here but this was in the same package as
the MAL_API class.
*/
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Sample {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
		//you enter your credentials of your MAL account when creating the object
		MAL_API api = new MAL_API("username","password");
		
		//getFirstResult will grab the first result of the search
		Node result = api.getFirstResult("naruto");
		int showID = api.getID(result);
		
		//adds the show to your Anime List with the default values
		api.addAnime(showID);
		
		//first value is episode number, second value is status, third value is score
		//1=watching, 2=completed, 3=onhold, 4=dropped, 6=plantowatch 
		String[] updatedValues = {"100","3","6"};
		
		//updates the anime of the show with the given values
		api.updateAnime(showID, updatedValues);
		
		//getResults will get all the results from the search
		ArrayList<Node> test = api.getResults("naruto");
		for(Node show: test){
			//this loop will print out the title and ID of each show that was from the search
			System.out.println(api.getTitle(show) + ": " + api.getID(show));
		}
		
		
		
		
	}

}
