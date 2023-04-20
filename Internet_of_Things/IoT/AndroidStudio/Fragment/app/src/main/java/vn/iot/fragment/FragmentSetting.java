package vn.iot.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static vn.iot.fragment.Constants.HOME_FRAGMENT_INDEX;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FragmentSetting extends Fragment {
    Button btnBack;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_setting, container, false);

        btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).selectFragment(HOME_FRAGMENT_INDEX);
            }
        });
        return view;
    }
}
