package azureUtil;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shengyuansun.serendipitiosgene.R;

public class MultiThreadDown extends Activity
{
	EditText url;
	EditText target;
	Button downBn;
	ProgressBar bar;
	DownUtil downUtil;
	private int mDownStatus;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_view);

		url = (EditText) findViewById(R.id.url);
		target = (EditText) findViewById(R.id.target);
		downBn = (Button) findViewById(R.id.down);
		bar = (ProgressBar) findViewById(R.id.bar);

		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 0x123)
				{
					bar.setProgress(mDownStatus);
				}
			}
		};
		downBn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				downUtil = new DownUtil(url.getText().toString(),
					target.getText().toString(), 6);
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{

							downUtil.download();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						final Timer timer = new Timer();
						timer.schedule(new TimerTask()
						{
							@Override
							public void run()
							{

								double completeRate = downUtil.getCompleteRate();
								mDownStatus = (int) (completeRate * 100);

								handler.sendEmptyMessage(0x123);
								if (mDownStatus >= 100)
								{
									timer.cancel();
								}
							}
						}, 0, 100);
					}
				}.start();
			}
		});
	}
}