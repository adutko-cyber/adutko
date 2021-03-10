package com.citra_usaha_jaya.aj77;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.app.*;
import android.content.*;
import android.webkit.*;
import android.os.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.graphics.*;
import java.net.*;
import java.io.*;
import java.nio.channels.*;
import android.util.*;
import android.nfc.*;
import android.graphics.fonts.*;
import java.net.CookieHandler;
import java.util.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;
import android.content.pm.*;
import java.util.jar.*;

public class MainActivity extends Activity {
	private Context konteks;
	private Activity mActivity;
	private WebView tampilanWeb;
	
	

	link alamat = new link();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActionBar ab = getActionBar();
		ab.setTitle("AJ77");
		ab.setSubtitle("Air Mineral AJ77");

		ab.setLogo(R.drawable.local);
		ab.setDisplayUseLogoEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));





		konteks = getApplicationContext();
		mActivity = MainActivity.this;


		tampilanWeb = findViewById(R.id.web_view);


		web_settings();



		tampilanWeb.setDownloadListener(new DownloadListener() {
				public void onDownloadStart(String url, String userAgent,
											String contentDisposition, String mimetype,
											long contentLength) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			});




		tampilanWeb.setWebViewClient(new WebViewClient(){


				public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
					try {
						webView.stopLoading();
					} catch (Exception e) {
					}

					if (webView.canGoBack()) {
						webView.goBack();
					}

					webView.loadUrl("about:blank");
					AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
					alertDialog.setTitle("Mohon Maaf");
					alertDialog.setIcon(R.drawable.error);
					alertDialog.setMessage("Tidak Dapat Menyambungkan Anda Ke jaringan");
					alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Silahkan Coba Lagi Nanti", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								finish();
								startActivity(getIntent());
							}
						});

					alertDialog.show();
					super.onReceivedError(webView, errorCode, description, failingUrl);
				} 


				@Override
				public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
					String url = request.getUrl().toString();

					if(url.startsWith("tel:") || url.startsWith("whatsapp:")) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivity(intent);
						return true;
					}

					if (url == null || url.startsWith("http://") || url.startsWith("https://")) 
						return false;


					//if(url.startsWith("http://")){
					//return false;
					//}

					if (url.startsWith("rtsp")) {
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						return true;

					}else if(url.startsWith("sms:")){

						handleSMSLink(url);
						return true;
					}

					return super.shouldOverrideUrlLoading(view, url);

				}
			});



		// ini tempat buat ambil alamat web atau lokal
		//tampilanWeb.loadUrl("http://" +alamat.getAlamatlink());

		tampilanWeb.loadUrl("file:///android_asset/index.html");

		
	}
	// handel String pesan
	protected void handleSMSLink(String url){


		// Inisialisasi Intent ke pesanan
		Intent intent = new Intent(Intent.ACTION_SENDTO);

		// mengekstrak no_hp dari alamat sms
		String no_hp = url.split("[:?]")[1];

		if(!TextUtils.isEmpty(no_hp)){
			// memasukakan data Intent
			// respon perpesanan
			intent.setData(Uri.parse("smsto:" + no_hp));
		}else {
			// jika pesan tanpa no hp
			intent.setData(Uri.parse("smsto:"));

			//  skema data
			//intent.setData(Uri.parse("sms:" + no_hp));
		}


		// kotak pesan
		if(url.contains("body=")){
			String kotak_pesan = url.split("body=")[1];

			// memasukan string pesan
			try{
				kotak_pesan = URLDecoder.decode(kotak_pesan,"UTF-8");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}

			if(!TextUtils.isEmpty(kotak_pesan)){
				// memasukan ke intens
				intent.putExtra("sms_body",kotak_pesan);
			}
		}

		if(intent.resolveActivity(getPackageManager())!=null){
			// Memulai aplikasi sms
			startActivity(intent);
		}else {
			Toast.makeText(konteks,"Tidak ada aplikasi sms tersedia",Toast.LENGTH_SHORT).show();
		}
	}

	private void web_settings(){
		WebSettings webSettings = tampilanWeb.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		//webSettings.setDisplayZoomControls(true);
		//webSettings.setBuiltInZoomControls(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccess(true);
		webSettings.enableSmoothTransition();
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setDisplayZoomControls(true);
		webSettings.setMediaPlaybackRequiresUserGesture(true);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setAppCacheEnabled(false);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.getDisplayZoomControls();
        webSettings.getCursiveFontFamily();
        webSettings.setFixedFontFamily("cursive");

        webSettings.setPluginState(WebSettings.PluginState.ON);
		webSettings.setUserAgentString("aj77");    

	}


	@Override
	public void onBackPressed() {

		if(tampilanWeb.canGoBack()){
			tampilanWeb.destroyDrawingCache();
			tampilanWeb.goBack();
		}else {
			super.onBackPressed();
		}
	}



	@Override
	protected void onPause()
	{

		super.onPause();
	}

}


