package fatbastard.ui.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fatbastard.ui.utils.Utils;

public class TaskReader {
	
	DefaultHandler handler;
	SAXParserFactory factory;
	SAXParser saxParser;
	ArrayList<Task> taskList = new ArrayList<Task>();
	Task task;

	public TaskReader() throws ParserConfigurationException, SAXException {
		
		factory = SAXParserFactory.newInstance();
		saxParser = factory.newSAXParser();
		handler = new DefaultHandler() {
			boolean taskStart = false;
			boolean textStart = false;
			boolean recoStart = false;
			
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				 
				if (qName.equalsIgnoreCase("task")) {
					taskStart = true;
				}
		 
				if (qName.equalsIgnoreCase("text")) {
					textStart = true;
				}
		 
				if (qName.equalsIgnoreCase("recommendation")) {
					recoStart = true;
				}
		 
				super.startElement(uri, localName, qName, attributes);
			}
			
			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if (qName.equalsIgnoreCase("task")) {
					taskList.add(task);
				}
				
				super.endElement(uri, localName, qName);
			}
			
			@Override
			public void characters(char[] ch, int start, int length)
					throws SAXException {
				
				if (taskStart){
					task = new Task();
					taskStart = false;
				}
				
				if (textStart){
					String text = new String(ch, start, length);
					task.setTaskDetails(text);
					textStart = false;
				}
		 
				if (recoStart) {
					String reco = new String(ch, start, length);
					task.addRecommendation(reco);
					recoStart = false;
				}
		 
				super.characters(ch, start, length);
			}
		};
	}
	
	public ArrayList<Task> getTaskList() throws SAXException, IOException, URISyntaxException {
		saxParser.parse(getFilename(), handler);
		return taskList;
	}

	private String getFilename() throws URISyntaxException, IOException {
		return Utils.getResourceFolder() + File.separator + "tasks.xml"; 
	}
	
	
	
}
