package com.example.game1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint.Join;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Fetcher {
	
	String baseUrl = "http://api.affil.walmart.com";	
	String vohUrl = "http://svpranay.github.io"; //baseUrl;// + "/voh";
	String vodUrl = "http://svpranay.github.io"; //baseUrl;// + "/vod";
	
	public void setItemForUI(View view, String type, SharedPreferences prefs) {
		RequestTask rt = new RequestTask();
		rt.setViewToUpdate(view, type, prefs);

		if (type == "vod") {
			rt.execute(vodUrl);
		} else {
			rt.execute(vohUrl);
		}
	}
	
	private void setItem(Item item, View rootView) {
		LinearLayout spinnerll = (LinearLayout) rootView.findViewById(R.id.progressBarLayout);
		spinnerll.setVisibility(View.GONE);

		LinearLayout mainll = (LinearLayout) rootView.findViewById(R.id.mainLayout);
		mainll.setVisibility(View.VISIBLE);
		
		TextView tv = (TextView) rootView.findViewById(R.id.price);
        tv.setText(item.price);
        tv = (TextView) rootView.findViewById(R.id.desc);
        tv.setText(item.desc);
        tv = (TextView) rootView.findViewById(R.id.title);
        tv.setText(item.title);
        
        ImageView iv = (ImageView) rootView.findViewById(R.id.productimage);
        ImageDownloader mDownload = new ImageDownloader();
        mDownload.download(item.image, iv);
	}

	class RequestTask extends AsyncTask<String, Void, String>{
		
		View view;
		String type;
		SharedPreferences prefs;
		
		public void setViewToUpdate(View view, String type, SharedPreferences prefs) {
			this.view = view;
			this.type = type;
			this.prefs = prefs;
		}

	    protected String doInBackground(String... uri) {
	    	StrictMode.ThreadPolicy policy = new StrictMode.
	    	          ThreadPolicy.Builder().permitAll().build();
	    	        StrictMode.setThreadPolicy(policy); 
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {	        	
	            HttpGet request = new HttpGet();
	            URI website = new URI(uri[0]);
	            request.setURI(website);
	            response = httpclient.execute(request);
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	            //TODO Handle problems..
	        	e.printStackTrace();
	        } catch (IOException e) {
	            //TODO Handle problems..
	        	e.printStackTrace();
	        } catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	        
	        return responseString;
	    }	    
	    
	    protected void onPostExecute(String response) {
	    	Item item = new Item();
	    	JSONObject jObject = null;
	    	try {
				jObject = new JSONObject(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	try {
	    		item.id = jObject.getString("itemId");
				item.desc = jObject.getString("shortDescription");
				item.price = Double.toString(jObject.getDouble("salePrice"));
				item.title = jObject.getString("name");
				item.image = jObject.getString("largeImage");				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Log.d("Fetcher", "Put the value in shared prefs");
	    	this.prefs.edit().putString("com.example.game1." + this.type + "ItemId", item.id).commit();	    	
	    	setItem(item, view);
	    }
	 }
	
}
