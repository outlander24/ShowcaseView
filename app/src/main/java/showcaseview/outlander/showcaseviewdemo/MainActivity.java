package showcaseview.outlander.showcaseviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import outlander.showcaseview.ShowcaseViewBuilder;

public class MainActivity extends AppCompatActivity {

    public boolean isShowcaseViewVisible = true;
    public ShowcaseViewBuilder showcaseViewBuilder;

    private FloatingActionButton fab;
    private TextView textView;
    private ImageView imageView;
    private Button buttton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imgBtn);
        buttton = (Button) findViewById(R.id.btn);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowcaseViewVisible) {
                    showcaseViewBuilder.hide();
                    showcaseViewBuilder.setTargetView(textView)
                            .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                            .addCustomView(R.layout.description_view, Gravity.BOTTOM)
                            .show();
                    addCLickListeners();
                    isShowcaseViewVisible = false;
                } else {
                    showcaseViewBuilder.hide();
                    showcaseViewBuilder.setTargetView(fab)
                            .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float), Gravity.TOP)
                            .addCustomView(R.layout.description_view, Gravity.TOP)
                            .show();
                    addCLickListeners();
                    isShowcaseViewVisible = true;
                }
            }
        });

        showcaseViewBuilder = ShowcaseViewBuilder.init(this)
                .setTargetView(fab)
                .setBackgroundOverlayColor(0xdd4d4d4d)
                .setRingColor(0xcc8e8e8e)
                .setRingWidth(20)
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float), Gravity.TOP)
                .setDrawableLeftMargin(16)
                .addCustomView(R.layout.description_view, Gravity.TOP)
                .addCustomView(R.layout.skip_layout)
                .setCustomViewMargin(70);

        showcaseViewBuilder.show();

        addCLickListeners();


    }

    private void addCLickListeners() {
        showcaseViewBuilder.setClickListenerOnView(R.id.btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaseViewBuilder.hide();
            }
        });

        showcaseViewBuilder.setClickListenerOnView(R.id.second, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "This is second click listener", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
