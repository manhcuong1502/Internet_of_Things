package vn.iot.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static vn.iot.fragment.Constants.HOME_FRAGMENT_INDEX;
import static vn.iot.fragment.Constants.SETTING_FRAGMENT_INDEX;

public class MainActivity extends FragmentActivity {

    Fragment fragment;
    Button btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFragment(SETTING_FRAGMENT_INDEX);

        //Set btn
        btnSetting=findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFragment(SETTING_FRAGMENT_INDEX);
            }
        });
    }


    public void selectFragment(int pos){
        Class fragmentClass = null;
        String fragmentTag = "";
        switch (pos){
            case HOME_FRAGMENT_INDEX:
                fragmentClass = FragmentHome.class;
                fragmentTag = "Home Fragment";
                break;
            case SETTING_FRAGMENT_INDEX:
                fragmentClass = FragmentSetting.class;
                fragmentTag = "Setting Fragment";

                break;
            default:
                break;
        }

        try {


            fragment = (Fragment) fragmentClass.newInstance();


            Bundle bundle = new Bundle();
            bundle.putString("fragmentTag", fragmentTag);

            fragment.setArguments(bundle);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

}