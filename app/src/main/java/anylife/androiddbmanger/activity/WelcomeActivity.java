package anylife.androiddbmanger.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import anylife.androiddbmanger.R;

public class WelcomeActivity extends AppCompatActivity {

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		context=WelcomeActivity.this;

		new Thread(new Runnable() {  //It is a demo,so
			@Override
			public void run() {
				try{
					Thread.sleep(2000);
				}catch (Exception e){

				}
				Intent intent=new Intent(context,LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}).start();

	}
}
