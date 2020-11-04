package com.pad.rzjys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.myscript.iink.PointerEvent;
import com.pad.rzjys.IdentifyUtils.IdentifyUtil;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdentifyUtil.SingletonIdentifyUtil();

        IdentifyUtil.setonIdentifyListener(new IdentifyUtil.onIdentifyListener() {
            @Override
            public void onIdentifyListener(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onIdentifyErrorListener() {

            }
        });
    }


    public void sss(View view) {

        ArrayList<PointerEvent> events = new ArrayList<PointerEvent>();

        // Stroke 1
        events.add(new PointerEvent().down(484.f, 424.f));
        events.add(new PointerEvent().move(484.f, 425.f));
        events.add(new PointerEvent().move(484.f, 428.f));
        events.add(new PointerEvent().move(484.f, 433.f));
        events.add(new PointerEvent().move(484.f, 452.f));
        events.add(new PointerEvent().move(484.f, 458.f));
        events.add(new PointerEvent().move(484.f, 463.f));
        events.add(new PointerEvent().move(483.f, 467.f));
        events.add(new PointerEvent().move(483.f, 474.f));
        events.add(new PointerEvent().move(483.f, 483.f));
        events.add(new PointerEvent().up(483.f, 484.f));


        // Stroke 2
        events.add(new PointerEvent().down(450.f, 426.f));
        events.add(new PointerEvent().move(454.f, 426.f));
        events.add(new PointerEvent().move(452.f, 426.f));
        events.add(new PointerEvent().move(458.f, 426.f));
        events.add(new PointerEvent().move(466.f, 426.f));
        events.add(new PointerEvent().move(484.f, 426.f));
        events.add(new PointerEvent().move(490.f, 428.f));
        events.add(new PointerEvent().move(496.f, 428.f));
        events.add(new PointerEvent().move(500.f, 428.f));
        events.add(new PointerEvent().move(507.f, 428.f));
        events.add(new PointerEvent().move(508.f, 428.f));
        events.add(new PointerEvent().up(509.f, 428.f));


        IdentifyUtil.toIdentify(events);

    }

}