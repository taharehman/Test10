package com.example.callingwebservice;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class MainActivity extends Activity {

	//private TextView tv;
	//private TextView lv;
	private final String SOAP_URL = "http://www.vectrasoft.net/araa_news/AraaAndroid/server.php";
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_NAME = "title";
	static final String KEY_DESC = "description";
	static final String KEY_DUR = "datetime";
	static final String KEY_THUMB_URL = "imageurl";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        //tv = (TextView) this.findViewById(R.id.mywidget);  
        //tv.setSelected(true);  // Set focus to the textview
		new GettingXML().execute();
	}

	public String decodeMethodUtf(String text)
	{ 

	try {
		String str = new String(text.getBytes("ISO-8859-1"), "utf-8");
		 Log.e("S2",str);
		  return str;
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	return null;
	}
	

	
	public class GettingXML extends AsyncTask<String, Context, String> {

		private String SOAP_ACTION = "http://www.vectrasoft.net/araa_news/AraaAndroid/#getNews";
		private final String SOAP_NAMESPACE = "http://www.vectrasoft.net/araa_news/AraaAndroid/";
		private String SOAP_METHOD_NAME = "getNews";
		private SoapObject request;
		private String catid = "cat370", result = "";
		private SoapSerializationEnvelope envp;
		private ProgressDialog progressDialog;
		
		ListView list;
	    LazyAdapter adapter;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(MainActivity.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Getting News from Server...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.onSaveInstanceState();
	        progressDialog.show();

		}
		@Override
		protected String doInBackground(String... params) {
			request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME);
			request.addProperty("cat_id", catid);
			SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envp.dotNet = true;
			envp.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);

			 try {
				 androidHttpTransport.call(SOAP_ACTION, envp);
				 //result = (String)envp.getResponse();
				 result = envp.getResponse().toString();
				 }
			 	catch (SoapFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = "";
					/*
					 * if (progDailog.isShowing()) { progDailog.dismiss(); }
					 */
				}
			 	catch (Exception e) {
					Log.i("WS Error->",e.toString());
				 }
			Log.v("What", "What" + result);
			System.out.println(result);
			return result;
		}
		
		protected void onPostExecute(String result) {
			
			String KEY_ITEM = "item"; // parent node
			String KEY_NAME = "title";
			String KEY_DESC = "description";
			String KEY_DUR = "datetime";
			String KEY_THUMB_URL = "imageurl";
			
			Log.e("Instant", "Instant" + result);
			// ////////Log.d("Result", result.toString());
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}


			if (result.equalsIgnoreCase(null) || result.equalsIgnoreCase("")) {
				result = "Service Not Available!";
			} 
			else {
				ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
				
				ParsingXML parser = new ParsingXML();
				//String xml = parser.getXmlFromUrl(SOAP_URL); // getting XML
				Document doc = parser.getDomElement(result); // getting DOM element
				
				NodeList nl = doc.getElementsByTagName(KEY_ITEM);
				// looping through all item nodes <item>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_NAME, decodeMethodUtf(parser.getValue(e, KEY_NAME)));
					map.put(KEY_DESC, decodeMethodUtf(parser.getValue(e, KEY_DESC)));
					map.put(KEY_DUR, decodeMethodUtf(parser.getValue(e, KEY_DUR)));
					map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
					// adding HashList to ArrayList
					menuItems.add(map);
				}
				
				// Adding menuItems to ListView
				//ListAdapter adapter = new SimpleAdapter(MainActivity.this, menuItems, R.layout.list_item, new String[] { KEY_NAME, KEY_DESC }, new int[] {
								//R.id.name, R.id.desciption});

				//setListAdapter(adapter);
				
				//ListView lv = getListView();
			
						        list=(ListView) findViewById(R.id.list1);
						        
						        //Getting adapter by passing xml data ArrayList
						        adapter=new LazyAdapter(MainActivity.this, menuItems);
						        list.setAdapter(adapter);
						        
						      list.setOnItemClickListener(new OnItemClickListener() {
			          public void onItemClick(AdapterView<?> parent, View view,
			              int position, long id) {
			               
			              // selected item
			        	  TextView txtview = (TextView)view.findViewById(R.id.artist);
			              String newsdescription = txtview.getText().toString();
			              //String newsdescription = ((TextView) view).getText().toString();
			               
			              // Launching new Activity on selecting single List Item
			              Intent i = new Intent(getApplicationContext(), SingleListItem.class);
			           
						// sending data to new activity
			              i.putExtra("newsdescription", newsdescription );
			              startActivity(i);
			             
			          }
			    });
			}
		}
		
	}

}
