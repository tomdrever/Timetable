package tomdrever.timetable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NewTimetableDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(R.layout.dialog_new_timetable, null);

        builder.setView(view)
                .setPositiveButton(R.string.dialog_new_timetable_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editTextName = (EditText) view.findViewById(R.id.edit_timetable_name);
                        EditText editTextDescription = (EditText) view.findViewById(R.id.edit_timetable_description);

                        if (editTextName.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Name field required", Toast.LENGTH_SHORT).show();
                        }
                        else if (Arrays.asList(getActivity().getFilesDir().list()).contains(editTextName.getText().toString())) {
                            Toast.makeText(getActivity(), "Timetable with that name already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // create new timetable - launch edit timetable
                            Intent intent = new Intent(getActivity(), EditTimetableActivity.class);
                            intent.putExtra("timetablename", editTextName.getText().toString());
                            intent.putExtra("timetabledescription", editTextDescription.getText().toString());
                            getActivity().startActivityForResult(intent, 100);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTimetableDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
