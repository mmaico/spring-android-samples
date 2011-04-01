/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.android.showcase.social.facebook;

import org.springframework.android.showcase.AbstractAsyncActivity;
import org.springframework.android.showcase.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Roy Clarkson
 */
public class FacebookActivity extends AbstractAsyncActivity 
{
	protected static final String TAG = FacebookActivity.class.getSimpleName();
	
	private FacebookConnectController _facebookConnectController;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		_facebookConnectController = new FacebookConnectController(this);
		
		setContentView(R.layout.facebook_activity_layout);
	}
	
	@Override
	public void onStart() 
	{
		super.onStart();
		
		if (_facebookConnectController.isConnected())
		{
			showFacebookOptions();
		}
		else
		{
			showConnectOption();
		}
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		if (getIntent().hasExtra("accessToken"))
		{
			String accessToken = getIntent().getExtras().getString("accessToken");
		
			if (accessToken != null)
			{
				_facebookConnectController.connect(accessToken);
				showFacebookOptions();
			}
		}
	}
	
	
	//***************************************
    // Private methods
    //***************************************
	private void showConnectOption()
	{
		String[] options = {"Connect"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.facebook_activity_options_list);
		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() 
				{
					public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) 
					{
						switch(position)
						{
							case 0:
								displayFacebookAuthorization();
								break;
							default:
								break;
						}
					}
				}
			);
	}
	
	private void showFacebookOptions()
	{
		String[] options = {"Disconnect", "Profile", "Wall Post"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
		ListView listView = (ListView) this.findViewById(R.id.facebook_activity_options_list);
		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() 
				{
					public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) 
					{
						Intent intent;
						switch(position)
						{
							case 0:
								_facebookConnectController.disconnect();
								showConnectOption();
								break;
							case 1:
								intent = new Intent();
								intent.setClass(parentView.getContext(), FacebookProfileActivity.class);
							    startActivity(intent);
								break;
							case 2:
								intent = new Intent();
								intent.setClass(parentView.getContext(), FacebookWallPostActivity.class);
							    startActivity(intent);
								break;
							default:
								break;
						}
					}
				}
			);
	}
	
	private void displayFacebookAuthorization()
	{		
		Intent intent = new Intent();
		intent.setClass(this, FacebookOAuthActivity.class);
		intent.putExtra("authUrl", _facebookConnectController.getAuthorizeUrl());
		startActivity(intent);
		finish();
	}
}