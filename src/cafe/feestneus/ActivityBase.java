package cafe.feestneus;

import android.app.Activity;
import android.content.Intent;

public class ActivityBase extends Activity
{
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(0, 0);
	}

	@Override
	public void startActivity(Intent intent) {
	    super.startActivity(intent);
	    overridePendingTransition(0, 0);
	}

	@Override
	public void finish() {
	    super.finish();
	    overridePendingTransition(0, 0);
	}
}