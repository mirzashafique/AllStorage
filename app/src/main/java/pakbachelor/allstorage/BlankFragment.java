package pakbachelor.allstorage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import e.mirzashafique.lib.Storage;


public class BlankFragment extends Fragment {
Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        button=view.findViewById(R.id.fragment_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage.create(getActivity()).showFiles(5).showImages(2).start();
            }
        });
        return view;
    }

}
