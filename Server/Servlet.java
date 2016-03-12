package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import org.json.*;

public class Servlet extends HttpServlet{
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	 {

		File map = new File("Map.json");
		
		String body  = "";
		try {
			String curr = request.getReader().readLine();
			while (curr != null){
				body = body+curr;
				curr = request.getReader().readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			JSONObject changedDesc = new JSONObject(body);
			request.getContentType();
			JSONObject currData = new JSONObject(readMap(map));

			String office = changedDesc.getString("Office");
			String id = changedDesc.getString("Id");
			String owner = changedDesc.getString("Owner");
			boolean free = changedDesc.getBoolean("Free");
			
			currData.getJSONObject(office).getJSONObject(id).put("Free", free);
			currData.getJSONObject(office).getJSONObject(id).put("Owner", owner);
				
			writeMap(map,currData.toString());
			

			response.getWriter().println(readMap(map));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException{
	
		File map = new File("Map.json");
		
		if(!map.exists()){
			
			map.createNewFile();
			
			writeMap(map, createDefault());
		}

	
		
		response.getWriter().println(readMap(map));
	}
	
	
	
	private String readMap = null;
	
	private String readMap(File map){
		
		if(readMap == null){
		
			String re = "";
			try{
				BufferedReader r  = new BufferedReader(new FileReader(map));
				String curr = r.readLine();
				while(curr != null){
					re = re+" "+curr;
					curr = r.readLine();
				}
				
				r.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			readMap = re;
		}
		//
		
		return readMap;
	}
	
	private void writeMap(File map, String create){
		try{
			BufferedWriter w  = new BufferedWriter(new FileWriter(map));
			w.write(create);
	        w.close();
	        readMap = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String createDefault(){
		
		JSONObject re = new JSONObject();
		
		try {
			JSONObject office_1 = new JSONObject();
			
			JSONObject desc1 = new JSONObject();
			desc1.put("Id", "0");
			desc1.put("Free", true);
			desc1.put("Owner", "-");
			office_1.put("0",desc1);
			
			JSONObject desc2 = new JSONObject();
			desc2.put("Id", "1");
			desc2.put("Free", true);
			desc2.put("Owner", "-");
			office_1.put("1",desc2);
			
			JSONObject desc3 = new JSONObject();
			desc3.put("Id", "2");
			desc3.put("Free", false);
			desc3.put("Owner", "Jack");
			office_1.put("2",desc3);
			
			re.put("Office_0", office_1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return re.toString();
	}
}