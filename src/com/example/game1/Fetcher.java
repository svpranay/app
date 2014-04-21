package com.example.game1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Fetcher {
	String baseUrl = "http://api.affil.walmart.com";
	String vohUrl = baseUrl + "/voh";
	String vodUrl = baseUrl + "/vod";
	
	public void setItemForUI(View view, String type) {
		RequestTask rt = new RequestTask();
		rt.setViewToUpdate(view);

		if (type == "vod") {
			rt.execute(vodUrl);
		} else {
			rt.execute(vohUrl);
		}
	}
	
	private void setItem(Item item, View rootView) {
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

	public Item getValueOfDay() {
		Item item = new Item();
		item.image = "http://i.walmartimages.com/i/p/00/88/59/09/46/0088590946960_Color_Black_SW_500X500.jpg";
		item.title = "Apple iPad 2 16GB Wi-Fi ( Black or White )";
		item.price = "134.56$";
		item.desc = "The all-new thinner and lighter design makes the Apple iPad 2, 16GB with Wi-Fi even more comfortable to hold. It's even more powerful with the dual-core A5 chip, yet has the same 10 hours of battery life. With two cameras, you can make FaceTime video calls, record HD video, and put a twist on your snapshots in Photo Booth.";
		item.desc = item.desc.replace(".", ".\n");
		return item;
	}
	
	public Item getValueOfHour() {
		Item item = new Item();
		item.image = "http://i.walmartimages.com/i/p/11/13/04/98/62/1113049862667_500X500.jpg";
		item.title = "Premier Mounts LPTM5080 Tilt Wall Mount for 50 - 80 Displays BONUS 15' HDMI Cable, Bundle";
		item.price = "567.21$";
		item.desc = "The Premier Mounts Tilt Wall Mount (LPTM5080) fits most 50-to-80-inch flat-panel displays and can support up to 300 pounds. This low-profile tilt mount for 50&quot; - 80&quot; displays is designed to hold a display 2 inches from a wall. Featuring eight degrees of continuous tilt, this flat-panel tilt mount has a rigid steel construction and universal-mounting brackets. The open design of the Premier Mounts Tilt Wall Mount (LPTM5080) features cable and electrical access holes for easy use and organization in almost any space. Multiple-stud mounting points for easy mounting are provided on this flat-panel tilt wall mount, making it stable and easy to use. There is also built-in lateral shift for fine tuning to allow simple adjustments. The safety locking screws can also be used for fine tuning. This Premier Mounts Tilt Wall Mount (LPTM5080) also includes patent-pending universal spacers.";
		item.desc = item.desc.replace(".", ".\n");
		return item;		
	}
	
	class RequestTask extends AsyncTask<String, Void, String>{
		
		View view;
		
		public void setViewToUpdate(View view) {
			this.view = view; 
		}

	    protected String doInBackground(String... uri) {
	    	HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
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
	        } catch (IOException e) {
	            //TODO Handle problems..
	        }
	        System.out.println(responseString);
	        return responseString;
	    }	    
	    
	    protected void onPostExecute(String response) {
	    	Item item = getItemFromString(response);
	    	setItem(item, view);
	    }
	    
		public Item getItemFromString(String response) {
			return null;
		}
	}
	
}
