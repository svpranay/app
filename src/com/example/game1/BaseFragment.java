package com.example.game1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseFragment extends Fragment {

	public String type;
	Fetcher fetcher = new Fetcher();
	
	public Item getItemForType() {
		if (type == "vod") {
			return fetcher.getValueOfDay();
		} else {
			return fetcher.getValueOfHour();
		}
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {    	
    	Item item = getItemForType();
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        
        
        TextView tv = (TextView) rootView.findViewById(R.id.price);
        tv.setText(item.price);
        tv = (TextView) rootView.findViewById(R.id.desc);
        tv.setText(item.desc);
        tv = (TextView) rootView.findViewById(R.id.title);
        tv.setText(item.title);
        
        ImageView iv = (ImageView) rootView.findViewById(R.id.productimage);
        ImageDownloader mDownload = new ImageDownloader();
        mDownload.download(item.image, iv);
        
        ImageButton button = (ImageButton) rootView.findViewById(R.id.buynow);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			  Intent openBrowser =  new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.walmart.com"));
			  startActivity(openBrowser);
			}

		});
	
        return rootView;
    }


}