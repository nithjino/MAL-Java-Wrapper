package com.bunu.MAL;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author bunu
 *
 */
//https://myanimelist.net/malappinfo.php?u=bunu&status=6&type=anime

public class MAL_API {
	
	private String encodedCredentials;
	private String username;
	
	public MAL_API(String username, String password){
		this.encodedCredentials = encodeCredentials(username,password);
		this.username = username;
	}
	
	private String encodeCredentials(String username, String password){
		byte[] encodedCredentials = Base64.getEncoder().encode((username+":"+password).getBytes());
		
		return new String (encodedCredentials);
	}

	//1=watching, 2=completed, 3=onhold, 4=dropped, 6=plantowatch 
	public ArrayList<String> getList(int status) throws IOException, ParserConfigurationException, SAXException {
		String list_url = "https://myanimelist.net/malappinfo.php?u="+this.username+"&status=6&type=anime";
		ArrayList<String> shows = new ArrayList<String>();
		URL url = new URL(list_url);
		URLConnection conn = url.openConnection();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(conn.getInputStream()));
		doc.getDocumentElement().normalize();
		
		NodeList tempList = doc.getElementsByTagName("anime");
		for (int i = 0, size = tempList.getLength(); i < size; i++){
			if(((Element)tempList.item(i)).getElementsByTagName("my_status").item(0).getTextContent().equals(Integer.toString(status))) {
				shows.add(((Element)tempList.item(i)).getElementsByTagName("series_title").item(0).getTextContent());
			}
		}
		return shows;
		
	}
	
	public ArrayList<Node> getResults(String search) throws IOException, ParserConfigurationException, SAXException{
		search = search.replaceAll("\\s+", "_");
		String baseURL = "https://myanimelist.net/api/anime/search.xml?q=";
		ArrayList<Node> shows = new ArrayList<Node>();
		
		URL url = new URL(baseURL+search);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(conn.getInputStream()));
		doc.getDocumentElement().normalize();
		NodeList tempList = doc.getElementsByTagName("entry");
		for (int i = 0, size = tempList.getLength(); i < size; i++){
			shows.add(tempList.item(i));
		}
		return shows;
	}
	
	public Node getFirstResult(String search) throws IOException, ParserConfigurationException, SAXException{
		search = search.replaceAll("\\s+", "_");
		String baseURL = "https://myanimelist.net/api/anime/search.xml?q=";
		URL url = new URL(baseURL+search);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(conn.getInputStream()));
		doc.getDocumentElement().normalize();
		
		return (doc.getElementsByTagName("entry")).item(0);
	}
	
	public boolean authenticateLogin() throws ParserConfigurationException, SAXException, IOException{
		String baseURL = "https://myanimelist.net/api/anime/search.xml?q=";
		URL url = new URL(baseURL+"bleach");
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
		try{
			conn.getInputStream();
			return true;
		} catch(IOException e){
			return false;
		}
	}
	
	public String[] getInfo(Node show){
		String[] info = new String[11];
		info[0] = ((Element)show).getElementsByTagName("id").item(0).getTextContent();
		info[1] = ((Element)show).getElementsByTagName("title").item(0).getTextContent();
		info[2] = ((Element)show).getElementsByTagName("english").item(0).getTextContent();
		info[3] = ((Element)show).getElementsByTagName("synonyms").item(0).getTextContent();
		info[4] = ((Element)show).getElementsByTagName("episodes").item(0).getTextContent();
		info[5] = ((Element)show).getElementsByTagName("type").item(0).getTextContent();
		info[6] = ((Element)show).getElementsByTagName("status").item(0).getTextContent();
		info[7] = ((Element)show).getElementsByTagName("start_date").item(0).getTextContent();
		info[8] = ((Element)show).getElementsByTagName("end_date").item(0).getTextContent();
		info[9] = cleanSynopsis(((Element)show).getElementsByTagName("synopsis").item(0).getTextContent());
		info[10] = ((Element)show).getElementsByTagName("image").item(0).getTextContent();
		return info;
	}
	
	public String getTitle(Node show){
		return ((Element)show).getElementsByTagName("title").item(0).getTextContent();
	}
	

	public int getID(Node show){
		return Integer.parseInt(((Element)show).getElementsByTagName("id").item(0).getTextContent());
	}
	
	public String getEnglishTitle(Node show){
		return ((Element)show).getElementsByTagName("english").item(0).getTextContent();
	}
	
	public String getSynonyms(Node show){
		return ((Element)show).getElementsByTagName("synonyms").item(0).getTextContent();
	}
	
	public int getNumOfEpisodes(Node show){
		return Integer.parseInt(((Element)show).getElementsByTagName("episodes").item(0).getTextContent());
	}
	
	public String getType(Node show){
		return ((Element)show).getElementsByTagName("type").item(0).getTextContent();
	}
	
	public String getStatus(Node show){
		return ((Element)show).getElementsByTagName("status").item(0).getTextContent();
	}
	
	public String getStartDate(Node show){
		return ((Element)show).getElementsByTagName("start_date").item(0).getTextContent();
	}
	
	public String getEndDate(Node show){
		return ((Element)show).getElementsByTagName("end_date").item(0).getTextContent();
	}
	
	public String getSynopsis(Node show){
		return cleanSynopsis(((Element)show).getElementsByTagName("synopsis").item(0).getTextContent());
	}
	
	public String getImage(Node show){
		return ((Element)show).getElementsByTagName("image").item(0).getTextContent();
	}
	
	public String getAnimeLink(Node show){
		return "https://myanimelist.net/anime/"+getID(show)+"/"+(getTitle(show).replaceAll("\\s", "_"));
	}

	public int addAnime(int showID) throws ParserConfigurationException, IOException{
		String add_url = "https://myanimelist.net/api/animelist/add/"+Integer.toString(showID)+".xml?data=";
		String encoded_xml = URLEncoder.encode(XMLtoString(createAnimeXML(null)),"UTF-8");
		URL url = new URL(add_url+encoded_xml);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
        if(((HttpURLConnection)conn).getResponseCode() == 201 || ((HttpURLConnection)conn).getResponseCode() == 400 ){
        	System.out.println("The anime was added successfully or already existed in the list");
        	return 1;
        }
        System.out.println("There was an error when attempting to add the anime");
        return -1;
	}
	
	public int addAnime(int showID,String[] values) throws ParserConfigurationException, IOException{
		String add_url = "https://myanimelist.net/api/animelist/add/"+Integer.toString(showID)+".xml?data=";
		String encoded_xml = URLEncoder.encode(XMLtoString(createAnimeXML(values)),"UTF-8");
		URL url = new URL(add_url+encoded_xml);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
        if(((HttpURLConnection)conn).getResponseCode() == 201 || ((HttpURLConnection)conn).getResponseCode() == 400 ){
        	System.out.println("The anime was added successfully or already existed in the list");
        	return 1;
        }
        System.out.println("There was an error when attempting to add the anime");
        return -1;
	}
	
	public int updateAnime(int showID, String[] values) throws ParserConfigurationException{
		try{
			String update_url = "https://myanimelist.net/api/animelist/update/"+Integer.toString(showID)+".xml?data=";
			String encoded_xml = URLEncoder.encode(XMLtoString(createAnimeXML(values)),"UTF-8");
			URL url = new URL(update_url+encoded_xml);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
			conn.getInputStream();
			System.out.println("The anime has been updated");
			return 1;
		} catch(IOException e){
			return -1;
		}
	}
	
	public int deleteAnime(int showID) throws ParserConfigurationException{
		try{
			String delete_url = "https://myanimelist.net/api/animelist/delete/"+Integer.toString(showID)+".xml";
			URL url = new URL(delete_url);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization","Basic " + this.encodedCredentials);
			conn.getInputStream();
			System.out.println("The anime has been deleted");
			return 1;
		}catch(IOException e){
			return -1;
		}
	}

	private String XMLtoString(Document doc) {
	    try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.transform(new DOMSource(doc), new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
	
	private String cleanSynopsis(String synopsis){
		synopsis = synopsis.replace("<br />", "");
		synopsis = synopsis.replace("[i]", "");
		synopsis = synopsis.replace("[/i]", "");
		synopsis = synopsis.replace("&#039;", "'");
		synopsis = synopsis.replace("&mdash;", "---");
		synopsis = synopsis.replace("<&quot;", "\"");
		synopsis = synopsis.replace("<br />", "\"");
		synopsis = synopsis.replace("&eacute", "e");
		return synopsis;
	}

	//1=watching, 2=completed, 3=onhold, 4=dropped, 6=plantowatch 
	private Document createAnimeXML(String[] values) throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		String episodeNo,statusNo,scoreNo;
		if(values != null && values.length == 3){
			episodeNo = (values[0] == null || values[0].replace("\\s+","") == "") ? "1" : values[0];
			statusNo = (values[1] == null || values[1].replace("\\s+","") == "") ? "1" : values[1];
			scoreNo = (values[2] == null || values[2].replace("\\s+","") == "") ? "7" : values[2];
		}
		else{
			episodeNo = "1";
			statusNo = "1";
			scoreNo = "7";
		}
		//root element
		Document doc = builder.newDocument();
		doc.setXmlStandalone(true);
		Element root = doc.createElement("entry");
		doc.appendChild(root);
		
		//creating elements
		Element episode = doc.createElement("episode");
		episode.appendChild(doc.createTextNode(episodeNo));
		root.appendChild(episode);
		
		Element status = doc.createElement("status");
		status.appendChild(doc.createTextNode(statusNo));
		root.appendChild(status);
		
		Element score = doc.createElement("score");
		score.appendChild(doc.createTextNode(scoreNo));
		root.appendChild(score);
		
		Element storage_type = doc.createElement("storage_type");
		storage_type.appendChild(doc.createTextNode(""));
		root.appendChild(storage_type);
		
		Element storage_value = doc.createElement("storage_value");
		storage_value.appendChild(doc.createTextNode(""));
		root.appendChild(storage_value);
		
		Element times_rewatched = doc.createElement("times_rewatched");
		times_rewatched.appendChild(doc.createTextNode(""));
		root.appendChild(times_rewatched);
		
		Element date_start = doc.createElement("date_start");
		date_start.appendChild(doc.createTextNode(""));
		root.appendChild(date_start);
		
		Element date_end = doc.createElement("date_finished");
		date_end.appendChild(doc.createTextNode(""));
		root.appendChild(date_end);
		
		Element priority = doc.createElement("priority");
		priority.appendChild(doc.createTextNode(""));
		root.appendChild(priority);
		
		Element enable_discussion = doc.createElement("enable_discussion");
		enable_discussion.appendChild(doc.createTextNode(""));
		root.appendChild(enable_discussion);
		
		Element enable_rewatching = doc.createElement("enable_rewatching");
		enable_rewatching.appendChild(doc.createTextNode(""));
		root.appendChild(enable_rewatching);
		
		Element comments = doc.createElement("comments");
		comments.appendChild(doc.createTextNode(""));
		root.appendChild(comments);
		
		Element tags = doc.createElement("tags");
		tags.appendChild(doc.createTextNode(""));
		root.appendChild(tags);
		
		return doc;
		
	}

}
