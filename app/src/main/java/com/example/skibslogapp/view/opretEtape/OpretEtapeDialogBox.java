package com.example.skibslogapp.view.opretEtape;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skibslogapp.R;
import com.example.skibslogapp.datalayer.local.EtapeDAO;
import com.example.skibslogapp.model.Etape;
import com.example.skibslogapp.model.Togt;
import com.example.skibslogapp.view.opretEtape.CrewAdapter;

import java.util.List;


public class OpretEtapeDialogBox extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText editSkipper, editStartDest,navnIndput ;
    private Togt togt;
    private View addButton;
    private Etape etape;
    RecyclerView besaetning;
    private List<String> beseatningsList = null;
    RecyclerView.Adapter adapter;
    private String skipper = "";
    private String startDestination = "";
    private TextView annulerEtape;
    private Button startEtape;
    private Etape newEtape;
    private EtapeDAO etapeDAO;


    public OpretEtapeDialogBox(Togt togt, Etape etape) {
       this.togt = togt;
       this.etape = etape;

       etape.setStatus(Etape.Status.FINISHED);
       newEtape = new Etape();
       //Setting the etape to active.
       newEtape.setStatus(Etape.Status.ACTIVE);
       skipper = etape.getSkipper();

       beseatningsList = etape.getBesaetning();
       System.out.println("BesætningsListe" + beseatningsList.size());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.opret_etape_dialog_box,null);

        builder.setView(view);


        annulerEtape = view.findViewById(R.id.opretEtape_annuler_button);
        annulerEtape.setOnClickListener(this);

        startEtape = view.findViewById(R.id.opretEtape_start_button);
        startEtape.setOnClickListener(this);

        editSkipper = view.findViewById(R.id.inputSkipper);
        editStartDest = view.findViewById(R.id.inputStartDest);
        navnIndput = view.findViewById(R.id.navnIndput);

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        besaetning = view.findViewById(R.id.besaetningList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        adapter = new CrewAdapter(beseatningsList,getContext());
        besaetning.setLayoutManager(layoutManager);

        besaetning.setAdapter(adapter);
        System.out.println("Adapter item count: " + adapter.getItemCount());

        showSkipper();
        showSlutDestination();

        scrollTobuttom();

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        editSkipper.setError(null);
        editStartDest.setError(null);
        navnIndput.setError(null);

        String navn = navnIndput.getText().toString();
        String skipper = editSkipper.getText().toString();
        String startDest = editStartDest.getText().toString();
        int besaetning = etape.getBesaetning().size();

        if (v == annulerEtape) {
            getFragmentManager().beginTransaction()
                    .remove(this)
                    .commit();
        }else if (v == startEtape && skipper.length() <= 0){
            editSkipper.setError("Der skal indtastes en skipper!");

        }else if (v == startEtape && startDest.length() <= 0){
            editStartDest.setError("Der skal indtastes en start destination!");

        }else if (v == startEtape && besaetning <= 0){
            navnIndput.setError("Der er ikke tilføjet nogen besaetning til etapen!");

        }else{
            if (v == startEtape){
                EtapeDAO etapeDAO = new EtapeDAO(getContext());
                newEtape.setBesaetning(beseatningsList);
                newEtape.setSkipper(skipper);
                newEtape.setStartDestination(startDest);
                etape.setSlutDestination(startDest);
                //Update the previus etape to status  FINISHED
                etapeDAO.updateEtape(etape);
                //Adding the new etape to database - NB: The new etape is beeing active in the constructor OpretEtapeDialogBox
                etapeDAO.addEtape(togt, newEtape);
                scrollTobuttom();
                getFragmentManager().beginTransaction()
                        .remove(this)
                        .commit();
            }
        }
        if (v == addButton) {
            if (navn.length() <= 0) {
                navnIndput.setError("Der skal indtastes et navn på et besætningsmedlem!");
                return;
            } else {
                beseatningsList.add(navn);
                System.out.println(navnIndput.getText().toString());
                navnIndput.setText("");
                adapter.notifyDataSetChanged();
//                    besaetning.smoothScrollToPosition(adapter.getItemCount() - 1);
                clearFocusOnDone(v);
            }
        }


//        if (v == startEtape) {
//
//            /*
//            Ensure that we have a slutdestination for the former etape
//             */
//            if (startDest.length() <= 0) {
//                editStartDest.setError("Der skal indtastes en start destinationt!");
//                return;
//            }
//
//                EtapeDAO etapeDAO = new EtapeDAO(getContext());
//                newEtape.setBesaetning(beseatningsList);
//                newEtape.setSkipper(skipper);
//                newEtape.setStartDestination(startDest);
//                etape.setSlutDestination(startDest);
//                //Update the previus etape to status  FINISHED
//                etapeDAO.updateEtape(etape);
//                //Adding the new etape to database - NB: The new etape is beeing active in the constructor OpretEtapeDialogBox
//                etapeDAO.addEtape(togt, newEtape);
//                scrollTobuttom();
//                getFragmentManager().beginTransaction()
//                        .remove(this)
//                        .commit();
//
//            }
    }

    /**
     * Makes the keyboard dissapere
     * @param v
     */
    private void clearFocusOnDone(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Show the previus Skipper in the new Etape
     */
    private void showSkipper(){
        if(skipper.length()>0){
            editSkipper.setText(skipper);
        }
    }

    /**
     * Show the previus slutDestination as the Start destination in the new Etape
     */
    private void showSlutDestination(){

        if(startDestination != null && startDestination.length()>0){
            editStartDest.setText(startDestination);
        }
    }

    private void scrollTobuttom(){
        if(adapter.getItemCount()>0){
            besaetning.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }
}




